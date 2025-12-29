package com.example.tuninghub.data.model

data class TaskDto (
    val tId:String?=null,
    val titulo:String?=null,
    val descripcion:String?=null,
    val fecInicio: Long?=0L,
    val fecFin:Long?=0L,
    val participantes:List<String>?= emptyList()
)