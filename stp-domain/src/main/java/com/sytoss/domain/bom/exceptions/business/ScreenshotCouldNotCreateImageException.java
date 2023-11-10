package com.sytoss.domain.bom.exceptions.business;

public class ScreenshotCouldNotCreateImageException extends RuntimeException {

    public ScreenshotCouldNotCreateImageException(Throwable cause) {
        super("Screenshot have been not created",cause);
    }
}
