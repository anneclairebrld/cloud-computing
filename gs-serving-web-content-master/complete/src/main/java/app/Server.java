package app;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class Server {
    private app.MySQLConnection mySQLConnection;

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
        //GetImage image = new GetImage(image64.getImageData());
        demo();
        return success;
    }

    private void demo() {
        //save a couple of people to test
        mySQLConnection.save(new User("Jack", "Bauer"));
        mySQLConnection.save(new User("Chloe", "O'Brian"));
        mySQLConnection.save(new User("Kim", "Bauer"));
        mySQLConnection.save(new User("David", "Palmer"));
        mySQLConnection.save(new User("Michelle", "Dessler"));

        //fetch all
        System.out.println("Customer found with findAll():");
        for (User user : mySQLConnection.findAll()){
            System.out.println(user.toString());
        }

    }

}

