package com.ssafy.mmart.controller

import com.ssafy.mmart.domain.ResultResponse
import com.ssafy.mmart.domain.review.Review
import com.ssafy.mmart.domain.review.dto.ReviewReq
import com.ssafy.mmart.service.ReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/reviews")
class ReviewController @Autowired constructor(
    val reviewService: ReviewService,
){
    @GetMapping
    fun getReview(@RequestParam reviewIdx: Int): ResultResponse<Review?> {
        return ResultResponse.success(reviewService.getReview(reviewIdx))
    }

    @GetMapping("/user")
    fun getUserReviews(@RequestParam userIdx: Int): ResultResponse<List<Review>?> {
        return ResultResponse.success(reviewService.getUserReviews(userIdx))
    }

    @GetMapping("/item")
    fun getItemReviews(@RequestParam itemIdx: Int): ResultResponse<List<Review>?> {
        return ResultResponse.success(reviewService.getItemReviews(itemIdx))
    }

    @PostMapping
    fun createReview(@RequestBody reviewReq: ReviewReq): ResultResponse<Review?> {
        return ResultResponse.success(reviewService.createReview(reviewReq))
    }

    @PutMapping
    fun updateReview(@RequestParam reviewIdx: Int, @RequestBody reviewReq: ReviewReq): ResultResponse<Review?> {
        return ResultResponse.success(reviewService.updateReview(reviewIdx, reviewReq))
    }

    @DeleteMapping
    fun deleteReview(@RequestParam reviewIdx: Int, @RequestParam userIdx: Int): ResultResponse<Review?> {
        return ResultResponse.success(reviewService.deleteReview(reviewIdx, userIdx))
    }
}