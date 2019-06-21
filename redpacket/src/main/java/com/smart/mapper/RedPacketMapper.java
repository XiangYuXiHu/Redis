package com.smart.mapper;


import com.smart.domain.RedPacket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RedPacketMapper {
    /***
     * 获取红包信息
     */
    RedPacket getRedPacket(Long id);
    /**
     * 减扣红包数
     */
    int decreaseRedPacket(Long id);
}
