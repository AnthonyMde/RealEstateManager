package com.amamode.realestatemanager.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.amamode.realestatemanager.domain.*

class EstateRepository(private val dao: EstateDao) : EstateService {

    override fun getEstateList(): LiveData<List<EstatePreview>> {
        return dao.getEstateListById().map { list ->
            list.map { it.toEstatePreview() }
        }
    }

    override suspend fun getEstateDetails(estateId: Long): EstateDetails {
        val estateEntity = dao.getEstateById(estateId)
        val interestPointsEntity = dao.getInterestPoints(estateId)
        val interestPoints = interestPointsEntity.map { it.toInterestPoint() }
        return estateEntity.toEstateDetails(interestPoints)
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
            onMarketDate = estateForm.onMarketDate,
            status = EstateStatus(estateForm.sold, estateForm.soldDate),
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

    override suspend fun editEstate(estateForm: EstateForm): EstateDetails {
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
