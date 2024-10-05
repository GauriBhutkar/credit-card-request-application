package com.bank.creditcardrequestprocessor.domain

import com.bank.creditcardrequest.domain.VerificationStatus
import com.bank.creditcardrequestprocessor.infra.response.StepResult
import java.time.LocalDateTime
import java.util.UUID

data class CreditCardApplicationProcess(
    val applicationId: UUID,
    val steps: List<CreditCardApplicationStep> = emptyList(),
    val overallScore: Int = 0,
    val dispatchStatus: DispatchStatus = DispatchStatus.PENDING,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
) {

    companion object {
        fun create(applicationId: UUID, config: CreditCardApplicationConfig): CreditCardApplicationProcess {
            val steps = config.steps.map { stepConfig ->
                CreditCardApplicationStep(
                    stepType = stepConfig.stepType,
                    isMandatory = stepConfig.isMandatory,
                    score = stepConfig.score ?: 0,
                    status = StepStatus.PENDING,
                )
            }

            return CreditCardApplicationProcess(
                applicationId = applicationId,
                steps = steps,
                overallScore = 0,
                dispatchStatus = DispatchStatus.PENDING,
            )
        }
    }

    fun updateStepResult(currentStep: ApplicationStep, stepResult: StepResult): CreditCardApplicationProcess {
        val updatedSteps = steps.map { step ->
            if (step.stepType == currentStep) {
                val newScore = stepResult.verificationScore
                val newStatus = when (stepResult.verificationStatus) {
                    VerificationStatus.VERIFIED -> StepStatus.COMPLETED
                    VerificationStatus.REJECTED -> StepStatus.FAILED
                    else -> StepStatus.PENDING
                }
                step.copy(score = newScore, status = newStatus)
            } else {
                step
            }
        }
        return this.copy(steps = updatedSteps)
    }

    fun updateOverAllScoreAndStatus(dispatchStatus: DispatchStatus): CreditCardApplicationProcess {
        return this.copy(
            overallScore = computeOverAllScore(),
            dispatchStatus = dispatchStatus,
        )
    }

    fun computeOverAllScore(): Int {
        return steps.fold(0) { acc, step -> step.score + acc }
    }
}

data class CreditCardApplicationStep(
    val stepType: ApplicationStep,
    val isMandatory: Boolean = false,
    val score: Int,
    val status: StepStatus,
)

enum class ApplicationStep {
    IDENTITY_VERIFICATION,
    EMPLOYMENT_VERIFICATION,
    RISK_EVALUATION,
    COMPLIANCE_CHECK,
    BEHAVIOR_ANALYSIS,
}

enum class StepStatus {
    PENDING,
    COMPLETED,
    FAILED,
}

enum class DispatchStatus {
    PENDING,
    STP,
    NEAR_STP,
    MANUAL_REVIEW,
    REJECTED,
}
