package com.smart.service;

public interface RedisRedPacketService {
	/**
	 * 保存redis抢红包列表
	 * @param redPacketId
	 * @param unitAmount
	 */
	void saveUserRedPacketByRedis(Long redPacketId, Double unitAmount);
}
