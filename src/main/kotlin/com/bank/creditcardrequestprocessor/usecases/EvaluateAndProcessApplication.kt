package com.bank.creditcardrequestprocessor.usecases

import com.bank.creditcardrequestprocessor.client.BehaviourAnalysisClient
import com.bank.creditcardrequestprocessor.client.ComplianceCheckClient
import com.bank.creditcardrequestprocessor.client.EmploymentVerificationClient
import com.bank.creditcardrequestprocessor.client.IdentityVerificationClient
import com.bank.creditcardrequestprocessor.client.RiskEvaluationClient
import com.bank.creditcardrequestprocessor.domain.CreditCardApplicationConfig
import com.bank.creditcardrequestprocessor.domain.CreditCardApplicationProcess
import com.bank.creditcardrequestprocessor.domain.CreditCardApplicationStep
import com.bank.creditcardrequestprocessor.domain.DispatchStatus
import com.bank.creditcardrequestprocessor.domain.StepStatus
import com.bank.creditcardrequestprocessor.infra.request.ProcessCreditCardInput
import com.bank.creditcardrequestprocessor.infra.response.CreditCardApplicationProcessResponse
import com.bank.creditcardrequestprocessor.repository.CreditCardApplicationProcessRepository
import reactor.core.publisher.Mono
import java.util.UUID

class EvaluateAndProcessApplication(
    identityVerificationClient: IdentityVerificationClient,
    complianceCheckClient: ComplianceCheckClient,
    behaviourAnalysisClient: BehaviourAnalysisClient,
    employmentVerificationClient: EmploymentVerificationClient,
    riskEvaluationClient: RiskEvaluationClient,
    private val creditCardApplicationProcessRepository: CreditCardApplicationProcessRepository,
    private val config: CreditCardApplicationConfig,
) {
    private val stepExecutor = StepExecutor(
        identityVerificationClient,
        complianceCheckClient,
        employmentVerificationClient,
        riskEvaluationClient,
        behaviourAnalysisClient,
    )

    operator fun invoke(
        applicationId: UUID,
        input: ProcessCreditCardInput,
    ): Mono<CreditCardApplicationProcessResponse> {
        val creditCardApplicationProcess = CreditCardApplicationProcess.create(applicationId, config)

        return executeMandatorySteps(creditCardApplicationProcess, input)
            .flatMap { process ->
                if (process.steps.any { it.status == StepStatus.FAILED }) {
                    concludeProcess(process, DispatchStatus.REJECTED)
                } else {
                    executeOptionalSteps(process, input)
                }
            }
    }

    private fun executeMandatorySteps(
        creditCardApplicationProcess: CreditCardApplicationProcess,
        input: ProcessCreditCardInput,
    ): Mono<CreditCardApplicationProcess> {
        val mandatorySteps = creditCardApplicationProcess.steps.filter { it.isMandatory }
        return executeSteps(creditCardApplicationProcess, input, mandatorySteps)
    }

    private fun executeOptionalSteps(
        creditCardApplicationProcess: CreditCardApplicationProcess,
        input: ProcessCreditCardInput,
    ): Mono<CreditCardApplicationProcessResponse> {
        val optionalSteps = creditCardApplicationProcess.steps.filterNot { it.isMandatory }
        return executeSteps(creditCardApplicationProcess, input, optionalSteps)
            .flatMap { updatedProcess ->
                concludeProcess(updatedProcess, determineDispatchStatus(updatedProcess))
            }
    }

    private fun determineDispatchStatus(process: CreditCardApplicationProcess): DispatchStatus {
        val overallScore = process.computeOverAllScore()
        return when {
            overallScore > 90 -> DispatchStatus.STP
            overallScore in 75..90 -> DispatchStatus.NEAR_STP
            overallScore in 50..75 -> DispatchStatus.MANUAL_REVIEW
            else -> DispatchStatus.REJECTED
        }
    }

    private fun concludeProcess(finalProcess: CreditCardApplicationProcess, dispatchStatus: DispatchStatus): Mono<CreditCardApplicationProcessResponse> {
        return creditCardApplicationProcessRepository.save(finalProcess.updateOverAllScoreAndStatus(dispatchStatus))
            .map { savedProcess ->
                CreditCardApplicationProcessResponse(
                    savedProcess.applicationId,
                    savedProcess.dispatchStatus,
                    savedProcess.overallScore,
                )
            }
    }

    private fun executeSteps(
        process: CreditCardApplicationProcess,
        input: ProcessCreditCardInput,
        steps: List<CreditCardApplicationStep>,
    ): Mono<CreditCardApplicationProcess> {
        return steps.fold(Mono.just(process)) { acc, step ->
            acc.flatMap { updatedProcess ->
                stepExecutor.executeStep(updatedProcess, step, input)
            }
        }
    }
}
