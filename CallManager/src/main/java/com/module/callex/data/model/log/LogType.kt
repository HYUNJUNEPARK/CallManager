package com.module.callex.data.model.log

enum class LogType(val type: String) {
    INCOMING("1"),
    OUTGOING("2"),
    MISSED("3"),
    VOICEMAIL("4"),
    REJECTED("5"),
    BLOCKED("6"),
    ANSWERED_EXTERNALLY_TYPE("7")
}