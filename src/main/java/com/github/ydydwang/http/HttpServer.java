package com.github.ydydwang.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ydydwang.http.channel.ChannelInboundHandler;
import com.github.ydydwang.http.channel.Sender;
import com.github.ydydwang.http.util.SenderUtils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public final class HttpServer {
	protected static final Logger logger = LoggerFactory.getLogger(ChannelInboundHandler.class);

	static final int PORT = 8080;

	public static void main(String[] args) throws Exception {
		SenderUtils.setInstance(new Sender());
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(10);
		ServerBootstrap b = new ServerBootstrap()
				.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 100)
				.childHandler(new ChannelInboundHandler());
		b.bind(PORT).sync();
	}

}
