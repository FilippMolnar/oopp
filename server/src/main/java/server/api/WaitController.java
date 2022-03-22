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
import org.springframework.web.socket.messaging.SessionConnectEvent;
import server.Utils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/api/wait")
public class WaitController {

    private final SimpMessageSendingOperations simpMessagingTemplate;
    private final List<Player> lobbyPlayers = new ArrayList<>();
    private final Utils utils;

    private final Logger LOGGER = LoggerFactory.getLogger(WaitController.class);
    private final GameController gameController;
    private final QuestionController questionController;
    private int gameID = 0;


    WaitController(SimpMessageSendingOperations simpMessagingTemplate, GameController gameController, QuestionController questionController) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.utils = new Utils(simpMessagingTemplate);
        this.gameController = gameController;
        this.questionController = questionController;
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

    private List<Question> getRandomQuestionTypes() {
        // 0 -> equal energy
        // 1 -> highest energy
        // 2 -> estimate answer
        final int nrEqual = 4;
        final int nrEstimate = 3;
        final int nrHighest = 13;
        List<Question> list = new ArrayList<>();
        for (int i = 0; i < nrEqual; i++)
            list.add(questionController.getTypeEqual());
        for (int i = 0; i < nrHighest; i++)
            list.add(questionController.getTypeMostLeast());
        for (int i = 0; i < nrEstimate; i++)
            list.add(questionController.getTypeEstimate());
        Collections.shuffle(list);
        return list;
    }

    @GetMapping("/getMostLeastQuestions")
    public static List<Question> get20RandomMostLeastQuestions() {
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < 20; i++)
            questions.add(questionController.getTypeMostLeast());
        return questions;
    }


    /**
     * Post mapping from a player to <b>Start</b> the game
     * Sends a message to start the game to all players
     */
    @PostMapping(path = {"", "/start"})
    public void startGame() {
        LOGGER.info("Starting game with id " + gameID);

        Game currentGame = gameController.getGame(gameID);
        lobbyPlayers.clear();
        //var playerList = IDToPlayers.get(gameID);
        var playerList = currentGame.getPlayers();
        if (playerList == null) {
            LOGGER.error("There are no players in the waiting room, but POST is called!");
            return;
        }
        var questionList = get20RandomMostLeastQuestions();
        currentGame.setQuestions(questionList);

        utils.sendToAllPlayers(playerList, "queue/startGame/gameID", gameID);

        gameID++;
    }

    public void addPlayerToGameID(String playerID, Player player) {
        player.setSocketID(playerID);
        gameController.addPlayerToGame(gameID, player);
    }

    @MessageMapping("/enterRoom")
    public void socketAddName(@Payload Player player, Principal principal) {
        LOGGER.info("add player with name " + player.getName() + " to the waiting room with sockets. The player's id is " + principal.getName());
        addName(player);
        addPlayerToGameID(principal.getName(), player);
    }

    @GetMapping(path = {"", "/"})
    public List<Player> getPlayersAlreadyWaiting() {
        return lobbyPlayers;
    }


    @EventListener
    private void handleSessionConnected(SessionConnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        LOGGER.info("New socket connection! " + headers.getUser().getName());
    }


    @MessageMapping("/disconnect")
    public void playerDisconnect(Player player) {
        if (lobbyPlayers.remove(player)) {
            System.out.println("Player " + player.getName() + " disconnected!");
            simpMessagingTemplate.convertAndSend("/topic/disconnect", player);
        }
    }
}
