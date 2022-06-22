package com.htt.bis.service

import com.htt.bis.exception.CannotSortByPropertyException
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Consumer

@Service
class SortService {

    private val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()

    fun buildSortFromSortStrings(sortStrings: List<String>?, clazz: Class<*>): Sort {
        return buildSortFromSortStrings(sortStrings, clazz, true)
    }

    fun buildSortFromSortStrings(sortStrings: List<String>?, clazz: Class<*>, camelCase: Boolean): Sort {
        if (sortStrings == null || sortStrings.isEmpty()) {
            return Sort.unsorted()
        }

        val orderList: MutableList<Sort.Order> = mutableListOf()
        val fields = clazz.declaredFields
        val properties: MutableList<String> = mutableListOf()
        for (f in fields) {
            properties.add(f.name)
        }

        sortStrings.forEach(Consumer { s: String ->
            if (s.startsWith("+") || s.startsWith("-")) {
                val property = s.substring(1)
                if (!properties.contains(property)) {
                    throw CannotSortByPropertyException("Cannot sort by $property for class ${clazz.simpleName}")
                }
                val sortColumn = if (camelCase) property else camelToSnakeCase(property)
                if (s.startsWith("+")) {
                    orderList.add(Sort.Order.asc(sortColumn))
                } else {
                    orderList.add(Sort.Order.desc(sortColumn))
                }
            }
        })

        return Sort.by(orderList)
    }

    private fun camelToSnakeCase(string: String): String {
        return camelRegex.replace(string) {
            "_${it.value}"
        }.lowercase(Locale.getDefault())
    }

}