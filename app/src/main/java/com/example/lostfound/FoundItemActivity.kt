package com.example.lostfound

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FoundItemActivity : AppCompatActivity() {

    var photoUri: Uri? = null

    // Camera Permission

    private val cameraPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {
                openCamera()
            } else {

                Toast.makeText(
                    this,
                    "Camera permission is required",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    // Camera

    private val cameraLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == RESULT_OK) {

                findViewById<ImageView>(R.id.imgItem)
                    .setImageURI(photoUri)

            } else {

                photoUri = null
            }
        }

    // Gallery

    private val galleryLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == RESULT_OK) {

                val uri = result.data?.data

                if (uri != null) {

                    photoUri = uri

                    try {
                        contentResolver.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    } catch (e: Exception) {
                    }

                    findViewById<ImageView>(R.id.imgItem)
                        .setImageURI(photoUri)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_found_item)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->

            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        val edtItemName =
            findViewById<EditText>(R.id.edtItemName)

        val edtDescription =
            findViewById<EditText>(R.id.edtDescription)

        val edtLocation =
            findViewById<EditText>(R.id.edtLocation)

        val edtContact =
            findViewById<EditText>(R.id.edtContact)

        val btnAddPhoto =
            findViewById<Button>(R.id.btnAddPhoto)

        val btnSubmit =
            findViewById<Button>(R.id.btnSubmitFound)

        val databaseHelper =
            DatabaseHelper(this)


        // ADD PHOTO BUTTON

        btnAddPhoto.setOnClickListener {

            val options = arrayOf(
                "Camera",
                "Gallery"
            )

            AlertDialog.Builder(this)
                .setTitle("Select Photo")
                .setItems(options) { _, which ->

                    if (which == 0) {

                        // Camera

                        if (
                            ContextCompat.checkSelfPermission(
                                this,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {

                            openCamera()

                        } else {

                            cameraPermission.launch(
                                Manifest.permission.CAMERA
                            )
                        }

                    } else {

                        // Gallery

                        openGallery()
                    }
                }
                .show()
        }


        // SUBMIT BUTTON

        btnSubmit.setOnClickListener {

            // Check photo first

            if (photoUri == null) {

                Toast.makeText(
                    this,
                    "Please add a photo of the found item",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val itemName =
                edtItemName.text.toString()

            val description =
                edtDescription.text.toString()

            val location =
                edtLocation.text.toString()

            val contact =
                edtContact.text.toString()

            val photo =
                photoUri.toString()

            val date = SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            ).format(Date())


            val result =
                databaseHelper.insertItem(
                    itemName,
                    description,
                    location,
                    contact,
                    "Found",
                    photo,
                    date
                )


            if (result) {

                Toast.makeText(
                    this,
                    "Found Item Saved Successfully",
                    Toast.LENGTH_SHORT
                ).show()

                edtItemName.text.clear()
                edtDescription.text.clear()
                edtLocation.text.clear()
                edtContact.text.clear()

                photoUri = null

                findViewById<ImageView>(R.id.imgItem)
                    .setImageResource(
                        android.R.drawable.ic_menu_camera
                    )

            } else {

                Toast.makeText(
                    this,
                    "Error Saving Item",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    // OPEN CAMERA

    private fun openCamera() {

        val values = ContentValues()

        values.put(
            MediaStore.Images.Media.DISPLAY_NAME,
            "found_item_${System.currentTimeMillis()}.jpg"
        )

        values.put(
            MediaStore.Images.Media.MIME_TYPE,
            "image/jpeg"
        )

        values.put(
            MediaStore.Images.Media.RELATIVE_PATH,
            "Pictures/LostFound"
        )

        photoUri =
            contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )

        val intent =
            Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        intent.putExtra(
            MediaStore.EXTRA_OUTPUT,
            photoUri
        )

        cameraLauncher.launch(intent)
    }


    // OPEN GALLERY

    private fun openGallery() {

        val intent =
            Intent(Intent.ACTION_OPEN_DOCUMENT)

        intent.type = "image/*"

        intent.addCategory(
            Intent.CATEGORY_OPENABLE
        )

        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        )

        galleryLauncher.launch(intent)
    }
}