package com.github.ydydwang.http.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import com.github.ydydwang.http.parser.HeaderParser;
import com.github.ydydwang.http.parser.Parser;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;

public class Session {
	public static enum Method {
		GET, POST
	}
	public static enum Header {
		CONTENT_LENGTH, CONTENT_TYPE
	}
	public static enum ContentType {
		MUTIPART, X_WWW_FORM_URLENCODED, TEXT
	}

	private Method method;
	private ByteBuf uri;
	private int contentLength;
	private ContentType contentType;
	private List<ByteBuf> byteBufList = new ArrayList<ByteBuf>();
	private List<ByteBuf> headerList = new ArrayList<ByteBuf>();
	private List<ByteBuf> sliceList = new ArrayList<ByteBuf>();
	private ByteBuf content;
	private boolean releaseable = Boolean.TRUE;
	private int contentLengthReceived;
	private Parser parser = HeaderParser.instance;

	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public ByteBuf getUri() {
		return uri;
	}
	public void setUri(ByteBuf uri) {
		this.uri = uri;
	}
	public int getContentLength() {
		return contentLength;
	}
	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}
	public ContentType getContentType() {
		return contentType;
	}
	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}
	public List<ByteBuf> getByteBufList() {
		return byteBufList;
	}
	public void addByteBuf(ByteBuf byteBuf) {
		this.byteBufList.add(byteBuf);
	}
	public List<ByteBuf> getHeaderList() {
		return headerList;
	}
	public void addHeader(ByteBuf byteBuf) {
		this.headerList.add(byteBuf);
	}
	public List<ByteBuf> getSliceList() {
		return sliceList;
	}
	public void newSliceList() {
		sliceList = new ArrayList<ByteBuf>();
	}
	public void addSlice(ByteBuf byteBuf) {
		this.sliceList.add(byteBuf);
	}
	public ByteBuf getContent() {
		return content;
	}
	public void setContent(ByteBuf content) {
		this.content = content;
	}
	public int getContentLengthReceived() {
		return contentLengthReceived;
	}
	public void plusContentLengthReceived(int contentLengthReceived) {
		this.contentLengthReceived += contentLengthReceived;
	}
	public Parser getParser() {
		return parser;
	}
	public void setParser(Parser parser) {
		this.parser = parser;
	}
	public boolean isReleaseable() {
		if (this.releaseable) {
			synchronized(this) {
				if (this.releaseable) {
					this.releaseable = Boolean.FALSE;
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}
	public void releaseQietly() {
		releaseUri();
		releaseSliceList();
		releaseHeaderList();
		releaseContent();
		releaseByteBufList();
	}
	public void releaseUri() {
		if (uri != null) {
			releaseQietly(uri);
		}
	}
	public void releaseByteBufList() {
		for (int i = byteBufList.size() - 1; i >= NumberUtils.INTEGER_ZERO; i--) {
			releaseQietly(byteBufList.get(i));
		}
		byteBufList.clear();
	}
	public void releaseHeaderList() {
		for (int i = headerList.size() - 1; i >= NumberUtils.INTEGER_ZERO; i--) {
			releaseQietly(headerList.get(i));
		}
		headerList.clear();
	}
	public void releaseSliceList() {
		for (int i = sliceList.size() - 1; i >= NumberUtils.INTEGER_ZERO; i--) {
			releaseQietly(sliceList.get(i));
		}
		sliceList.clear();
	}
	public void releaseContent() {
		if (content != null) {
			releaseQietly(content);
		}
	}
	private static void releaseQietly(ByteBuf byteBuf) {
		try {
			if (byteBuf.refCnt() > NumberUtils.INTEGER_ZERO) {
				ReferenceCountUtil.release(byteBuf, byteBuf.refCnt());
			}
		} catch (Exception e) {
		}
	}
}