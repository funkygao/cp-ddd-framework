package io.github.dddplus.runtime.policy;

import io.github.dddplus.runtime.IExceptionWeakLogging;
import io.github.dddplus.runtime.IExceptionIgnoreProfilerError;

public class MyBusinessException extends RuntimeException implements IExceptionWeakLogging, IExceptionIgnoreProfilerError {
    public MyBusinessException(String reason) {
        super(reason);
    }
}
