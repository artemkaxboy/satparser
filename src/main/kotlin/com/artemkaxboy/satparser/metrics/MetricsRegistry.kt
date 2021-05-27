package com.artemkaxboy.satparser.metrics

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.ImmutableTag
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import kotlinx.coroutines.newSingleThreadContext
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger

private const val ERROR_COUNTER_NAME = "application.errors"
private const val DB_LOADED_SATELLITE_GAUGE_NAME = "application.satellite.db"

private const val SATELLITES_ONLINE_COUNT = "application.satellite.online"

private const val LOCAL_SATELLITES_STATUS_TAG_NAME = "status"
private const val LOCAL_SATELLITES_STATUS_ALL = "all"
private const val LOCAL_SATELLITES_STATUS_NEW = "new"
private const val LOCAL_SATELLITES_STATUS_CLOSED = "closed"
private const val LOCAL_SATELLITES_STATUS_CHANGED = "changed"


@Component
class MetricsRegistry(private val meterRegistry: MeterRegistry) {

    private val satelliteOnlineCount = makeIntegerGauge(SATELLITES_ONLINE_COUNT)

    private val satelliteLocalAllCount = makeIntegerGauge(
        DB_LOADED_SATELLITE_GAUGE_NAME,
        ImmutableTag(LOCAL_SATELLITES_STATUS_TAG_NAME, LOCAL_SATELLITES_STATUS_ALL),
    )

    private val satelliteLocalNewCount = makeIntegerGauge(
        DB_LOADED_SATELLITE_GAUGE_NAME,
        ImmutableTag(LOCAL_SATELLITES_STATUS_TAG_NAME, LOCAL_SATELLITES_STATUS_NEW),
    )

    private val satelliteLocalClosedCount = makeIntegerGauge(
        DB_LOADED_SATELLITE_GAUGE_NAME,
        ImmutableTag(LOCAL_SATELLITES_STATUS_TAG_NAME, LOCAL_SATELLITES_STATUS_CLOSED),
    )

    private val satelliteLocalChangedCount = makeIntegerGauge(
        DB_LOADED_SATELLITE_GAUGE_NAME,
        ImmutableTag(LOCAL_SATELLITES_STATUS_TAG_NAME, LOCAL_SATELLITES_STATUS_CHANGED),
    )

    fun updateOnlineSatellitesCount(value: Int) = satelliteOnlineCount.set(value)

    fun updateLocalAllSatellitesCount(value: Int) = satelliteLocalAllCount.set(value)

    fun updateLocalNewSatellitesCount(value: Int) = satelliteLocalNewCount.set(value)

    fun updateLocalClosedSatellitesCount(value: Int) = satelliteLocalClosedCount.set(value)

    fun updateLocalChangedSatellitesCount(value: Int) = satelliteLocalChangedCount.set(value)

    /*-----------------------------------------------------------------------------*/

    val counters = mutableMapOf<ObjectIdentifier, Counter>()

    fun getCounter(name: String, tag: Tag? = null) = getCounter(name, if (tag == null) emptyList() else listOf(tag))

    fun getCounter(name: String, tags: List<Tag>) = getCounter(ObjectIdentifier(name, tags))

    fun getCounter(identifier: ObjectIdentifier): Counter {

        return counters[identifier] ?: meterRegistry.counter(identifier.name, identifier.tags)
            .also { counters[identifier] = it }
    }

    val counterContext = newSingleThreadContext("CounterContext")

    fun createCounter(identifier: ObjectIdentifier) {

    }

    fun countError(type: String) = countAndReturn(meterRegistry.counter(ERROR_COUNTER_NAME, "type", type))
    private fun countAndReturn(counter: Counter) = counter.apply { increment() }.count().toLong()

    private fun makeIntegerGauge(name: String, tag: Tag? = null, value: AtomicInteger = AtomicInteger()) =
        makeIntegerGauge(name, if (tag == null) emptyList() else listOf(tag), value)

    private fun makeIntegerGauge(
        name: String,
        tags: List<Tag>,
        value: AtomicInteger = AtomicInteger()
    ): AtomicInteger {
        return requireNotNull(meterRegistry.gauge(name, tags, value))
    }

    data class ObjectIdentifier(val name: String, val tags: List<Tag>)
}
