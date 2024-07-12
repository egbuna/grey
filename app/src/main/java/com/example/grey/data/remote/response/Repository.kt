package com.example.grey.data.remote.response

import com.google.gson.annotations.SerializedName

data class Repository(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("stargazers_count")
    val stars: Int,
    @SerializedName("owner")
    val owner: Owner,
    @SerializedName("language")
    val language: String?,
    @SerializedName("topics")
    val topics: List<String> = emptyList()
) {
    data class Owner(
        @SerializedName("login")
        val login: String,
        @SerializedName("id")
        val id: Long,
        @SerializedName("avatar_url")
        val avatarUrl: String?
    )
}
