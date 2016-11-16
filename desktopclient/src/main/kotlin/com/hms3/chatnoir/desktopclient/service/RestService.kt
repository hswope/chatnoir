package com.hms3.chatnoir.desktopclient.service

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider
import com.hms3.chatnoir.desktopclient.utility.JavaFXExecutor
import org.glassfish.jersey.client.JerseyClientBuilder
import org.glassfish.jersey.client.JerseyWebTarget
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture
import java.util.function.BiFunction
import javax.ws.rs.client.Entity
import javax.ws.rs.core.MultivaluedHashMap


abstract class RestService {
    protected val log = LoggerFactory.getLogger(javaClass)

    protected lateinit var baseTarget : JerseyWebTarget
    protected val headers = MultivaluedHashMap<String,Any>()

    protected fun filterHeaders(keys : Array<String>) : MultivaluedHashMap<String,Any>{
        val filteredHeaders = MultivaluedHashMap<String,Any>()
        for (key in keys) {
            if (headers.containsKey(key)){
                filteredHeaders.put(key,headers.get(key))
            }
        }
        return filteredHeaders
    }

    protected fun setBaseUrl(baseUrl : String){
        baseTarget = JerseyClientBuilder.createClient().register(JacksonJsonProvider::class.java).target(baseUrl)
    }

    protected inline fun <reified T : Any> post(url : String, body : Any, headerNames : Array<String> = arrayOf()) : CompletableFuture<T>{
        val future : CompletableFuture<T> = CompletableFuture()

        CompletableFuture
            .supplyAsync({
                baseTarget.path(url).request().headers(filterHeaders(headerNames)).post(Entity.json(body), T::class.java)
            })
            .handleAsync(BiFunction{ t : T? , throwable : Throwable? ->
                if(t != null) {
                    log.info("Rest post ${url} succeeded")
                    future.complete(t)
                }
                else{
                    log.info("Rest post ${url} failed", throwable)
                    future.completeExceptionally(throwable)
                }}, JavaFXExecutor()
            )

        return future
    }


    protected inline fun <reified T : Any> get(url : String, headerNames : Array<String> = arrayOf()) : CompletableFuture<T>{
        val future : CompletableFuture<T> = CompletableFuture()

        CompletableFuture
                .supplyAsync({
                    baseTarget.path(url).request().headers(filterHeaders(headerNames)).get(T::class.java)
                })
                .handleAsync(BiFunction{ t : T? , throwable : Throwable? ->
                    if(t != null) {
                        log.info("Rest get ${url} succeeded")
                        future.complete(t)
                    }
                    else{
                        log.info("Rest get ${url} failed", throwable)
                        future.completeExceptionally(throwable)
                    }}, JavaFXExecutor()
                )

        return future
    }

}