package commons;

public class DecreaseTimeJoker extends Joker{
    public DecreaseTimeJoker(String name, String imagePath) {
        super(name, imagePath);
    }

    public void onClick(){
        if(isUsed()){
            return;
        }
        System.out.println("DecreaseTimeJoker");
        //TODO send message to decrease_time, somehow import server utils
//        this.serverUtils.sendThroughSocket("/app/decrease_time", new Player(appController.getName()));
        use();
    }
}
