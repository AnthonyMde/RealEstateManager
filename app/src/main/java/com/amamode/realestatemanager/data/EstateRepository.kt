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
        val estatePhotosUriEntity = dao.getEstatePhotosUri(estateId)
        val estatePhotosUri = estatePhotosUriEntity.map { it.uriString }
        val interestPoints = interestPointsEntity.map { it.toInterestPoint() }
        return estateEntity.toEstateDetails(interestPoints, estatePhotosUri)
    }

    override suspend fun createEstate(
        estateForm: EstateForm,
        interestPoints: Array<InterestPoint>,
        estatePhotosUri: Array<String>
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

        val interestPointsEntity =
            interestPoints.map { it.toInterestPointEntity(estateId) }.toTypedArray()
        val estatePhotoUriEntity =
            estatePhotosUri.map { toPhotoUriEntity(estateId, it) }.toTypedArray()

        dao.insert(*interestPointsEntity)
        dao.insert(*estatePhotoUriEntity)
    }

    override suspend fun updateEstate(
        estateId: Long,
        estateForm: EstateForm,
        interestPoints: Array<InterestPoint>,
        estatePhotosUri: Array<String>
    ) {
        val estateEntity = EstateEntity(
            id = estateId,
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
        dao.update(estateEntity)

        if (estateId == -1L) {
            throw IllegalArgumentException("Can not create estate")
        }

        val interestPointsEntity =
            interestPoints.map { it.toInterestPointEntity(estateId) }.toTypedArray()
        val estatePhotoUriEntity =
            estatePhotosUri.map { toPhotoUriEntity(estateId, it) }.toTypedArray()
        dao.deleteInterestPoints(estateId)
        dao.insert(*interestPointsEntity)
        dao.insert(*estatePhotoUriEntity)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    private fun InterestPoint.toInterestPointEntity(estateId: Long) = InterestPointEntity(
        estateId = estateId,
        name = this.name
    )

    private fun toPhotoUriEntity(estateId: Long, uri: String) = EstatePhotoUriEntity(
        estateId = estateId,
        uriString = uri
    )
}
