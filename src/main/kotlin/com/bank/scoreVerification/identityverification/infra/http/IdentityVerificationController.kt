package com.bank.scoreVerification.identityverification.infra.http

import com.bank.creditcardrequestprocessor.infra.response.StepResult
import com.bank.scoreVerification.identityverification.client.IdentityVerificationESBClient
import com.bank.scoreVerification.identityverification.infra.request.IdentityVerificationInput
import com.bank.scoreVerification.identityverification.repositories.IdentityVerificationRepository
import com.bank.scoreVerification.identityverification.usecases.VerifyIdentity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/credit-card")
class IdentityVerificationController(
    esbClient: IdentityVerificationESBClient,
    repository: IdentityVerificationRepository,
) {
    private val evaluate = VerifyIdentity(esbClient, repository)

    @PostMapping("/identity-verification")
    fun evaluateRisk(
        @RequestBody input: IdentityVerificationInput,
    ): Mono<ResponseEntity<StepResult>> {
        return evaluate(input).map {
            ResponseEntity(it, HttpStatus.OK)
        }
    }
}
