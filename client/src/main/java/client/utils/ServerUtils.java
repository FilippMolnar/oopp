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

import commons.Game;
import commons.Player;
import commons.Question;
import commons.Score;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import javafx.application.Platform;
import org.apache.commons.lang3.tuple.Pair;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {

    // use this variable to define the server address and port to connect to
    private String SERVER;
    private final List<List<Object>> subscribeParameters = new ArrayList<>();
    private StompSession session;
    private String WEBSOCKET_SERVER;

    public void getQuotesTheHardWay() throws IOException {
        var url = new URL(SERVER + "api/quotes");
        var is = url.openConnection().getInputStream();
        var br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    public void initializeServer(String server) {
        // 172.435q3...
        SERVER = "http://" + server + ":8080";
        WEBSOCKET_SERVER = "ws://" + server + ":8080/websocket";
        System.out.println("Trying to connect on another thread");
        session = connect(WEBSOCKET_SERVER);
        for (List<Object> l : subscribeParameters) {
            System.out.println();
            subscribeForSocketMessages((String) l.get(0), (Class<Object>) l.get(1), (Consumer<Object>) l.get(2));
        }
    }

    /**
     * Connects the websockets to a url specifed in <code>WebSocketConfig</code> class on the server side
     *
     * @param url url to connect to
     * @return a new StompSession
     */
    private StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        System.out.println("calling connect");
        try {
            return stomp.connect(url, new StompSessionHandlerAdapter() {
            }).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            throw new RuntimeException();
        }
        throw new IllegalArgumentException("Something went bad here!");
    }

    /**
     * Utility function to be used from the client to register events when there is a message on a channel. <br\>
     * The client <code>StompSession</code>(A subprotocol of websockets) listens to messages coming and calls
     * the consumer function
     *
     * @param dest      the channel name where the client wants to listen to
     * @param classType the class type of the expected object response. eg: <code>Player</code> maybe in the future
     *                  <code> Emoji</code>
     * @param consumer  the callback to execute when a message is received
     */
    public <T> void subscribeForSocketMessages(String dest, Class<T> classType, Consumer<T> consumer) {
        if (session == null) {
            subscribeParameters.add(List.of(dest, classType, consumer));
            return;
        }
        System.out.println("Registered to listen on the track " + dest);
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            @Nonnull
            public Type getPayloadType(@Nonnull StompHeaders headers) {
                return classType;
            }

            @Override
            @SuppressWarnings("unchecked")
            public void handleFrame(@Nonnull StompHeaders headers, Object payload) {
                Platform.runLater(() -> {
                    System.out.println("Consumer called! for track" + dest);
                    consumer.accept((T) payload);
                });
            }
        });
    }

    /**
     * Method to be used in the future to send data from the client to the server through websockets
     *
     * @param destination url provided in a socket controller with <code>@MessageMapping</code>
     * @param obj         object to send over the socket protocol
     */
    public void sendThroughSocket(String destination, Object obj) {
        System.out.println("Sending object " + obj + "to " + destination);
        session.send(destination, obj);
    }

    /**
     * Does a get request on the mapping <code>api/wait</code> receiving a list of players that are
     * already in the waiting room. <br\>
     * It is used by the <code>WaitingRoomCtrl</code> class to initialize the
     * UI based on the list it receives.
     *
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

    public Question getRandomQuestion() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/wait/question")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }


    public void postStartGame() {
        ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/wait/start")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(null);
    }

    /**
     * Does a post request to the api endpoint <code>api/wait</code> sending a Player object
     *
     * @param name the name of the player
     */
    public void postName(String name) {
        ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/wait")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(new Player(name), APPLICATION_JSON), Player.class);
    }

    /**
     * gets 20 question objects from the server
     * This method should be used to store the questions on the client
     * side
     *
     * @param gameID id of the game
     * @return a list of 20 question retrieved from the server
     */
    public List<Question> getAllGameQuestions(int gameID) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/game/getQuestions/" + gameID) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    public Pair postGameScore(int gameID, Pair<Player, Integer> result) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/game/score")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(result, APPLICATION_JSON), Pair.class);
    }

    public Map<Player, Integer> getScoreboard(int gameID) {
        var q = ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/leaderboard/" + gameID)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Response.class);
        Map<Player, Integer> scoreboard = q.readEntity(Map.class);
        return scoreboard;
    }

    public Game getGameMapping(int gameID) {
        var q = ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/game/getGame/" + gameID)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Response.class);
        return q.readEntity(Game.class);
    }
    // "/game/leaderboard/{gameID}"
    public Map<Integer, List<String>> getLeaderboard(int gameID) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/game/leaderboard/" + gameID)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<Map<Integer, List<String>>>() {
                });
    }

    /**
     * This method is used by single players, who do not have a game ID
     * and just need to get 20 questions at the start of the game.
     *
     * @return 20 random questions
     */
    public ArrayList<Question> getLeastMostQuestions() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/wait/getMostLeastQuestions") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    public List<Score> getSingleLeaderboard() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/score") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Score>>() {
                });
    }

    public Score addScore(Score score) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/score") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(score, APPLICATION_JSON), Score.class);
    }

}
