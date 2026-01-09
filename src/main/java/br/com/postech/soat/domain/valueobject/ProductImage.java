package br.com.postech.soat.domain.valueobject;

import br.com.postech.soat.domain.exception.InvalidProductImageException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.lang3.StringUtils;

public record ProductImage(String value) {
    public ProductImage{
        validate(value);
    }

    private void validate(String value){
        if (StringUtils.isBlank(value)) {
            throw new InvalidProductImageException("URL da imagem não pode ser nula ou vazia.");
        }

        try {
            URI uri = new URI(value);
            String scheme = uri.getScheme();

            if (scheme == null || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))) {
                throw new InvalidProductImageException("Url inválida: apenas http e https são permitidos.");
            }
            if (uri.getHost() == null) {
                throw new InvalidProductImageException("URL inválida: domínio ausente.");
            }
        } catch (URISyntaxException e) {
            throw new InvalidProductImageException("URL inválida: " + value);
        }
    }
}
