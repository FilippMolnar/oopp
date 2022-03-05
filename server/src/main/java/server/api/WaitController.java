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

import commons.Activity;
import commons.Player;
import commons.Question;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@RestController
@RequestMapping("/api/wait")
public class WaitController {

    private final SimpMessageSendingOperations simpMessagingTemplate;
    private final List<Player> lobbyPlayers = new ArrayList<>();
    private final RestTemplate restTemplate;

    WaitController(SimpMessageSendingOperations simpMessagingTemplate){
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.restTemplate = new RestTemplate();
    }

    public List<Player> getLobbyPlayers() {
        return lobbyPlayers;
    }

    /**
     * Adds the player to the current player list in the lobby and then pushes it to the socket channel
     * @param player Player that is sent in the request
     */
    @PostMapping(path = {"", "/"})
    public void addName(@RequestBody Player player) {
        lobbyPlayers.add(player);
        simpMessagingTemplate.convertAndSend("/topic/waitingRoom", player);
        System.out.println(lobbyPlayers);
    }

    @GetMapping(path = {"", "/"})
    public List<Player> getPlayersAlreadyWaiting() {
        return lobbyPlayers;
    }

    @GetMapping(path = "/act")
    public String getAct() {
        Activity act = restTemplate.getForObject("http://localhost:8080/data/rand", Activity.class);
        return act.getImagePath();
    }

    @MessageMapping("/disconnect")
    public void playerDisconnect(Player player) {
        if (lobbyPlayers.remove(player)) {
            System.out.println("Player " + player.name + " disconnected!");
            simpMessagingTemplate.convertAndSend("/topic/disconnect", player);
        }
    }

    @MessageMapping("/startGame")
    public void startGame(Player player) {
        //TODO set counter to 0th question in (map of games)
        for(Player p : lobbyPlayers){
            simpMessagingTemplate.convertAndSend("/topic/render_question", p);
        }
    }

    public void createQuestion(){
        Random r = new Random();
        System.out.println(r.nextInt(2));
    }

}
