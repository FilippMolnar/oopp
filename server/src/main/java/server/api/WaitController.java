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
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import server.Utils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


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
        List<String> playerNames = lobbyPlayers.stream().map(Player::getName).toList();
        LOGGER.info("Players in waiting room are:" + playerNames);
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
    public List<Question> get20RandomMostLeastQuestions() {
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

        Game currentGame = gameController.getGame(gameID);
        LOGGER.info("Starting game with id " + gameID + " with " + currentGame.getPlayers().size() + " players");

        lobbyPlayers.clear();
        var playerList = currentGame.getPlayers();
        if (playerList == null) {
            LOGGER.error("There are no players in the waiting room, but POST is called!");
            return;
        }
//        var questionList = get20RandomMostLeastQuestions();
        var questionList = questionController.get20RandomQuestions();
        currentGame.setQuestions(questionList);
        utils.sendToAllPlayers(playerList, "queue/startGame/gameID", gameID);
        gameID++;
    }

    public void addPlayerToGameID(String socketID, Player player) {
        player.setSocketID(socketID);
        gameController.addPlayerToGame(gameID, player);
    }

    @MessageMapping("/enterRoom")
    public void socketAddName(@Payload Player player, Principal principal) {
        LOGGER.info("add player with name " + player.getName() + " to the waiting room(gameID = "
                + gameID + " with sockets. The player's id is " + principal.getName());
        addName(player);
        addPlayerToGameID(principal.getName(), player);
        simpMessagingTemplate.convertAndSendToUser(principal.getName(), "queue/socket", "ana");
    }

    @GetMapping(path = {"", "/"})
    public List<Player> getPlayersAlreadyWaiting() {
        return lobbyPlayers;
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        if (headers.getUser() == null) {
            LOGGER.error("The socket disconnect event did not have a socket id configured.This should probably not happen!");
            return;
        }
        String socketID = headers.getUser().getName();
        Game game = gameController.getGameFromSocket(socketID);
        if (game == null) {
            LOGGER.warn("Socket message: Game is null meaning that the player is not in the game right now");
            return;
        }
        var optionalPlayer = game.getPlayers().stream().filter(p -> p.getSocketID().equals(socketID)).findFirst();
        if (optionalPlayer.isPresent()) {
            Player player = optionalPlayer.get();
            game.removePlayer(player);
            if (lobbyPlayers.remove(player)) {
                LOGGER.info("Socket message: Player " + player.getName() + " disconnected because he closed the socket!");
                simpMessagingTemplate.convertAndSend("/topic/disconnect", player);
            } else {
                LOGGER.info("Socket message: Player " + player.getName() + " disconnected from the game");
            }
        } else {
            LOGGER.error("There is no player with this socket ID in the game" + game.getGameID());
        }
    }

    @MessageMapping("/disconnectFromGame")
    public void disconnectFromGame(List<Object> pair) {
        Map map = (Map) pair.get(0);
        LOGGER.info("Receiving : " + pair);
        Player player = new Player((String) map.get("name"), (String) map.get("socketID"));
        int goodGameId = (Integer) pair.get(1);
        Game game = gameController.getGame(goodGameId);
        if (game == null) {
            LOGGER.error("Remove exit button for " + player.getName() + " failed because game is null");
        } else {
            game.removePlayer(player);
            LOGGER.info("Remove exit button: " + player.getName() + " from the game with id " + game.getGameID());
        }
    }


    @MessageMapping("/disconnect")
    public void playerDisconnectWaitingRoom(Player player) {
        Game game = gameController.getGame(gameID);
        if (lobbyPlayers.remove(player)) {
            game.removePlayer(player);
            LOGGER.info("Manual remove waiting room: " + player.getName() + " succeeded!");
            simpMessagingTemplate.convertAndSend("/topic/disconnect", player);
        } else {
            List<String> playerNames = lobbyPlayers.stream().map(Player::getName).toList();
            LOGGER.error("Manual remove waiting room for " + player.getName() + " fail, he is not in the lobby players: " + playerNames);
        }
    }

    @MessageMapping("/decrease_time")
    public void decreaseTime(Player player) {
        int gid = (int) player.getGameID();
        Game currentGame = gameController.getGame(gid);
        var playerList = currentGame.getPlayers();
        sendToAllOtherUsers(playerList,"queue/decrease_time/gameID", gid, player);

    }

    @MessageMapping("/cover_hands")
    public void coverHands(Player player) {
        int gid = (int) player.getGameID();
        Game currentGame = gameController.getGame(gid);
        var playerList = currentGame.getPlayers();
        sendToAllOtherUsers(playerList,"queue/cover_hands/gameID", gid, player);

    }

    @MessageMapping("/cover_ink")
    public void coverInk(Player player) {
        int gid = (int) player.getGameID();
        Game currentGame = gameController.getGame(gid);
        var playerList = currentGame.getPlayers();
        sendToAllOtherUsers(playerList,"queue/cover_ink/gameID", gid, player);
    }

    public void sendToAllOtherUsers(Set<Player> playerList, String destination, int gID, Player player){
        if(playerList == null) return;
        for (Player p : playerList) {
            String playerID = p.getSocketID();
            if(player.getName().equals(p.getName())) continue;
            simpMessagingTemplate.convertAndSendToUser(playerID, destination, gID);
        }
    }
}
