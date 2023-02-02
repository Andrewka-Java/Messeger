package com.messenger.route

import org.http4k.contract.ContractRoute

interface ContractRoutes {
    fun contractRoutes(): List<ContractRoute>
}
