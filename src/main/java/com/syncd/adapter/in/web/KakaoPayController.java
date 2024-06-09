package com.syncd.adapter.in.web;

import com.syncd.adapter.in.web.payment.PayApproveResDto;
import com.syncd.adapter.in.web.payment.PayReadyReqDto;
import com.syncd.adapter.in.web.payment.PayReadyResDto;
import com.syncd.adapter.in.web.payment.PayReadyResSimpleDto;
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
            PayReadyResSimpleDto response = kakaoPayService.initiatePayment(payReadyReqDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error initiating payment: " + e.getMessage());
        }
    }

    @GetMapping("/success")
    public ResponseEntity<?> paymentSuccess(@RequestParam("pg_token") String pgToken, @RequestParam("tid") String tid) {
        System.out.println(tid);
        try {
            PayApproveResDto response = kakaoPayService.getApprove(pgToken,"sangjun1389@ajou.ac.kr");
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
