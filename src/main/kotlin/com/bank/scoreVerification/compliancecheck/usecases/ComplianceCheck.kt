package com.bank.scoreVerification.compliancecheck.usecases

import com.bank.creditcardrequestprocessor.infra.response.StepResult
import com.bank.scoreVerification.compliancecheck.client.ComplianceCheckESBClient
import com.bank.scoreVerification.compliancecheck.infra.request.ComplianceCheckInput
import com.bank.scoreVerification.compliancecheck.repositories.ComplianceCheckRepository
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono

class ComplianceCheck(
    private val complianceCheckESBClient: ComplianceCheckESBClient,
    private val complianceCheckRepository: ComplianceCheckRepository,
) {
    private val logger = LoggerFactory.getLogger(ComplianceCheck::class.java)

    operator fun invoke(complianceCheckInput: ComplianceCheckInput): Mono<StepResult> {
        logger.debug("Processing compliance check for customer {}", complianceCheckInput.customerId)
        return complianceCheckESBClient.complianceCheck(complianceCheckInput)
            .flatMap { complianceCheckRepository.save(it) }
            .map { StepResult(it.verificationStatus, it.verificationScore) }
    }
}
