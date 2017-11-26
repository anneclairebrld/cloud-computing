package app;

import database.MySQLConnection;
import database.StorageCtrl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class Server {
    private MySQLConnection mySQLConnection = new MySQLConnection("images");
    private StorageCtrl storageCtrl = new StorageCtrl();
    private Integer image_id; // if eq = 0 : does not exist
    private String tableName = "Storage_Details";

    @RequestMapping(value = "/mainpage", method = RequestMethod.GET)
    public void start() {
        return;
    }

    @RequestMapping(value = "images", method = RequestMethod.POST)
    public @ResponseBody Response saveImage(@RequestBody Picture image64) throws IOException {
        System.out.println("Got a post request to the server");

        MosaicGenerator mosaicGenerator = new MosaicGenerator();
        mosaicGenerator.run(image64.getImageData(), "out", image64.getDifficulty(), image64.getPixelWidth());

        Response success = new Response("success", image64);

        //image id generated when i post in the database
        System.out.println("insert element in table");
        image_id = mySQLConnection.post(66, "IMAGE_LOC", tableName);
        mySQLConnection.get("all", tableName);
        System.out.println("modified element in table");
        mySQLConnection.put(image_id, 77, tableName);
        //GetImage image = new GetImage(image64.getImageData());
        return success;
    }
}

