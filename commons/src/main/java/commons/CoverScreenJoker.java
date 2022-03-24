package commons;

public class CoverScreenJoker extends Joker{
    public CoverScreenJoker(String name, String imagePath) {
        super(name, imagePath);
    }

    public void onClick(){
        if(isUsed()){
            return;
        }
        System.out.println("Cover Screen Joker");
        use();
    }
}
