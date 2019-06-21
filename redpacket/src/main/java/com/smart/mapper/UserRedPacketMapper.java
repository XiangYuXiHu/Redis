package com.smart.mapper;

import com.smart.domain.UserRedPacket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRedPacketMapper {
    /**
     * 插入抢红包信息
     */
    int grabRedPacket(UserRedPacket userRedPacket);
}
