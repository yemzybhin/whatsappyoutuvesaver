package ade.yemi.moreapps.models

import com.google.gson.annotations.SerializedName

data class AppContent(
        @SerializedName("message")val message: String? = null,
        @SerializedName("ads") val ads: List<Ads>,
        @SerializedName("success")val success: Boolean
)
data class Ads(
        @SerializedName("message") val message: String? = null,
        @SerializedName("visible")val visible: Boolean? = null,
        @SerializedName("Title")val Title: String? = null,
        @SerializedName("Description")val Description: String? = null,
        @SerializedName("Link")val Link: String? = null,
        @SerializedName("Imagelink")val Imagelink: String? = null,
        @SerializedName("VideoLink")val VideoLink: String? = null,
        @SerializedName("SmallImagelink")val SmallImagelink: String? = null
)

//data class ShowYoutubeLayout(
//        val message: String? = null,
//        val success: Boolean
//)