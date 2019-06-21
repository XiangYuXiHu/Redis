package com.smart.service.impl;

import com.smart.domain.RedPacket;
import com.smart.domain.UserRedPacket;
import com.smart.mapper.RedPacketMapper;
import com.smart.mapper.UserRedPacketMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.smart.service.UserRedPacketService;

@Service
public class UserRedPacketServiceImpl implements UserRedPacketService {

    @Autowired
    private UserRedPacketMapper userRedPacketMapper;
    @Autowired
    private RedPacketMapper redPacketMapper;

    private static final int FAILED=0;
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int grapRedPacket(Long redPacketId, Long userId) {
        RedPacket redPacket = redPacketMapper.getRedPacket(redPacketId);
        Integer leftRedPacket = redPacket.getStock();
        if(leftRedPacket > 0){
            redPacketMapper.decreaseRedPacket(redPacketId);
            /**
             * 生成抢红包信息
             */
            UserRedPacket userRedPacket = new UserRedPacket();
            userRedPacket.setRedPacketId(redPacketId);
            userRedPacket.setUserId(userId);
            userRedPacket.setAmount(redPacket.getUnitAmount());
            int result = userRedPacketMapper.grabRedPacket(userRedPacket);
            return result;
        }
        return FAILED;
    }
}
