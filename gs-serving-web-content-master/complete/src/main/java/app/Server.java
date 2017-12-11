package app;

import database.DatabaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

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

    //saving the image on the storage
    @RequestMapping(value = "images", method = RequestMethod.POST)
    public @ResponseBody Response saveImage(@RequestBody Picture image64) throws IOException {
        System.out.println("Got a post request to the server");

        BufferedImage pixelised_image  = mosaicGenerator.run(image64.getImageData(), "out", image64.getDifficulty(), image64.getPixelWidth());

        //get the required information from the mosaic generator and post the image
        Integer[] pixel_size = {mosaicGenerator.pixelatedImage.getPixelWidth(), mosaicGenerator.pixelatedImage.getPixelHeight()};
        databaseController.post(pixelised_image, pixel_size, mosaicGenerator.pixelatedImage.getRGBs());

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
    @RequestMapping(value = "/otherswork", method = RequestMethod.GET)
    public @ResponseBody List<BufferedImage> getOthersWork(){
        System.out.println("Requesting images of other peoples work");
        return databaseController.getAllImages();
    }
}

