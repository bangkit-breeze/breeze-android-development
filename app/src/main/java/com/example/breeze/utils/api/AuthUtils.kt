package com.example.breeze.utils.api

class AuthUtils {
    companion object {
        fun getAuthToken(token: String) = "Bearer $token"
    }
}