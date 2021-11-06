package com.college.transfer;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TransferProducerApplication.class})
@ComponentScan(basePackages = {"com.college.transfer"})
public class TestConfig {
}
