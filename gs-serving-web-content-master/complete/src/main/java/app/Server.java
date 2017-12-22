package app;

import com.google.gson.Gson;
import database.DatabaseController;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class Server {
    //access points to controller classes required
    private DatabaseController databaseController = new DatabaseController();
    private MosaicGenerator mosaicGenerator = new MosaicGenerator();
    private String loc;
    private Map<String, List<Integer>> coloredIndexes = new HashMap<>();
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
    public @ResponseBody String saveImage(@RequestBody Picture image64) throws IOException {
        System.out.println("Got a post request to the server");
        BufferedImage pixelised_image  = mosaicGenerator.run(image64.getImageData(), "out", image64.getDifficulty(), image64.getPixelWidth());

        //get the required information from the mosaic generator and post the image
        Integer[] pixel_size = {mosaicGenerator.pixelatedImage.getPixelWidth(), mosaicGenerator.pixelatedImage.getPixelHeight()};
        loc = databaseController.post(pixelised_image, pixel_size, mosaicGenerator.pixelatedImage.getRGBs(), mosaicGenerator.pixelatedImage.getxNum(), mosaicGenerator.pixelatedImage.getyNum());
        return loc;
    }

    //why is this a method post ? --> should be get
    @RequestMapping(value = "coloring", method = RequestMethod.POST)
    public @ResponseBody PixelatedImage getImage(){
        System.out.println("Image requested from FrontEnd :)");
        return mosaicGenerator.pixelatedImage;
    }

    @RequestMapping(value = "/track", method = RequestMethod.POST)
    public @ResponseBody Integer keepTrack (@RequestBody String data){
        System.out.println(data);
        JSONObject json = new JSONObject(data);
        String id = json.getString("id");
        Integer index = json.getInt("index");
        //System.out.println("got here with " + id + " " + index);
        if(coloredIndexes.containsKey(id)) coloredIndexes.get(id).add(index);
        else{
            coloredIndexes.put(id, new ArrayList<Integer>());
            coloredIndexes.get(id).add(index);
        }
        System.out.println("indexes for id: " + coloredIndexes);
        return 1;
    }

    @RequestMapping(value = "/getTrack", method = RequestMethod.POST)
    public @ResponseBody List<Integer> getTrack (@RequestBody String id){
        System.out.println("requested the tracking of: " + id);
        return coloredIndexes.get(id);
    }

    //this is for the otherswork page
    @RequestMapping(value = "otherswork", method = RequestMethod.GET)
    public @ResponseBody Map<String, String> getOthersWork(){
        System.out.println("Requesting images of other peoples work");
        return databaseController.getAllImages();
    }

    @RequestMapping(value = "getinfo", method = RequestMethod.POST)
    public @ResponseBody PixelatedImage getInfo(@RequestBody String loc) throws IOException {
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

    @MessageMapping("/interact/{loc}")
    @SendTo("/topic/game/{loc}")
    public Interaction interact(String req) throws Exception{
        Interaction action = new Interaction(req);
        //System.out.println("row: " + action.getRow() + " col: " + action.getCol() + " color: " + action.getColor());
        return action;
    }
}

