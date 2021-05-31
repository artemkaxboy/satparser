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
private const val ERROR_TYPE_TAG_NAME = "type"

private const val ONLINE_SATELLITES_GAUGE_NAME = "application.satellite.online"

private const val LOCAL_SATELLITES_GAUGE_NAME = "application.satellite.db"
private const val LOCAL_SATELLITES_STATUS_TAG_NAME = "status"

private const val TAG_ALL = "all"
private const val TAG_NEW = "new"
private const val TAG_CLOSED = "closed"
private const val TAG_CHANGED = "changed"

@Component
class MetricsRegistry(private val meterRegistry: MeterRegistry) {

    private val counters = ConcurrentHashMap<ObjectIdentifier, Counter>()
    private val gauges = ConcurrentHashMap<ObjectIdentifier, AtomicInteger>()

    @PostConstruct
    private fun initMeters() {

        Meter.values().forEach {
            getGauge(it.meterName, it.tag)
        }
    }

    fun updateMeter(meter: Meter, value: Int) {

        getGauge(meter.meterName).set(value)
    }

    fun count(meter: Meter, tag: Tag?) =
        getCounter(meter.meterName, tag ?: meter.tag).apply { increment() }.count()

    fun countError(type: String): Double {

        count(Meter.ERRORS, ImmutableTag(ERROR_TYPE_TAG_NAME, type))
        return count(Meter.ERRORS, null)
    }

    private fun getCounter(name: String, tag: Tag? = null) =
        getCounter(name, if (tag == null) emptyList() else listOf(tag))

    private fun getCounter(name: String, tags: List<Tag>) = getCounter(ObjectIdentifier(name, tags))

    private fun getCounter(identifier: ObjectIdentifier): Counter {

        return counters.getOrPut(identifier) { meterRegistry.counter(identifier.name, identifier.tags) }
    }

    private fun getGauge(name: String, tag: Tag? = null) = getGauge(name, if (tag == null) emptyList() else listOf(tag))

    private fun getGauge(name: String, tags: List<Tag>) = getGauge(ObjectIdentifier(name, tags))

    private fun getGauge(identifier: ObjectIdentifier): AtomicInteger {

        return gauges.getOrPut(identifier) {
            requireNotNull(meterRegistry.gauge(identifier.name, identifier.tags, AtomicInteger()))
        }
    }

    private data class ObjectIdentifier(val name: String, val tags: List<Tag>)
}

enum class Meter(val meterName: String, val tag: Tag?) {

    SATELLITES_ONLINE(ONLINE_SATELLITES_GAUGE_NAME, null),

    SATELLITES_DB_ALL(
        LOCAL_SATELLITES_GAUGE_NAME,
        ImmutableTag(LOCAL_SATELLITES_STATUS_TAG_NAME, TAG_ALL)
    ),

    SATELLITES_DB_NEW(
        LOCAL_SATELLITES_GAUGE_NAME,
        ImmutableTag(LOCAL_SATELLITES_STATUS_TAG_NAME, TAG_NEW)
    ),

    SATELLITES_DB_CLOSED(
        LOCAL_SATELLITES_GAUGE_NAME,
        ImmutableTag(LOCAL_SATELLITES_STATUS_TAG_NAME, TAG_CLOSED)
    ),

    SATELLITES_DB_CHANGED(
        LOCAL_SATELLITES_GAUGE_NAME,
        ImmutableTag(LOCAL_SATELLITES_STATUS_TAG_NAME, TAG_CHANGED)
    ),

    ERRORS(ERROR_COUNTER_NAME, ImmutableTag(ERROR_TYPE_TAG_NAME, TAG_ALL)),
}
