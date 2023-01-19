package com.ex.simmanager.data.model.activated

/**
 * @param number phoneNumber
 * @param simType USIM : 0 / eSIM : 1
 */
data class ActivatedSimItem(
    val number : String,
    val isEmbedded: Boolean
)
