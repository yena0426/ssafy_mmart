package com.ssafy.mmart.controller

import com.ssafy.mmart.domain.ResultResponse
import com.ssafy.mmart.domain.payment.Payment
import com.ssafy.mmart.domain.payment.dto.PaymentReq
import com.ssafy.mmart.service.PaymentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/payments")
class PaymentController @Autowired constructor(
    val paymentService: PaymentService,
) {
   @GetMapping
   fun getPayments(@RequestParam userIdx: Int): ResultResponse<List<Payment>?> {
       return ResultResponse.success(paymentService.getPayments(userIdx))
   }

    @GetMapping("/{paymentIdx}")
    fun getPayment(@RequestParam userIdx: Int, @PathVariable paymentIdx: Int): ResultResponse<Payment?> {
        return ResultResponse.success(paymentService.getPayment(userIdx, paymentIdx))
    }

    @PostMapping
    fun createPayment(@RequestParam userIdx: Int): ResultResponse<Payment?> {
        return ResultResponse.success(paymentService.createPayment(userIdx))
    }

    @DeleteMapping
    fun deletePayment(@RequestParam paymentIdx: Int, @RequestParam userIdx: Int): ResultResponse<Payment?> {
        return ResultResponse.success(paymentService.deletePayment(paymentIdx, userIdx))
    }
}