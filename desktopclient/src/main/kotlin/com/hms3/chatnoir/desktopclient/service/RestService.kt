package com.hms3.chatnoir.desktopclient.service

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider
import com.hms3.chatnoir.desktopclient.utility.JavaFXExecutor
import org.glassfish.jersey.client.JerseyClientBuilder
import org.glassfish.jersey.client.JerseyWebTarget
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture
import java.util.function.BiFunction
import javax.ws.rs.client.Entity
import javax.ws.rs.core.GenericType
import javax.ws.rs.core.MultivaluedHashMap

abstract class RestService {
    protected val log: Logger = LoggerFactory.getLogger(javaClass)
    protected lateinit var baseTarget : JerseyWebTarget
    protected val headers = MultivaluedHashMap<String,Any>()

    protected fun Array<String>.filterHeaders(): MultivaluedHashMap<String,Any>{
        val filteredHeaders = MultivaluedHashMap<String,Any>()
        filter { headers.containsKey(it) }
                .forEach { filteredHeaders.put(it, headers[it]) }
        return filteredHeaders
    }

    protected fun setBaseUrl(baseUrl : String){
        baseTarget = JerseyClientBuilder.createClient().register(JacksonJsonProvider::class.java).target(baseUrl)
    }

    protected fun <T : Any> post(url : String, genericType: GenericType<T>, body : Any, headerNames : Array<String> = arrayOf()) : CompletableFuture<T>{
        val future : CompletableFuture<T> = CompletableFuture()

        CompletableFuture
                .supplyAsync({
                    baseTarget.path(url).request().headers(headerNames.filterHeaders()).post(Entity.json(body), genericType)
                })
                .handleAsync(BiFunction{ t : T? , throwable : Throwable? ->
                    if(throwable == null) {
                        log.info("Rest @POST $url succeeded")
                        future.complete(t)
                    }
                    else{
                        log.info("Rest @POST $url failed", throwable)
                        future.completeExceptionally(throwable)
                    }}, JavaFXExecutor()
                )

        return future
    }

    protected fun <T : Any> get(url : String, genericType: GenericType<T>, headerNames : Array<String> = arrayOf()) : CompletableFuture<T>{
        val future : CompletableFuture<T> = CompletableFuture()

        CompletableFuture
                .supplyAsync({
                    baseTarget.path(url).request().headers(headerNames.filterHeaders()).get(genericType)
                })
                .handleAsync(BiFunction{ t : T? , throwable : Throwable? ->
                    if(throwable == null) {
                        log.info("Rest @GET $url succeeded")
                        future.complete(t)
                    }
                    else{
                        log.info("Rest @GET $url failed", throwable)
                        future.completeExceptionally(throwable)
                    }}, JavaFXExecutor()
                )

        return future
    }
}