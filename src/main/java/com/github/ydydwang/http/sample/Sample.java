package com.github.ydydwang.http.sample;

import com.github.ydydwang.http.consts.ByteBufs;

import io.netty.buffer.ByteBuf;

public class Sample {
	public static final ByteBuf response = ByteBufs.toByteBuf("HTTP/1.1 200 OK\r\n"
			+ "Content-Type: text/plain\r\n"
			+ "Content-Length: 10\r\n"
			+ "\r\n"
			+ "HELLO NAHA\r\n");
}
