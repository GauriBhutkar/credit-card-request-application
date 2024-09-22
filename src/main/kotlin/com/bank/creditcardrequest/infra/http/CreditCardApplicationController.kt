package com.bank.creditcardrequest.infra.http

import com.bank.creditcardrequest.infra.request.CustomerDetailsInput
import com.bank.creditcardrequest.infra.response.ApplicationStatus
import com.bank.creditcardrequest.repositories.ApplicationRepository
import com.bank.creditcardrequest.usecases.SaveApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/credit-card")
class CreditCardApplicationController(
    applicationRepository: ApplicationRepository,
) {
    private val saveApplication = SaveApplication(applicationRepository)

    @PostMapping("/apply")
    fun applyForCreditCard(
        @RequestBody applicationDetails: CustomerDetailsInput,
    ): Mono<ResponseEntity<ApplicationStatus>> {
        return saveApplication(applicationDetails).map {
            ResponseEntity(it, HttpStatus.ACCEPTED)
        }
    }
}
