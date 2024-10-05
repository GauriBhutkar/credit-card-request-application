package com.bank.creditcardrequestprocessor.infra.http

import com.bank.creditcardrequestprocessor.client.BehaviourAnalysisClient
import com.bank.creditcardrequestprocessor.client.ComplianceCheckClient
import com.bank.creditcardrequestprocessor.client.EmploymentVerificationClient
import com.bank.creditcardrequestprocessor.client.IdentityVerificationClient
import com.bank.creditcardrequestprocessor.client.RiskEvaluationClient
import com.bank.creditcardrequestprocessor.domain.CreditCardApplicationConfig
import com.bank.creditcardrequestprocessor.infra.request.ProcessCreditCardInput
import com.bank.creditcardrequestprocessor.infra.response.ApplicationStatusResponse
import com.bank.creditcardrequestprocessor.infra.response.CreditCardApplicationProcessResponse
import com.bank.creditcardrequestprocessor.repository.CreditCardApplicationProcessRepository
import com.bank.creditcardrequestprocessor.usecases.EvaluateAndProcessApplication
import com.bank.creditcardrequestprocessor.usecases.GetApplicationStatus
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.UUID

@RestController
@RequestMapping("/api/v1/credit-card")
class CreditCardProcessorController(
    identityVerificationClient: IdentityVerificationClient,
    complianceCheckClient: ComplianceCheckClient,
    behaviourAnalysisClient: BehaviourAnalysisClient,
    employmentVerificationClient: EmploymentVerificationClient,
    riskEvaluationClient: RiskEvaluationClient,
    creditCardApplicationProcessRepository: CreditCardApplicationProcessRepository,
    creditCardApplicationConfig: CreditCardApplicationConfig,
) {
    private val evaluateAndProcessApplication =
        EvaluateAndProcessApplication(
            identityVerificationClient,
            complianceCheckClient,
            behaviourAnalysisClient,
            employmentVerificationClient,
            riskEvaluationClient,
            creditCardApplicationProcessRepository,
            creditCardApplicationConfig,
        )
    private val getApplicationStatus = GetApplicationStatus(creditCardApplicationProcessRepository)

    @PostMapping("/applications/{applicationId}/process")
    fun processCreditCard(
        @PathVariable applicationId: UUID,
        @RequestBody input: ProcessCreditCardInput,
    ): Mono<ResponseEntity<CreditCardApplicationProcessResponse>> {
        return evaluateAndProcessApplication(applicationId, input).map {
            ResponseEntity(it, HttpStatus.OK)
        }
    }

    @GetMapping("/applications/{applicationId}/status")
    fun getStatus(@PathVariable applicationId: UUID): Mono<ResponseEntity<ApplicationStatusResponse>> {
        return getApplicationStatus(applicationId).map {
            ResponseEntity(it, HttpStatus.OK)
        }
    }
}
