/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.archunit;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.Location;

/**
 * Verifies that every dependency used exclusively in test code
 * carries {@code <scope>test</scope>} in pom.xml.
 *
 * <p>Strategy:
 * <ol>
 *   <li>Parse pom.xml with maven-model to get all non-test-scoped dependencies.</li>
 *   <li>For each, locate its JAR in the local Maven repo and extract the fully-qualified
 *       class names it provides (via ArchUnit's {@link ClassFileImporter}).</li>
 *   <li>Import ONLY {@code target/classes} and {@code target/test-classes} — NOT the full
 *       classpath — so dependency JARs never pollute the "used by main" set.</li>
 *   <li>Collect all type names referenced by main and test bytecode using
 *       {@code getDirectDependenciesFromSelf()}. Because ArchUnit records the target class
 *       name even for unresolved types, this works without importing the dependency JARs
 *       into the same class set.</li>
 *   <li>A dependency is a violation when none of its classes are referenced by main code
 *       but at least one is referenced by test code.</li>
 * </ol>
 *
 * @author Ronald Brill
 */
public class TestScopeDependencyTest {

    private static final Path PROJECT_ROOT = resolveProjectRoot();
    private static final Path LOCAL_REPO   = resolveLocalRepo();

    @Test
    void allTestOnlyDependenciesMustHaveTestScope() throws Exception {

        // ── 1. Parse pom.xml ──────────────────────────────────────────────────
        Model model = readPomModel();

        List<org.apache.maven.model.Dependency> candidates = model.getDependencies().stream()
                .filter(d -> {
                    String scope = d.getScope(); // null == "compile" (Maven default)
                    return scope == null
                            || (!scope.equalsIgnoreCase("test")
                            &&  !scope.equalsIgnoreCase("provided")
                            &&  !scope.equalsIgnoreCase("system"));
                })
                .collect(Collectors.toList());

        if (candidates.isEmpty()) {
            return;
        }

        // ── 2. Resolve JARs → collect fully-qualified class names per dependency ─
        //
        // We use class names (not just package names) so the match against bytecode
        // references is precise and not confused by shared package prefixes.
        Map<org.apache.maven.model.Dependency, Set<String>> depToClassNames = new LinkedHashMap<>();

        for (org.apache.maven.model.Dependency dep : candidates) {
            Path jarPath = resolveJarPath(dep, model);
            if (jarPath == null || !jarPath.toFile().exists()) {
                continue; // not yet in local repo — run `mvn dependency:resolve` first
            }

            JavaClasses jarClasses = new ClassFileImporter()
                    .importLocations(Collections.singleton(Location.of(jarPath.toUri().toURL())));

            Set<String> classNames = jarClasses.stream()
                    .map(JavaClass::getName)
                    .collect(Collectors.toSet());

            if (!classNames.isEmpty()) {
                depToClassNames.put(dep, classNames);
            }
        }

        if (depToClassNames.isEmpty()) {
            return;
        }

        // ── 3. Import ONLY target/classes and target/test-classes ─────────────
        //
        // Deliberately NOT using importClasspath() — that would pull in all dependency
        // JARs and make every dep appear "used in main".
        //
        // allowMissingTypes() is essential: our project classes reference types from
        // dependency JARs which are NOT imported here. Without it ArchUnit would throw
        // on unresolved types instead of simply recording their names.

        Path targetClasses     = PROJECT_ROOT.resolve("target/classes");
        Path targetTestClasses = PROJECT_ROOT.resolve("target/test-classes");

        JavaClasses mainClasses = new ClassFileImporter()
                .importLocations(Collections.singleton(Location.of(targetClasses.toUri().toURL())));

        JavaClasses testClasses = new ClassFileImporter()
                .importLocations(Collections.singleton(Location.of(targetTestClasses.toUri().toURL())));

        // ── 4. Collect referenced type names from bytecode ────────────────────
        //
        // getDirectDependenciesFromSelf() returns all type-level references in the
        // bytecode (field types, method signatures, calls, annotations, instanceof, …).
        // getTargetClass().getName() gives the FQN even for types ArchUnit could not
        // resolve — exactly what we need to match against the JAR's class list.

        Set<String> classesReferencedByMain = referencedClassNames(mainClasses);
        Set<String> classesReferencedByTest = referencedClassNames(testClasses);

        // ── 5. Detect violations ──────────────────────────────────────────────
        Map<org.apache.maven.model.Dependency, Set<String>> violations = new LinkedHashMap<>();

        for (Map.Entry<org.apache.maven.model.Dependency, Set<String>> entry : depToClassNames.entrySet()) {
            org.apache.maven.model.Dependency dep = entry.getKey();
            Set<String> jarClassNames = entry.getValue();

            boolean usedInMain = jarClassNames.stream().anyMatch(classesReferencedByMain::contains);
            boolean usedInTest = jarClassNames.stream().anyMatch(classesReferencedByTest::contains);

            if (!usedInMain && usedInTest) {
                Set<String> hitClasses = jarClassNames.stream()
                        .filter(classesReferencedByTest::contains)
                        .collect(Collectors.toCollection(TreeSet::new));
                violations.put(dep, hitClasses);
            }
        }

        if (!violations.isEmpty()) {
            fail(buildViolationMessage(violations));
        }
    }

    // ── pom.xml parsing ───────────────────────────────────────────────────────

    private static Model readPomModel() throws Exception {
        try (FileReader reader = new FileReader(PROJECT_ROOT.resolve("pom.xml").toFile())) {
            return new MavenXpp3Reader().read(reader);
        }
    }

    // ── JAR path resolution ───────────────────────────────────────────────────

    private static Path resolveJarPath(org.apache.maven.model.Dependency dep, Model model) {
        String version = resolveProperty(dep.getVersion(), model);
        if (version == null || version.isBlank()) return null;

        String groupPath  = dep.getGroupId().replace('.', '/');
        String classifier = dep.getClassifier();
        String jarName    = dep.getArtifactId() + "-" + version
                + (classifier != null && !classifier.isBlank() ? "-" + classifier : "")
                + ".jar";

        return LOCAL_REPO
                .resolve(groupPath)
                .resolve(dep.getArtifactId())
                .resolve(version)
                .resolve(jarName);
    }

    private static String resolveProperty(String value, Model model) {
        if (value == null) return null;
        if (value.startsWith("${") && value.endsWith("}")) {
            String key = value.substring(2, value.length() - 1);
            String resolved = model.getProperties().getProperty(key);
            return resolved != null ? resolved : value;
        }
        return value;
    }

    // ── ArchUnit helpers ──────────────────────────────────────────────────────

    private static Set<String> referencedClassNames(JavaClasses classes) {
        return classes.stream()
                .flatMap(c -> c.getDirectDependenciesFromSelf().stream())
                .map(dep -> dep.getTargetClass().getName())
                .collect(Collectors.toSet());
    }

    // ── Failure message ───────────────────────────────────────────────────────

    private static String buildViolationMessage(
            Map<org.apache.maven.model.Dependency, Set<String>> violations) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\nThe following dependencies are used ONLY in test code ")
          .append("but are missing <scope>test</scope> in pom.xml:\n\n");

        violations.forEach((dep, classes) -> {
            sb.append("  ").append(dep.getGroupId())
              .append(":").append(dep.getArtifactId())
              .append(":").append(dep.getVersion()).append("\n");
            classes.stream().limit(5)
                   .forEach(c -> sb.append("    referenced class: ").append(c).append("\n"));
            if (classes.size() > 5) {
                sb.append("    ... and ").append(classes.size() - 5).append(" more\n");
            }
        });

        sb.append("\nFix: add <scope>test</scope> to each dependency listed above in pom.xml.\n");
        return sb.toString();
    }

    // ── Utility ───────────────────────────────────────────────────────────────

    private static Path resolveProjectRoot() {
        String p = System.getProperty("maven.multiModuleProjectDirectory");
        return Paths.get(p != null ? p : System.getProperty("user.dir"));
    }

    private static Path resolveLocalRepo() {
        String p = System.getProperty("maven.repo.local");
        return Paths.get(p != null ? p : System.getProperty("user.home") + "/.m2/repository");
    }
}