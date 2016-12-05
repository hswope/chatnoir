package com.hms3.chatnoir.server.security

import com.hms3.chatnoir.server.repository.UserRepository
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerRequestFilter
import javax.annotation.security.PermitAll
import javax.annotation.security.DenyAll
import java.util.StringTokenizer
import javax.annotation.security.RolesAllowed
import java.util.HashSet
import java.util.Arrays
import javax.ws.rs.container.ResourceInfo
import org.glassfish.jersey.internal.util.Base64
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response

@Component
open class BasicAuthFilter : ContainerRequestFilter {

    @Autowired
    private lateinit var userRepository : UserRepository

    @Context
    private lateinit var resourceInfo: ResourceInfo

    private val AUTHORIZATION_PROPERTY = "Authorization"
    private val AUTHENTICATION_SCHEME = "Basic"
    private val ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED)
            .entity("You cannot access this resource").build()
    private val ACCESS_FORBIDDEN = Response.status(Response.Status.FORBIDDEN)
            .entity("Access blocked for all users !!").build()

    override fun filter(requestContext: ContainerRequestContext) {
        val method = resourceInfo.resourceMethod

        //Access allowed for all
        if (!method.isAnnotationPresent(PermitAll::class.java)) {

            //Access denied for all
            if (method.isAnnotationPresent(DenyAll::class.java)) {
                requestContext.abortWith(ACCESS_FORBIDDEN)
                return
            }

            //Get request headers
            val headers = requestContext.headers

            //Fetch authorization header
            val authorization = headers[AUTHORIZATION_PROPERTY]

            //If no authorization information present; block access
            if (authorization == null || authorization.isEmpty()) {
                requestContext.abortWith(ACCESS_DENIED)
                return
            }

            //Get encoded username and password
            val encodedUserPassword = authorization[0].replaceFirst((AUTHENTICATION_SCHEME + " ").toRegex(), "")

            //Decode username and password
            val usernameAndPassword = String(Base64.decode(encodedUserPassword.toByteArray()))

            //Split username and password tokens
            val tokenizer = StringTokenizer(usernameAndPassword, ":")
            val username = tokenizer.nextToken()
            val password = tokenizer.nextToken()

            val user = userRepository.findByUsername(username)
            if (user != null && user.password == password){
                requestContext.securityContext = SecurityContext(user)
            }
            else {
                requestContext.abortWith(ACCESS_DENIED)
                return
            }

            //Verify user access
            if (method.isAnnotationPresent(RolesAllowed::class.java)) {
                val rolesAnnotation = method.getAnnotation(RolesAllowed::class.java)
                val rolesSet = HashSet(Arrays.asList<String>(*rolesAnnotation.value))

                //Is user valid?
                if (!isUserAllowed(username, password, rolesSet)) {
                    requestContext.abortWith(ACCESS_DENIED)
                    return
                }
            }
        }
    }

    private fun isUserAllowed(username : String, password : String, roleSet : Set<String>) : Boolean{
        return true
    }
}