package com.bank.scoreVerification.riskevaluation.client

import com.bank.scoreVerification.riskevaluation.domain.RiskEvaluationRecord
import com.bank.scoreVerification.riskevaluation.infra.request.RiskEvaluationInput
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class RiskEvaluationESBClient {
    private val logger = LoggerFactory.getLogger(RiskEvaluationESBClient::class.java)

    fun evaluateRisk(input: RiskEvaluationInput): Mono<RiskEvaluationRecord> {
        logger.debug("Fetching risk details for customerId: {}", input.customerId)
        return Mono.empty()
    }
}
