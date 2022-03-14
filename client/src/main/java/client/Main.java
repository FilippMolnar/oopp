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

import client.controllers.HomeScreenCtrl;
import client.controllers.MainAppController;
import client.controllers.QuestionInsertNumberCtrl;
import client.controllers.QuestionMultiOptionsCtrl;
import client.controllers.WaitingRoomCtrl;
import client.controllers.LeaderBoardCtrl;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
     * @param parts path to the .fxml file
     * @return the location of the path
     */
    private URL getLocation(String... parts) {
        var path = Path.of("", parts).toString();
        return Main.class.getClassLoader().getResource(path);
    }

    /**
     * Simple example that creates a scene without dependency injection.
     * If you provide <code>QuoteOverviewCtrl</code> or <code>AddQuoteCtrl</code> in the <code>example.fxml</code> file then
     * the code will not compile because the constructor of those classes relies on dependency injection from <code>Juice</code>
     * We can delete this function because we can just use what they have done.
     * @param primaryStage - stage received by JavaFX in the start method
     * @throws IOException
     */
    private void loadSimpleExample(Stage primaryStage) throws IOException{
        String[] path = {"client","scenes","clock.fxml"};
        FXMLLoader loader = new FXMLLoader(getLocation(path));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("FXML simple load.No Controller");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * This way of loading the stages makes sure the <code>Juice</code> injects the right dependencies to the controllers
     * @param primaryStage
     */
    private void loadUsingTemplateDependencyInjection(Stage primaryStage){
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
        MainAppController appcontroller = INJECTOR.getInstance(MainAppController.class);
        appcontroller.initialize(primaryStage, waitingRoom, home, leaderBoard, qMulti, qInsert);
    }
    @Override
    public void start(Stage primaryStage) throws IOException{
        loadUsingTemplateDependencyInjection(primaryStage);
    }
}
