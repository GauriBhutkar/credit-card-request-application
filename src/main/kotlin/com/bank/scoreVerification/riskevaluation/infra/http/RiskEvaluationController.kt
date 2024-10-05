package com.bank.scoreVerification.riskevaluation.infra.http

import com.bank.creditcardrequestprocessor.infra.response.StepResult
import com.bank.scoreVerification.riskevaluation.client.RiskEvaluationESBClient
import com.bank.scoreVerification.riskevaluation.infra.request.RiskEvaluationInput
import com.bank.scoreVerification.riskevaluation.repositories.RiskEvaluationRepository
import com.bank.scoreVerification.riskevaluation.usecases.EvaluateRisk
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/credit-card")
class RiskEvaluationController(
    checkESBClient: RiskEvaluationESBClient,
    riskEvaluationRepository: RiskEvaluationRepository,
) {
    private val evaluate = EvaluateRisk(checkESBClient, riskEvaluationRepository)

    @PostMapping("/risk-evaluation")
    fun evaluateRisk(
        @RequestBody input: RiskEvaluationInput,
    ): Mono<ResponseEntity<StepResult>> {
        return evaluate(input).map {
            ResponseEntity(it, HttpStatus.OK)
        }
    }
}
