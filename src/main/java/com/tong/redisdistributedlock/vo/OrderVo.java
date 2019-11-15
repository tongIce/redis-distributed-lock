package com.tong.redisdistributedlock.vo;

import lombok.Data;

/**
 * @author tong.yuan
 * @date 2019/11/12
 * @description
 */

@Data
public class OrderVo {

    private Long orderId;
    private Long userId;
    private String userName;
    private String goodsName;
    private Long goodsId;

}
