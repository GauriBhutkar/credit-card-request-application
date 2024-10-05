package com.bank.creditcardrequestprocessor.usecases

import com.bank.creditcardrequestprocessor.client.*
import com.bank.creditcardrequestprocessor.domain.ApplicationStep
import com.bank.creditcardrequestprocessor.domain.CreditCardApplicationProcess
import com.bank.creditcardrequestprocessor.domain.CreditCardApplicationStep
import com.bank.creditcardrequestprocessor.infra.request.ProcessCreditCardInput
import com.bank.scoreVerification.behaviouralanalysis.infra.request.BehaviourCheckInput
import com.bank.scoreVerification.compliancecheck.infra.request.ComplianceCheckInput
import com.bank.scoreVerification.employmentVerification.infra.request.EmploymentVerificationInput
import com.bank.scoreVerification.identityverification.infra.request.IdentityVerificationInput
import com.bank.scoreVerification.riskevaluation.infra.request.RiskEvaluationInput
import reactor.core.publisher.Mono

class StepExecutor(
    private val identityVerificationClient: IdentityVerificationClient,
    private val complianceCheckClient: ComplianceCheckClient,
    private val employmentVerificationClient: EmploymentVerificationClient,
    private val riskEvaluationClient: RiskEvaluationClient,
    private val behaviourAnalysisClient: BehaviourAnalysisClient,
) {
    fun executeStep(
        creditCardApplicationProcess: CreditCardApplicationProcess,
        step: CreditCardApplicationStep,
        input: ProcessCreditCardInput,
    ): Mono<CreditCardApplicationProcess> {
        return when (step.stepType) {
            ApplicationStep.IDENTITY_VERIFICATION ->
                identityVerificationClient.identityVerification(IdentityVerificationInput(input.customerId, input.customerName, input.mobileNumber))
                    .map { creditCardApplicationProcess.updateStepResult(ApplicationStep.IDENTITY_VERIFICATION, it) }

            ApplicationStep.COMPLIANCE_CHECK ->
                complianceCheckClient.checkCompliance(ComplianceCheckInput(input.customerId, input.customerName, input.mobileNumber, input.employmentDetails, input.creditCardLimit, input.bankStatement))
                    .map { creditCardApplicationProcess.updateStepResult(ApplicationStep.COMPLIANCE_CHECK, it) }

            ApplicationStep.EMPLOYMENT_VERIFICATION ->
                employmentVerificationClient.verifyEmployment(EmploymentVerificationInput(input.customerId, input.customerName, input.mobileNumber, input.employmentDetails))
                    .map { creditCardApplicationProcess.updateStepResult(ApplicationStep.EMPLOYMENT_VERIFICATION, it) }

            ApplicationStep.RISK_EVALUATION ->
                riskEvaluationClient.evaluateRisk(RiskEvaluationInput(input.customerId, input.customerName, input.nationality, input.employmentDetails, input.creditCardLimit, input.additionalInput))
                    .map { creditCardApplicationProcess.updateStepResult(ApplicationStep.RISK_EVALUATION, it) }

            ApplicationStep.BEHAVIOR_ANALYSIS ->
                behaviourAnalysisClient.analyzeBehavior(BehaviourCheckInput(input.customerId, input.bankStatement))
                    .map { creditCardApplicationProcess.updateStepResult(ApplicationStep.BEHAVIOR_ANALYSIS, it) }
        }
    }
}
