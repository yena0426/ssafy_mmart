package com.ssafy.mmart.controller

import com.ssafy.mmart.domain.ResultResponse
import com.ssafy.mmart.domain.gotCart.dto.GotCartReq
import com.ssafy.mmart.domain.gotCart.dto.GotCartRes
import com.ssafy.mmart.domain.gotCart.dto.GotCartToPayRes
import com.ssafy.mmart.service.GotCartService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/gotcarts")
class GotCartController @Autowired constructor(
    val gotCartService: GotCartService,
){
    @GetMapping("/{userIdx}")
    fun getGotCarts(@PathVariable userIdx: Int): ResultResponse<GotCartRes> {
        return ResultResponse.success(gotCartService.getGotCarts(userIdx))
    }

    @GetMapping
    fun getGotCartsByEmail(@RequestParam email: String): ResultResponse<GotCartToPayRes> {
        return ResultResponse.success(gotCartService.getGotCartsByEmail(email))
    }

    @PostMapping
    fun createGotCart(@RequestBody gotCartReq: GotCartReq): ResultResponse<GotCartRes> {
        return ResultResponse.success(gotCartService.createGotCart(gotCartReq))
    }

    @PutMapping
    fun updateGotCart(@RequestBody gotCartReq: GotCartReq): ResultResponse<GotCartRes> {
        return ResultResponse.success(gotCartService.updateGotCart(gotCartReq))
    }

    @DeleteMapping("/{userIdx}")
    fun deleteGotCarts(@PathVariable userIdx: Int): ResultResponse<GotCartRes> {
        return ResultResponse.success(gotCartService.deleteGotCarts(userIdx))
    }

    @DeleteMapping
    fun deleteGotCart(@RequestParam userIdx: Int, @RequestParam itemIdx: Int): ResultResponse<GotCartRes> {
        return ResultResponse.success(gotCartService.deleteGotCart(userIdx, itemIdx))
    }
}