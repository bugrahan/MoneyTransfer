package com.revolut.bugrahan.impl;

import com.revolut.bugrahan.Main;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

public class UserApiTest extends JerseyTest {
    private HttpServer server;
    private WebTarget target;

    @Before
    @Override
    public void setUp() throws Exception {
        server = Main.startServer();
        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
    }

    @After
    @Override
    public void tearDown() throws Exception {
        server.shutdownNow();
    }


    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig(UserApiServiceImpl.class);
    }

    @Test
    public void testGetUser_exists() {
        Response response = target.path("user/1000").request(MediaType.APPLICATION_JSON).get();
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testGetUser_does_not_exist() {
        Response response = target.path("user/0000").request(MediaType.APPLICATION_JSON).get();
        assertEquals(404, response.getStatus());
    }

    @Test
    public void testCreateUser_already_exists() {
        Response response = target.path("user").request().post(Entity.json("{\"id\":1000,\"name\":\"Bugrahan Memis\",\"userType\":\"PREMIUM\"}"));
        assertEquals(404, response.getStatus());
    }

    @Test
    public void testCreateUser_does_not_exist() {
        Response response = target.path("user").request().post(Entity.json("{\"id\":9000,\"name\":\"David Beckham\",\"userType\":\"PREMIUM\"}"));
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testCreateUser_missing_info() {
        Response response = target.path("user").request().post(Entity.json("{\"id\":\"\",\"name\":\"Hugh Grant\",\"userType\":\"PREMIUM\"}"));
        assertEquals(404, response.getStatus());
    }

    //@Test
    public void testDeleteUser_exist() {
        Response response = target.path("user/1000").request(MediaType.APPLICATION_JSON).get();
        assertEquals(200, response.getStatus());
    }

    //@Test
    public void testDeleteUser_does_not_exist() {
        Response response = target.path("user/0000").request(MediaType.APPLICATION_JSON).get();
        assertEquals(404, response.getStatus());
    }
}