package com.syncd.adapter.in.web;

import com.syncd.adapter.in.web.payment.PayApproveResDto;
import com.syncd.adapter.in.web.payment.PayReadyReqDto;
import com.syncd.adapter.in.web.payment.PayReadyResDto;
import com.syncd.application.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payment")
public class KakaoPayController {
    private final KakaoPayService kakaoPayService;

    @PostMapping("/ready")
    public ResponseEntity<?> readyToPay(@RequestBody PayReadyReqDto payReadyReqDto) {
        try {
            // Set your application's callback URLs
            payReadyReqDto.setApproval_url("http://localhost:8080/v1/payment/success");
            payReadyReqDto.setFail_url("http://localhost:8080/v1/payment/fail");
            payReadyReqDto.setCancel_url("http://localhost:8080/v1/payment/cancel");

            PayReadyResDto response = kakaoPayService.initiatePayment(payReadyReqDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error initiating payment: " + e.getMessage());
        }
    }

    @GetMapping("/success")
    public ResponseEntity<?> paymentSuccess(@RequestParam("pg_token") String pgToken, @RequestParam("user_id") String userId) {
        try {
            PayApproveResDto response = kakaoPayService.getApprove(pgToken, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error approving payment: " + e.getMessage());
        }
    }

    @GetMapping("/fail")
    public ResponseEntity<?> paymentFail() {
        return ResponseEntity.status(500).body("Payment failed!");
    }

    @GetMapping("/cancel")
    public ResponseEntity<?> paymentCancel() {
        return ResponseEntity.ok("Payment cancelled!");
    }
}
