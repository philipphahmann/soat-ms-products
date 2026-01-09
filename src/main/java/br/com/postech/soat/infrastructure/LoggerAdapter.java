package br.com.postech.soat.infrastructure;

import br.com.postech.soat.application.adapters.LoggerPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggerAdapter implements LoggerPort {
    private final Logger logger = LoggerFactory.getLogger(LoggerAdapter.class);

    @Override
    public void info(String message) {
        logger.info(message);
    }

//    @Override
//    public void warn(String message) {
//        logger.warn(message);
//    }

//    @Override
//    public void error(String message) {
//        logger.error(message);
//    }
}
