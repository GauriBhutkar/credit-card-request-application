package com.bank.scoreVerification.identityverification.infra.request

import java.util.UUID

class IdentityVerificationInput(
    val customerId: UUID,
    val customerName: String,
    val mobileNumber: String,
)
