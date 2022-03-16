package commons;

public class DoublePointsJoker extends Joker{
    public DoublePointsJoker(String name, String imagePath) {
        super(name, imagePath);
    }

    public void onClick(){
        if(isUsed()){
            return;
        }
        System.out.println("DoublePointsJoker");
        use();
    }
}
