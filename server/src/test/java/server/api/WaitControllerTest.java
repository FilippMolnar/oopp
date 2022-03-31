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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class WaitControllerTest {

    private WaitController waitController;
    private List<Player> lobby = new ArrayList<>();
    private final SimpMessagingTemplate mockSimpMessagingTemplate = Mockito.mock(SimpMessagingTemplate.class);
    private final GameController mockGameController = Mockito.mock(GameController.class);
    private final Player player1 = new Player("player1");
    private final Player player2 = new Player("player2");
    private final Player player3 = new Player("player3");

    @BeforeEach
    public void setup() {
        reset(mockSimpMessagingTemplate);
        reset(mockGameController);
        when(mockGameController.getGame(any(Integer.class))).thenReturn(new Game()); // return empty game

        lobby.clear();
        waitController = new WaitController(mockSimpMessagingTemplate, mockGameController, null);
    }

    @Test
    public void addsNameTest() {
        waitController.addName(new Player("Name"));
        lobby.add(new Player("Name"));
        assertEquals(lobby, waitController.getLobbyPlayers());
    }

    @Test
    public void remove2PlayersTest() {
        waitController.addName(player1);
        waitController.addName(player2);
        waitController.addName(player3);

        waitController.playerDisconnectWaitingRoom(player2); // [1,2,3] - [2] = [1,3]
        assertEquals(waitController.getLobbyPlayers(), List.of(player1, player3));

        waitController.playerDisconnectWaitingRoom(player1);// [1,3] - 1 = [3]
        assertEquals(waitController.getLobbyPlayers(), List.of(player3));
    }

    @Test
    public void removeInexistentPlayer() {
        waitController.addName(player1);

        waitController.playerDisconnectWaitingRoom(player2);
        assertEquals(waitController.getLobbyPlayers(), List.of(player1));
    }

    @Test
    public void checkSocketCalledAfterPostRequest() {
        waitController.addName(new Player("Name"));
        verify(mockSimpMessagingTemplate,times(1)).convertAndSend(
                anyString(),eq(new Player("Name")));
    }

    @Test
    public void coverHands(){
        player1.setGameID(1);
        waitController.coverHands(player1);

    }
}
