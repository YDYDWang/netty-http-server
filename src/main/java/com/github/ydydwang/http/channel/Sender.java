package com.github.ydydwang.http.channel;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ydydwang.http.bean.Session;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

public class Sender {
	private static final Logger logger = LoggerFactory.getLogger(Sender.class);

	public void send(String address, ByteBuf byteBuf
			, ChannelHandlerContext ctx, Session session) throws Exception {
		writeAndFlush(address, byteBuf, ctx, session);
	}

	public void send(String address, List<ByteBuf> byteBufList
			, ChannelHandlerContext ctx, Session session) throws Exception {
		for (ByteBuf byteBuf: byteBufList) {
			ctx.write(duplicate(byteBuf));
		}
		ctx.flush();
		if (logger.isDebugEnabled()) {
			CompositeByteBuf compositeByteBuf = null;
			try {
				compositeByteBuf = Unpooled.compositeBuffer(byteBufList.size());
				for (ByteBuf byteBuf : byteBufList) {
					compositeByteBuf.addComponent(Boolean.TRUE, duplicate(byteBuf));
				}
				if (compositeByteBuf.capacity() <= 4 * 1024) {
					logger.debug(compositeByteBuf.toString(StandardCharsets.UTF_8));
				}
			} finally {
				if (compositeByteBuf != null) {
					compositeByteBuf.release();
				}
			}
		}
	}

	public void sendThenClose(String address, ByteBuf byteBuf
			, ChannelHandlerContext ctx, Session session) throws Exception {
		writeAndFlush(address, byteBuf, ctx, session).addListener(ChannelFutureListener.CLOSE);
	}

	protected ChannelFuture writeAndFlush(String address, ByteBuf byteBuf
			, ChannelHandlerContext ctx, Session session) throws Exception {
		ChannelFuture channelFuture = ctx.writeAndFlush(duplicate(byteBuf));
		if (logger.isDebugEnabled()) {
			byteBuf.resetReaderIndex();
			logger.debug(byteBuf.toString(StandardCharsets.UTF_8));
		}
		return channelFuture;
	}

	public final ChannelFuture pureWriteAndFlush(String address, ByteBuf byteBuf
			, ChannelHandlerContext ctx, Session session) throws Exception {
		return ctx.writeAndFlush(duplicate(byteBuf));
	}

	private ByteBuf duplicate(ByteBuf byteBuf) {
		ByteBuf duplicate = byteBuf.duplicate();
		duplicate.retain();
		return duplicate;
	}
}
