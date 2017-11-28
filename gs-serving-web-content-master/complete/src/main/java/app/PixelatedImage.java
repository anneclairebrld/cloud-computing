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
}
