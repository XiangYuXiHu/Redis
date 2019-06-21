package com.smart.service;

import com.smart.domain.RedPacket;

public interface RedPacketService {
    /**
     * 获取红包
     */
    RedPacket getRedPacket(Long id);
    /**
     * 扣减红包
     */
    int decreaseRedPacket(Long id);
}
