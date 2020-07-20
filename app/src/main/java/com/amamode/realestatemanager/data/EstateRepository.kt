package com.amamode.realestatemanager.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.amamode.realestatemanager.domain.*
import java.util.*

class EstateRepository(private val dao: EstateDao) : EstateService {

    override fun getEstateList(): LiveData<List<Estate>> {
        return dao.getEstateListById().map { list ->
            list.map { it.toEstate() }
        }
    }

    override fun getInterestPoints(estateId: Long): LiveData<List<InterestPointEntity>> {
        return dao.getInterestPoints(estateId)
    }

    override suspend fun createEstate(
        estateForm: EstateForm,
        interestPoints: Array<InterestPoint>
    ) {
        val estateEntity = EstateEntity(
            owner = estateForm.owner ?: "unknown owner",
            type = estateForm.type ?: "unknown type",
            rooms = estateForm.rooms ?: 0,
            surface = estateForm.surface ?: 0,
            price = estateForm.price ?: 0,
            onMarketDate = estateForm.onMarketDate.toString(),
            status = EstateStatus(estateForm.sold, estateForm.soldDate.toString()),
            address = EstateAddress(estateForm.street, estateForm.zipCode, estateForm.city),
            description = estateForm.description
        )
        val estateId = dao.insert(estateEntity)

        if (estateId == -1L) {
            throw IllegalArgumentException("Can not create estate")
        }

        val interestPointEntity =
            interestPoints.map { it.toInterestPointEntity(estateId) }.toTypedArray()

        dao.insert(*interestPointEntity)
    }

    override suspend fun editEstate(estateForm: EstateForm): Estate {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    private fun InterestPoint.toInterestPointEntity(estateId: Long) = InterestPointEntity(
        estateId = estateId,
        name = this.name
    )
}
