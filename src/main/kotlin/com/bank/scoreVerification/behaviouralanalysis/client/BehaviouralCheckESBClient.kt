package com.bank.scoreVerification.behaviouralanalysis.client

import com.bank.scoreVerification.behaviouralanalysis.domain.BehaviourCheckRecord
import com.bank.scoreVerification.behaviouralanalysis.infra.request.BehaviourCheckInput
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class BehaviouralCheckESBClient {
    private val logger = LoggerFactory.getLogger(BehaviouralCheckESBClient::class.java)

    fun behaviourCheck(input: BehaviourCheckInput): Mono<BehaviourCheckRecord> {
        logger.debug("Fetching behaviour details for customerId: {}", input.customerId)
        return Mono.empty()
    }
}
