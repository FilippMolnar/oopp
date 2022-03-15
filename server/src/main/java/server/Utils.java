package server;

import commons.Player;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.util.Set;

public class Utils {

    private final SimpMessageSendingOperations simpMessagingTemplate;

    public Utils(SimpMessageSendingOperations simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendToAllPlayers(Set<Player> playerList, String destination, Object payload){
        for (Player player : playerList) {
            String playerID = player.getSocketID();
            simpMessagingTemplate.convertAndSendToUser(playerID, destination, payload);
        }
    }
}
