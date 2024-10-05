package com.bank.scoreVerification.behaviouralanalysis.infra.http

import com.bank.creditcardrequestprocessor.infra.response.StepResult
import com.bank.scoreVerification.behaviouralanalysis.client.BehaviouralCheckESBClient
import com.bank.scoreVerification.behaviouralanalysis.infra.request.BehaviourCheckInput
import com.bank.scoreVerification.behaviouralanalysis.repositories.BehaviourCheckRepository
import com.bank.scoreVerification.behaviouralanalysis.usecases.BehaviourCheck
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/credit-card")
class BehaviourCheckController(
    behaviouralCheckESBClient: BehaviouralCheckESBClient,
    behaviourCheckRepository: BehaviourCheckRepository,
) {
    private val behaviourCheck = BehaviourCheck(behaviouralCheckESBClient, behaviourCheckRepository)

    @PostMapping("/behaviour-check")
    fun applyForCreditCard(
        @RequestBody behaviourCheckInput: BehaviourCheckInput,
    ): Mono<ResponseEntity<StepResult>> {
        return behaviourCheck(behaviourCheckInput).map {
            ResponseEntity(it, HttpStatus.OK)
        }
    }
}
