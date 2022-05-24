package ade.yemi.moreapps.models

data class AllAppDetails(
        val message: String? = null,
        val apps: List<App>,
        val success: Boolean
)
data class App(
        val _id: String? = null,
        val name: String? = null,
        val playStoreUrl: String? = null,
        val appIconUrl: String? = null,
        val createdAt: String? = null,
        val updatedAt: String? = null,
        val version: Int? = 0
)


