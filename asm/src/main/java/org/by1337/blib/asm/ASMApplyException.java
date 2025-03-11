package org.by1337.blib.asm;

public class ASMApplyException extends RuntimeException {
    public ASMApplyException(String message) {
        super(message);
    }

    public ASMApplyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ASMApplyException(Throwable cause) {
        super(cause);
    }

    public ASMApplyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ASMApplyException() {
    }
}
