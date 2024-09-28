package com.bank.creditcardrequest.usecases

import com.bank.creditcardrequest.domain.CreditCardApplication
import com.bank.creditcardrequest.infra.request.CustomerDetailsInput
import com.bank.creditcardrequest.infra.response.ApplicationStatus
import com.bank.creditcardrequest.infra.response.Status
import com.bank.creditcardrequest.repositories.ApplicationRepository
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono

class SaveApplication(
    private val applicationRepository: ApplicationRepository,
) {
    private val logger = LoggerFactory.getLogger(SaveApplication::class.java)

    operator fun invoke(applicationInput: CustomerDetailsInput): Mono<ApplicationStatus> {
        val creditCardApplication = CreditCardApplication.from(applicationInput)

        return applicationRepository.save(creditCardApplication)
            .map {
                logger.info("Application saved successfully with ID: ${creditCardApplication.id}")
                ApplicationStatus(it.id, Status.UNDER_REVIEW)
            }
    }
}
