package com.github.fernthedev.fernutils;

import com.github.fernthedev.fernutils.strings.StringUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StringTest {


    @Test
    @DisplayName("Camel case to snake case")
    void testCamelCaseToSnakeCase() {
        String test1 = "someRandomWord";
        String expectation1 = "some_Random_Word";

        Assertions.assertEquals(expectation1, StringUtil.camelCaseToSnakeCase(test1));


        String test2 = "SomeRandomWord";
        String expectation2 = "Some_Random_Word";

        Assertions.assertEquals(expectation2, StringUtil.camelCaseToSnakeCase(test2));

    }

    @Test
    @DisplayName("Camel case to snake case")
    void testCamelCaseToCamelCase() {
        String test1 = "someRandomWord";
        String expectation1 = "SomeRandomWord";

        Assertions.assertEquals(expectation1, StringUtil.lowerCamelCaseToUpperCamelCase(test1));


        String test2 = "SomeRandomWord";
        String expectation2 = "someRandomWord";

        Assertions.assertEquals(expectation2, StringUtil.upperCamelCaseToLowerCamelCase(test2));

    }
}
