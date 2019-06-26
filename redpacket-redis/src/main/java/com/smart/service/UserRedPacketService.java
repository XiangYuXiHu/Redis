package com.smart.service;

public interface UserRedPacketService {
    /**
     *
     * @param redPacketId
     * @param userId
     * @return
     */
    Long grapRedPacketByRedis(Long redPacketId, Long userId);
}
