package app;

import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReduceColors {

    private Picture pic;

    // ReduceColros gets image from Server
    public ReduceColors(Picture image) throws IOException {
        //System.out.println(image.getHeight());
        this.pic = image;

        String[] parts = pic.getImageData().split(",");
        String imString = parts[1];
        int difficulty = pic.getDifficulty();

        System.out.println("difficulty" + difficulty);

        generateColorPalette(imString,difficulty);
    }

    public void generateColorPalette(String imageString, int numColors) throws IOException {
        List<Color> colors = new ArrayList<Color>();
        Random r = new Random();
        //int height = pic.getHeight();
        //int width = pic.getWidth();

//        String[] parts = pic.getImageData().split(",");
//        String imageString = parts[1];

        // create a buffered image
        BufferedImage image = null;
        byte[] imageByte;

        BASE64Decoder decoder = new BASE64Decoder();
        imageByte = decoder.decodeBuffer(imageString);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
        image = ImageIO.read(bis);
        int actualHeight =  image.getHeight();
        int actualWidth = image.getWidth();

        for (int i = 0; i < 100; i++) {
            //System.out.println(r.nextInt(actualWidth) + " " + r.nextInt(actualHeight));
            int rgb = image.getRGB(r.nextInt(actualWidth), r.nextInt(actualHeight));
            Color c = new Color(rgb);
            colors.add(c);
            //double[] vector = new double[] {c.getRed(), c.getGreen(), c.getBlue()};
        }


        System.out.println(image.getHeight() +  " " + image.getWidth());
        System.out.println(colors);
    }
}
