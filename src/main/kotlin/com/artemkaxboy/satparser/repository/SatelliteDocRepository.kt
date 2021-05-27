package com.artemkaxboy.satparser.repository

import com.artemkaxboy.satparser.entity.SatelliteDoc
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

interface SatelliteDocRepositoryI : MongoRepository<SatelliteDoc, String> {

    fun findByName(name: String): SatelliteDoc?
}

@Repository
class SatelliteDocRepository(satelliteDocRepositoryI: SatelliteDocRepositoryI) :
    SatelliteDocRepositoryI by satelliteDocRepositoryI {

    fun upsert(docs: Collection<SatelliteDoc>): List<SatelliteDoc> {
        return docs
            .filterNot { findByName(it.name) == it }
            .let {
                saveAll(it)
            }
    }
}
