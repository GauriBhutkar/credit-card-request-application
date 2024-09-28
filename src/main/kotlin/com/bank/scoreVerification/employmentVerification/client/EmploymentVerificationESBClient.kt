package com.bank.scoreVerification.employmentVerification.client

import com.bank.scoreVerification.employmentVerification.domain.EmploymentRecord
import com.bank.scoreVerification.employmentVerification.infra.request.EmploymentVerificationInput
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class EmploymentVerificationESBClient {
    private val logger = LoggerFactory.getLogger(EmploymentVerificationESBClient::class.java)

    fun verifyEmploymentDetails(input: EmploymentVerificationInput): Mono<EmploymentRecord> {
        logger.debug("Fetching employer details for customerId: {}", input.customerId)
        return Mono.empty()
    }
}
