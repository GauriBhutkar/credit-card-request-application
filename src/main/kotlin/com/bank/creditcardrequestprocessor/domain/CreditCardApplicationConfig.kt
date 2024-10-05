package com.bank.creditcardrequestprocessor.domain

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "credit-card-application")
class CreditCardApplicationConfig {
    lateinit var steps: List<CreditCardApplicationStepConfig>
}

data class CreditCardApplicationStepConfig(
    val stepType: ApplicationStep,
    val isMandatory: Boolean,
    val score: Int?,
)
