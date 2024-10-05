package com.bank.scoreVerification.compliancecheck.infra.http

import com.bank.creditcardrequestprocessor.infra.response.StepResult
import com.bank.scoreVerification.compliancecheck.client.ComplianceCheckESBClient
import com.bank.scoreVerification.compliancecheck.infra.request.ComplianceCheckInput
import com.bank.scoreVerification.compliancecheck.repositories.ComplianceCheckRepository
import com.bank.scoreVerification.compliancecheck.usecases.ComplianceCheck
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/credit-card")
class ComplianceCheckController(
    complianceCheckESBClient: ComplianceCheckESBClient,
    employmentVerificationRepository: ComplianceCheckRepository,
) {
    private val complianceCheck = ComplianceCheck(complianceCheckESBClient, employmentVerificationRepository)

    @PostMapping("/compliance-check")
    fun applyForCreditCard(
        @RequestBody complianceCheckInput: ComplianceCheckInput,
    ): Mono<ResponseEntity<StepResult>> {
        return complianceCheck(complianceCheckInput).map {
            ResponseEntity(it, HttpStatus.OK)
        }
    }
}
