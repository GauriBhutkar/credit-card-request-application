package com.bank.scoreVerification.compliancecheck.client

import com.bank.scoreVerification.compliancecheck.domain.ComplianceCheckRecord
import com.bank.scoreVerification.compliancecheck.infra.request.ComplianceCheckInput
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ComplianceCheckESBClient {
    private val logger = LoggerFactory.getLogger(ComplianceCheckESBClient::class.java)

    fun complianceCheck(input: ComplianceCheckInput): Mono<ComplianceCheckRecord> {
        logger.debug("Fetching employer details for customerId: {}", input.customerId)
        return Mono.empty()
    }
}
