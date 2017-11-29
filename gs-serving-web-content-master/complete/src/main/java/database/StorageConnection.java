package database;

// Imports the Google Cloud client library

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.print.DocFlavor;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;

import static org.apache.commons.codec.CharEncoding.UTF_8;

public class StorageConnection {
    Storage storage;
    String bucketName;
    Bucket bucket;

    //Sets up connection to storage
    public StorageConnection() {
        storage = StorageOptions.getDefaultInstance().getService();
    }

    //get access to the bucket you want to be writing to
    public void connectToBucket(String bucketName) {
        this.bucketName = bucketName;
        Page<Bucket> buckets = storage.list();
        for (Bucket bucket : buckets.iterateAll()) {
            if (bucket.getName().equals(bucketName)) {
                this.bucket = bucket;
            }
        }
    }

    public String postImage(BufferedImage image){
        //create a id
        DateTimeFormatter dtf = DateTimeFormat.forPattern("-YYYY-MM-dd-HHmmssSSS");
        String fileName = DateTime.now(DateTimeZone.UTC).toString(dtf);

        //get correct image format
        byte[] bytes = bufferedImage_to_bytes(image);

        //create blob and store
        BlobId blobId = BlobId.of(bucketName, fileName);                                //creates identifier
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        Blob blob = storage.create(blobInfo, bytes);
        return fileName;
    }

    //posting image that you have edited
    public void postImage(BufferedImage image, String fileName) {
        //get correct image format
        byte[] bytes = bufferedImage_to_bytes(image);

        //create blob and store
        BlobId blobId = BlobId.of(bucketName, fileName);                                //get element and writes on top
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        Blob blob = storage.create(blobInfo, bytes);
    }


    public BufferedImage getImage(String imageName) {
        //get data from storage
        BlobId blobId = BlobId.of(bucketName, imageName);
        byte[] content = storage.readAllBytes(blobId);

        //get correct format
        BufferedImage image_out = bytes_to_bufferedImage(content);
        return image_out;
    }

    public boolean deleteBlob(String blobName) {
        BlobId blobId = BlobId.of(bucketName, blobName);
        boolean deleted = storage.delete(blobId);
        if (deleted) {
            return true;
        } else {
            return false;
        }
    }

    private byte[] bufferedImage_to_bytes(BufferedImage image) {
        //get bytes from image
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    private BufferedImage bytes_to_bufferedImage(byte[] bytes){
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  bufferedImage;
    }

    public void write_out(BufferedImage image_out, String outputFilename) {
        File outputFile = new File("./out/", outputFilename);
        String content_type = "png";
        try {
            ImageIO.write(image_out, content_type,  outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        //do i need this?
    }

}
