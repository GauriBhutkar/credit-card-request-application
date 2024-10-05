package com.bank.scoreVerification.identityverification.usecases

import com.bank.creditcardrequest.domain.VerificationStatus
import com.bank.creditcardrequestprocessor.infra.response.StepResult
import com.bank.scoreVerification.identityverification.client.IdentityVerificationESBClient
import com.bank.scoreVerification.identityverification.domain.IdentityVerificationRecord
import com.bank.scoreVerification.identityverification.domain.IdentityVerificationReport
import com.bank.scoreVerification.identityverification.infra.request.IdentityVerificationInput
import com.bank.scoreVerification.identityverification.repositories.IdentityVerificationRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.UUID

class VerifyIdentityTest {
    private val esbClient: IdentityVerificationESBClient = mockk()
    private val repository: IdentityVerificationRepository = mockk()
    private val verifyIdentity: VerifyIdentity = VerifyIdentity(esbClient, repository)
    private val customerId = UUID.randomUUID()
    private val customerName = "John Doe"

    private val identityVerificationRecord = IdentityVerificationRecord(
        UUID.randomUUID(),
        customerId,
        customerName,
        IdentityVerificationReport("John Doe", "Married", "Male", 0, 20000.0),
        "",
        "AUTOMATIC",
        VerificationStatus.VERIFIED,
        20,
    )

    @Test
    fun `should verify identity and return Result`() {
        val input = IdentityVerificationInput(
            customerId = customerId,
            customerName = customerName,
            "111111111",
        )

        val expectedResult = StepResult(
            verificationStatus = VerificationStatus.VERIFIED,
            verificationScore = 20,
        )
        every { esbClient.verifyIdentity(input) } returns Mono.just(identityVerificationRecord)
        every { repository.save(identityVerificationRecord) } returns Mono.just(identityVerificationRecord)

        val result: Mono<StepResult> = verifyIdentity(input)

        StepVerifier.create(result).consumeNextWith { assertEquals(it, expectedResult) }.verifyComplete()
    }
}
