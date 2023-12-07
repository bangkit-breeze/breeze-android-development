package com.example.breeze.data.model.auth

data class RegistrationRequest(
    val fullname: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)