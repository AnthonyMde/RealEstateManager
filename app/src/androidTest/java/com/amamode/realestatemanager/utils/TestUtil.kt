package com.amamode.realestatemanager.utils

import com.amamode.realestatemanager.data.room.entity.EstateEntity
import com.amamode.realestatemanager.domain.entity.EstateAddress
import com.amamode.realestatemanager.domain.entity.EstateStatus
import com.amamode.realestatemanager.ui.creation.EstateType
import java.util.*

class TestUtil {
    companion object {
        fun getEstateEntityMock(
            id: Long = 1L,
            owner: String = "owner",
            rooms: Int = 10,
            price: Int = 1000,
            surface: Int = 100
        ): EstateEntity =
            EstateEntity(
                id = id,
                owner = owner,
                type = EstateType.HOUSE.name,
                rooms = rooms,
                surface = surface,
                price = price,
                onMarketDate = Date(),
                status = EstateStatus(
                    true,
                    Date()
                ),
                address = EstateAddress(
                    "street",
                    1,
                    "city"
                ),
                description = "description"
            )
    }
}
