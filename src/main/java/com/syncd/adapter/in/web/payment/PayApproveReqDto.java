package com.syncd.adapter.in.web.payment;

import lombok.Data;

@Data
public class PayApproveReqDto {
    private String tid;
    private String partner_order_id;
    private String partner_user_id;
    private String pg_token;
}

