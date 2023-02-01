package com.module.simmanager.model

/**
 * @param number phoneNumber
 * @param simType USIM : 0 / eSIM : 1
 */
data class SimItem(
    val number : String?,
    val isEmbedded: Boolean?,
    val carrierName: CharSequence?,
    val simSlotIndex: Int?
)
