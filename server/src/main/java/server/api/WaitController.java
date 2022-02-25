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

import commons.Player;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/wait")
public class WaitController {

    private final SimpMessageSendingOperations simpMessagingTemplate;
    private final List<Player> lobbyPlayers = new ArrayList<>();


    WaitController(SimpMessageSendingOperations simpMessagingTemplate){
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public List<Player> getLobbyPlayers() {
        return lobbyPlayers;
    }

    /**
     * Adds the player to the current player list in the lobby and then pushes it to the socket channel
     * @param player Player that is sent in the request
     */
    @PostMapping(path = { "", "/" })
    public void addName(@RequestBody Player player) {
        lobbyPlayers.add(player);
        simpMessagingTemplate.convertAndSend("/topic/waitingRoom",player);
        System.out.println(lobbyPlayers);
    }

    @GetMapping(path = { "", "/" })
    public List<Player> getPlayersAlreadyWaiting(){
        return lobbyPlayers;
    }

    @MessageMapping("/waitingRoom") // /app/waitingRoom
    @SendTo("/topic/waitingRoom")
    public Player newPlayerInWaitingRoom(Player player){
        System.out.println("Sending newPlayer message in waitingRoom to all listeners");
        return player;
    }
}
