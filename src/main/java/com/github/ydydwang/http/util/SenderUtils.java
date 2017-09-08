package com.github.ydydwang.http.util;

import java.util.List;

import com.github.ydydwang.http.bean.Session;
import com.github.ydydwang.http.channel.Sender;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class SenderUtils {
	private static Sender sender;

	public static void setInstance(Sender sender) {
		SenderUtils.sender = sender;
	}

	public static void send(String address, ByteBuf byteBuf
			, ChannelHandlerContext ctx, Session session) throws Exception {
		sender.send(address, byteBuf, ctx, session);
	}

	public static void send(String address, List<ByteBuf> byteBufList
			, ChannelHandlerContext ctx, Session session) throws Exception {
		sender.send(address, byteBufList, ctx, session);
	}

	public static void sendThenClose(String address, ByteBuf byteBuf
			, ChannelHandlerContext ctx, Session session) throws Exception {
		sender.sendThenClose(address, byteBuf, ctx, session);
	}

	public static void pureWriteAndFlush(String address, ByteBuf byteBuf
			, ChannelHandlerContext ctx, Session session) throws Exception {
		sender.pureWriteAndFlush(address, byteBuf, ctx, session);
	}

}
