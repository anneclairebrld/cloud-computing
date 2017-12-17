package app;
import javax.imageio.ImageIO;
import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class PixelatedImage {
   private BufferedImage image;
   private Integer pixelWidth;
   private Integer pixelHeight;
   private Integer xNum;
   private Integer yNum;
   private List<Color> colors;
   private List<Integer> indexes;
   private List<Integer> RGBs;

   public PixelatedImage(BufferedImage image){
       this.image = image;
   }

   public PixelatedImage(byte[] image) throws IOException {
       InputStream in = new ByteArrayInputStream(image);
       BufferedImage bufferedImage = ImageIO.read(in);
       this.image = bufferedImage;
    }

   public PixelatedImage(){}

   public void setImage(BufferedImage image){
       this.image = image;
    }

   public Integer getPixelWidth() {
        return pixelWidth;
    }

   public Integer getPixelHeight() {
        return pixelHeight;
    }

   public Integer getxNum() {
        return xNum;
    }

   public Integer getyNum() {
        return yNum;
    }

   public List<Color> getColors() {
        return colors;
    }

   public void setPixelWidth(Integer pixelWidth){
       this.pixelWidth = pixelWidth;
    }


    public List<Integer> getRGBs() {return this.RGBs;}

    public void  setPixelHeight(Integer pixelHeight){
        this.pixelHeight = pixelHeight;
    }
    public void setxNum(Integer xNum){
        this.xNum = xNum;
    }
    public void setyNum(Integer yNum){
        this.yNum = yNum;
    }
    public void setColors(List<Color> colors){
        this.colors = colors;
    }

    public void setColorsFromInts(List<Integer> colors) {
        System.out.println("setting colors: " + colors);
        List<Color> result = new ArrayList<Color>();
        for (int i = 0; i< colors.size(); i++){
            Color c = new Color(colors.get(i));
            result.add(c);
        }
        this.colors = result;
    }

    public void setIndexes(){
        this.indexes = new ArrayList<Integer>();
        System.out.println("image is: " + image);
        System.out.println("image has height and width: " + image.getHeight() + " " + image.getWidth());
        for(int y = 0; y < image.getHeight(); y+=pixelHeight){
            for(int x = 0; x < image.getWidth(); x+=pixelWidth){
                for(int c = 1; c<=colors.size(); c++){
                    if (colors.get(c - 1).getRGB() == image.getRGB(x, y)){
                        this.indexes.add(c);
                        c = colors.size();
                    }
                }
            }
        }
        setRGBs();
    }

    private void setRGBs(){
        this.RGBs = new ArrayList<Integer>();
        for (int i = 0; i < colors.size(); i++){
            RGBs.add(colors.get(i).getRGB());
        }

        if (colors.size() < 20) {
            for( int i = colors.size(); i < 20; i++){
                RGBs.add(0);
            }
        }
    }

    public List<Integer> getIndexes() {
        return indexes;
    }
}
