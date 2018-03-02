package com.km.letmeoutkmitl.firebase.model

/**
 * Created by jongzazaal on 3/1/2018.
 */
data class SendNotificationModel(var to:String = "/topics/",
                                 var priority:String = "high",
                                 var notification: NotificationModel = NotificationModel()
                                 ) {
}