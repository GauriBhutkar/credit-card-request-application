package com.bank.creditcardrequest.usecases

import com.bank.creditcardrequest.infra.request.CustomerDetailsInput
import com.bank.creditcardrequest.infra.response.ApplicationStatus
import com.bank.creditcardrequest.infra.response.Status
import com.bank.creditcardrequest.repositories.ApplicationRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class SaveApplicationTest {

    private val applicationRepository: ApplicationRepository = mock()
    private val saveApplication: SaveApplication = SaveApplication(applicationRepository)

    @Test
    fun `should return ApplicationStatus with UUID and Status UNDER_REVIEW`() {
        val customerDetailsInput = mock<CustomerDetailsInput>()

        val result: Mono<ApplicationStatus> = saveApplication.invoke(customerDetailsInput)

        StepVerifier.create(result)
            .assertNext { applicationStatus ->
                assertNotNull(applicationStatus.applicationId)
                assertEquals(Status.UNDER_REVIEW, applicationStatus.status)
            }
            .verifyComplete()
    }
}