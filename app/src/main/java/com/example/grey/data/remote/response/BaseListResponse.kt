package com.example.grey.data.remote.response

import com.google.gson.annotations.SerializedName

open class BaseListResponse<T> {
    @SerializedName("total_count")
    val totalCount: Int = 0

    @SerializedName("incomplete_results")
    val incompleteResults: Boolean = false

    @SerializedName("items")
    lateinit var items: List<T>
}
