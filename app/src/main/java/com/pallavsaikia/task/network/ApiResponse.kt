package com.pallavsaikia.task.network

class ApiResponse{
    var data:Any?=null
    var error:Throwable?=null
    constructor(data:Any){
        this.data=data
        this.error=null
    }
    constructor(error:Throwable){
        this.error=error
        this.data=null
    }
}