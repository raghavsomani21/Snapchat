package com.raghavsomani.snapchat

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import com.google.firebase.storage.UploadTask
import com.google.android.gms.tasks.OnSuccessListener
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class CreateSnapActivity : AppCompatActivity() {

    var snapImageView:ImageView? = null;
    var messageEditText:EditText? = null;
    val imageName = UUID.randomUUID().toString()+".jpg";

    fun nextClicked (view:View){
        // Get the data from an ImageView as bytes
        snapImageView?.isDrawingCacheEnabled = true
        snapImageView?.buildDrawingCache()
        val bitmap = (snapImageView?.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = FirebaseStorage.getInstance().getReference().child("images").child(imageName).putBytes(data)
        uploadTask.addOnFailureListener(OnFailureListener {
            // Handle unsuccessful uploads
            Log.i("Upload:","failed");
        }).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> {taskSnapshot ->
            //taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            // ...
                    Log.i("Upload:","Successful");
                    Toast.makeText(this, "Upload Successful", Toast.LENGTH_LONG).show();
        })
    }

    fun chooseImageClicked(view: View){
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else {
            uploadPhoto()
        }
    }

    fun uploadPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val selectedImage = data!!.data

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                snapImageView?.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_snap)

            snapImageView = findViewById(R.id.snapImageView)
            messageEditText = findViewById(R.id.messageEditText)

    }
}
