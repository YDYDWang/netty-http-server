package com.github.ydydwang.http.parser;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.github.ydydwang.http.bean.Session;
import com.github.ydydwang.http.consts.ByteBufs;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public abstract class AbstractParser implements Parser {
	protected static final ByteBuf contentLength = ByteBufs.toByteBuf("Content-Length");
	protected static final ByteBuf contentType = ByteBufs.toByteBuf("Content-Type");
	protected static final ByteBuf multipart = ByteBufs.toByteBuf("multipart");
	protected static final ByteBuf text = ByteBufs.toByteBuf("text");
	protected static final ByteBuf application = ByteBufs.toByteBuf("application");
	protected static final ByteBuf space = ByteBufs.toByteBuf(" ");
	protected static final ByteBuf colon = ByteBufs.toByteBuf(":");
	protected static final ByteBuf semicolon = ByteBufs.toByteBuf(";");

	public static boolean isNotEmpty(ByteBuf byteBuf) {
		return byteBuf.readableBytes() > NumberUtils.INTEGER_ZERO;
	}

	public static void addToContent(Session session, ByteBuf content) {
		if (CollectionUtils.isNotEmpty(session.getSliceList())) {
			CompositeByteBuf compositeByteBuf = PooledByteBufAllocator.DEFAULT
					.compositeBuffer(session.getSliceList().size() + NumberUtils.INTEGER_ONE);
			for (ByteBuf byteBuf : session.getSliceList()) {
				compositeByteBuf.addComponent(byteBuf);
			}
			session.setContent(compositeByteBuf);
			session.releaseSliceList();
		} else {
			session.setContent(content);
		}
	}
}
