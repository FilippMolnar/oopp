/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import commons.Player;
import commons.Quote;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {

    // use this variable to define the server address and port to connect to
    private static final String SERVER = "http://localhost:8080";
    private static final String WEBSOCKET_SERVER = "ws://localhost:8080/websocket";
    private final StompSession session = connect(WEBSOCKET_SERVER);

    public void getQuotesTheHardWay() throws IOException {
        var url = new URL(SERVER + "api/quotes");
        var is = url.openConnection().getInputStream();
        var br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    private StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connect(url, new StompSessionHandlerAdapter() {
            }).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            System.out.println(e);
            System.out.println(Arrays.toString(e.getStackTrace()));
            throw new RuntimeException();
        }
        throw new IllegalArgumentException("Something went bad here!");
    }

    /**
     * Utility function to be used from the client to register events when there is a message on a channel. <br\>
     * The client <code>StompSession</code>(A subprotocol of websockets) listens to messages coming and calls
     * the consumer function
     * @param dest the channel name where the client wants to listen to
     * @param classType the class type of the expected object response. eg: <code>Player</code> maybe in the future
     *                   <code> Emoji</code>
     * @param consumer the callback to execute when a message is received
     */
    public <T> void registerForMessages(String dest,Class<T> classType, Consumer<T> consumer) {
        System.out.println("Registered to listen on the track " + dest);
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return classType;
            }

            @Override
            @SuppressWarnings("unchecked")
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("Consumer called!");
                consumer.accept((T) payload);
            }
        });
    }

    /**
     * Method to be used in the future to send data from the client to the server through websockets
     * @param destination url provided in a socket controller with <code>@MessageMapping</code>
     * @param obj object to send over the socket protocol
     */
    public void send(String destination, Object obj) {
        System.out.println("Sending object " + obj + "to " + destination);
        session.send(destination, obj);
    }

    /**
     * Does a get request on the mapping <code>api/wait</code> receiving a list of players that are
     * already in the waiting room. <br\>
     * It is used by the <code>WaitingRoomCtrl</code> class to initialize the
     * UI based on the list it receives.
     * @return List of players that are currently in the waiting room
     */
    public List<Player> getAllNamesInWaitingRoom() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/wait")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    /**
     * Does a post request to the api endpoint <code>api/wait</code> sending a Player object
     * @param name the name of the player
     */
    public void postName(String name) {
        ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/wait")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(new Player(name), APPLICATION_JSON), Player.class);
    }

    public List<Quote> getQuotes() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Quote>>() {
                });
    }

    public Quote addQuote(Quote quote) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(quote, APPLICATION_JSON), Quote.class);
    }

    public String addName(String name) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/wait") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(name, APPLICATION_JSON), String.class);
    }
}
