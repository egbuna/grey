package com.example.grey.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserDetailResponse(
    @SerializedName("login")
    val login: String = "",
    @SerializedName("id")
    val id: Long = 1,
    @SerializedName("avatar_url")
    val avatarUrl: String = "",
    @SerializedName("location")
    val location: String? = null,
    @SerializedName("bio")
    val bio: String? = null,
    @SerializedName("followers")
    val followers: Int = 0,
    @SerializedName("following")
    val following: Int = 0,
    @SerializedName("blog")
    val blog: String? = null,
    @SerializedName("name")
    val name: String? = null
)
