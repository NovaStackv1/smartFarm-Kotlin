package com.example.smartfarm.ui.features.profile.modle

data class UserData(
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val profilePictureUrl: String? = "",
    val joinDate: String? = null
)

data class ProfileState(
    val userData: UserData? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

