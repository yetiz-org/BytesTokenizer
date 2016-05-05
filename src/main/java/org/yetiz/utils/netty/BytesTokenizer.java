package org.yetiz.utils.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.yetiz.utils.netty.exception.InvalidArgumentException;
import org.yetiz.utils.netty.exception.StreamIOException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by yeti on 2016/5/5.
 */
public class BytesTokenizer {
	private ByteBuf data;
	private byte[] separator;
	private Queue<ByteBuf> splits = new ArrayDeque<>();
	private int index = 0;

	public BytesTokenizer(byte[] data, byte[] separator) {
		this(data, separator, true);
	}

	public BytesTokenizer(byte[] data, byte[] separator, boolean directBuffer) {
		if (separator == null) {
			throw new InvalidArgumentException("separator can't be null");
		}

		initByteBuf(directBuffer);
		this.separator = separator;
		this.data.writeBytes(data);
		split();
	}

	public BytesTokenizer(InputStream inputStream, byte[] separator) {
		this(inputStream, separator, true);
	}

	public BytesTokenizer(InputStream inputStream, byte[] separator, boolean directBuffer) {
		if (separator == null) {
			throw new InvalidArgumentException("separator can't be null");
		}

		initByteBuf(directBuffer);
		this.separator = separator;
		load(inputStream);
		split();
	}

	private void initByteBuf(boolean directBuffer) {
		if (directBuffer) {
			data = Unpooled.directBuffer();
		} else {
			data = Unpooled.buffer();
		}
	}

	private void split() {
		ByteBuf wrapped = Unpooled.wrappedBuffer(data);

		int startIndex = 0;
		int boundIndex;
		boolean matched = true;
		while (wrapped.readableBytes() > 0) {
			if (wrapped.readByte() == separator[0]) {
				boundIndex = wrapped.readerIndex() - 1;

				for (int i = 1; i < separator.length; i++) {
					if (wrapped.readByte() != separator[i]) {
						matched = false;
						break;
					}
				}

				if (matched) {
					splits.offer(
						wrapped
							.readerIndex(startIndex)
							.readBytes(boundIndex - startIndex));

					wrapped.skipBytes(separator.length);
					startIndex = wrapped.readerIndex();
				} else {
					wrapped.readerIndex(boundIndex + 1);
					matched = true;
				}
			}
		}

		if (wrapped.readerIndex(startIndex).isReadable()) {
			splits.offer(wrapped.readBytes(wrapped.readableBytes()));
		}
	}

	public boolean hasNext() {
		return splits.peek() != null;
	}

	public ByteBuf next() {
		return splits.poll();
	}

	public void close() {
		this.data.release();
	}

	private void load(InputStream inputStream) {
		try {
			while (inputStream.available() > 0) {
				byte[] reads = new byte[1024];
				int length = inputStream.read(reads);
				data.writeBytes(reads, 0, length);
			}
		} catch (IOException e) {
			throw new StreamIOException(e);
		}
	}
}
