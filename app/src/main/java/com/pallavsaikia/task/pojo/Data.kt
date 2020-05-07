package com.pallavsaikia.task.pojo

data class Data(
    val expiryDate: String?,
    val img: String,
    val isTrending: Boolean,
    val itemHeadline: String,
    val itemText: String
){
    var time:Long=0L
}