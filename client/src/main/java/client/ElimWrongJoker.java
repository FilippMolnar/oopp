package client;

import client.controllers.MainAppController;
import client.utils.ServerUtils;
import commons.Activity;
import commons.Question;

import java.util.ArrayList;

public class ElimWrongJoker extends Joker {

    public ElimWrongJoker(String name, String imagePath, ServerUtils serverUtils) {
        super(name, imagePath, serverUtils);
    }

    public void onClick(MainAppController mainCtrl, Question question){
        if(isUsed()){
            return;
        }
        ArrayList<Integer> wrong_options = new ArrayList<>();
        int i = 0;
        for(Activity a : question.getChoices()){
            if(a.id != question.getCorrect().id){
                wrong_options.add(i);
            }
            i++;
        }
        int index = (int)(Math.random() * wrong_options.size());
        System.out.println(wrong_options);
        System.out.println(index);
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
        use();
    }
}
