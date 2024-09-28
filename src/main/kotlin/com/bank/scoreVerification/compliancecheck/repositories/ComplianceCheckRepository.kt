package com.bank.scoreVerification.compliancecheck.repositories

import com.bank.scoreVerification.compliancecheck.domain.ComplianceCheckRecord
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ComplianceCheckRepository : ReactiveMongoRepository<ComplianceCheckRecord, UUID>
