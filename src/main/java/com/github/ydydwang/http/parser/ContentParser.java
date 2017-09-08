package com.github.ydydwang.http.parser;

import com.github.ydydwang.http.bean.Session;
import com.github.ydydwang.http.sample.Sample;
import com.github.ydydwang.http.util.SenderUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ContentParser extends AbstractParser {
	public static final Parser instance = new ContentParser();

	@Override
	public void parser(String address, ByteBuf byteBuf, Session session
			, ChannelHandlerContext ctx) throws Exception {
		session.plusContentLengthReceived(byteBuf.readableBytes());
		if (session.getContentLengthReceived() >= session.getContentLength()) {
			addToContent(session, byteBuf);
//			System.out.println(session.getMethod());
//			System.out.println(session.getUri().toString(StandardCharsets.UTF_8));
//			System.out.println(session.getContentLength());
//			System.out.println(session.getContentType());
//			System.out.println(session.getContent().toString(StandardCharsets.UTF_8));
//			System.out.println();
			SenderUtils.sendThenClose(address, Sample.response, ctx, session);
		} else {
			session.addSlice(byteBuf);
		}
	}

}
