package com.syncd.application.service;

import com.syncd.adapter.in.web.payment.PayApproveReqDto;
import com.syncd.adapter.in.web.payment.PayApproveResDto;
import com.syncd.adapter.in.web.payment.PayReadyReqDto;
import com.syncd.adapter.in.web.payment.PayReadyResDto;
import com.syncd.adapter.in.web.payment.PayReadyResSimpleDto;
import com.syncd.adapter.out.persistence.repository.user.UserDao;
import com.syncd.adapter.out.persistence.repository.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class KakaoPayService {
    private final UserDao userDao;

    @Value("${pay.admin-key}")
    private String adminKey;

    @Value("${pay.cid}")
    private String cid;

    @Value("${pay.APPROVAL_URL}")
    private String approvalUrl;

    @Value("${pay.FAIL_URL}")
    private String failUrl;

    @Value("${pay.CANCEL_URL}")
    private String cancelUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public PayReadyResSimpleDto initiatePayment(PayReadyReqDto payReadyReqDto) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        headers.set("Authorization", "SECRET_KEY " + adminKey);

        HttpEntity<PayReadyReqDto> request = new HttpEntity<>(payReadyReqDto, headers);

        payReadyReqDto.setCid(cid);
        payReadyReqDto.setApproval_url(approvalUrl);
        payReadyReqDto.setCancel_url(cancelUrl);
        payReadyReqDto.setFail_url(failUrl);

        ResponseEntity<PayReadyResDto> response = restTemplate.postForEntity(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                request,
                PayReadyResDto.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            PayReadyResDto payReadyResDto = response.getBody();
            PayReadyResSimpleDto simpleDto = new PayReadyResSimpleDto();
            if (payReadyResDto != null) {
                simpleDto.setNext_redirect_pc_url(payReadyResDto.getNext_redirect_pc_url());

                String targetUserId = "sangjun1389@ajou.ac.kr";

                Optional<UserEntity> optionalUser = userDao.findById(targetUserId);
                if (optionalUser.isPresent()) {
                    UserEntity user = optionalUser.get();
                    user.setTid(payReadyResDto.getTid());
                    userDao.save(user);
                } else {
                    throw new Exception("User not found: " + targetUserId);
                }
            }
            return simpleDto;
        } else {
            throw new Exception("Failed to initiate payment: " + response.getStatusCode().toString());
        }
    }

    @Transactional
    public PayApproveResDto getApprove(String pgToken, String userId) throws Exception {
        System.out.println("getApprove");
        Optional<UserEntity> optionalUser = userDao.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new Exception("User not found");
        }

        UserEntity user = optionalUser.get();
        String tid = user.getTid();

        PayApproveReqDto payApproveReqDto = new PayApproveReqDto();
        payApproveReqDto.setCid("TC0ONETIME");
        payApproveReqDto.setTid(tid);
        payApproveReqDto.setPartner_order_id("partner_order_id");
        payApproveReqDto.setPartner_user_id("partner_user_id");
        payApproveReqDto.setPg_token(pgToken);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        headers.set("Authorization", "SECRET_KEY " + adminKey);

        HttpEntity<PayApproveReqDto> request = new HttpEntity<>(payApproveReqDto, headers);

        ResponseEntity<PayApproveResDto> response = restTemplate.postForEntity(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                request,
                PayApproveResDto.class
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new Exception("Failed to approve payment: " + response.getStatusCode().toString());
        }
    }
}
