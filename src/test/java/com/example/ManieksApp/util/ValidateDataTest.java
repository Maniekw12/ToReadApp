package com.example.ManieksApp.util;

import com.example.ManieksApp.exceptions.EmptyDataException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidateDataTest {

    @Test
    void validateName_withValidName_shouldPass() {
        assertDoesNotThrow(() -> ValidateData.validateName("John"));
    }

    @Test
    void validateName_withNull_shouldThrowEmptyDataException() {
        Exception exception = assertThrows(EmptyDataException.class, () ->
                ValidateData.validateName(null)
        );
        assertEquals("Name cannot be null or empty", exception.getMessage());
    }

    @Test
    void validateName_withEmpty_shouldThrowEmptyDataException() {
        Exception exception = assertThrows(EmptyDataException.class, () ->
                ValidateData.validateName("")
        );
        assertEquals("Name cannot be null or empty", exception.getMessage());
    }

    @Test
    void validateAuthor_withValidAuthor_shouldPass() {
        assertDoesNotThrow(() -> ValidateData.validateAuthor("Stephen King"));
    }

    @Test
    void validateAuthor_withNull_shouldThrowEmptyDataException() {
        Exception exception = assertThrows(EmptyDataException.class, () ->
                ValidateData.validateAuthor(null)
        );
        assertEquals("Author cannot be null or empty", exception.getMessage());
    }

    @Test
    void validateAuthor_withEmpty_shouldThrowEmptyDataException() {
        Exception exception = assertThrows(EmptyDataException.class, () ->
                ValidateData.validateAuthor("")
        );
        assertEquals("Author cannot be null or empty", exception.getMessage());
    }
}
