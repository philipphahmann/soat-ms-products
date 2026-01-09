package br.com.postech.soat.domain.exception;

import br.com.postech.soat.commons.infrastructure.exception.BaseException;

public class InvalidProductCategoryException extends BaseException {
    public InvalidProductCategoryException(String message) {
        super(message);
    }
}
