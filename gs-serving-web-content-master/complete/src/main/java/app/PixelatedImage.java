package app;
import java.awt.*;

import java.awt.image.BufferedImage;
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

   public PixelatedImage(BufferedImage image){
       this.image = image;
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

    public void setIndexes(){
        this.indexes = new ArrayList<Integer>();
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
    }

    public List<Integer> getIndexes() {
        return indexes;
    }
}
