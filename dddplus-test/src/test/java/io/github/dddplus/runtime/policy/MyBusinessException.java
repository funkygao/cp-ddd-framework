package io.github.dddplus.runtime.policy;

import io.github.dddplus.IExceptionWeakLogging;
import io.github.dddplus.IExceptionIgnoreProfilerError;

public class MyBusinessException extends RuntimeException implements IExceptionWeakLogging, IExceptionIgnoreProfilerError {
    public MyBusinessException(String reason) {
        super(reason);
    }
}
