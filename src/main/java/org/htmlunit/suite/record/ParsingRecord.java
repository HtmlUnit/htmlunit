package org.htmlunit.suite.record;

public class ParsingRecord extends Record {
    public ParsingRecord(String testName, String className, String testStatus,
                        String parsedContent, String parsingErrors, boolean validParse) {
        super(testName, className, testStatus);
        addToRecord("parsedContent", parsedContent);
        addToRecord("parsingErrors", parsingErrors);
        addToRecord("validParse", validParse);
    }
}
