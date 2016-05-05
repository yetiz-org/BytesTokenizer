package org.yetiz.utils.netty.exception;

/**
 * Created by yeti on 2016/5/5.
 */
public class StreamIOException extends RuntimeException {
	public StreamIOException() {
	}

	public StreamIOException(String message) {
		super(message);
	}

	public StreamIOException(Throwable cause) {
		super(cause);
	}
}
