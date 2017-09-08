package com.github.ydydwang.http.consts;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class ByteBufs {
	public static final ByteBuf DOT = toByteBuf(",");
	public static final ByteBuf START_OBJECT = toByteBuf("{");
	public static final ByteBuf END_OBJECT = toByteBuf("}");
	public static final ByteBuf START_ARRAY = toByteBuf("[");
	public static final ByteBuf END_ARRAY = toByteBuf("]");
	public static final ByteBuf QUOTE = toByteBuf("\"");
	public static final ByteBuf STATUS_ONE_KV = toByteBuf("\"status\":1");
	public static final ByteBuf STATUS_ONE_KVO = toByteBuf("{\"status\":1}");
	public static final ByteBuf STATUS_TWO_KVO = toByteBuf("{\"status\":2}");
	public static final ByteBuf SYNC_VERSION_K = toByteBuf("\"sync_version\":");
	public static final ByteBuf ACTIVE_TYPES_K = toByteBuf("\"active_types\":");
	public static final ByteBuf ACTIVE_TYPE_FOUR_KVO = toByteBuf("{\"active_type\":4}");
	public static final ByteBuf ACTIVE_TYPE_FIVE_KVO = toByteBuf("{\"active_type\":5}");
	public static final ByteBuf KEY_K = toByteBuf("\"key\":");
	public static final ByteBuf ACTIVE_TYPE_ONE_KV = toByteBuf("\"active_type\":1");
	public static final ByteBuf ACTIVE_TYPE_THREE_KV = toByteBuf("\"active_type\":3");
	public static final ByteBuf ACTIVE_TYPE_SIX_KV = toByteBuf("\"active_type\":6");
	public static final ByteBuf ACTIVE_TYPE_EIGHT_KV = toByteBuf("\"active_type\":8");
	public static final ByteBuf IP_K = toByteBuf("\"ip\":");
	public static final ByteBuf PORT_K = toByteBuf("\"port\":");
	public static final ByteBuf APP_VERSION_K = toByteBuf("\"app_version\":");
	public static final ByteBuf ACTIVE_TYPE_SEVEN_KV = toByteBuf("\"active_type\":7");
	public static final ByteBuf TIME_K = toByteBuf("\"time\":");
	public static final ByteBuf ACTIVE_TYPE_NINE_KV = toByteBuf("\"active_type\":9");
	public static final ByteBuf CONTENT_K = toByteBuf("\"content\":");
	public static final ByteBuf SCHEDULE_K = toByteBuf("\"schedule\":");
	public static final ByteBuf BEACON_LIB_VERSION_K = toByteBuf("\"beacon_lib_version\":");
	public static final ByteBuf BEACON_K = toByteBuf("\"beacon\":");
	public static final ByteBuf FILE_SIZE_K = toByteBuf("\"file_size\":");
	public static final ByteBuf EMPTY_ARRAY = toByteBuf("[]");

	public static ByteBuf toByteBuf(String string) {
		byte[] bytes = string.getBytes();
		return toByteBuf(bytes);
	}

	public static ByteBuf toByteBuf(byte[] bytes) {
		return PooledByteBufAllocator.DEFAULT.directBuffer(bytes.length).writeBytes(bytes);
	}
}
