package com.amamode.realestatemanager.data

import com.amamode.realestatemanager.domain.Estate
import com.amamode.realestatemanager.domain.EstateForm
import com.amamode.realestatemanager.domain.EstateService

class EstateRepository : EstateService {
    override fun getEstateList(): List<Estate> {
        return emptyList()
    }

    override fun createEstate(estateForm: EstateForm): Estate {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editEstate(estateForm: EstateForm): Estate {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteEstate(estateId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
