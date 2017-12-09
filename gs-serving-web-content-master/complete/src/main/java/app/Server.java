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
    private MosaicGenerator mosaicGenerator = new MosaicGenerator();

    @RequestMapping(value = "/mainpage", method = RequestMethod.GET)
    public void start() {
        return;
    }

    @RequestMapping(value = "images", method = RequestMethod.POST)
    public @ResponseBody Response saveImage(@RequestBody Picture image64) throws IOException {
        System.out.println("Got a post request to the server");

        BufferedImage pixelised_image  = mosaicGenerator.run(image64.getImageData(), "out", image64.getDifficulty(), image64.getPixelWidth());
        //how do i get the pixels and extract the rgb from it

        Integer[] pixel_size = {mosaicGenerator.pixelatedImage.getPixelWidth(), mosaicGenerator.pixelatedImage.getPixelHeight()};
        databaseController.post(pixelised_image, pixel_size, mosaicGenerator.pixelatedImage.getRGBs());
        System.out.println(databaseController.getColours(databaseController.getMydbImageID()));
        System.out.println();
        Response success = new Response("success", image64);
        return success;
    }

    @RequestMapping(value = "coloring", method = RequestMethod.POST)
    public @ResponseBody PixelatedImage getImage(){
        System.out.println("Image requested from FrontEnd :)");
        //System.out.println(mosaicGenerator.pixelatedImage.getPixelWidth());
        return mosaicGenerator.pixelatedImage;
    }
}

