package com.htt.bis.facade

import com.htt.bis.exception.DomainObjectFoundException

annotation class Facade

inline fun <reified T, R> T?.ifFound(onNotNull: (T) -> R): R {
    if (this != null) {
        return onNotNull(this)
    } else {
        throw DomainObjectFoundException()
    }
}
