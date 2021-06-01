package com.artemkaxboy.satparser.service

import com.artemkaxboy.satparser.alerting.AlertGateway
import com.artemkaxboy.satparser.entity.PhpSatelliteEntity
import com.artemkaxboy.satparser.metrics.Meter
import com.artemkaxboy.satparser.metrics.MetricsRegistry
import com.artemkaxboy.satparser.repository.PhpSatelliteRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.interceptor.TransactionAspectSupport
import javax.transaction.Transactional
import kotlin.math.max

private val logger = KotlinLogging.logger {}

private const val UPDATE_THRESHOLD_PERCENT = 10

@Service
class PhpSatelliteService(

    private val phpSatelliteRepository: PhpSatelliteRepository,
    private val metricsRegistry: MetricsRegistry,
    private val alertGateway: AlertGateway,
) {

    private fun isTooMuchChanges(allElementsCount: Int, changedElementsCount: Int): Boolean {
        return allElementsCount * UPDATE_THRESHOLD_PERCENT / 100 >= changedElementsCount
    }

    @Transactional
    fun sync(fetchedList: Collection<PhpSatelliteEntity>) {

        val dbSatellites = findAll()

        isTooMuchChanges(dbSatellites.size)

        saveNewSatellites(fetchedList, dbSatellites)
        saveChangedSatellites(fetchedList, dbSatellites)
        saveClosedSatellites(fetchedList, dbSatellites)

        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()
    }

    fun findAll(): List<PhpSatelliteEntity> {

        return phpSatelliteRepository.findByStatusIs(PhpSatelliteEntity.Status.OPENED.value)
            .log("Got db satellites: ")
            .also { metricsRegistry.updateGauge(Meter.SATELLITES_DB_ALL, it.size) }
    }


    private fun saveNewSatellites(
        fetchedList: Collection<PhpSatelliteEntity>,
        existingList: Collection<PhpSatelliteEntity>
    ): List<PhpSatelliteEntity> {

        val newSatellites = findNewSatellites(fetchedList, existingList)

        if (isTooMuchChanges(max(fetchedList, existingList)))

        return newSatellites
            .log("Save new satellites: ")
            .also { metricsRegistry.updateGauge(Meter.SATELLITES_DB_NEW, it.size) }
            .let { phpSatelliteRepository.saveAll(it) }
    }

    private fun findNewSatellites(
        newSatellites: Collection<PhpSatelliteEntity>,
        oldSatellites: Collection<PhpSatelliteEntity>,
    ): List<PhpSatelliteEntity> {

        val oldSatelliteNames = oldSatellites.map { it.name }
        return newSatellites.filterNot { oldSatelliteNames.contains(it.name) }
    }

    private fun saveClosedSatellites(
        fetchedSatellites: Collection<PhpSatelliteEntity>,
        existingList: Collection<PhpSatelliteEntity>,
    ): List<PhpSatelliteEntity> {

        return findClosedSatellites(fetchedSatellites, existingList)
            .log("Save closed satellites: ")
            .also { metricsRegistry.updateGauge(Meter.SATELLITES_DB_CLOSED, it.size) }
            .let { phpSatelliteRepository.saveAll(it) }
    }

    private fun findClosedSatellites(
        fetchedSatellites: Collection<PhpSatelliteEntity>,
        existingSatellites: Collection<PhpSatelliteEntity>,
    ): List<PhpSatelliteEntity> {

        val fetchedSatelliteNames = fetchedSatellites.map { it.name }
        return existingSatellites.filterNot { fetchedSatelliteNames.contains(it.name) }
            .map { it.close() }
    }

    private fun saveChangedSatellites(
        fetchedSatellites: Collection<PhpSatelliteEntity>,
        existingSatellites: Collection<PhpSatelliteEntity>
    ): List<PhpSatelliteEntity> {

        return findChangedSatellites(fetchedSatellites, existingSatellites)
            .log("Save changed satellites: ")
            .also { metricsRegistry.updateGauge(Meter.SATELLITES_DB_CHANGED, it.size) }
            .also { phpSatelliteRepository.saveAll(it) }
    }

    private fun findChangedSatellites(
        newSatellites: Collection<PhpSatelliteEntity>,
        oldSatellites: Collection<PhpSatelliteEntity>
    ): List<PhpSatelliteEntity> {

        val oldSatellitesByName = oldSatellites.associateBy { it.name }

        return newSatellites
            .filter { it.isNeedUpdateFrom(oldSatellitesByName[it.name]) ?: false }
            .map { it.mergeWithExisting(requireNotNull(oldSatellitesByName[it.name])) }
    }
}

private fun List<PhpSatelliteEntity>.log(message: String): List<PhpSatelliteEntity> {
    logger.info { "${message}$size"}
    logger.debug {
        takeIf { isNotEmpty() }
            ?.joinToString(prefix = message) { it.name }
            ?: "${message}nothing"
    }
    return this
}
