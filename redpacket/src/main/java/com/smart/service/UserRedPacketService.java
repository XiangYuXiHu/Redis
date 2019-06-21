package com.smart.service;

public interface UserRedPacketService {
    /**
     * 保存抢红包的信息
     */
    int grapRedPacket(Long redPacketId,Long userId);
}
