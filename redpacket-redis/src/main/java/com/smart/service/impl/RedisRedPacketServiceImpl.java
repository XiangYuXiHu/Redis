package com.smart.service.impl;

import com.smart.model.UserRedPacket;
import com.smart.service.RedisRedPacketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RedisRedPacketServiceImpl implements RedisRedPacketService {

    private static Logger logger = LoggerFactory.getLogger(RedisRedPacketServiceImpl.class);
    private static final String PREFIX = "red_packet_list_";
    /**
     * 每次取出1000条数据
     */
    private static final int TIME_SIZE = 1000;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DataSource dataSource;

    @Async
    public void saveUserRedPacketByRedis(Long redPacketId, Double unitAmount) {
        logger.info("----------开始保存数据----------------");
        long start = System.currentTimeMillis();
        BoundListOperations ops = redisTemplate.boundListOps(PREFIX + redPacketId);
        Long SIZE = ops.size();
        Long times = SIZE % TIME_SIZE == 0 ? SIZE/TIME_SIZE : (SIZE/TIME_SIZE+1);

        int count=0;
        List<UserRedPacket> userRedPackets=new ArrayList<>(TIME_SIZE);
        for(int i=0;i<times;i++){
            List userIdList=null;
            if(i==0){
                userIdList = ops.range(i * TIME_SIZE, (i + 1) * TIME_SIZE);
            }else{
                userIdList=ops.range(i*TIME_SIZE+1,(i+1)*TIME_SIZE);
            }
            userRedPackets.clear();
            for(int j=0;j<userIdList.size();j++){
                String  values= userIdList.get(j).toString();
                String[] arr = values.split("-");
                String userIdStr=arr[0];
                String timeStr=arr[1];
                long userId = Long.parseLong(userIdStr);
                long time = Long.parseLong(timeStr);

                UserRedPacket userRedPacket = new UserRedPacket();
                userRedPacket.setRedPacketId(redPacketId);
                userRedPacket.setUserId(userId);
                userRedPacket.setGrabTime(new Date(time));
                userRedPacket.setAmount(unitAmount);
                userRedPacket.setNote("抢红包 "+redPacketId);
                userRedPackets.add(userRedPacket);
            }
            count+=executeBatch(userRedPackets);
        }
        redisTemplate.delete(PREFIX+redPacketId);
        long end=System.currentTimeMillis();
        System.out.println("保存数据结束，耗时" + (end - start) + "毫秒，共" + count + "条记录被保存");
    }

    private int executeBatch(List<UserRedPacket> userRedPackets) {
        Connection conn=null;
        Statement statement=null;
        int[] count=null;
        try{
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            statement = conn.createStatement();
            for(UserRedPacket userRedPacket :userRedPackets){
                String sql1 = "update T_RED_PACKET set stock = stock-1 where id=" + userRedPacket.getRedPacketId();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String sql2 = "insert into T_USER_RED_PACKET(red_packet_id, user_id, " + "amount, grab_time, note)"
                        + " values (" + userRedPacket.getRedPacketId() + ", " + userRedPacket.getUserId() + ", "
                        + userRedPacket.getAmount() + "," + "'" + df.format(userRedPacket.getGrabTime()) + "'," + "'"
                        + userRedPacket.getNote() + "')";
                statement.addBatch(sql1);
                statement.addBatch(sql2);
            }
            count= statement.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("抢红包批量执行程序错误");
        }finally {
            try {
                if(conn!=null&&!conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return count.length/2;
    }

}
