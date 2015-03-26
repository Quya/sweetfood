/**
 * BusinessException.java
 * @Date 2007-4-16
 * @Copyright www.hollyinfo.com 2002-2007
 */
package com.quya.common.utils.exception;

import org.springframework.core.NestedRuntimeException;

public class BusinessException extends NestedRuntimeException {
	
	private int exceptionType;

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 构造方法
     * @param message
     */
    public BusinessException(String message) {
        super(message);
    }
    /**
     * 构造方法

     * @param message
     */
    public BusinessException(String message , int exceptionType) {
        super(message);
        this.exceptionType = exceptionType;
    }
    public int getExceptionType() {
		return exceptionType;
	}
	public void setExceptionType(int exceptionType) {
		this.exceptionType = exceptionType;
	}
	/**
     * 构造方法
     * @param message
     * @param cause
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

}
