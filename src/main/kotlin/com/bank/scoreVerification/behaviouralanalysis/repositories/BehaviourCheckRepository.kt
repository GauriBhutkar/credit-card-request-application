package com.bank.scoreVerification.behaviouralanalysis.repositories

import com.bank.scoreVerification.behaviouralanalysis.domain.BehaviourCheckRecord
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BehaviourCheckRepository : ReactiveMongoRepository<BehaviourCheckRecord, UUID>
