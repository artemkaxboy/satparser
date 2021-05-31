package com.artemkaxboy.satparser.alerting

import com.artemkaxboy.satparser.metrics.MetricsRegistry
import org.springframework.stereotype.Component

@Component
class MetricsAlertGateway(

    private val metricsRegistry: MetricsRegistry
) : AlertGateway {

    override fun alert(category: String) =
        metricsRegistry.countError(category).toLong()

    override fun alert(category: String, message: () -> String) =
        alert(category)
}
