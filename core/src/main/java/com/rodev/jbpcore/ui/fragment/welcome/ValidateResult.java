package com.rodev.jbpcore.ui.fragment.welcome;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

// TODO: создать пул объектов
@AllArgsConstructor
@NoArgsConstructor
public class ValidateResult {
    private boolean success = true;
    private String errorMessage;

    public void asError(String errorMessage) {
        success = false;
        this.errorMessage = errorMessage;
    }

    public void asSuccess() {
        success = true;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void restore() {
        success = true;
        errorMessage = null;
    }
}
