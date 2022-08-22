package com.sih.project.model

import com.sih.project.util.UserTypes

data class User(
    var id: String? = null,
    var name: String? = "",
    var email: String? = "",
    var phone: String? = "",
    var type: String? = UserTypes.USER.name
): java.io.Serializable
