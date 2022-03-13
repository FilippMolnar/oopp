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
package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Activity;
import commons.Game;
import commons.Player;
import commons.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.io.IOException;
import java.security.Principal;
import java.util.*;


@RestController
@RequestMapping("/api/wait")
public class WaitController {

    private final SimpMessageSendingOperations simpMessagingTemplate;
    private final List<Player> lobbyPlayers = new ArrayList<>();
    private final RestTemplate restTemplate;

    private final GameController gameController;

    /**
     * A <b>player</b> is identified by a uniquely assigned <b>string</b> and not by the name because people in different games might have the same name.
     * Thus we need associate a random string to each new web socket connection and talk with that connection
     */
    //private final Map<String, Pair<Integer, Player>> playerToGameId = new HashMap<>(); // map the player's string id to the game id
    //private final Map<Integer, List<String>> IDToPlayers = new HashMap<>(); // given an id get all the players with that id
    private final Logger LOGGER = LoggerFactory.getLogger(WaitController.class);
    private int gameID = 0;


    WaitController(SimpMessageSendingOperations simpMessagingTemplate,GameController gameController) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.restTemplate = new RestTemplate();
        this.gameController = gameController;
    }

    public List<Player> getLobbyPlayers() {
        return lobbyPlayers;
    }

    /**
     * Adds the player to the current player list in the lobby and then pushes it to the socket channel
     *
     * @param player Player that is sent in the request
     */
    @PostMapping(path = {"", "/"})
    public void addName(@RequestBody Player player) {
        lobbyPlayers.add(player);
        simpMessagingTemplate.convertAndSend("/topic/waitingRoom", player);
        LOGGER.info("Players in waiting room are\n" + lobbyPlayers);
    }

    private List<Integer> getRandomQuestionTypes(){
        // 0 -> equal energy
        // 1 -> highest energy
        // 2 -> estimate answer
        final int nrEqual = 4;
        final int nrEstimate = 3;
        final int nrHighest = 13;
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < nrEqual; i++)
            list.add(0);
        for(int i = 0; i < nrHighest; i++)
            list.add(1);
        for(int i = 0; i < nrEstimate; i++)
            list.add(2);
        Collections.shuffle(list);
        return list;
    }

    /**
     * Post mapping from a player to <b>Start</b> the game
     * Sends a message to start the game to all players
     */
    @PostMapping(path = {"", "/start"})
    public void startGame() {
        LOGGER.info("Starting game with id " + gameID);

        gameController.addNewGame(gameID,lobbyPlayers);

        Game current = gameController.getGame(gameID);
        lobbyPlayers.clear();
        //var playerList = IDToPlayers.get(gameID);
        var playerList = current.getPlayers();
        if (playerList == null) {
            LOGGER.error("There are no players in the waiting room, but POST is called!");
            return;
        }
        var question = QuestionController.getRandomQuestion();
        var questionTypeList = getRandomQuestionTypes();
        for (Player player : playerList) {
            String playerID = player.getSocketID();
            LOGGER.info("Sending question " + question.getChoices());
            simpMessagingTemplate.convertAndSendToUser(playerID, "queue/renderQuestion", question);
            simpMessagingTemplate.convertAndSendToUser(playerID, "queue/startGame/gameID",gameID);
            simpMessagingTemplate.convertAndSendToUser(playerID, "queue/startGame/questionTypes", questionTypeList);
            LOGGER.info("Sent message to start game to " + player.getName());
        }
        gameID++;
    }

    public void addPlayerToGameID(String playerID, Player player) {

        player.socketID = playerID;
        gameController.addPlayerToGame(gameID,player);
        /*playerToGameId.put(playerID, Pair.of(gameID, player));
        var currentList = IDToPlayers.getOrDefault(gameID, new ArrayList<>());
        if (currentList.size() == 0)
            IDToPlayers.put(gameID, currentList);
        currentList.add(playerID); // for this game ID we have a new player so we add it there
        System.out.println(IDToPlayers);*/
    }

    @MessageMapping("/enterRoom")
    public void socketAddName(@Payload Player player, Principal principal) {
        LOGGER.info("add player with name " + player.name + " to the waiting room with sockets. The player's id is " + principal.getName());
        addName(player);
        addPlayerToGameID(principal.getName(), player);
    }

    @GetMapping(path = {"", "/"})
    public List<Player> getPlayersAlreadyWaiting() {
        return lobbyPlayers;
    }

    /**
     * get a random activity by calling the api through the server
     *
     * @return a random Activity
     */
    public Activity getRandomActivity() {
        String json = restTemplate.getForObject("http://localhost:8080/data/rand", String.class);
        Activity res;

        ObjectMapper mapper = new ObjectMapper();
        try {
            res = mapper.readValue(json, Activity.class);
        } catch (IOException e) {
            System.out.println(e);
            LOGGER.error("IO Exception at get reading act!");
            return null;
        }
        return res;
    }

    /**
     * Fetches the following question for the current game
     * @param gameID identifier for the current game
     * @return the following question
     */
    @GetMapping(path = "api/question/{gameID}")
    public Question getQuestion(@PathVariable("gameID") int gameID)
    {
        Game cur = gameController.getGame(gameID);
        Question q = cur.getQuestion();
        return q;
    }

    @EventListener
    private void handleSessionConnected(SessionConnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        LOGGER.info("New socket connection! " + headers.getUser().getName());
    }


    @MessageMapping("/disconnect")
    public void playerDisconnect(Player player) {
        if (lobbyPlayers.remove(player)) {
            System.out.println("Player " + player.name + " disconnected!");
            simpMessagingTemplate.convertAndSend("/topic/disconnect", player);
        }
    }
}
