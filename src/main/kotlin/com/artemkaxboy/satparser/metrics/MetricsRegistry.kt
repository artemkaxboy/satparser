package com.artemkaxboy.satparser.metrics

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.ImmutableTag
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import javax.annotation.PostConstruct

private const val ERROR_COUNTER_NAME = "application.errors"
private const val ERROR_COUNTER_TYPE_TAG = "type"
private const val DB_LOADED_SATELLITE_GAUGE_NAME = "application.satellite.db"

private const val SATELLITES_ONLINE_COUNT = "application.satellite.online"

private const val LOCAL_SATELLITES_STATUS_TAG_NAME = "status"
private const val LOCAL_SATELLITES_STATUS_ALL = "all"
private const val LOCAL_SATELLITES_STATUS_NEW = "new"
private const val LOCAL_SATELLITES_STATUS_CLOSED = "closed"
private const val LOCAL_SATELLITES_STATUS_CHANGED = "changed"

private const val DEFAULT_COUNT_VALUE = 0

@Component
class MetricsRegistry(private val meterRegistry: MeterRegistry) {

    @PostConstruct
    fun initMeters() {

        updateOnlineSatellitesCount(DEFAULT_COUNT_VALUE)
        updateLocalAllSatellitesCount(DEFAULT_COUNT_VALUE)
        updateLocalNewSatellitesCount(DEFAULT_COUNT_VALUE)
        updateLocalClosedSatellitesCount(DEFAULT_COUNT_VALUE)
        updateLocalChangedSatellitesCount(DEFAULT_COUNT_VALUE)
    }

    fun updateOnlineSatellitesCount(value: Int) = getGauge(SATELLITES_ONLINE_COUNT).set(value)

    fun updateLocalAllSatellitesCount(value: Int) = getGauge(
        DB_LOADED_SATELLITE_GAUGE_NAME,
        ImmutableTag(LOCAL_SATELLITES_STATUS_TAG_NAME, LOCAL_SATELLITES_STATUS_ALL),
    ).set(value)

    fun updateLocalNewSatellitesCount(value: Int) = getGauge(
        DB_LOADED_SATELLITE_GAUGE_NAME,
        ImmutableTag(LOCAL_SATELLITES_STATUS_TAG_NAME, LOCAL_SATELLITES_STATUS_NEW),
    ).set(value)

    fun updateLocalClosedSatellitesCount(value: Int) = getGauge(
        DB_LOADED_SATELLITE_GAUGE_NAME,
        ImmutableTag(LOCAL_SATELLITES_STATUS_TAG_NAME, LOCAL_SATELLITES_STATUS_CLOSED),
    ).set(value)

    fun updateLocalChangedSatellitesCount(value: Int) = getGauge(
        DB_LOADED_SATELLITE_GAUGE_NAME,
        ImmutableTag(LOCAL_SATELLITES_STATUS_TAG_NAME, LOCAL_SATELLITES_STATUS_CHANGED),
    ).set(value)

    /*-----------------------------------------------------------------------------*/

    val counters = ConcurrentHashMap<ObjectIdentifier, Counter>()

    fun getCounter(name: String, tag: Tag? = null) = getCounter(name, if (tag == null) emptyList() else listOf(tag))

    fun getCounter(name: String, tags: List<Tag>) = getCounter(ObjectIdentifier(name, tags))

    fun getCounter(identifier: ObjectIdentifier): Counter {

        return counters.getOrPut(identifier) { meterRegistry.counter(identifier.name, identifier.tags) }
    }

    val gauges = ConcurrentHashMap<ObjectIdentifier, AtomicInteger>()

    fun getGauge(name: String, tag: Tag? = null) = getGauge(name, if (tag == null) emptyList() else listOf(tag))

    fun getGauge(name: String, tags: List<Tag>) = getGauge(ObjectIdentifier(name, tags))

    fun getGauge(identifier: ObjectIdentifier): AtomicInteger {

        return gauges.getOrPut(identifier) {
            requireNotNull(meterRegistry.gauge(identifier.name, identifier.tags, AtomicInteger()))
        }
    }

    fun countError(type: String) =
        countAndReturn(getCounter(ERROR_COUNTER_NAME, ImmutableTag(ERROR_COUNTER_TYPE_TAG, type)))

    private fun countAndReturn(counter: Counter) = counter.apply { increment() }.count().toLong()

    data class ObjectIdentifier(val name: String, val tags: List<Tag>)
}
