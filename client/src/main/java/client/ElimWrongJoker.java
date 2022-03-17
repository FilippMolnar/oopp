package client;

import client.controllers.MainAppController;
import client.controllers.questions.QuestionMultiOptionsCtrl;
import client.utils.ServerUtils;
import commons.Activity;
import commons.Question;

import java.util.ArrayList;

public class ElimWrongJoker extends Joker {

    public ElimWrongJoker(String name, String imagePath, ServerUtils serverUtils) {
        super(name, imagePath, serverUtils);
    }

    public void onClick(MainAppController mainCtrl){
        if(isUsed()){
            return;
        }
        if(mainCtrl.getCurrentScene().getController() instanceof QuestionMultiOptionsCtrl qCtrl){
            Question question = mainCtrl.getCurrentQuestion();
            ArrayList<Integer> wrong_options = new ArrayList<>();
            int i = 0;
            for(Activity a : question.getChoices()){
                if(a.id != question.getCorrect().id){
                    wrong_options.add(i);
                }
                i++;
            }
            int index = (int)(Math.random() * wrong_options.size());
            switch (wrong_options.get(index)) {
                case 0 -> qCtrl.getOptionA().setDisable(true);
                case 1 -> qCtrl.getOptionB().setDisable(true);
                case 2 -> qCtrl.getOptionC().setDisable(true);

            }
            use();
        }
    }
}
