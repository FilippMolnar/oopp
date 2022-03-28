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
@RequestMapping("/api/data")
public class ImportData {
    @GetMapping(path = "/load")
    public static String ImportAllFiles() {
        String loc = "src/main/resources/GoodActivities/";

        for(int j=0;j<=78;j++) {

            String groupID = "";
            String location = loc;
            if(j<10)groupID+="0";
            groupID+=j;
            //System.out.println(location);
            File dir = new File("server/"+location+groupID);
            File[] listing = dir.listFiles();

            //the corresponding image id in list
            int id = -1;
            if(listing == null)continue;
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
                    Activity act = new Activity(temp.title, temp.consumption_in_wh,  groupID + "/" + image.getName());

                    // Add activity to database
                    ActivityController.addActivity(act);

                } catch (IOException e) {
                    if (id == -2) id = -1;
                    else id = i;
                }
            }
        }
        return "Data was loaded in database!";
    }
}
