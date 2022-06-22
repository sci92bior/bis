package com.htt.bis.config

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import java.util.function.Function
import java.util.stream.Collectors


class KeycloakRealmRoleConverter : Converter<Jwt, Collection<GrantedAuthority>> {

    val REALM_ACCESS = "realm_access"
    val ROLES = "roles"
    override fun convert(jwt: Jwt): Collection<GrantedAuthority> {
        val realmAccess = jwt.claims[REALM_ACCESS] as Map<*, *>?
        return (realmAccess!![ROLES] as List<String>?)!!.stream()
            .map(Function<String, String> { roleName: String -> roleName })
            .map { role: String? -> SimpleGrantedAuthority(role) }
            .collect(Collectors.toList())
    }

}