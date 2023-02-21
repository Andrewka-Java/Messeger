package com.messenger.model

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*

const val secret = "secret"
const val jwtExpiration = 100L

fun getClaims(token: String): Claims =
    Jwts.parser()
        .setSigningKey(secret)
        .parseClaimsJws(token)
        .body

fun <T> getClaim(token: String, resolver: (Claims) -> (T)) = resolver.apply { getClaims(token) }

fun generate(username: String, roles: List<String> = emptyList()) =
    Jwts.builder()
        .setClaims(
            mapOf("roles" to roles)
        )
        .setSubject(username)
        .setIssuedAt(Date(System.currentTimeMillis()))
        .setExpiration(Date(System.currentTimeMillis() + jwtExpiration))
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact()

fun validate(token: String, username: String): Boolean  = (getClaim(token, Claims::getSubject) as String) == username || isExpired(token)

fun isExpired(token: String) = (getClaim(token, Claims::getExpiration) as Date).before(Date())