package com.syncd.exceptions;

public class CustomException extends RuntimeException {
    private final ErrorInfo errorInfo;
    private final String detailedMessage;

    public CustomException(ErrorInfo errorInfo, String additionalDetail) {
        super(errorInfo.getMessage());
        this.errorInfo = errorInfo;
        this.detailedMessage = createMessage(errorInfo, additionalDetail);
    }

    private static String createMessage(ErrorInfo errorInfo, String additionalDetail) {
        return errorInfo.getMessage() + (additionalDetail != null ? ": " + additionalDetail : "");
    }

    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }
}