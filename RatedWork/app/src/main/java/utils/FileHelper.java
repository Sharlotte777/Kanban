package utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.UUID;

public class FileHelper {
    private static StorageReference storageRef = FirebaseStorage.getInstance().getReference("attachments");

    public static void uploadFile(Context context, Uri fileUri, OnFileUploadedListener listener) {
        String extension = getFileExtension(context, fileUri);
        String fileName = UUID.randomUUID().toString() + "." + extension;
        StorageReference fileRef = storageRef.child(fileName);
        fileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    if (listener != null) listener.onSuccess(uri.toString());
                }))
                .addOnFailureListener(e -> {
                    if (listener != null) listener.onFailure(e);
                });
    }

    private static String getFileExtension(Context context, Uri uri) {
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public interface OnFileUploadedListener {
        void onSuccess(String downloadUrl);
        void onFailure(Exception e);
    }
}

