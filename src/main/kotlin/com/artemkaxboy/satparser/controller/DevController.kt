package com.artemkaxboy.satparser.controller

import com.artemkaxboy.satparser.metrics.MetricsRegistry
import mu.KotlinLogging
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

private val logger = KotlinLogging.logger {}

@Controller
@Profile("dev")
class TestController(
    private val metricsRegistry: MetricsRegistry,
) {

    @RequestMapping(value = ["/echo/{request}"], method = [RequestMethod.GET])
    @ResponseBody
    fun test(@PathVariable("request") request: String): String {
        return request.also { logger.trace { "got new echo request: $request" } }
    }

    @RequestMapping(value = ["/error/{category}"], method = [RequestMethod.GET])
    @ResponseBody
    fun error(@PathVariable("category") category: String): String {
        return metricsRegistry.countError(category).toLong().toString()
    }
}
