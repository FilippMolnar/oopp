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
package client;

import client.controllers.*;
import client.controllers.questions.QuestionInsertNumberCtrl;
import client.controllers.questions.QuestionMultiOptionsCtrl;
import client.controllers.questions.QuestionSameAsCtrl;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

import static com.google.inject.Guice.createInjector;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule()); // creates Guice injector based on the module
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * return the URL to the .fxml file
     *
     * @param parts path to the .fxml file
     * @return the location of the path
     */
    private URL getLocation(String... parts) {
        var path = Path.of("", parts).toString();
        return Main.class.getClassLoader().getResource(path);
    }

    /**
     * This way of loading the stages makes sure the <code>Juice</code> injects the right dependencies to the controllers
     *
     * @param primaryStage
     */
    private void loadUsingTemplateDependencyInjection(Stage primaryStage) {
        var waitingRoom = FXML.load(WaitingRoomCtrl.class,
                "client", "scenes", "waiting_room.fxml");
        var home = FXML.load(HomeScreenCtrl.class,
                "client", "scenes", "HomeScreen.fxml");
        var leaderBoard = FXML.load(LeaderBoardCtrl.class,
                "client", "scenes", "Leaderboard.fxml");
        var qMulti = FXML.load(QuestionMultiOptionsCtrl.class,
                "client", "scenes", "QuestionMultiOptions.fxml");
        var qInsert = FXML.load(QuestionInsertNumberCtrl.class,
                "client", "scenes", "QuestionInsertNumber.fxml");
        var qTransition = FXML.load(TransitionSceneCtrl.class,
                "client", "scenes", "transition_between_questions.fxml");
        var sameAs = FXML.load(QuestionSameAsCtrl.class,
                "client", "scenes", "QuestionSameAs.fxml");
        MainAppController appcontroller = INJECTOR.getInstance(MainAppController.class);
        appcontroller.initialize(primaryStage, waitingRoom, home, leaderBoard, qMulti, qInsert, sameAs, qTransition);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        loadUsingTemplateDependencyInjection(primaryStage);
    }
}
