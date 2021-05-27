package com.artemkaxboy.satparser.task

import org.springframework.scheduling.annotation.Scheduled

private const val SECOND = 1_000L

class UpdateTask(
    private val tasks: Set<Task>,
) {

    @Scheduled(fixedDelayString = "#{@devConfiguration.properties.testTaskInterval.toMillis()}", initialDelay = SECOND)
    fun updateTask() {

        tasks.forEach { task ->
            task.run()
        }
    }
}
