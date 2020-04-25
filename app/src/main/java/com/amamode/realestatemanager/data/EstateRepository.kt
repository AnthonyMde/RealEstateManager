package com.amamode.realestatemanager.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.amamode.realestatemanager.domain.Estate
import com.amamode.realestatemanager.domain.EstateService

class EstateRepository(private val dao: EstateDao) : EstateService {

    override fun getEstateList(): LiveData<List<Estate>> {
        return dao.getEstateListById().map { list ->
            list.map {
                Estate(
                    id = it.id,
                    owner = it.owner,
                    type = it.type,
                    rooms = it.rooms,
                    surface = it.surface,
                    price = it.price
                )
            }
        }
    }

    override suspend fun createEstate(estate: Estate) {
        val estateEntity = EstateEntity(
            owner = estate.owner,
            type = estate.type,
            rooms = estate.rooms ?: 0,
            surface = estate.surface ?: 0,
            price = estate.price ?: 0
        )
        dao.insert(estateEntity)
    }

    override suspend fun editEstate(estate: Estate): Estate {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteEstate(estateId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
