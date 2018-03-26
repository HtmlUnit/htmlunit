The jar files of the applets contained in this folder are generated using 
mvn compile jar:jar
in the testApplets module (same level than htmlunit and core-js in SVN repository) and then copied here.

Applets' sources can't be in the same source tree than HtmlUnit's tests or main classes otherwise
the classes would already be available in the classpath during test execution. 