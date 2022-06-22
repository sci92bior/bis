package com.htt.bis.domain

enum class ObjectType (val bucket: String){
    EXPLOSIVE_MATERIAL("explosive-materials"),
    INITIATION_SYSTEM("initianion-systems"),
    BUILD_MATERIALS("build-materials"),
    EXPLOSIVE_UNITS("explosive-units"),
    OBSTACLE("obstacles"),
    DESTRUCTIONS("destructions")
}