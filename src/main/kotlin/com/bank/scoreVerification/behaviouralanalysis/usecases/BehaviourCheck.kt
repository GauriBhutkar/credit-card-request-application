package com.bank.scoreVerification.behaviouralanalysis.usecases

import com.bank.scoreVerification.behaviouralanalysis.client.BehaviouralCheckESBClient
import com.bank.scoreVerification.behaviouralanalysis.infra.request.BehaviourCheckInput
import com.bank.scoreVerification.behaviouralanalysis.infra.response.BehaviourCheckResult
import com.bank.scoreVerification.behaviouralanalysis.repositories.BehaviourCheckRepository
import com.bank.scoreVerification.compliancecheck.usecases.ComplianceCheck
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono

class BehaviourCheck(
    private val behaviouralCheckESBClient: BehaviouralCheckESBClient,
    private val behaviourCheckRepository: BehaviourCheckRepository,
) {
    private val logger = LoggerFactory.getLogger(ComplianceCheck::class.java)

    operator fun invoke(input: BehaviourCheckInput): Mono<BehaviourCheckResult> {
        logger.debug("Processing behaviour check for customer {}", input.customerId)
        return behaviouralCheckESBClient.behaviourCheck(input)
            .flatMap { behaviourCheckRepository.save(it) }
            .map { BehaviourCheckResult(it.verificationStatus, it.verificationScore) }
    }
}
