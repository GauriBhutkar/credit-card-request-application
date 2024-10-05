package com.bank.scoreVerification.identityverification.client

import com.bank.scoreVerification.identityverification.domain.IdentityVerificationRecord
import com.bank.scoreVerification.identityverification.infra.request.IdentityVerificationInput
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class IdentityVerificationESBClient {
    private val logger = LoggerFactory.getLogger(IdentityVerificationESBClient::class.java)

    fun verifyIdentity(input: IdentityVerificationInput): Mono<IdentityVerificationRecord> {
        logger.debug("Executing identity verification for customerId: {}", input.customerId)
        return Mono.empty()
    }
}
