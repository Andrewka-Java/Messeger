package com.messenger

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.then

fun Filter.then(handlers: List<HttpHandler>): HttpHandler = handlers.reduce { _, next -> then(next) }
