package com.htt.bis.common

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.ComparableExpression
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.SimpleExpression
import com.querydsl.core.types.dsl.StringPath

fun StringPath.containsIgnoreCaseOrNull(string: String?): BooleanExpression? {
    return if (string == null) {
        null
    } else {
        this.containsIgnoreCase(string)
    }
}

fun <T> NumberPath<T>.goeOrNull(right: T?): BooleanExpression? where T : Number, T : Comparable<*> {
    return if (right == null) {
        null
    } else {
        this.goe(right)
    }
}

fun <T> NumberPath<T>.loeOrNull(right: T?): BooleanExpression? where T : Number, T : Comparable<*> {
    return if (right == null) {
        null
    } else {
        this.loe(right)
    }
}

fun <T> SimpleExpression<T>.eqOrNull(right: T?): BooleanExpression? {
    return if (right == null) {
        null
    } else {
        this.eq(right)
    }
}

fun <T> ComparableExpression<T>.goeOrNull(right: T?): BooleanExpression? where T : Comparable<*> {
    return if (right == null) {
        null
    } else {
        this.goe(right)
    }
}
