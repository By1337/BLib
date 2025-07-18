package org.by1337.blib.core.nms.verify;

import java.util.ArrayList;
import java.util.List;

public class NMSVerifyException extends Exception {
    public NMSVerifyException() {
    }

    public NMSVerifyException(String message) {
        super(message);
    }

    public NMSVerifyException(String message, Throwable cause) {
        super(message, cause);
    }

    public NMSVerifyException(Throwable cause) {
        super(cause);
    }

    public NMSVerifyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NMSVerifyException trim() {
        StackTraceElement[] stack = getStackTrace();
        List<StackTraceElement> newStack = new ArrayList<>();
        boolean hasNms = false;
        for (StackTraceElement stackTraceElement : stack) {
            if (!stackTraceElement.getClassName().startsWith("net.minecraft.")) {
                newStack.add(stackTraceElement);
            } else if (!hasNms) {
                hasNms = true;
                newStack.add(new StackTraceElement("from nms", "any", "any", 0));
            }
        }
        setStackTrace(newStack.toArray(new StackTraceElement[0]));
        if (getCause() != null && getCause() instanceof NMSVerifyException e) {
            e.clearStackTrace();
        }
        for (Throwable throwable : getSuppressed()) {
            if (throwable instanceof NMSVerifyException e) {
                e.trim();
            }
        }
        return this;
    }

    private void clearStackTrace() {
        if (getCause() == null) return;
        setStackTrace(new StackTraceElement[0]);
        if (getCause() instanceof NMSVerifyException e) {
            e.clearStackTrace();
        }
    }
}
