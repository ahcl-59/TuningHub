package com.example.tuninghub.data.model

import com.google.firebase.Timestamp

data class ConnectionDto(
    val cid: String? = null,
    val uid1: String? = null,
    val uid2: String? = null,
    val status: String? = "pending",
    val timestamp: Timestamp? = null,
)