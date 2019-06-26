package com.smart.model;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RedPacket implements Serializable {

    private static final long serialVersionUID = 9036484563091364939L;
    // 红包编号
    private Long id;
    // 发红包的用户id
    private Long userId;
    // 红包金额
    private Double amount;
    // 发红包日期
    private Date sendDate;
    // 红包总数
    private Integer total;
    // 单个红包的金额
    private Double unitAmount;
    // 红包剩余个数
    private Integer stock;
    // 版本（为后续扩展用）
    private Integer version;
    // 备注
    private String note;

}
