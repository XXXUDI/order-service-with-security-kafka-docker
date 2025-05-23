package com.socompany.orderservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling // <-- MaGiC
public class RetryableTaskSchedulerConfig {

}
