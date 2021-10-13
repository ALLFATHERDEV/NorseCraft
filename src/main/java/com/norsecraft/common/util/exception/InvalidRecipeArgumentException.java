package com.norsecraft.common.util.exception;

import org.apache.logging.log4j.message.ParameterizedMessage;

public class InvalidRecipeArgumentException extends RuntimeException {

    public InvalidRecipeArgumentException(String message, Object... args) {
        super(ParameterizedMessage.format(message, args));
    }

}
