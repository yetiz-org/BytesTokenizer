package org.yetiz.utils.netty.exception;

/**
 * Created by yeti on 2016/5/5.
 */
public class InvalidArgumentException extends RuntimeException {
	public InvalidArgumentException() {
	}

	public InvalidArgumentException(String message) {
		super(message);
	}

	public InvalidArgumentException(Throwable cause) {
		super(cause);
	}
}
