package database;

import org.omg.CORBA.INTERNAL;

import java.awt.image.BufferedImage;

//This class controls MySQLConnection and StorageConnection
public class DatabaseController {
    //Connections
    private MySQLConnection mySQLConnection = new MySQLConnection("images");
    private StorageConnection storageConnection = new StorageConnection();

    //Access names
    private String MySQL_table_storageName = "Storage_Details";
    //private String MySQL_table_colouringPathName = "";
    private String Storage_bucket_originalName = "original_imgs";
    private String Storage_bucket_modifyingImagesName = "modifying_imgs";

    //Other global variables
    private Integer db_image_id = 0;            // if = 0 : does not exist
    private String storage_image_location = ""; // if = "": does not exist

    public DatabaseController(){}

    public void post(BufferedImage image) {
        storageConnection.connectToBucket(Storage_bucket_originalName);
        storage_image_location = storageConnection.postImage(image);
        db_image_id = mySQLConnection.post(storage_image_location, MySQL_table_storageName);
    }

    public void postModifiedImage(BufferedImage image){
        storageConnection.connectToBucket(Storage_bucket_modifyingImagesName);
        storageConnection.postImage(image, storage_image_location);
    }

    public BufferedImage getImage(String name) {
        if(name.equals("mine")){
            name = storage_image_location;
        }
        storageConnection.connectToBucket(Storage_bucket_originalName);
        return storageConnection.getImage(name);
    }



    //public BufferedImage getImage(Integer id) {
        //get name from the database
        //storageConnection.connectToBucket(Storage_bucket_originalName);
        //return storageConnection.getImage(name);
    //}


    //get the width and height from the db
    public int[] getPixelSize(Integer image_id){
        int width = 0;
        int height = 0;
        if (image_id.equals(0) ){
            image_id = db_image_id;
        }

        String[] info = {image_id.toString(), "PIXELSIZE"};
        String result = mySQLConnection.get(info, MySQL_table_storageName, "ID");
        int[] pixel_size = {width, height};
        return pixel_size;
    }

    // method to get the image location => for when you want to have it for yourself and colour it
    public String get(Integer image_id){
        // get image loc
        //duplicate
        //add to own image_id db`
        //recreate a new image for modifying
        return "success";
    }

//    public BufferedImage[] getAllImages(){
//       do something like get all elements from the db and then iterate through whatever the db returns and gets them all from storage
//    }

    //deletes everything that has to do with the image
    public void delete(){
        storageConnection.connectToBucket(Storage_bucket_originalName);
        storageConnection.deleteBlob(storage_image_location);
        storageConnection.connectToBucket(Storage_bucket_modifyingImagesName);
        storageConnection.deleteBlob(storage_image_location);
        storage_image_location="";

        mySQLConnection.delete(db_image_id, MySQL_table_storageName);
        db_image_id = 0;
    }
}
