package app;

public class Picture {
    private Integer width;
    private Integer height;
    private String imageData;

    public Picture(){}

    public Picture(Integer width, Integer height, String imageData){
        this.width = width;
        this.height = height;
        this.imageData = imageData;
    }
    public Integer getWidth(){
        return width;
    }
    public Integer getHeight(){
        return height;
    }
    public String getImageData(){
        return imageData;
    }

}
