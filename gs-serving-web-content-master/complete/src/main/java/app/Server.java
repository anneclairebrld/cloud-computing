package app;

import database.DatabaseController;
import database.MySQLConnection;
import database.StorageConnection;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class Server {
    private DatabaseController databaseController = new DatabaseController();

    @RequestMapping(value = "/mainpage", method = RequestMethod.GET)
    public void start() {
        return;
    }

    @RequestMapping(value = "images", method = RequestMethod.POST)
    public @ResponseBody Response saveImage(@RequestBody Picture image64) throws IOException {
        System.out.println("Got a post request to the server");

        //buffered image is just a bytestream -- it has no format
        MosaicGenerator mosaicGenerator = new MosaicGenerator();
        BufferedImage pixelised_image  = mosaicGenerator.run(image64.getImageData(), "out", image64.getDifficulty(), image64.getPixelWidth());
        
        //why does this return image64 ? ==> is that the image ?

        Integer[] pixel_size = {20, 35};
        databaseController.post(pixelised_image, pixel_size);
        databaseController.getPixelSize(databaseController.getMydbImageID());
        Response success = new Response("success", image64);
        return success;
    }
}

