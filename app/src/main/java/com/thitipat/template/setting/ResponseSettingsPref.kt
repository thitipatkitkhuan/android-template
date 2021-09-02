package com.thitipat.template.setting

data class ResponseSettingsPref(
    val prefServer: String,
    val prefPort: String,
    val prefDatabase: String,
    val prefUser: String,
    val prefPassword: String,
    val prefTimeout: String
)