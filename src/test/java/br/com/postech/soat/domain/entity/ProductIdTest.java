package br.com.postech.soat.domain.entity;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ProductIdTest {
    
    @Test
    void shouldGenerateValidId() {
        ProductId productId = ProductId.generate();
        assertNotNull(productId);
        assertNotNull(productId.getValue());
    }

    @Test
    void shouldCreateFromExistingUUID() {
        UUID uuid = UUID.randomUUID();
        ProductId productId = ProductId.of(uuid);
        
        assertNotNull(productId);
        assertEquals(uuid, productId.getValue());
    }

    @Test
    void shouldThrowExceptionWhenUuidIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new ProductId(null);
        });
        
        assertEquals("ProductId não pode ser nulo", exception.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        ProductId id1 = ProductId.of(uuid1);
        ProductId id1Copy = ProductId.of(uuid1);
        ProductId id2 = ProductId.of(uuid2);

        // Testar igualdade
        assertEquals(id1, id1); // O mesmo objeto
        assertEquals(id1, id1Copy); // Objetos diferentes, mesmo valor
        assertNotEquals(id1, id2); // Valores diferentes
        assertNotEquals(id1, null); // Com nulo
        assertNotEquals(id1, "Uma String Qualquer"); // Outra classe

        // Testar HashCode
        assertEquals(id1.hashCode(), id1Copy.hashCode());
        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testToString() {
        UUID uuid = UUID.randomUUID();
        ProductId productId = ProductId.of(uuid);

        assertEquals(uuid.toString(), productId.toString());
    }
}
