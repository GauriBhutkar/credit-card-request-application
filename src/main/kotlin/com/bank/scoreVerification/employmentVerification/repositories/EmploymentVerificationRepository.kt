package com.bank.scoreVerification.employmentVerification.repositories

import com.bank.scoreVerification.employmentVerification.domain.EmploymentRecord
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface EmploymentVerificationRepository : ReactiveMongoRepository<EmploymentRecord, UUID>
