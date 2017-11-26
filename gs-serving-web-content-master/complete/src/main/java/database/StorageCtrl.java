package database;

// Imports the Google Cloud client library
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
public class StorageCtrl {

    public StorageCtrl() {
        System.out.println("Connecting to storage");
        Storage storage = StorageOptions.getDefaultInstance().getService();
        String bucketname = "test_img_bucket0";

        Page<Bucket> buckets = storage.list();
        System.out.println("printing buckets");
        for (Bucket bucket : buckets.iterateAll()) {
            System.out.println(bucket);
        }
    }
}
