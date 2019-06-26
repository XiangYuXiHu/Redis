package com.smart.service.impl;

import com.smart.service.RedisRedPacketService;
import com.smart.service.UserRedPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class UserRedPacketServiceImpl implements UserRedPacketService {

    @Autowired
    RedisTemplate<String,Serializable> redisTemplate;
    @Autowired
    RedisRedPacketService redisRedPacketService;
    @Autowired
    DefaultRedisScript<String> redisScript;

    @Override
    public Long grapRedPacketByRedis(Long redPacketId, Long userId) {
        String args=userId+"-"+System.currentTimeMillis();
        String key=String.valueOf(redPacketId);

        Object res = redisTemplate.execute((RedisConnection connection) -> connection.eval(
                redisScript.getScriptAsString().getBytes(),
                ReturnType.INTEGER,
                1,
                key.getBytes(),
                args.getBytes()));
        Long result= (Long) res;
        if(result==2){
            String unitAmountStr = (String) redisTemplate.opsForHash().get("red_packet_" + redPacketId,"unit_amount");
            Double unitAmount = Double.valueOf(unitAmountStr);
            redisRedPacketService.saveUserRedPacketByRedis(redPacketId,unitAmount);
        }
        return result;
    }
}
