package com.revolut.bugrahan.impl;

import com.revolut.bugrahan.Main;
import com.revolut.bugrahan.api.UserApiService;
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

    @Test
    public void isBalanceGreaterThanZero_yes() {
        UserApiService service = new UserApiServiceImpl();
        assertTrue(service.isBalanceGreaterThanZero(1000L));
    }

    @Test
    public void isBalanceGreaterThanZero_no() {
        UserApiService service = new UserApiServiceImpl();
        assertFalse(service.isBalanceGreaterThanZero(1003L));
    }

    @Test
    public void deleteUser_exist_withoutBalance() {
        Response response = target.path("user/1003").request(MediaType.APPLICATION_JSON).delete();
        assertEquals(200, response.getStatus());
    }

    @Test
    public void deleteUser_exist_withBalance() {
        Response response = target.path("user/1000").request(MediaType.APPLICATION_JSON).delete();
        assertEquals(404, response.getStatus());
    }

    @Test
    public void deleteUser_notExist() {
        Response response = target.path("user/0000").request(MediaType.APPLICATION_JSON).delete();
        assertEquals(404, response.getStatus());
    }

    @Test
    public void deleteUser_withNoAccount() {
        target.path("user").request().post(Entity.json("{\"id\":1500,\"name\":\"Julia Roberts\",\"userType\":\"PREMIUM\"}"));
        Response response = target.path("user/1500").request(MediaType.APPLICATION_JSON_TYPE).delete();
        assertEquals(200, response.getStatus());
    }

    @Test
    public void updateUser_name() {
        Response response = target.path("user/1000").request().put(Entity.json("{\"name\":\"Julia Roberts\"}"));
        assertEquals(200, response.getStatus());
    }

    @Test
    public void updateUser_withId() {
        Response response = target.path("user/1000").request().put(Entity.json("{\"id\":1000,\"name\":\"Dr. Watson\"}"));
        assertEquals(200, response.getStatus());
    }

    @Test
    public void updateUser_withWrongId() {
        Response response = target.path("user/1000").request().put(Entity.json("{\"id\":1001,\"name\":\"Julia Roberts\"}"));
        assertEquals(404, response.getStatus());
    }

}