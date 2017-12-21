package database;

import org.apache.tomcat.util.codec.binary.Base64;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//This class controls MySQLConnection and StorageConnection
public class DatabaseController {
    //Connections
    private MySQLConnection mySQLConnection = new MySQLConnection("images");
    private StorageConnection storageConnection = new StorageConnection();

    //Access names
    private String MySQLTableStrgName = "Storage_Details";
    //private String MySQL_table_colouringPathName = "";
    private String strgBucketOriginalName = "original_imgs";
    private String strgBucketModImageName = "modifying_imgs";

    //Other global variables
    private Integer dbImageID = null;
    private String strgImageLocation = null;

    public DatabaseController(){}

    public String post(BufferedImage image, Integer[] pixelsize, List<Integer> colours, Integer width, Integer height) {
        storageConnection.connectToBucket(strgBucketOriginalName);
        strgImageLocation = storageConnection.postImage(image);
        dbImageID = mySQLConnection.post(strgImageLocation, pixelsize, colours, width, height, MySQLTableStrgName);
        return strgImageLocation;
    }

    public void postModifiedImage(BufferedImage image){
        storageConnection.connectToBucket(strgBucketModImageName);
        storageConnection.postImage(image, strgImageLocation);
    }

    public BufferedImage getBufferedImage(String name){
        storageConnection.connectToBucket(strgBucketOriginalName);
        return storageConnection.getBufferedImage(name);
    }


    public byte[] getImage(String name) {
        if(name.equals("mine")){
            name = strgImageLocation;
        }
        storageConnection.connectToBucket(strgBucketOriginalName);
        return storageConnection.getImage(name);
    }

    //get the width and height from the db
    public Integer[] getImageSize(String loc){
        String[] info = {loc, "WIDTH"};
        String[] info2 = {loc, "HEIGHT"};
        String width = mySQLConnection.get(info, MySQLTableStrgName, "IMAGE_LOC");
        String height = mySQLConnection.get(info2, MySQLTableStrgName, "IMAGE_LOC");
        //System.out.println(width + " , " + height);

        Integer[] image_size = {Integer.parseInt(width), Integer.parseInt(height)};
        return image_size;
    }



    //get the width and height from the db
    public Integer[] getPixelSize(String loc){
        String[] info = {loc, "PIXELWIDTH"};
        String[] info2 = {loc, "PIXELHEIGHT"};
        String width = mySQLConnection.get(info, MySQLTableStrgName, "IMAGE_LOC");
        String height = mySQLConnection.get(info2, MySQLTableStrgName, "IMAGE_LOC");
        //System.out.println(width + " , " + height);

        Integer[] pixel_size = {Integer.parseInt(width), Integer.parseInt(height)};
        return pixel_size;
    }

    public String getImageLoc(Integer image_id){
        String[] info = {image_id.toString(), "IMAGE_LOC"};
        return mySQLConnection.get(info, MySQLTableStrgName, "ID");
    }

    public  String getImageID(String loc) {
        String[] info = {loc, "ID"};
        System.out.println("location was: " + loc + " and ID is: " + mySQLConnection.get(info, MySQLTableStrgName, "IMAGE_LOC"));
        return mySQLConnection.get(info, MySQLTableStrgName, "IMAGE_LOC");
    }
    //there are 20 colours
   /* public List<Integer> getColours(Integer image_id) {
        List<Integer> colours = new ArrayList<Integer>();
        for(int i = 0; i < 20; i++){
            String[] info = {image_id.toString(), "COLOUR" + (i+1)};
            Integer result =  Integer.parseInt(mySQLConnection.get(info, "Storage_Details", "ID"));
            if (result != 0) {
                colours.add(result);
            }
        }

        return colours;
    }*/

    public List<Integer> getColours(String loc) {
        List<Integer> colours = new ArrayList<Integer>();
        for(int i = 0; i < 20; i++){
            String[] info = {loc, "COLOUR" + (i+1)};
            Integer result =  Integer.parseInt(mySQLConnection.get(info, "Storage_Details", "IMAGE_LOC"));
            if (result != 0) {
                colours.add(result);
            }
        }

        return colours;
    }

    //get all images

    //deletes everything that has to do with the image
    public void delete(){
        storageConnection.connectToBucket(strgBucketOriginalName);
        storageConnection.deleteBlob(strgImageLocation);
        storageConnection.connectToBucket(strgBucketModImageName);
        storageConnection.deleteBlob(strgImageLocation);
        strgImageLocation = null;

        mySQLConnection.delete(dbImageID, MySQLTableStrgName);
        dbImageID = null;
    }

    //gets all images saved in the bucket storage
    public Map<String, String> getAllImages(){
        String result = mySQLConnection.getAll("IMAGE_LOC", MySQLTableStrgName);
        String[] result_array =result.split(",");
        Map<String, String> images = new HashMap<String, String>();
        storageConnection.connectToBucket(strgBucketOriginalName);

        for(int i = 0 ; i< result_array.length ; i++){
            Base64 codec = new Base64();
            String imageBase64Data= codec.encodeBase64String(storageConnection.getImage(result_array[i]));
            String imageDataURL= "data:image/png;base64," + imageBase64Data ;
            images.put(imageDataURL, result_array[i]);
        }

        return images;
    }

    //getters and setters of global variables
    public Integer getMydbImageID(){
        return dbImageID;
    }

    public void setMydbImageID(Integer dbImageID){
        this.dbImageID = dbImageID;
    }

    public String getMyStrgImageLoc() {
        return strgImageLocation;
    }

    public void setMyStrgImageLoc(String strgImageLocation){
        this.strgImageLocation = strgImageLocation;
    }

}
