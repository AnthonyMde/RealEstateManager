package com.amamode.realestatemanager.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.amamode.realestatemanager.domain.Estate
import com.amamode.realestatemanager.domain.EstateForm
import com.amamode.realestatemanager.domain.EstateService

class EstateRepository(private val dao: EstateDao) : EstateService {

    override fun getEstateList(): LiveData<List<Estate>> {
        return dao.getEstateListById().map { list ->
            list.map { it.toEstate() }
        }
    }

    override suspend fun createEstate(estateForm: EstateForm) {
        val estateEntity = EstateEntity(
            owner = estateForm.owner ?: "unknown owner",
            type = estateForm.type ?: "unknown type",
            rooms = estateForm.rooms ?: 0,
            surface = estateForm.surface ?: 0,
            price = estateForm.price ?: 0
        )
        dao.insert(estateEntity)
    }

    override suspend fun editEstate(estateForm: EstateForm): Estate {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}
