/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit;

/**
 * Test of coding style using Eclipse JDT.
 * Currently checks for missing @Override annotations.
 *
 * To run it, you need:
 * <ul>
 *   <li>org.eclipse.core.contenttype_x.jar</li>
 *   <li>org.eclipse.core.jobs_x.jar</li>
 *   <li>org.eclipse.core.resources_x.jar</li>
 *   <li>org.eclipse.core.runtime_x.jar</li>
 *   <li>org.eclipse.equinox.preferences_x.jar</li>
 *   <li>org.eclipse.osgi_x.jar</li>
 * </ul>
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class CodeChecker {

    /*
    private List<String> errors_ = new ArrayList<String>();
    private ASTParser parser_ = ASTParser.newParser(AST.JLS3);

    @After
    public void after() {
        final StringBuilder sb = new StringBuilder();
        for (final String error : errors_) {
            sb.append("\n" + error);
        }

        final int errorsNumber = errors_.size();
        if (errorsNumber == 1) {
            fail("CodeChecker error: " + sb);
        }
        else if (errorsNumber > 1) {
            fail("CodeChecker " + errorsNumber + " errors: " + sb);
        }
    }

    private void addFailure(final String error) {
        errors_.add(error);
    }

    @Test
    public void codeChecker() throws Exception {
        process(new File("src/main/java"));
        process(new File("src/test/java"));
    }

    private void process(final File dir) throws Exception {
        for (final File file : dir.listFiles()) {
            if (file.isDirectory() && !file.getName().equals(".svn")) {
                process(file);
            }
            else {
                if (file.getName().endsWith(".java")) {
                    final List<String> lines = CodeStyleTest.getLines(file);
                    checkOverride(lines);
                }
            }
        }
    }

    / **
     * Checks if the given file has any missing @Override annotation.
     *
     * @param lines lines
     * @throws Exception If an error occurs
     * /
    private void checkOverride(final List<String> lines) throws Exception {
        final CharArrayWriter writer = new CharArrayWriter();
        for (final String line : lines) {
            writer.write(line);
            writer.write("\n");
        }
        parser_.setSource(writer.toCharArray());
        final CompilationUnit cu = (CompilationUnit) parser_.createAST(null);

        for (final Object type : cu.types()) {
            if (type instanceof TypeDeclaration) {
                checkOverride((TypeDeclaration) type);
            }
        }
    }

    private void checkOverride(final TypeDeclaration type) throws Exception {
        for (final TypeDeclaration tt : type.getTypes()) {
            checkOverride(tt);
        }
        for (final MethodDeclaration method : type.getMethods()) {
            boolean isPrivate = false;
            for (final Object m : method.modifiers()) {
                if (m instanceof Modifier && ((Modifier) m).isPrivate()) {
                    isPrivate = true;
                    break;
                }
            }
            if (!isPrivate && !method.isConstructor() && !hasOverride(method) && shouldOverride(method)) {
                final String className = getFullyQualifiedName((TypeDeclaration) method.getParent()).replace('$', '.');
                addFailure("@Override is not defined for " +  className + "." + method.getName());
            }
        }
    }

    private static boolean hasOverride(final MethodDeclaration method) {
        for (final Object modifier : method.modifiers()) {
            if (modifier instanceof MarkerAnnotation) {
                final String name = ((MarkerAnnotation) modifier).getTypeName().getFullyQualifiedName();
                if (name.equals("Override") || name.equals("java.lang.Override")) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String getFullyQualifiedName(final TypeDeclaration type) {
        String name = type.getName().getFullyQualifiedName();
        final CompilationUnit root = (CompilationUnit) type.getRoot();
        for (ASTNode t = type.getParent(); t != root; t = t.getParent()) {
            name = ((TypeDeclaration) t).getName().getFullyQualifiedName() + '$' + name;
        }

        if (root.getPackage() != null) {
            final String packageName = root.getPackage().getName().getFullyQualifiedName();
            name = packageName + '.' + name;
        }
        return name;
    }

    private static boolean shouldOverride(final MethodDeclaration method) throws Exception {
        final TypeDeclaration type = (TypeDeclaration) method.getParent();
        final Class<?> klass = Class.forName(getFullyQualifiedName(type));
        final Class<?>[] types = new Class[method.parameters().size()];
        for (int i = 0; i < types.length; i++) {
            final SingleVariableDeclaration parameter = (SingleVariableDeclaration) method.parameters().get(i);
            types[i] = getClassOf(parameter.getType(), method.isVarargs() && i == method.parameters().size() - 1);
        }

        // Check we have the method
        assertNotNull(klass.getDeclaredMethod(method.getName().getFullyQualifiedName(), types));

        for (Class<?> c = klass.getSuperclass(); c != null; c = c.getSuperclass()) {
            try {
                c.getDeclaredMethod(method.getName().getFullyQualifiedName(), types);
                return true;
            }
            catch (final NoSuchMethodException e) {
                //ignore
            }
        }
        return false;
    }

    private static Class<?> getClassOf(final Type type, final boolean isVararg) {
        Class<?> klass = null;
        String name = null;
        if (isVararg) {
            if (type instanceof SimpleType) {
                final Class<?> elemntClass = getClassOf(type, false);
                klass = getArrayClassOf(elemntClass.getName(), 1, false);
            }
            else if (type instanceof PrimitiveType) {
                klass = getPrimitiveArrayType(type.toString(), 1);
            }
        }
        else if (type instanceof SimpleType) {
            name = ((SimpleType) type).getName().getFullyQualifiedName();
        }
        else if (type instanceof PrimitiveType) {
            //TODO: "char x[]" and not "char[] x" comes as Primitive type, this should be handled.
            name = ((PrimitiveType) type).getPrimitiveTypeCode().toString();
            klass = getPrimitiveType(name);
        }
        else if (type instanceof ArrayType) {
            final ArrayType arrayType = (ArrayType) type;
            final Type elementType = arrayType.getElementType();
            if (elementType instanceof SimpleType) {
                final Class<?> elemntClass = getClassOf(elementType, false);
                klass = getArrayClassOf(elemntClass.getName(), arrayType.getDimensions(), false);
            }
            else if (elementType instanceof PrimitiveType) {
                klass = getPrimitiveArrayType(elementType.toString(), arrayType.getDimensions());
            }
            else {
                throw new IllegalStateException("Can not process type of " + type.getClass().getName());
            }
        }
        else if (type instanceof ParameterizedType) {
            klass = getClassOf(((ParameterizedType) type).getType(), isVararg);
        }
        else {
            throw new IllegalStateException("Can not process type of " + type.getClass().getName());
        }
        if (klass == null) {
            klass = tryClassName(name);
        }
        final CompilationUnit cu = (CompilationUnit) type.getRoot();
        if (klass == null && cu.getPackage() != null) {
            klass = tryClassName(cu.getPackage().getName().getFullyQualifiedName() + '.' + name);
        }
        if (klass == null) {
            for (final Object t : cu.types()) {
                klass = searchClassInAllTypes((TypeDeclaration) t, name);
                if (klass != null) {
                    break;
                }
            }
        }
        if (klass == null) {
            klass = tryClassName("java.lang." + name);
        }
        if (klass == null) {
        outer:
            for (final Object i : cu.imports()) {
                String imported = ((ImportDeclaration) i).toString().trim().substring(7);
                imported = imported.substring(0, imported.length() - 1);
                if (imported.endsWith('.' + name)) {
                    klass = tryClassName(imported);
                    if (klass != null) {
                        break;
                    }
                    final int period = imported.lastIndexOf('.');
                    while (period != -1) {
                        imported = imported.substring(0, period) + '$' + imported.substring(period + 1);
                        klass = tryClassName(imported);
                        if (klass != null) {
                            break outer;
                        }
                    }
                }
                else if (imported.endsWith(".*;")) {
                    //TODO: we need to check for any inner class (which needs '$' instead of '.')
                    klass = tryClassName(imported.substring(0, imported.length() - 1) + name);
                    if (klass != null) {
                        break;
                    }
                }
            }
        }
        if (klass == null) {
            throw new IllegalStateException("Can not get class of " + type);
        }
        return klass;
    }

    private static Class<?> tryClassName(final String className) {
        try {
            return Class.forName(className);
        }
        catch (final Throwable t) {
            return null;
        }
    }

    private static Class<?> searchClassInAllTypes(final TypeDeclaration typeDeclaration, final String name) {
        Class<?> klass = tryClassName(getFullyQualifiedName(typeDeclaration) + '$' + name);
        if (klass != null) {
            return klass;
        }
        for (final Object type : typeDeclaration.getTypes()) {
            klass = searchClassInAllTypes((TypeDeclaration) type, name);
            if (klass != null) {
                return klass;
            }
        }
        return null;
    }

    private static Class<?> getPrimitiveArrayType(final String name, final int dimensions) {
        final String className;
        if (name.equals("byte")) {
            className = "B";
        }
        else if (name.equals("short")) {
            className = "S";
        }
        else if (name.equals("int")) {
            className = "I";
        }
        else if (name.equals("long")) {
            className = "J";
        }
        else if (name.equals("char")) {
            className = "C";
        }
        else if (name.equals("float")) {
            className = "F";
        }
        else if (name.equals("double")) {
            className = "D";
        }
        else if (name.equals("boolean")) {
            className = "Z";
        }
        else {
            throw new IllegalArgumentException("Invalid primitive type " + name);
        }

        return getArrayClassOf(className, dimensions, true);
    }

    private static Class<?> getArrayClassOf(final String className, final int dimensions, final boolean primitive) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < dimensions; i++) {
            builder.append('[');
        }
        if (!primitive) {
            builder.append('L');
        }
        builder.append(className);
        if (!primitive) {
            builder.append(';');
        }
        return tryClassName(builder.toString());
    }

    private static Class<?> getPrimitiveType(final String name) {
        if (name.equals("byte")) {
            return byte.class;
        }
        if (name.equals("short")) {
            return short.class;
        }
        if (name.equals("int")) {
            return int.class;
        }
        if (name.equals("long")) {
            return long.class;
        }
        if (name.equals("char")) {
            return char.class;
        }
        if (name.equals("float")) {
            return float.class;
        }
        if (name.equals("double")) {
            return double.class;
        }
        if (name.equals("boolean")) {
            return boolean.class;
        }
        if (name.equals("void")) {
            return void.class;
        }
        return null;
    }

    */
}
