package app;

import database.MySQLConnection;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class Server {
    private MySQLConnection mySQLConnection = new MySQLConnection("images");
    private Integer image_id;

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
        System.out.println("just before connection stuff");
        mySQLConnection.get_tables();

        // This will be different once we are using object storage
        Integer imageLoc = 0;
        //image_id = mySQLConnection.post(imageLoc);

        image_id = 2;
        String info = "" + image_id;
        System.out.println("printintg out the information of image_ide number 2");
        mySQLConnection.get(info, "Storage_Details");
        System.out.println("Printing out all the info on the table");
        mySQLConnection.get("all", "Storage_Details");

        //GetImage image = new GetImage(image64.getImageData());
        return success;
    }
}

