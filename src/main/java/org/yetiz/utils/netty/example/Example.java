package org.yetiz.utils.netty.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import org.yetiz.utils.netty.BytesTokenizer;

/**
 * Created by yeti on 2016/5/5.
 */
public class Example {
	public static void main(String... args) {
		byte[] data = new byte[]{0x01, 0x02, 0x03, 0x05, 0x05, 0x03, 0x02, 0x01, 0x05, 0x05, 0x03, 0x05, 0x05};
		byte[] separator = new byte[]{0x05, 0x05};

		BytesTokenizer tokenizer = new BytesTokenizer(data, separator);
		while (tokenizer.hasNext()) {
			ByteBuf buf = tokenizer.next();
			while (buf.isReadable()) {
				System.out.print(buf.readByte());
			}

			System.out.println();
		}

		tokenizer = new BytesTokenizer(new ByteBufInputStream(Unpooled.wrappedBuffer(data)), separator);
		while (tokenizer.hasNext()) {
			ByteBuf buf = tokenizer.next();
			while (buf.isReadable()) {
				System.out.print(buf.readByte());
			}

			System.out.println();
		}
	}
}
