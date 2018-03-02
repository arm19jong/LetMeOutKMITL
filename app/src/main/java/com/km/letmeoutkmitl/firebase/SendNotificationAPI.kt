package com.km.letmeoutkmitl.firebase

import com.google.gson.JsonObject
import com.km.letmeoutkmitl.firebase.model.NotificationModel
import com.km.letmeoutkmitl.firebase.model.SendNotificationModel
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


/**
 * Created by jongzazaal on 2/28/2018.
 */
interface SendNotificationAPI {

    @Headers("Authorization:key=AIzaSyCU90UXRLpIBNjxb4M7-k9B7pDs131i7Xg")
    @POST("send")
    fun sendNotification(@Body noti: SendNotificationModel): Call<JsonObject>
}