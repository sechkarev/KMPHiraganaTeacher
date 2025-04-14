package com.sechkarev.hiraganateacherkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform