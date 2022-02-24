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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WaitControllerTest {

    private WaitController sut;
    private List<Player> lobby;
    private MockSimpMessagingTemplate mockSimpMessagingTemplate;

    @BeforeEach
    public void setup() {
        mockSimpMessagingTemplate = new MockSimpMessagingTemplate();
        sut = new WaitController(mockSimpMessagingTemplate);
        lobby = new ArrayList<>();
    }

    @Test
    public void addsNameTest() {
        var actual = sut.addName(new Player("Name"));
        lobby.add(new Player("Name"));
        assertEquals(lobby, actual);
    }
    @Test
    public void checkSocketCalledAfterPostRequest(){
        sut.addName(new Player("Name"));
        assertTrue(mockSimpMessagingTemplate.sendMesasgeToUser);
        assertEquals(Player.class, mockSimpMessagingTemplate.objectSend.getClass(),
                "Object send to the socket should be of type player");
        try{
            Player player = (Player) mockSimpMessagingTemplate.objectSend;
        }catch (ClassCastException e){
            fail();
        }
    }
}
