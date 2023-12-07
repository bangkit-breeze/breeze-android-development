package com.example.breeze.data.model.auth

data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)