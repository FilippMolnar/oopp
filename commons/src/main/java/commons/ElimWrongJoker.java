package commons;

import java.util.ArrayList;

public class ElimWrongJoker extends Joker {

    public ElimWrongJoker(String name, String imagePath) {
        super(name, imagePath);
    }

    public void onClick(){
        //TODO get access to the question, show which question is wrong(inject to fxml)
//        ArrayList<Integer> wrong_options = new ArrayList<>();
//        int i = 0;
//        for(Activity a : question.getChoices()){
//            if(a.id != question.getCorrect().id){
//                wrong_options.add(i);
//            }
//            i++;
//        }
//        int index = (int)(Math.random() * wrong_options.size());
//        System.out.println(wrong_options);
//        System.out.println(index);
//        switch(wrong_options.get(index)){
//            case 0:
//                optionA.setText("wrong");
//                break;
//            case 1:
//                optionB.setText("wrong");
//                break;
//            case 2:
//                optionC.setText("wrong");
//                break;
//        }
        return;
    }
}
