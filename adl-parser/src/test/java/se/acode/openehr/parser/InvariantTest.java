package se.acode.openehr.parser;

import java.util.List;

/**
 * Created by pieter.bos on 13/10/15.
 */
public class InvariantTest extends ParserTestBase {

    private List attributeList;

    public InvariantTest(String test) throws Exception {
        super(test);
        ADLParser parser = new ADLParser(loadFromClasspath(
                "adl-test-entry.invariant.test.adl"));
        attributeList = parser.parse().getDefinition().getAttributes();
    }

    public void test_invariants() {

    }
}
