package app;

import database.DatabaseController;
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

    @RequestMapping(value = "/peoplesimages",method = RequestMethod.GET)
    public void startOthersWork(){return;}

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
        List<String> ids = new ArrayList<String>();
        List<byte[]> bufferedImgs = new ArrayList<byte[]>();
        return databaseController.getAllImages();
    }

    @RequestMapping(value = "getcolors", method = RequestMethod.POST)
    public @ResponseBody List<Integer> getColors(@RequestBody String loc){
        System.out.println("Requesting color of images of other peoples work from id: " + loc);
        //Integer id = new Integer(databaseController.getImageID(loc));
        //Integer id = new Integer(Integer.parseInt(idSting));
        System.out.println("Image at loc " + loc + " has " + databaseController.getColours(loc).size() + " colors.");
        return databaseController.getColours(loc);
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

