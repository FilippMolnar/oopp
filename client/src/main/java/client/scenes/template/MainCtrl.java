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
package client.scenes.template;

import client.controllers.QuestionInsertNumberCtrl;
import client.controllers.QuestionMultiOptionsCtrl;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;

    private QuoteOverviewCtrl overviewCtrl;
    private Scene overview;

    private AddQuoteCtrl addCtrl;
    private Scene add;

    private QuestionInsertNumberCtrl qInsertCtrl;
    private Scene qInsert;

    private QuestionMultiOptionsCtrl qMultiCtrl;
    private Scene qMulti;

    public void initialize(Stage primaryStage, Pair<QuoteOverviewCtrl, Parent> overview,
           Pair<AddQuoteCtrl, Parent> add,
           Pair<QuestionInsertNumberCtrl, Parent> qInsert,
           Pair<QuestionMultiOptionsCtrl, Parent> qMulti) {
        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());

        this.qInsertCtrl = qInsert.getKey();
        this.qInsert = new Scene(qInsert.getValue());
        this.qMultiCtrl = qMulti.getKey();
        this.qMulti = new Scene(qMulti.getValue());

        showOverview();
        primaryStage.show();
    }

    public void showOverview() {
        primaryStage.setTitle("Quotes: Overview");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();
    }

    public void showAdd() {
        primaryStage.setTitle("Quotes: Adding Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }
    public void showQuestionInsert() {
        primaryStage.setTitle("Insert Number question");
        primaryStage.setScene(qInsert);
//        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }
    public void showQuestionMulti() {
        primaryStage.setTitle("Multiple choice question");
        primaryStage.setScene(qMulti);
//        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }
}