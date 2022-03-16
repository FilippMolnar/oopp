package server.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Activity;
import commons.TemplateActivity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.api.ActivityController;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/data")
public class ImportData {
    @GetMapping(path = "/load")
    public static String ImportAllFiles() {
        String location = "server/src/main/resources/33";
        File dir = new File(location);
        File[] listing = dir.listFiles();

        //the corresponding image id in list
        int id = -1;

        for (int i = 0; i < listing.length; i++) {
            File log = listing[i];
            File image = listing[i];
            ObjectMapper mapper = new ObjectMapper();
            try {
                // Read the activity from JSON file
                TemplateActivity temp = mapper.readValue(log, TemplateActivity.class);

                // Create a new activity to add to database
                if (id >= 0) {
                    image = listing[id];
                    id = -1;
                } else {
                    image = listing[i + 1];
                    id = -2;
                }
                Activity act = new Activity(temp.title, temp.consumption_in_wh, location + "/" + image.getName());

                // Add activity to database
                ActivityController.addActivity(act);

            } catch (IOException e) {
                if (id == -2) id = -1;
                else id = i;
            }
        }

        return "Data was loaded in database!";
    }
}