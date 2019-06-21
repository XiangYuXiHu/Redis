package com.smart.service.impl;

import com.smart.domain.RedPacket;
import com.smart.mapper.RedPacketMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.smart.service.RedPacketService;

@Service
public class RedPacketServiceImpl  implements RedPacketService{

    @Autowired
    private RedPacketMapper redPacketMapper;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public RedPacket getRedPacket(Long id) {
        return redPacketMapper.getRedPacket(id);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int decreaseRedPacket(Long id) {
        return redPacketMapper.decreaseRedPacket(id);
    }
}
