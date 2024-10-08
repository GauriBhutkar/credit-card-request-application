package com.bank.scoreVerification.riskevaluation.usecases

import com.bank.creditcardrequestprocessor.infra.response.StepResult
import com.bank.scoreVerification.riskevaluation.client.RiskEvaluationESBClient
import com.bank.scoreVerification.riskevaluation.infra.request.RiskEvaluationInput
import com.bank.scoreVerification.riskevaluation.repositories.RiskEvaluationRepository
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono

class EvaluateRisk(
    private val riskEvaluationESBClient: RiskEvaluationESBClient,
    private val riskEvaluationRepository: RiskEvaluationRepository,
) {
    private val logger = LoggerFactory.getLogger(EvaluateRisk::class.java)

    operator fun invoke(input: RiskEvaluationInput): Mono<StepResult> {
        logger.debug("Processing risk check for customer {}", input.customerId)
        return riskEvaluationESBClient.evaluateRisk(input)
            .flatMap { riskEvaluationRepository.save(it) }
            .map { StepResult(it.verificationStatus, it.verificationScore) }
    }
}
