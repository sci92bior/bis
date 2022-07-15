package com.htt.bis.domain

enum class ObjectType (val bucket: String){
    EXPLOSIVE_MATERIAL("explosive-materials"),
    BUILD_MATERIALS("build-materials"),
    EXPLOSIVE_UNITS("explosive-units"),
    OBSTACLE("obstacles"),
    DESTRUCTIONS("destructions"),
    CATEGORY("category"),
    SIMPLE_ENTITY("simple-entity"),
}