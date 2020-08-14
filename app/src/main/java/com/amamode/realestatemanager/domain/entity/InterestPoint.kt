package com.amamode.realestatemanager.domain.entity

import com.amamode.realestatemanager.R

enum class InterestPoint(
    val nameRes: Int
) {
    METRO(R.string.interest_point_metro),
    SHOP(R.string.interest_point_shop),
    SCHOOL(R.string.interest_point_school),
    PARC(R.string.interest_point_parc)
}
