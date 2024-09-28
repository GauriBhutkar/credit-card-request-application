package com.bank.scoreVerification.employmentVerification.infra.http

import com.bank.scoreVerification.employmentVerification.client.EmploymentVerificationESBClient
import com.bank.scoreVerification.employmentVerification.infra.request.EmploymentVerificationInput
import com.bank.scoreVerification.employmentVerification.infra.response.EmploymentVerificationResult
import com.bank.scoreVerification.employmentVerification.repositories.EmploymentVerificationRepository
import com.bank.scoreVerification.employmentVerification.usecases.VerifyEmploymentDetails
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/credit-card")
class EmploymentVerificationController(
    employmentVerificationESBClient: EmploymentVerificationESBClient,
    employmentVerificationRepository: EmploymentVerificationRepository,
) {
    private val verifyEmploymentDetails = VerifyEmploymentDetails(employmentVerificationESBClient, employmentVerificationRepository)

    @PostMapping("/employment-verifications")
    fun applyForCreditCard(
        @RequestBody employmentVerificationInput: EmploymentVerificationInput,
    ): Mono<ResponseEntity<EmploymentVerificationResult>> {
        return verifyEmploymentDetails(employmentVerificationInput).map {
            ResponseEntity(it, HttpStatus.ACCEPTED)
        }
    }
}
