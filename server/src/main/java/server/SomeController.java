package server;

import commons.Player;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class SomeController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private List<Player> players = new ArrayList<>();
    private Player lastPlayerName;


    SomeController(SimpMessagingTemplate simpMessagingTemplate){
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping("/enterRoom")
    public List<Player> getPlayersAlreadyWaiting(){
        return players;
    }

    @PostMapping("/enterRoom")
    public String enterRoom(@RequestBody String name) {
        System.out.println("Someone made a post request with their name");
//        simpMessagingTemplate.convertAndSend("/topic/waitingRoom",name);
        return "Hello " + name;
    }

    @MessageMapping("/waitingRoom") // /app/waitingRoom
    @SendTo("/topic/waitingRoom")
    public Player newPlayerInWaitingRoom(Player player){
        System.out.println("Sending newPlayer in waitingRoom");
        lastPlayerName = player;
        players.add(player);
        return player;
    }
}