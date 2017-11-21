package app;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
public class Server {

    @RequestMapping(value = "/mainpage", method = RequestMethod.GET)
    public void start() {
        return;
    }

    @RequestMapping(value = "images", method = RequestMethod.POST)
    public @ResponseBody Response saveImage(@RequestBody Picture image64) throws IOException {
        System.out.println("Got a post request to the server");
        Response success = new Response("success", image64);
        MosaicGenerator mosaicGenerator = new MosaicGenerator();
        mosaicGenerator.run(image64.getImageData(), "out", image64.getDifficulty(), image64.getPixelWidth());
        //ReduceColors rc = new ReduceColors(image64);
        //GetImage image = new GetImage(image64.getImageData());
        //MosaicGenerator mosaicGenerator = new MosaicGenerator();
        //mosaicGenerator.run(image64.getImageData(), "generated", 5, 200);
        return success;
    }

}
