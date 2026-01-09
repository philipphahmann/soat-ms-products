package br.com.postech.soat.domain.exception;

import br.com.postech.soat.commons.infrastructure.exception.BaseException;

public class InvalidProductSKUException extends BaseException {
    public InvalidProductSKUException(String message) {
        super(message);
    }
}