package com.ex.simmanager.data.model.sim

/**
 * @param number phoneNumber
 * @param simType USIM : 0 / eSIM : 1
 */
data class SimItem(
    val number : String,
    val isEmbedded: Boolean
)
