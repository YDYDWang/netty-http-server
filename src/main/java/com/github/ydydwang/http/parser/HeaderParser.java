package com.github.ydydwang.http.parser;

import java.nio.charset.StandardCharsets;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.github.ydydwang.http.bean.Session;
import com.github.ydydwang.http.bean.Session.ContentType;
import com.github.ydydwang.http.bean.Session.Method;
import com.github.ydydwang.http.consts.CompleteType;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;

public class HeaderParser extends AbstractParser {
	public static final Parser instance = new HeaderParser();

	@Override
	public void parser(String address, ByteBuf byteBuf, Session session
			, ChannelHandlerContext ctx) throws Exception {
		int readerIndex = NumberUtils.INTEGER_ZERO;
		boolean stillHeader = Boolean.TRUE;
		loop : for (int i = NumberUtils.INTEGER_ZERO; i < byteBuf.readableBytes(); i++) {
			switch (byteBuf.getByte(i)) {
				case CompleteType.LF:
					ByteBuf header = byteBuf.slice(readerIndex, i - readerIndex - NumberUtils.INTEGER_ONE);
					readerIndex = ++i;
					if (handleHeader(header, session) == Boolean.FALSE) {
						stillHeader = Boolean.FALSE;
						break loop;
					}
				break;
			}
		}
		if (stillHeader) {
			if (readerIndex < byteBuf.readableBytes()) {
				ByteBuf slice = byteBuf.slice(readerIndex, byteBuf.readableBytes() - readerIndex);
				session.addSlice(slice);
			}
		} else if (session.getContentLength() > NumberUtils.INTEGER_ZERO) {
			session.setParser(ContentParser.instance);
			if (readerIndex < byteBuf.readableBytes()) {
				ByteBuf slice = byteBuf.slice(readerIndex, byteBuf.readableBytes() - readerIndex);
				session.getParser().parser(address, slice, session, ctx);
			}
		}
	}

	public static boolean validate(Session session) {
		if (session.getMethod() == null
				|| session.getUri() == null
				|| session.getContentType() == null) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public static void extracHeaders(Session session) {
		extracFirstLine(session);
		for (int i = NumberUtils.INTEGER_ONE; i < session.getHeaderList().size(); i++) {
			ByteBuf header = session.getHeaderList().get(i);
			if (startWith(header, contentLength)) {
				int skipLength = skip(header, contentLength.readableBytes());
				int from = contentLength.readableBytes() + skipLength;
				int contentLength = Integer.valueOf(header.slice(from, header.readableBytes() - from).toString(StandardCharsets.UTF_8));
				session.setContentLength(contentLength);
			} else if (startWith(header, contentType)) {
				int skipLength = skip(header, contentType.readableBytes());
				int from = contentLength.readableBytes() + skipLength;
				if (startWith(header, text, from)) {
					session.setContentType(ContentType.TEXT);;
				} else if (startWith(header, multipart, from)) {
					session.setContentType(ContentType.MUTIPART);;
				} else if (startWith(header, application, from)) {
					session.setContentType(ContentType.X_WWW_FORM_URLENCODED);;
				}
			}
		}
	}

	public static int skip(ByteBuf byteBuf, int from) {
		int length = NumberUtils.INTEGER_ZERO;
		for (int i = from; i < byteBuf.readableBytes(); i++) {
			byte b = byteBuf.getByte(i);
			if (b == ':' || b == ' ') {
				length++;
			} else {
				break;
			}
		}
		return length;
	}

	public static boolean startWith(ByteBuf header, ByteBuf prefix) {
		if (header.readableBytes() > prefix.readableBytes()) {
			for (int i = NumberUtils.INTEGER_ZERO; i < prefix.readableBytes(); i++) {
				if (prefix.getByte(i) != header.getByte(i)) {
					return Boolean.FALSE;
				}
			}
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public static boolean startWith(ByteBuf header, ByteBuf prefix, int from) {
		// FIXME check it is better if ((header.readableBytes() - from) > prefix.readableBytes()) {
		if (header.readableBytes() > prefix.readableBytes()) {
			for (int i = from; i < prefix.readableBytes(); i++) {
				if (prefix.getByte(i) != header.getByte(i)) {
					return Boolean.FALSE;
				}
			}
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public static void extracFirstLine(Session session) {
		ByteBuf header = session.getHeaderList().get(NumberUtils.INTEGER_ZERO);
		if (header.getByte(0) == 'G' && header.getByte(1) == 'E' && header.getByte(2) == 'T') {
			session.setMethod(Method.GET);
			extracUri(header, session, 4);
		} else if (header.getByte(0) == 'P' && header.getByte(1) == 'O' && header.getByte(2) == 'S' && header.getByte(3) == 'T') {
			session.setMethod(Method.POST);
			extracUri(header, session, 5);
		}
	}

	public static void extracUri(ByteBuf header, Session session, int from) {
		for (int i = from + NumberUtils.INTEGER_ONE; i < header.readableBytes(); i++) {
			switch (header.getByte(i)) {
				case ' ':
					ByteBuf uri = header.slice(from, i - from);
					session.setUri(uri);
					i = header.readableBytes();
				break;
			}
		}
	}

	public static boolean handleHeader(ByteBuf header, Session session) {
		if (isNotEmpty(header)) {
			addToHeaderList(session, header);
			return Boolean.TRUE;
		}
		extracHeaders(session);
		if (validate(session)) {
			// TODO RESPONSE HEADER MISSING
		}
		return Boolean.FALSE;
	}

	public static void addToHeaderList(Session session, ByteBuf header) {
		if (CollectionUtils.isNotEmpty(session.getSliceList())) {
			CompositeByteBuf compositeByteBuf = PooledByteBufAllocator.DEFAULT
					.compositeBuffer(session.getSliceList().size() + NumberUtils.INTEGER_ONE);
			for (ByteBuf byteBuf : session.getSliceList()) {
				compositeByteBuf.addComponent(byteBuf);
			}
			session.addHeader(compositeByteBuf);
			session.releaseSliceList();
		} else {
			session.addHeader(header);
		}
	}
}
