package ade.yemi.moreapps.Network

import ade.yemi.moreapps.models.AllAppDetails
import ade.yemi.moreapps.models.AppContent
import ade.yemi.moreapps.models.ShowYoutubeLayout
import retrofit2.Call
import retrofit2.http.GET

//private const val BASE_URL = "https://facts-guru.herokuapp.com/api/v1/apps/get"
interface  RetrofitInterface1{
    @get:GET("jsonholder/yeminitionads.json")
    val post: Call<AppContent?>?

    companion object{
        const val BASE_URL = "https://yemzybhin.github.io"
    }

}
interface  RetrofitInterface{
    @get:GET("api/v1/apps/get")
    val post: Call<AllAppDetails?>?

    companion object{
        const val BASE_URL = "https://facts-guru.herokuapp.com"
    }

}

interface RetrofitInterface2{
    @get:GET("jsonholder/youtubewhatsappsavershow1.json")
    val post: Call<ShowYoutubeLayout?>?

    companion object{
        const val BASE_URL = "https://yemzybhin.github.io"
    }
}
