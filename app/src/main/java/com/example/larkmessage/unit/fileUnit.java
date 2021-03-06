package com.example.larkmessage.unit;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.larkmessage.MessageActivity;
import com.example.larkmessage.MomentActivity;
import com.example.larkmessage.entity.Friend;
import com.example.larkmessage.entity.Message;
import com.example.larkmessage.entity.Moment;
import com.example.larkmessage.entity.UserItem;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.CancellableTask;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

public class fileUnit {
    private Uri file;
    private  StorageMetadata metadata;
    private CancellableTask uploadTask;

    public  void  upLoadImage(Uri path, Activity activity, UserItem userItem, final Moment moment)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://larkmessage.appspot.com");
        file = path;//Uri.fromFile(new File(path));
        StorageReference storageRef = storage.getReference();
// Create the file metadata
        metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();

// Upload file and metadata to the path 'images/mountains.jpg'
        uploadTask = storageRef.child("Moment/"+userItem.getEmail()+":"+file.getLastPathSegment()).putFile(file, metadata);

// Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle successful uploads on complete
                // ...
               // taskSnapshot
                updateStatues(moment);
            }
        });


    }
    public void  updateStatues(Moment moment)
    {
        moment.setFinish(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("MomentList").document(moment.getPath())
                .update("finish",moment.getFinish())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    public void  updateStatues(Message message,Friend friend)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        message.setStatus(true);
        db.collection("Chatting").document(friend.getMessageId()).collection("MessageList").document(message.getPath())
                .update("status",message.getStatus())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }





    public  void  upLoadMessageImage(Uri path, Activity activity, UserItem userItem,  final Message message,final Friend friend)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://larkmessage.appspot.com");
        file = path;//Uri.fromFile(new File(path));
        StorageReference storageRef = storage.getReference();
// Create the file metadata
        metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();

// Upload file and metadata to the path 'images/mountains.jpg'
        uploadTask = storageRef.child("Message/"+userItem.getEmail()+":"+file.getLastPathSegment()).putFile(file, metadata);

// Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle successful uploads on complete
                // ...
                // taskSnapshot
                updateStatues(message,friend);
            }
        });


    }
    public void downloadMessageImage(Message message, final ImageView imageView) throws IOException {
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://larkmessage.appspot.com");
        StorageReference storageRef = storage.getReference();
        StorageReference Ref = storageRef.child("Message/"+message.getImageResource());

        final File localFile = File.createTempFile(message.getImageResource(), "jpg");
        Ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                Bitmap bitmap= BitmapFactory.decodeFile(localFile.getPath());
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
    public void downloadImage(Moment moment, final ImageView imageView) throws IOException {
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://larkmessage.appspot.com");
        StorageReference storageRef = storage.getReference();
        StorageReference Ref = storageRef.child("Moment/"+moment.getImage());

        final File localFile = File.createTempFile(moment.getImage(), "jpg");
        Ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                Bitmap bitmap= BitmapFactory.decodeFile(localFile.getPath());
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
    void upLoadMusic(String path)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://larkmessage.appspot.com/Moment");
        file = Uri.fromFile(new File(path));
        StorageReference storageRef = storage.getReference();
// Create the file metadata
        metadata = new StorageMetadata.Builder()
                .setContentType("mp3")
                .build();

// Upload file and metadata to the path 'images/mountains.jpg'
        uploadTask = storageRef.child("images/"+file.getLastPathSegment()).putFile(file, metadata);

// Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle successful uploads on complete
                // ...
            }
        });


    }

    public static File createImageFile(UserItem userItem) throws ParseException {
        // Create an image file name
        String imageFileName = "JPEG_" + DateUnit.getSystemTimeAndDate() + ":"+userItem.getEmail();
        try {
            File image = File.createTempFile(imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    Environment.getExternalStorageDirectory()      /* directory */);
            return image;
        } catch (IOException e) {
            //do noting
            return null;
        }
    }



}
