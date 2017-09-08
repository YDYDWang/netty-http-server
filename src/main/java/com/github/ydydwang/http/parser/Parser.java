package com.github.ydydwang.http.parser;

import com.github.ydydwang.http.bean.Session;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface Parser {

	void parser(String address, ByteBuf byteBuf, Session session
			, ChannelHandlerContext ctx) throws Exception;
}
