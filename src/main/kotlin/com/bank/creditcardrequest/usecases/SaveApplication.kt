package com.bank.creditcardrequest.usecases

import com.bank.creditcardrequest.infra.request.CustomerDetailsInput
import com.bank.creditcardrequest.infra.response.ApplicationStatus
import com.bank.creditcardrequest.infra.response.Status
import com.bank.creditcardrequest.repositories.ApplicationRepository
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.*

class SaveApplication(
    private val applicationRepository: ApplicationRepository
) {
    operator fun invoke(application: CustomerDetailsInput): Mono<ApplicationStatus> {
        return ApplicationStatus(UUID.randomUUID(), Status.UNDER_REVIEW).toMono()
    }
}