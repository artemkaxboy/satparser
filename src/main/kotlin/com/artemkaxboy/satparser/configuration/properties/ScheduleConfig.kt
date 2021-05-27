package com.artemkaxboy.satparser.configuration.properties

import com.artemkaxboy.satparser.task.Task
import com.artemkaxboy.satparser.task.UpdateTask
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
class ScheduleConfig(
    private val tasks: Set<Task>,
) {

    @Profile("prod", "scheduler")
    @Bean
    fun updateTask(): UpdateTask {
        return UpdateTask(tasks)
    }
}
