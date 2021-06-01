package com.artemkaxboy.satparser.service

import com.artemkaxboy.satparser.alerting.AlertGateway
import com.artemkaxboy.satparser.alerting.CATEGORY_CHANGES
import com.artemkaxboy.satparser.entity.PhpSatelliteEntity
import com.artemkaxboy.satparser.metrics.Gauges
import com.artemkaxboy.satparser.metrics.MetricsRegistry
import com.artemkaxboy.satparser.repository.PhpSatelliteRepository
import com.artemkaxboy.satparser.tools.rollbackTransaction
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.interceptor.TransactionAspectSupport
import javax.transaction.Transactional
import kotlin.math.absoluteValue

private val logger = KotlinLogging.logger {}

private const val UPDATE_THRESHOLD_PERCENT = 10

@Service
class PhpSatelliteService(

    private val phpSatelliteRepository: PhpSatelliteRepository,
    private val metricsRegistry: MetricsRegistry,
    private val alertGateway: AlertGateway,
) {

    private fun isTooMuchChanges(allElementsCount: Int, changedElementsCount: Int): Boolean {
        return changedElementsCount > allElementsCount * UPDATE_THRESHOLD_PERCENT / 100
    }

    private fun isTooMuchDifference(elementsCount: Int, newElementsCount: Int): Boolean {
        return (elementsCount - newElementsCount).absoluteValue > elementsCount * UPDATE_THRESHOLD_PERCENT / 100
    }

    @Transactional
    fun sync(fetchedList: Collection<PhpSatelliteEntity>) {

        val dbSatellites = findAll()

        if (isTooMuchDifference(dbSatellites.size, fetchedList.size)) {

            alertGateway.alert(CATEGORY_CHANGES)
                .let { "#$it - Changes between existing and new satellite list are too massive\n" +
                        "Saved satellites count: ${dbSatellites.size}\n" +
                        "Online satellites count: ${fetchedList.size}" }
                .also { logger.error { it } }

            rollbackTransaction()
            return
        }

        saveNewSatellites(fetchedList, dbSatellites)
        saveChangedSatellites(fetchedList, dbSatellites)
        saveClosedSatellites(fetchedList, dbSatellites)
    }

    fun findAll(): List<PhpSatelliteEntity> {

        return phpSatelliteRepository.findByStatusIs(PhpSatelliteEntity.Status.OPENED.value)
            .log("Got db satellites: ")
            .also { metricsRegistry.updateGauge(Gauges.SATELLITES_DB_ALL, it.size) }
    }


    private fun saveNewSatellites(
        fetchedList: Collection<PhpSatelliteEntity>,
        existingList: Collection<PhpSatelliteEntity>
    ): List<PhpSatelliteEntity> {

        val newSatellites = findNewSatellites(fetchedList, existingList)

//        if (isTooMuchChanges(max(fetchedList, existingList)))

        return newSatellites
            .log("Save new satellites: ")
            .also { metricsRegistry.updateGauge(Gauges.SATELLITES_DB_NEW, it.size) }
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
            .also { metricsRegistry.updateGauge(Gauges.SATELLITES_DB_CLOSED, it.size) }
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
            .also { metricsRegistry.updateGauge(Gauges.SATELLITES_DB_CHANGED, it.size) }
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
