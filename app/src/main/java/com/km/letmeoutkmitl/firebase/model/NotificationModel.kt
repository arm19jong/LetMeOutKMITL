package com.km.letmeoutkmitl.firebase.model

/**
 * Created by jongzazaal on 3/1/2018.
 */
data class NotificationModel(var title:String = "กรุณามาเลื่อนรถ",
                             var body:String = "รถของคุณขวางทางอยู่กรุณามาเลื่อนรถออกไปด้วย",
                             var badge: Int = 1,
                             var sound:String = "default") {
}