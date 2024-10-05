package com.bank.scoreVerification.identityverification.usecases

import com.bank.creditcardrequestprocessor.infra.response.StepResult
import com.bank.scoreVerification.identityverification.client.IdentityVerificationESBClient
import com.bank.scoreVerification.identityverification.infra.request.IdentityVerificationInput
import com.bank.scoreVerification.identityverification.repositories.IdentityVerificationRepository
import com.bank.scoreVerification.riskevaluation.usecases.EvaluateRisk
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono

class VerifyIdentity(
    private val esbClient: IdentityVerificationESBClient,
    private val repository: IdentityVerificationRepository,
) {
    private val logger = LoggerFactory.getLogger(EvaluateRisk::class.java)

    operator fun invoke(input: IdentityVerificationInput): Mono<StepResult> {
        logger.debug("Processing identity check for customer {}", input.customerId)
        return esbClient.verifyIdentity(input)
            .flatMap { repository.save(it) }
            .map { StepResult(it.verificationStatus, it.verificationScore) }
    }
}
