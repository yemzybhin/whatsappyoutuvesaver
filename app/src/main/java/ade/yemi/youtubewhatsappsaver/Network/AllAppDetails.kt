package ade.yemi.moreapps.models

import com.google.gson.annotations.SerializedName

data class AllAppDetails(
        @SerializedName("message")val message: String? = null,
        @SerializedName("apps")val apps: List<App>,
        @SerializedName("success")val success: Boolean
)
data class App(
        @SerializedName("_id")val _id: String? = null,
        @SerializedName("name")val name: String? = null,
        @SerializedName("playStoreUrl")val playStoreUrl: String? = null,
        @SerializedName("appIconUrl")val appIconUrl: String? = null,
        @SerializedName("createdAt")val createdAt: String? = null,
        @SerializedName("updatedAt")val updatedAt: String? = null,
        @SerializedName("version")val version: Int? = 0
)


