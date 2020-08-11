package com.amamode.realestatemanager.data

import com.amamode.realestatemanager.domain.*
import com.amamode.realestatemanager.ui.creation.EstateType
import timber.log.Timber

class EstateRepository(private val dao: EstateDao) : EstateService {

    override suspend fun getEstateList(): List<EstateDetails> {
        return dao.getEstateListById().map { estate ->
            getEstateDetails(estate.id)
        }
    }

    override suspend fun filter(filterData: FilterEntity): List<EstateDetails> {
        val list = dao.filter(
            filterData.owner,
            filterData.type?.name,
            filterData.minPrice,
            filterData.maxPrice,
            filterData.minSurface,
            filterData.maxSurface,
            filterData.fromDate,
            filterData.city,
            filterData.zipCode
        )
        var result = list.map { getEstateDetails(it.id) }
        result =
            result.filter { estateDetails -> estateDetails.estatePhotos.size >= filterData.minPhotos ?: 0 }
        if (!filterData.interestPoints.isNullOrEmpty()) {
            result =
                result.filter { estateDetails -> estateDetails.interestPoint.containsAll(filterData.interestPoints) }
        }
        return result
    }

    override suspend fun getEstateDetails(estateId: Long): EstateDetails {
        val estateEntity = dao.getEstateById(estateId)
        val interestPointsEntity = dao.getInterestPoints(estateId)
        val estatePhotosUriEntity = dao.getEstatePhotosUri(estateId)
        val estatePhotos = estatePhotosUriEntity.map { Pair(it.uriString, it.description) }
        val interestPoints = interestPointsEntity.map { it.toInterestPoint() }
        return estateEntity.toEstateDetails(interestPoints, estatePhotos)
    }

    // Returns true if creation is successful
    override suspend fun createEstate(
        estateForm: EstateForm,
        interestPoints: Array<InterestPoint>,
        estatePhotosUri: Array<Pair<String, String>>
    ): Boolean {
        val estateEntity = EstateEntity(
            owner = estateForm.owner ?: "unknown owner",
            type = estateForm.type?.name ?: EstateType.UNKNOWN.name,
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
            Timber.e("Room : Cannot create estate")
            return false
        }

        val interestPointsEntity =
            interestPoints.map { it.toInterestPointEntity(estateId) }.toTypedArray()
        val estatePhotoUriEntity =
            estatePhotosUri.map { toPhotoUriEntity(estateId, it.first, it.second) }.toTypedArray()

        if (dao.insert(*interestPointsEntity).contains(-1L)) {
            Timber.e("Room : Cannot save estate's POI")
            return false
        }
        if (dao.insert(*estatePhotoUriEntity).contains( -1L)) {
            Timber.e("Room : Cannot save estate's photos")
            return false
        }
        return true
    }

    override suspend fun updateEstate(
        estateId: Long,
        estateForm: EstateForm,
        interestPoints: Array<InterestPoint>,
        estatePhotosUri: Array<Pair<String, String>>
    ) {
        val estateEntity = EstateEntity(
            id = estateId,
            owner = estateForm.owner ?: "unknown owner",
            type = estateForm.type?.name ?: EstateType.UNKNOWN.name,
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
            estatePhotosUri.map { toPhotoUriEntity(estateId, it.first, it.second) }.toTypedArray()
        dao.deleteInterestPoints(estateId)
        dao.deleteEstatePhotos(estateId)
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

    private fun toPhotoUriEntity(estateId: Long, uri: String, desc: String) = EstatePhotoEntity(
        estateId = estateId,
        uriString = uri,
        description = desc
    )
}
