package com.murt.chat

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform