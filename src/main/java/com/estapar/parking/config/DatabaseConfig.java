package com.estapar.parking.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.estapar.parking.repository")
@EntityScan(basePackages = "com.estapar.parking.model.entity")
@EnableTransactionManagement
public class DatabaseConfig {}