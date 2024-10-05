package com.bank.scoreVerification.employmentVerification.usecases

import com.bank.creditcardrequest.domain.VerificationStatus
import com.bank.creditcardrequest.infra.request.EmploymentDetails
import com.bank.creditcardrequest.infra.request.EmploymentStatus
import com.bank.creditcardrequestprocessor.infra.response.StepResult
import com.bank.scoreVerification.employmentVerification.client.EmploymentVerificationESBClient
import com.bank.scoreVerification.employmentVerification.domain.EmployerDetails
import com.bank.scoreVerification.employmentVerification.domain.EmploymentRecord
import com.bank.scoreVerification.employmentVerification.domain.EmploymentType
import com.bank.scoreVerification.employmentVerification.domain.IncomeInformation
import com.bank.scoreVerification.employmentVerification.infra.request.EmploymentVerificationInput
import com.bank.scoreVerification.employmentVerification.repositories.EmploymentVerificationRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.UUID

class VerifyEmploymentDetailsTest {

    private val employmentVerificationESBClient: EmploymentVerificationESBClient = mockk()
    private val employmentVerificationRepository: EmploymentVerificationRepository = mockk()
    private val verifyEmploymentDetails: VerifyEmploymentDetails =
        VerifyEmploymentDetails(employmentVerificationESBClient, employmentVerificationRepository)

    private val employmentRecord = EmploymentRecord(
        id = UUID.randomUUID(),
        employerDetails = EmployerDetails(
            employerName = "Tech Solutions",
            employerContact = "+971-55-567-8901",
            employerAddress = "1234 Business Bay, Dubai",
        ),
        jobTitle = "Software Engineer",
        position = "Senior Engineer",
        employmentStatus = "Active",
        incomeInformation = IncomeInformation(
            amount = 20000.0,
            frequency = "Monthly",
        ),
        employmentType = EmploymentType.PERMANENT,
        verificationMethod = "Direct contact with employer",
        verificationStatus = VerificationStatus.VERIFIED,
        verificationScore = 85,
    )

    @Test
    fun `should verify employment details and return EmploymentVerificationResult`() {
        val customerId = UUID.randomUUID()
        val input = EmploymentVerificationInput(
            customerId,
            "John Doe",
            "9987654321",
            EmploymentDetails(EmploymentStatus.FULL_TIME, "Tech Solutions"),
        )

        val expectedResult = StepResult(
            verificationStatus = VerificationStatus.VERIFIED,
            verificationScore = 85,
        )
        every { employmentVerificationESBClient.verifyEmploymentDetails(input) } returns Mono.just(employmentRecord)
        every { employmentVerificationRepository.save(employmentRecord) } returns Mono.just(employmentRecord)

        val result: Mono<StepResult> = verifyEmploymentDetails(input)

        StepVerifier.create(result).consumeNextWith { assertEquals(it, expectedResult) }.verifyComplete()
    }
}
