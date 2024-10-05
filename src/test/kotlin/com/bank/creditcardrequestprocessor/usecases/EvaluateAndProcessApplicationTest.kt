package com.bank.creditcardrequestprocessor.usecases

import com.bank.creditcardrequest.domain.VerificationStatus
import com.bank.creditcardrequest.infra.request.EmploymentDetails
import com.bank.creditcardrequest.infra.request.EmploymentStatus
import com.bank.creditcardrequestprocessor.client.*
import com.bank.creditcardrequestprocessor.domain.*
import com.bank.creditcardrequestprocessor.infra.request.ProcessCreditCardInput
import com.bank.creditcardrequestprocessor.infra.response.CreditCardApplicationProcessResponse
import com.bank.creditcardrequestprocessor.infra.response.StepResult
import com.bank.creditcardrequestprocessor.repository.CreditCardApplicationProcessRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.io.File
import java.util.UUID

class EvaluateAndProcessApplicationTest {

    private val identityVerificationClient: IdentityVerificationClient = mockk()
    private val complianceCheckClient: ComplianceCheckClient = mockk()
    private val behaviourAnalysisClient: BehaviourAnalysisClient = mockk()
    private val employmentVerificationClient: EmploymentVerificationClient = mockk()
    private val riskEvaluationClient: RiskEvaluationClient = mockk()
    private val creditCardApplicationProcessRepository: CreditCardApplicationProcessRepository = mockk()
    private val config: CreditCardApplicationConfig = mockk()

    private val evaluateAndProcessApplication = EvaluateAndProcessApplication(
        identityVerificationClient,
        complianceCheckClient,
        behaviourAnalysisClient,
        employmentVerificationClient,
        riskEvaluationClient,
        creditCardApplicationProcessRepository,
        config
    )

    private val applicationId = UUID.randomUUID()
    private val input = ProcessCreditCardInput(
        customerId = UUID.randomUUID(),
        customerName = "John Doe",
        mobileNumber = "1111111111",
        nationality = "INDIAN",
        235000,
        EmploymentDetails(EmploymentStatus.FULL_TIME, "Global Inc"),
        20000,
        File.createTempFile("bank-statement", ".pdf"),
        additionalInput = ""
    )

    @BeforeEach
    fun setUp() {
        every { config.steps } returns listOf(
            CreditCardApplicationStepConfig(ApplicationStep.IDENTITY_VERIFICATION, true, null),
            CreditCardApplicationStepConfig(ApplicationStep.COMPLIANCE_CHECK, false, null),
            CreditCardApplicationStepConfig(ApplicationStep.EMPLOYMENT_VERIFICATION, false, null),
            CreditCardApplicationStepConfig(ApplicationStep.RISK_EVALUATION, false, null),
            CreditCardApplicationStepConfig(ApplicationStep.BEHAVIOR_ANALYSIS, false, null)
        )
    }

    @Test
    fun `should reject application if any mandatory step fails`() {
        every { creditCardApplicationProcessRepository.save(any()) } answers { Mono.just(firstArg()) }
        every { identityVerificationClient.identityVerification(any()) } returns Mono.just(StepResult(VerificationStatus.REJECTED, 0))

        val result: Mono<CreditCardApplicationProcessResponse> = evaluateAndProcessApplication(applicationId, input)

        StepVerifier.create(result)
            .expectNextMatches { response ->
                assertEquals(DispatchStatus.REJECTED, response.dispatchStatus)
                response.applicationId == applicationId
            }
            .verifyComplete()

        verify(exactly = 1) { creditCardApplicationProcessRepository.save(any()) }
    }

    @Test
    fun `should process application with STP dispatch status when overall score is above 90`() {
        every { creditCardApplicationProcessRepository.save(any()) } answers { Mono.just(firstArg()) }
        every { identityVerificationClient.identityVerification(any()) } returns Mono.just(StepResult(VerificationStatus.VERIFIED, 20))
        every { complianceCheckClient.checkCompliance(any()) } returns Mono.just(StepResult(VerificationStatus.VERIFIED, 20))
        every { riskEvaluationClient.evaluateRisk(any()) } returns Mono.just(StepResult(VerificationStatus.VERIFIED, 15))
        every { behaviourAnalysisClient.analyzeBehavior(any()) } returns Mono.just(StepResult(VerificationStatus.VERIFIED, 18))
        every { employmentVerificationClient.verifyEmployment(any()) } returns Mono.just(StepResult(VerificationStatus.VERIFIED, 20))

        val result: Mono<CreditCardApplicationProcessResponse> = evaluateAndProcessApplication(applicationId, input)

        StepVerifier.create(result)
            .expectNextMatches { response ->
                assertEquals(DispatchStatus.STP, response.dispatchStatus)
                assertEquals(93, response.overallScore)
                response.applicationId == applicationId
            }
            .verifyComplete()

        verify(exactly = 1) { creditCardApplicationProcessRepository.save(any()) }
    }

    @Test
    fun `should process application with NEAR_STP dispatch status when overall score is between 75 and 90`() {
        every { creditCardApplicationProcessRepository.save(any()) } answers { Mono.just(firstArg()) }
        every { identityVerificationClient.identityVerification(any()) } returns Mono.just(StepResult(VerificationStatus.VERIFIED, 15))
        every { complianceCheckClient.checkCompliance(any()) } returns Mono.just(StepResult(VerificationStatus.VERIFIED, 18))
        every { riskEvaluationClient.evaluateRisk(any()) } returns Mono.just(StepResult(VerificationStatus.VERIFIED, 15))
        every { behaviourAnalysisClient.analyzeBehavior(any()) } returns Mono.just(StepResult(VerificationStatus.VERIFIED, 18))
        every { employmentVerificationClient.verifyEmployment(any()) } returns Mono.just(StepResult(VerificationStatus.VERIFIED, 20))

        val result: Mono<CreditCardApplicationProcessResponse> = evaluateAndProcessApplication(applicationId, input)

        StepVerifier.create(result)
            .expectNextMatches { response ->
                assertEquals(DispatchStatus.NEAR_STP, response.dispatchStatus)
                assertEquals(86, response.overallScore)
                response.applicationId == applicationId
            }
            .verifyComplete()
    }

    @Test
    fun `should process application with MANUAL_REVIEW dispatch status when overall score is between 50 and 75`() {
        every { creditCardApplicationProcessRepository.save(any()) } answers { Mono.just(firstArg()) }
        every { identityVerificationClient.identityVerification(any()) } returns Mono.just(StepResult(VerificationStatus.VERIFIED, 20))
        every { complianceCheckClient.checkCompliance(any()) } returns Mono.just(StepResult(VerificationStatus.VERIFIED, 6))
        every { riskEvaluationClient.evaluateRisk(any()) } returns Mono.just(StepResult(VerificationStatus.VERIFIED, 8))
        every { behaviourAnalysisClient.analyzeBehavior(any()) } returns Mono.just(StepResult(VerificationStatus.VERIFIED, 10))
        every { employmentVerificationClient.verifyEmployment(any()) } returns Mono.just(StepResult(VerificationStatus.VERIFIED, 8))

        val result: Mono<CreditCardApplicationProcessResponse> = evaluateAndProcessApplication(applicationId, input)

        StepVerifier.create(result)
            .expectNextMatches { response ->
                assertEquals(DispatchStatus.MANUAL_REVIEW, response.dispatchStatus)
                assertEquals(52, response.overallScore)
                response.applicationId == applicationId
            }
            .verifyComplete()
    }

    @Test
    fun `should reject application with REJECTED dispatch status when overall score is below 50`() {
        every { creditCardApplicationProcessRepository.save(any()) } answers { Mono.just(firstArg()) }
        every { identityVerificationClient.identityVerification(any()) } returns Mono.just(StepResult(VerificationStatus.VERIFIED, 10))
        every { complianceCheckClient.checkCompliance(any()) } returns Mono.just(StepResult(VerificationStatus.VERIFIED, 6))
        every { riskEvaluationClient.evaluateRisk(any()) } returns Mono.just(StepResult(VerificationStatus.VERIFIED, 8))
        every { behaviourAnalysisClient.analyzeBehavior(any()) } returns Mono.just(StepResult(VerificationStatus.VERIFIED, 6))
        every { employmentVerificationClient.verifyEmployment(any()) } returns Mono.just(StepResult(VerificationStatus.VERIFIED, 8))

        val result: Mono<CreditCardApplicationProcessResponse> = evaluateAndProcessApplication(applicationId, input)

        StepVerifier.create(result)
            .expectNextMatches { response ->
                assertEquals(DispatchStatus.REJECTED, response.dispatchStatus)
                assertEquals(38, response.overallScore)
                response.applicationId == applicationId
            }
            .verifyComplete()
    }
}
