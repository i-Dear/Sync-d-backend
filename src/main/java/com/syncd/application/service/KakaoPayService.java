package com.syncd.application.service;

import com.syncd.adapter.in.web.payment.PayApproveReqDto;
import com.syncd.adapter.in.web.payment.PayApproveResDto;
import com.syncd.adapter.in.web.payment.PayReadyReqDto;
import com.syncd.adapter.in.web.payment.PayReadyResDto;
import com.syncd.adapter.out.persistence.repository.user.UserDao;
import com.syncd.adapter.out.persistence.repository.user.UserEntity;
import com.syncd.domain.user.User;
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

    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public PayReadyResDto initiatePayment(PayReadyReqDto payReadyReqDto) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        headers.set("Authorization", "KakaoAK " + adminKey);

        HttpEntity<PayReadyReqDto> request = new HttpEntity<>(payReadyReqDto, headers);

        ResponseEntity<PayReadyResDto> response = restTemplate.postForEntity(
                "https://open-api.kakaopay.com/v1/payment/ready",
                request,
                PayReadyResDto.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new Exception("Failed to initiate payment");
        }
    }

    @Transactional
    public PayApproveResDto getApprove(String pgToken, String userId) throws Exception {
        Optional<UserEntity> optionalUser = userDao.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new Exception("User not found");
        }

        UserEntity user = optionalUser.get();

        String tid = user.getTid();  // Assuming you have a field for storing tid in the User entity

        PayApproveReqDto payApproveReqDto = new PayApproveReqDto();
        payApproveReqDto.setTid(tid);
        payApproveReqDto.setPartner_order_id("partner_order_id");  // Use actual partner order ID
        payApproveReqDto.setPartner_user_id(user.getId().toString());  // Assuming user ID is String
        payApproveReqDto.setPg_token(pgToken);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        headers.set("Authorization", "KakaoAK " + adminKey);

        HttpEntity<PayApproveReqDto> request = new HttpEntity<>(payApproveReqDto, headers);

        ResponseEntity<PayApproveResDto> response = restTemplate.postForEntity(
                "https://open-api.kakaopay.com/v1/payment/approve",
                request,
                PayApproveResDto.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new Exception("Failed to approve payment");
        }
    }
}