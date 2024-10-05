package com.bank.creditcardrequestprocessor.client

import com.bank.creditcardrequestprocessor.infra.response.StepResult
import com.bank.scoreVerification.compliancecheck.infra.request.ComplianceCheckInput
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class ComplianceCheckClient(
    @Value("\${app.compliance-check.host}") val host: String,
    private val webClient: WebClient = WebClient.create(),
) {
    private val logger = LoggerFactory.getLogger(ComplianceCheckClient::class.java)
    fun checkCompliance(input: ComplianceCheckInput): Mono<StepResult> {
        logger.debug("Compliance check for customerId: {}", input.customerId)
        return webClient.post()
            .uri("$host/api/v1/credit-card/compliance-check")
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromObject(input))
            .retrieve()
            .bodyToMono(StepResult::class.java)
            .doOnError {
                val error = "Unable to complete the check: ${it.message}"
                logger.error(error)
                throw Exception(error)
            }
    }
}
