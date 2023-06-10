package org.app.validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    private Validator validator;

    @BeforeEach
    void initializeValidator() {
        validator = new Validator();
    }
    @ParameterizedTest
    @ValueSource( strings = {"Jean-Claude", "O'Neill","John"})
    void testIsValidName_Correct(String name) {
        assertTrue(validator.isValidName(name));
    }

    @ParameterizedTest
    @ValueSource( strings = {"-john", "john-", "J.Doe"})
    void testIsValidName_Incorrect(String name) {
        assertFalse(validator.isValidName(name));
    }

    @ParameterizedTest
    @ValueSource( strings = {"john@gmail.com", "example@domain.com"})
    void testIsValidEmail_Correct(String email) {
        assertTrue(validator.isValidEmail(email));
    }

    @ParameterizedTest
    @ValueSource( strings = {"email", "example@", "@domain.com"})
    void testIsValidEmail_Incorrect(String email) {
        assertFalse(validator.isValidEmail(email));
    }


    @ParameterizedTest
    @ValueSource( strings = {"234", "1"})
    void testIsNumeric_Correct(String input) {
        assertTrue(validator.isNumeric(input));
    }

    @ParameterizedTest
    @ValueSource( strings = {"abc", "@"})
    void testIsNumeric_Incorrect(String input) {
        assertFalse(validator.isNumeric(input));
    }


}