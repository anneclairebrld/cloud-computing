package database;

import java.awt.image.BufferedImage;

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
    private Integer dbImageID = 0;            // if = 0 : does not exist
    private String strgImageLocation = ""; // if = "": does not exist

    public DatabaseController(){}

    public void post(BufferedImage image) {
        storageConnection.connectToBucket(strgBucketOriginalName);
        strgImageLocation = storageConnection.postImage(image);
        dbImageID = mySQLConnection.post(strgImageLocation, MySQLTableStrgName);
    }

    public void postModifiedImage(BufferedImage image){
        storageConnection.connectToBucket(strgBucketModImageName);
        storageConnection.postImage(image, strgImageLocation);
    }

    public BufferedImage getImage(String name) {
        if(name.equals("mine")){
            name = strgImageLocation;
        }
        storageConnection.connectToBucket(strgBucketOriginalName);
        return storageConnection.getImage(name);
    }

    //get the width and height from the db
    public int[] getPixelSize(Integer image_id){
        String[] info = {image_id.toString(), "PIXELSIZE"};
        String result = mySQLConnection.get(info, MySQLTableStrgName, "ID");

        //parsing the string to get info out
        String[] parts = result.split(",");
        int[] pixel_size = {Integer.parseInt(parts[1]), Integer.parseInt(parts[2])};
        return pixel_size;
    }

    public String getImageLoc(Integer image_id){
        String[] info = {image_id.toString(), "IMAGE_LOC"};
        return mySQLConnection.get(info, MySQLTableStrgName, "ID");
    }

    //get all images

    //deletes everything that has to do with the image
    public void delete(){
        storageConnection.connectToBucket(strgBucketOriginalName);
        storageConnection.deleteBlob(strgImageLocation);
        storageConnection.connectToBucket(strgBucketModImageName);
        storageConnection.deleteBlob(strgImageLocation);
        strgImageLocation ="";

        mySQLConnection.delete(dbImageID, MySQLTableStrgName);
        dbImageID = 0;
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
