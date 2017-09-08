package com.github.ydydwang.http.util;

import java.net.InetSocketAddress;

import io.netty.channel.ChannelHandlerContext;

public class SocketAddressUtils {
	private static final String COLON = ":";

	public static String getAddress(ChannelHandlerContext ctx) {
		InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
		return socketAddress.getHostString() + COLON + socketAddress.getPort();
	}

	public static String getIp(ChannelHandlerContext ctx) {
		InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
		return socketAddress.getHostString();
	}

}
