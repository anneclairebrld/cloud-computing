package app;

public class Picture {
    private Integer pixelWidth;
    private Integer pixelHeight;
    private Integer difficulty;
    private String imageData;

    public Picture(){}

    public Picture(Integer width, Integer height, String imageData, Integer difficulty){
        this.pixelWidth = width;
        this.pixelHeight = height;
        this.difficulty = difficulty;
        this.imageData = imageData;
    }
    public Integer getPixelWidthWidth(){
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
