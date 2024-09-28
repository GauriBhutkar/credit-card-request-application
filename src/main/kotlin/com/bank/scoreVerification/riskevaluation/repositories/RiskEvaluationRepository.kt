package com.bank.scoreVerification.riskevaluation.repositories

import com.bank.scoreVerification.riskevaluation.domain.RiskEvaluationRecord
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RiskEvaluationRepository : ReactiveMongoRepository<RiskEvaluationRecord, UUID>
