package app;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;

public class GetImage {

// tokenize the data
        public GetImage(String sourceData) throws IOException {
            String[] parts = sourceData.split(",");
            String imageString = parts[1];

    // create a buffered image
            BufferedImage image = null;
            byte[] imageByte;

            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();

    // write the image to a file
            File outputfile = new File("image.png");
            ImageIO.write(image, "png", outputfile);

        }
}
