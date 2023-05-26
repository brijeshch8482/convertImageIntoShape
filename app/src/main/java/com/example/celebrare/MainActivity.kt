package com.example.celebrare

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.celebrare.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.image_frame_dialog.*
import java.io.File
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var contentBinding: ActivityMainBinding
    private var PICK_IMAGE_GALLERY_REQUEST_CODE = 104
    private var currentPhotoPath = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        contentBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        chooseFromDeviceButton.setOnClickListener{
            openImagesDocument()

        }



    }

    @SuppressLint("ResourceType")
    private fun imageFrameDialog(uri: Uri) {

        var id = -1

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.image_frame_dialog)
        dialog.dialogImage.setImageURI(uri)

        dialog.original.setOnClickListener{

            Picasso.get().load(uri).into(dialog.dialogImage)

            id = 0
        }

        dialog.clear.setOnClickListener{

            dialog.dismiss()
        }

        dialog.frame1.setOnClickListener{

            Picasso.get()
                .load(uri)
                .transform(MaskTransformation(this, R.drawable.user_image_frame_1))
                .into(dialog.dialogImage)

            id = 1

        }
        dialog.frame2.setOnClickListener{
            Picasso.get()
                .load(uri)
                .transform(MaskTransformation(this, R.drawable.user_image_frame_2))
                .into(dialog.dialogImage)

            id = 2

        }
        dialog.frame3.setOnClickListener{

            Picasso.get()
                .load(uri)
                .transform(MaskTransformation(this, R.drawable.user_image_frame_3))
                .into(dialog.dialogImage)

            id = 3

        }
        dialog.frame4.setOnClickListener{

            Picasso.get()
                .load(uri)
                .transform(MaskTransformation(this, R.drawable.user_image_frame_4))
                .into(dialog.dialogImage)

            id = 4

        }




        dialog.dialog_button.setOnClickListener{
            if(id == 1){

                Picasso.get()
                    .load(uri)
                    .transform(MaskTransformation(this, R.drawable.user_image_frame_1))
                    .into(imageIv)

                dialog.dismiss()

            }else if(id == 2){

                Picasso.get()
                    .load(uri)
                    .transform(MaskTransformation(this, R.drawable.user_image_frame_2))
                    .into(imageIv)

                dialog.dismiss()

            }else if(id == 3){

                Picasso.get()
                    .load(uri)
                    .transform(MaskTransformation(this, R.drawable.user_image_frame_3))
                    .into(imageIv)

                dialog.dismiss()

            }else if(id == 4){

                Picasso.get()
                    .load(uri)
                    .transform(MaskTransformation(this, R.drawable.user_image_frame_4))
                    .into(imageIv)

                dialog.dismiss()

            }else if(id == 0){

                Picasso.get().load(uri).into(imageIv)
                dialog.dismiss()

            }else{

                Picasso.get().load(uri).into(imageIv)
                dialog.dismiss()
            }


        }

        dialog.show()
    }



    private fun openImagesDocument() {
        val pictureIntent = Intent(Intent.ACTION_GET_CONTENT)
        pictureIntent.type = "image/*"
        pictureIntent.addCategory(Intent.CATEGORY_OPENABLE)
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        pictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(Intent.createChooser(pictureIntent, "Select Picture"),
            PICK_IMAGE_GALLERY_REQUEST_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            val uri = UCrop.getOutput(data!!)

            if (uri != null) {
                imageFrameDialog(uri)
            }

        } else if (requestCode == PICK_IMAGE_GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val sourceUri = data.data
            val file: File = getImageFile()
            val destinationUri = Uri.fromFile(file)
            if (sourceUri != null) {
                openCropActivity(sourceUri, destinationUri)
            }
        }
    }

    @Throws(IOException::class)
    private fun getImageFile(): File {
        val imageFileName = "JPEG_" + System.currentTimeMillis() + "_"
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM
            ), "Camera"
        )
        println(storageDir.absolutePath)
        if (storageDir.exists()) println("File exists") else println("File not exists")
        val file = File.createTempFile(
            imageFileName, ".jpg", storageDir
        )
        currentPhotoPath = "file:" + file.absolutePath
        return file
    }



    @RequiresApi(Build.VERSION_CODES.S)
    private fun openCropActivity(sourceUri: Uri, destinationUri: Uri) {

        val options = UCrop.Options()
        options.setCircleDimmedLayer(true)
        options.setCropFrameColor(ContextCompat.getColor(this, R.color.teal_700))

        UCrop.of(sourceUri, destinationUri)
            .withMaxResultSize(800, 800)
            .withAspectRatio(0f, 0f)
            .start(this)
    }



    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}