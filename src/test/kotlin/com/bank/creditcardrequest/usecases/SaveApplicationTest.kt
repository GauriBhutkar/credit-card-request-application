package com.bank.creditcardrequest.usecases

import com.bank.creditcardrequest.domain.CreditCardApplication
import com.bank.creditcardrequest.infra.request.CustomerDetailsInput
import com.bank.creditcardrequest.infra.request.EmploymentDetails
import com.bank.creditcardrequest.infra.request.EmploymentStatus
import com.bank.creditcardrequest.infra.response.ApplicationStatus
import com.bank.creditcardrequest.infra.response.Status
import com.bank.creditcardrequest.repositories.ApplicationRepository
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.io.File
import java.util.UUID

class SaveApplicationTest {

    private val applicationRepository: ApplicationRepository = mock()
    private val saveApplication: SaveApplication = SaveApplication(applicationRepository)

    @Test
    fun `should return ApplicationStatus with UUID and Status UNDER_REVIEW`() {
        val customerDetailsInput = CustomerDetailsInput(
            UUID.randomUUID(),
            "John Doe",
            "9912345678",
            "INDIAN",
            "Delhi",
            100000,
            EmploymentDetails(EmploymentStatus.FULL_TIME, "Deloitte"),
            28000,
            File.createTempFile("bank_statement", ".pdf"),
        )
        val savedApplication = CreditCardApplication(
            UUID.randomUUID(),
            customerDetailsInput.customerId,
            customerDetailsInput.customerName,
            customerDetailsInput.mobileNumber,
            customerDetailsInput.nationality,
            customerDetailsInput.address,
            customerDetailsInput.annualIncome,
            com.bank.creditcardrequest.domain.EmploymentDetails("FULL_TIME", "Deloitte"),
            customerDetailsInput.creditCardLimit,
            customerDetailsInput.bankStatement,
        )
        every {
            applicationRepository.save(any())
        } returns Mono.just(savedApplication)

        val result: Mono<ApplicationStatus> = saveApplication.invoke(customerDetailsInput)

        StepVerifier.create(result)
            .assertNext { response ->
                assertEquals(response.applicationId, savedApplication.id)
                assertEquals(Status.UNDER_REVIEW, response.status)
            }
            .verifyComplete()
    }
}
