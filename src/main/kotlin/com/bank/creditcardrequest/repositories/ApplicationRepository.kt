package com.bank.creditcardrequest.repositories

import com.bank.creditcardrequest.domain.CreditCardApplication
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ApplicationRepository : ReactiveMongoRepository<CreditCardApplication, UUID>
