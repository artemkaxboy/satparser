package com.artemkaxboy.satparser.task

import org.springframework.scheduling.annotation.Scheduled

private const val SECOND = 1_000L

class UpdateTask(
    private val tasks: Set<ITask>,
) {

    @Scheduled(fixedDelayString = "#{@devConfiguration.properties.testTaskInterval.toMillis()}", initialDelay = SECOND)
    fun run() {

        tasks.forEach { task ->
            task.run()
        }
    }
}
