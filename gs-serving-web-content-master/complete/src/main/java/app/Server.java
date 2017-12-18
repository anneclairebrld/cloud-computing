package app;

import com.google.gson.Gson;
import database.DatabaseController;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONObject;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/*Imports for Websockets*/
/*End of Websockets imports*/

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class Server {
    //access points to controller classes required
    private DatabaseController databaseController = new DatabaseController();
    private MosaicGenerator mosaicGenerator = new MosaicGenerator();

    //starting up
    @RequestMapping(value = "/mainpage", method = RequestMethod.GET)
    public void start() {
        return;
    }

    @RequestMapping("/peoplesimages")
    public void startOthersWork(){return;}

    @RequestMapping("/colorPage")
    public void resumeColoring(){return;}

    //saving the image on the storage
    @RequestMapping(value = "images", method = RequestMethod.POST)
    public @ResponseBody Response saveImage(@RequestBody Picture image64) throws IOException {
        System.out.println("Got a post request to the server");
        BufferedImage pixelised_image  = mosaicGenerator.run(image64.getImageData(), "out", image64.getDifficulty(), image64.getPixelWidth());

        //get the required information from the mosaic generator and post the image
        Integer[] pixel_size = {mosaicGenerator.pixelatedImage.getPixelWidth(), mosaicGenerator.pixelatedImage.getPixelHeight()};
        databaseController.post(pixelised_image, pixel_size, mosaicGenerator.pixelatedImage.getRGBs(), mosaicGenerator.pixelatedImage.getxNum(), mosaicGenerator.pixelatedImage.getyNum());

        Response success = new Response("success", image64);
        return success;
    }

    //why is this a method post ? --> should be get
    @RequestMapping(value = "coloring", method = RequestMethod.POST)
    public @ResponseBody PixelatedImage getImage(){
        System.out.println("Image requested from FrontEnd :)");
        return mosaicGenerator.pixelatedImage;
    }


    //this is for the otherswork page
    @RequestMapping(value = "otherswork", method = RequestMethod.GET)
    public @ResponseBody Map<byte[], String> getOthersWork(){
        System.out.println("Requesting images of other peoples work");
        return databaseController.getAllImages();
    }

    @RequestMapping(value = "getinfo", method = RequestMethod.POST)
    public @ResponseBody PixelatedImage getInfo(@RequestBody String loc) throws IOException {
        System.out.println("Requesting info of images of other peoples work: " + loc);
        loc = loc.replaceAll("\"", "");
        System.out.println("Requesting info of images of other peoples work: " + loc);
        PixelatedImage image = new PixelatedImage(databaseController.getBufferedImage(loc));
        image.setColorsFromInts(databaseController.getColours(loc));
        Integer[] pixelSize = databaseController.getPixelSize(loc);
        Integer[] imageSize = databaseController.getImageSize(loc);
        image.setPixelWidth(pixelSize[0]);
        image.setPixelHeight(pixelSize[1]);
        image.setxNum(imageSize[0]);
        image.setyNum(imageSize[1]);
        image.setIndexes();
        return image;
    }
    //End of OthersWork Page

    @MessageMapping("/interact")
    @SendTo("/topic/game")
    public Interaction interact(String req) throws Exception{
        Interaction action = new Interaction(req);
        //System.out.println("row: " + action.getRow() + " col: " + action.getCol() + " color: " + action.getColor());

        return action;
    }
}

