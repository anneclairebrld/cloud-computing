package app;

public class Picture {
    private Integer pixelWidth;
    private Integer pixelHeight;
    private Integer difficulty;
    private String imageData;
    private Integer width;
    private Integer height;

    public Picture(){}

    public Picture(Integer width, Integer height, Integer pixelWidth, Integer pixelHeight, String imageData, Integer difficulty){
        this.width = width;
        this.height = height;
        this.pixelWidth = pixelWidth;
        this.pixelHeight = pixelHeight;
        this.difficulty = difficulty;
        this.imageData = imageData;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getPixelWidth(){
        return pixelWidth;
    }
    public Integer getPixelHeight(){
        return pixelHeight;
    }
    public Integer getDifficulty(){
        return difficulty;
    }
    public String getImageData(){
        return imageData;
    }

}
