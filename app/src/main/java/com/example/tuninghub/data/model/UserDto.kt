package com.example.tuninghub.data.model

data class UserDto(
    val uid: String?=null,
    val nombre: String?=null,
    val apellido: String?=null,
    val email: String="",
    val instrumento: String?=null,
    val ciudad: String?=null,
    val situacionLaboral: String?=null,
    val fotoPerfil: String?=null,
    val bio: String?=null,
    val enlace: String?=null
)