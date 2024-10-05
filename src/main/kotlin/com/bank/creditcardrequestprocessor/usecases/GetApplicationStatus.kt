package com.bank.creditcardrequestprocessor.usecases

import com.bank.creditcardrequestprocessor.infra.response.ApplicationStatusResponse
import com.bank.creditcardrequestprocessor.repository.CreditCardApplicationProcessRepository
import reactor.core.publisher.Mono
import java.util.UUID

class GetApplicationStatus(
    private val creditCardApplicationProcessRepository: CreditCardApplicationProcessRepository,
) {
    operator fun invoke(applicationId: UUID): Mono<ApplicationStatusResponse> {
        return creditCardApplicationProcessRepository.findById(applicationId)
            .map { ApplicationStatusResponse(it.applicationId, it.dispatchStatus) }
    }
}
