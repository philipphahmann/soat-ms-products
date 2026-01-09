package br.com.postech.soat.domain.valueobject;

import br.com.postech.soat.domain.exception.InvalidProductImageException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ProductImageTest {

    @Test
    void constructor_whenUrlHttpValid_shouldCreateInstance() {
        String validUrl = "http://example.com/image.png";

        ProductImage productImage = assertDoesNotThrow(() -> new ProductImage(validUrl));

        assertNotNull(productImage);
        assertEquals(validUrl, productImage.value());
    }

    @Test
    void constructor_whenUrlHttpsValid_shouldCreateInstance() {
        String validUrl = "https://secure.example.com/foto.jpg?query=123";

        ProductImage productImage = assertDoesNotThrow(() -> new ProductImage(validUrl));

        assertNotNull(productImage);
        assertEquals(validUrl, productImage.value());
    }

    @Test
    void constructor_whenUrlNull_shouldThrowInvalidProductImageException() {
        Exception exception = assertThrows(InvalidProductImageException.class, () -> {
            new ProductImage(null);
        });

        assertEquals("URL da imagem não pode ser nula ou vazia.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void constructor_whenUrlIsBlank_shouldThrowInvalidProductImageException(String blankValue) {
        Exception exception = assertThrows(InvalidProductImageException.class, () -> {
            new ProductImage(blankValue);
        });

        assertEquals("URL da imagem não pode ser nula ou vazia.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "ftp://example.com/image.png",
        "file:///c:/image.png"
    })
    void constructor_whenSchemaIsInvalid_shouldThrowInvalidProductImageException(String invalidSchemeUrl) {
        Exception exception = assertThrows(InvalidProductImageException.class, () -> {
            new ProductImage(invalidSchemeUrl);
        });

        assertEquals("Url inválida: apenas http e https são permitidos.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "http://",
        "https://",
        "http://.com"
    })
    void constructor_whenHostIsInvalid_shouldThrowInvalidProductImageException(String noHostUrl) {
        Exception exception = assertThrows(InvalidProductImageException.class, () -> {
            new ProductImage(noHostUrl);
        });

        assertTrue(exception.getMessage().contains("URL inválida"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "nao e uma url",
            "http://example.com/imagem com espaco.png"
    })
    void constructor_whenFormatIsInvalid_shouldThrowInvalidProductImageException(String malformedUrl) {
        Exception exception = assertThrows(InvalidProductImageException.class, () -> {
            new ProductImage(malformedUrl);
        });

        assertEquals("URL inválida: " + malformedUrl, exception.getMessage());
    }
}