package database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Storage {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer Id;
    private String Image_Location;
    private List<Integer> Colours;
    private Integer[] PixelSize;

    public Integer getID(){
        return Id;
    }

    //this is actually not used but hey!
    public void setId(Integer id) {
        Id = id;
    }

    public String getImage_Location() {
        return Image_Location;
    }

    public void setImage_Location(String image_Location) {
        Image_Location = image_Location;
    }

    public List<Integer> getColours(){
        return Colours;
    }

    public void setColours(List<Integer> colours) {
        Colours = colours;
    }

    public void setPixelSize(Integer[] pixelSize) {
        PixelSize = pixelSize;
    }

    public Integer[] getPixelSize(){
        return PixelSize;
    }
}
