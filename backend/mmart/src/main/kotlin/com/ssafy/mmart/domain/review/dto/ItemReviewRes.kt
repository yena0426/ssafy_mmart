package com.ssafy.mmart.domain.review.dto

import com.ssafy.mmart.domain.item.dto.GetItemRes
import com.ssafy.mmart.domain.user.dto.UserRes

data class ItemReviewRes(
    val reviewRes: List<ReviewRes>,
    val pos: Double,
)