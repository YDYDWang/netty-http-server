package com.github.ydydwang.http.channel;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ydydwang.http.bean.Session;
import com.github.ydydwang.http.cache.ByteBufCacheManager;
import com.github.ydydwang.http.cache.SessionCacheManager;
import com.github.ydydwang.http.util.SocketAddressUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class ChannelInboundHandler extends ChannelInboundHandlerAdapter {
	protected static final Logger logger = LoggerFactory.getLogger(ChannelInboundHandler.class);
	private static final String STRING_FORMAT_CONNECTION_OPEN = "Connection from: %s";
	private static final String STRING_FORMAT_CONNECTION_CLOSE = "Disconnected from: %s";

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		String address = SocketAddressUtils.getAddress(ctx);
		SessionCacheManager.put(address, new Session());
		if (logger.isDebugEnabled()) {
			logger.debug(String.format(STRING_FORMAT_CONNECTION_OPEN, address));
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		String address = SocketAddressUtils.getAddress(ctx);
		Session session = SessionCacheManager.get(address);
		session.addByteBuf((ByteBuf) msg);
//		System.out.println((ByteBuf) msg);
		System.out.println(((ByteBuf) msg).toString(StandardCharsets.UTF_8));
		try {
			session.getParser().parser(address, (ByteBuf) msg, session, ctx);
		} catch (Exception e) {
		} finally {
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		String address = SocketAddressUtils.getAddress(ctx);
		release(address);
		logger.error(cause.getMessage(), cause.getCause());
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		String address = SocketAddressUtils.getAddress(ctx);
		ByteBufCacheManager.remove(address);
		release(address);
		if (logger.isDebugEnabled()) {
			logger.debug(String.format(STRING_FORMAT_CONNECTION_CLOSE, address));
		}
	}

	private static void release(String address) {
		Session session = SessionCacheManager.get(address);
		if (session != null && session.isReleaseable()) {
			session.releaseQietly();
			SessionCacheManager.remove(address);
		}
	}
}
