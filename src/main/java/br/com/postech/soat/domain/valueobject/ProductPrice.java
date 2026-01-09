package br.com.postech.soat.domain.valueobject;

import br.com.postech.soat.domain.exception.InvalidProductPriceException;
import java.math.BigDecimal;

public record ProductPrice(BigDecimal value) {
    public ProductPrice{
        validate(value);
    }

    private void validate(BigDecimal value) {
        if (value == null) {
            throw new InvalidProductPriceException("O valor do produto não pode ser nulo");
        }

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidProductPriceException("O valor do produto não pode menor ou igual a que zero");
        }
    }
}
