package com.example.breeze.utils

class AuthUtils {
    companion object {
        fun getAuthToken(token: String) = "Bearer $token"
    }
}