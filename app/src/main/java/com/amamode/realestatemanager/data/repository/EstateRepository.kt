package com.amamode.realestatemanager.data.repository

import com.amamode.realestatemanager.data.room.EstateDao
import com.amamode.realestatemanager.data.room.entity.EstateEntity
import com.amamode.realestatemanager.data.room.entity.EstatePhotoEntity
import com.amamode.realestatemanager.data.room.entity.InterestPointEntity
import com.amamode.realestatemanager.domain.*
import com.amamode.realestatemanager.domain.entity.EstateAddress
import com.amamode.realestatemanager.domain.entity.EstateDetails
import com.amamode.realestatemanager.domain.entity.EstateStatus
import com.amamode.realestatemanager.domain.entity.InterestPoint
import com.amamode.realestatemanager.domain.services.EstateService
import com.amamode.realestatemanager.ui.creation.EstateType
import timber.log.Timber

class EstateRepository(private val dao: EstateDao) :
    EstateService {

    override suspend fun getEstateList(): List<EstateDetails> {
        return dao.getEstateListById().map { estate ->
            getEstateDetails(estate.id)
        }
    }

    /**
     * Ask room to filter the list of estate based on all the parameters.
     * First we retrieve the filtered list. Then we apply manually two parameters manually (not
     * present directly in the estate table : the minimum required photos and the POI.
     * @param filterData all the filter parameters
     * @return estate list filtered.
     */
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

    /**
     * Return the details for one estate.
     * First retrieve the estateEntity from room and then add to it the missing photos
     * and POI.
     * @param estateId the id of the target estate
     * @return estate details with all data filled
     */
    override suspend fun getEstateDetails(estateId: Long): EstateDetails {
        val estateEntity = dao.getEstateById(estateId)
        val interestPointsEntity = dao.getInterestPoints(estateId)
        val estatePhotosUriEntity = dao.getEstatePhotosUri(estateId)
        val estatePhotos = estatePhotosUriEntity.map { Pair(it.uriString, it.description) }
        val interestPoints = interestPointsEntity.map { it.toInterestPoint() }
        return estateEntity.toEstateDetails(interestPoints, estatePhotos)
    }

    /**
     * First creates the estate. Then uses is database ID to store POI and photos.
     * @param estateForm all params needed for creation.
     * @param interestPoints all the POI for this estate
     * @param estatePhotosUri all the photo URI associated with its description
     * @return true if creation is successful.
     */
    override suspend fun createEstate(
        estateForm: EstateForm,
        interestPoints: Array<InterestPoint>,
        estatePhotosUri: Array<Pair<String, String>>
    ): Boolean {
        val estateEntity =
            EstateEntity(
                owner = estateForm.owner ?: "unknown owner",
                type = estateForm.type?.name ?: EstateType.UNKNOWN.name,
                rooms = estateForm.rooms ?: 0,
                surface = estateForm.surface ?: 0,
                price = estateForm.price ?: 0,
                onMarketDate = estateForm.onMarketDate,
                status = EstateStatus(
                    estateForm.sold,
                    estateForm.soldDate
                ),
                address = EstateAddress(
                    estateForm.street,
                    estateForm.zipCode,
                    estateForm.city
                ),
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
        val estateEntity =
            EstateEntity(
                id = estateId,
                owner = estateForm.owner ?: "unknown owner",
                type = estateForm.type?.name ?: EstateType.UNKNOWN.name,
                rooms = estateForm.rooms ?: 0,
                surface = estateForm.surface ?: 0,
                price = estateForm.price ?: 0,
                onMarketDate = estateForm.onMarketDate,
                status = EstateStatus(
                    estateForm.sold,
                    estateForm.soldDate
                ),
                address = EstateAddress(
                    estateForm.street,
                    estateForm.zipCode,
                    estateForm.city
                ),
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

    private fun InterestPoint.toInterestPointEntity(estateId: Long) =
        InterestPointEntity(
            estateId = estateId,
            name = this.name
        )

    private fun toPhotoUriEntity(estateId: Long, uri: String, desc: String) =
        EstatePhotoEntity(
            estateId = estateId,
            uriString = uri,
            description = desc
        )
}
