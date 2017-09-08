package com.github.ydydwang.http.cache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;

public class ByteBufCacheManager {
	private static final Map<String, List<ByteBuf>> map = new HashMap<String, List<ByteBuf>>();

	public static void add(String address, ByteBuf byteBuf) {
		if (!map.containsKey(address)) {
			synchronized (map) {
				if (!map.containsKey(address)) {
					map.put(address, new LinkedList<ByteBuf>());
				}
			}
		}
		map.get(address).add(byteBuf);
	}

	public static CompositeByteBuf get(String address) {
		List<ByteBuf> byteBufList = map.get(address);
		CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer(byteBufList.size());
		for (ByteBuf byteBuf : byteBufList) {
			compositeByteBuf.addComponent(Boolean.TRUE, byteBuf);
		}
		return compositeByteBuf;
	}

	public static void remove(String address) {
		List<ByteBuf> byteBufList = map.get(address);
		if (CollectionUtils.isNotEmpty(byteBufList)) {
			byteBufList.forEach(byteBuf -> {
				try {
					ReferenceCountUtil.release(byteBuf, byteBuf.refCnt());
				} catch (Exception e) {
				}
			});
			map.remove(address);
		}
	}
}
