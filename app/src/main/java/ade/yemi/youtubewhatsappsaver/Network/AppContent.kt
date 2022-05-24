package ade.yemi.moreapps.models

data class AppContent(
        val message: String? = null,
        val ads: List<Ads>,
        val success: Boolean
)
data class Ads(
        val message: String? = null,
        val visible: Boolean? = null,
        val Title: String? = null,
        val Description: String? = null,
        val Link: String? = null,
        val Imagelink: String? = null,
        val VideoLink: String? = null,
        val SmallImagelink: String? = null
)