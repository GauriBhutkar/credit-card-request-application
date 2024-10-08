package com.bank.scoreVerification.employmentVerification.usecases

import com.bank.creditcardrequestprocessor.infra.response.StepResult
import com.bank.scoreVerification.employmentVerification.client.EmploymentVerificationESBClient
import com.bank.scoreVerification.employmentVerification.infra.request.EmploymentVerificationInput
import com.bank.scoreVerification.employmentVerification.repositories.EmploymentVerificationRepository
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono

class VerifyEmploymentDetails(
    private val employmentVerificationESBClient: EmploymentVerificationESBClient,
    private val employmentVerificationRepository: EmploymentVerificationRepository,
) {
    private val logger = LoggerFactory.getLogger(VerifyEmploymentDetails::class.java)

    operator fun invoke(employmentVerificationInput: EmploymentVerificationInput): Mono<StepResult> {
        logger.debug("Processing employment verification for customer {}", employmentVerificationInput.customerId)
        return employmentVerificationESBClient.verifyEmploymentDetails(employmentVerificationInput)
            .flatMap { employmentVerificationRepository.save(it) }
            .map { StepResult(it.verificationStatus, it.verificationScore) }
    }
}
