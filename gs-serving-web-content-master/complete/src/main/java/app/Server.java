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
    private MySQLConnection mySQLConnection = new MySQLConnection();

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
        //GetImage image = new GetImage(image64.getImageData());
        return success;
    }
}

