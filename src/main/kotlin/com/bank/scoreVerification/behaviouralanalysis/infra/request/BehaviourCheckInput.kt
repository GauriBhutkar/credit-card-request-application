package com.bank.scoreVerification.behaviouralanalysis.infra.request

import java.io.File
import java.util.UUID

class BehaviourCheckInput(
    val customerId: UUID,
    val bankStatement: File,
)
