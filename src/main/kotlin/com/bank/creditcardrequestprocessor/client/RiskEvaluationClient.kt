package com.bank.creditcardrequestprocessor.client

import com.bank.creditcardrequestprocessor.infra.response.StepResult
import com.bank.scoreVerification.riskevaluation.infra.request.RiskEvaluationInput
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class RiskEvaluationClient(
    @Value("\${app.risk-evaluation.host}") val host: String,
    private val webClient: WebClient = WebClient.create(),
) {
    private val logger = LoggerFactory.getLogger(RiskEvaluationClient::class.java)
    fun evaluateRisk(input: RiskEvaluationInput): Mono<StepResult> {
        logger.debug("Risk Evaluation for customerId: {}", input.customerId)
        return webClient.post()
            .uri("$host/api/v1/credit-card/risk-evaluation")
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
