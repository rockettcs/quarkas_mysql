package com.integra;

import org.apache.camel.ProducerTemplate;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

@Path("/cache")
public class CacheResource {

    @Inject
    ProducerTemplate producerTemplate;

    @POST
    @Path("/set")
    public Response set(@QueryParam("key") String key, @QueryParam("value") String value) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("key",key);
        headers.put("value", value);
        producerTemplate.sendBodyAndHeaders("direct:set", null, headers);
        return Response.ok("Value set in Redis and MySQL").build();
    }

    @GET
    @Path("/get")
    public Response get(@QueryParam("key") String key) {
        String value = producerTemplate.requestBodyAndHeader("direct:get", null, "key", key, String.class);
        if (value != null) {
            return Response.ok("Value: " + value).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Key not found").build();
        }
    }
}