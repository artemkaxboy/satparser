package com.artemkaxboy.satparser.repository

import com.artemkaxboy.satparser.entity.PhpSatelliteEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface PhpSatelliteRepositoryI : JpaRepository<PhpSatelliteEntity, Int> {

    fun findByStatusIs(status: Int): List<PhpSatelliteEntity>
}

@Repository
class PhpSatelliteRepository(
    phpSatelliteRepositoryI: PhpSatelliteRepositoryI,
) : PhpSatelliteRepositoryI by phpSatelliteRepositoryI
