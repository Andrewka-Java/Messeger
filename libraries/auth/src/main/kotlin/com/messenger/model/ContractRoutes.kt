package com.messenger.model

import org.http4k.contract.ContractRoute

interface ContractRoutes {
    fun contractRoutes(): List<ContractRoute>
}
