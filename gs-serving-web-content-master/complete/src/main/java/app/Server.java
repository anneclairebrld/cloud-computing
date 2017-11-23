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
    private MySQLConnection mySQLConnection;

    @RequestMapping(value = "/mainpage", method = RequestMethod.GET)
    public void start() {
        return;
    }

    @RequestMapping(value = "images", method = RequestMethod.POST)
    public @ResponseBody Response saveImage(@RequestBody Picture image64) throws IOException {
        System.out.println("Got a post request to the server");
        //System.out.println("base64 String: " + image64.getImageData());
        System.out.println("Image Width: " + image64.getWidth());
        System.out.println("Image Height: " + image64.getHeight());
        //byte[] data = Base64.decodeBase64(image64);
        //String s = new String(data);
        //System.out.println(s);
        Response success = new Response("success", image64);
        System.out.println("just before connection stuff");
        MySQLConnection mySQLConnection = new MySQLConnection();
        //GetImage image = new GetImage(image64.getImageData());
        return success;
    }
}

