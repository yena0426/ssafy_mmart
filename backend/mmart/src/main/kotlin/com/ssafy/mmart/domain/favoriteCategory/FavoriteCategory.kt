package com.ssafy.mmart.domain.favoriteCategory

import com.ssafy.mmart.domain.Base
import com.ssafy.mmart.domain.category.Category
import com.ssafy.mmart.domain.user.User
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
data class FavoriteCategory (
    @Id
    @Column(name = "favoriteCategoryIdx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val favoriteCategoryIdx : Int? = null,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="userIdx")
    val user: User,

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name="categoryIdx")
    val category: Category,
): Base()