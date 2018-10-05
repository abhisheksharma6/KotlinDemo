package com.vs.kotlindemo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*




class MainActivity : AppCompatActivity(), View.OnClickListener {
    val CAMERA_REQUEST_CODE = 0
    val PICK_IMAGE = 111
    lateinit var imageFilePath: String

    // private var listNotes = ArrayList<Note>()

    /*
    var array = arrayOf("Melbourne", "Vienna", "Vancouver", "Toronto", "Calgary", "Adelaide", "Perth", "Auckland", "Hamburg",
            "Munich", "New York", "Sydney", "Paris", "London", "Bangkok")
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //In Kotlin you can directly use the name of id of views to perform tasks just like I did
        //cameraButton is the id of open Camera Button
        cameraButton.setOnClickListener(this)
        //openGallery is the id of open Gallery Button
        openGallery.setOnClickListener(this)
        // we don't implement any functioning for this button in this type of situation
        // the else statement run just like default statement in switch in java
        checkingDefaultCase.setOnClickListener(this)


       /*
        addDataToList()
        val lvNotes: ListView = findViewById(R.id.lvNotes)
        val notesAdapter = NotesAdapter(this, listNotes)
        lvNotes.adapter = notesAdapter
        lvNotes.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
             Toast.makeText(this, "Click on " + listNotes[i].title, Toast.LENGTH_SHORT).show()
        }
        */
  /*
   val adapter = ArrayAdapter(this, R.layout.listview_item, array)
        val listView:ListView = findViewById(R.id.listview_1)
        listView.adapter = adapter

        listView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                //value of item that is clicked
                val itemValue = listView.getItemAtPosition(p2) as String

                //Toast the values
                Toast.makeText(applicationContext, "Position :$p2\nItemValue :$itemValue", Toast.LENGTH_SHORT).show()
            }

        }
*/
        //getting recyclerview from xml
        //val recyclerView = findViewById(R.id.recyclerView) as RecyclerView

        //adding a layoutmanager
        //recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        //creating an arraylist to store users using the data class user
        //val users = ArrayList<User>()

        //adding some dummy data to the list
        /*users.add(User("Abhishek", "Pathankot Punjab"))
        users.add(User("Yogesh", "Delhi"))
        users.add(User("Pankaj", "Dasuya Punjab"))
        users.add(User("Alok", "Ludhiana Punjab"))
*/
        //creating our adapter
  //      val adapter = CustomAdapter(users)

        //now adding the adapter to recyclerview
    //    recyclerView.adapter = adapter


       /*
       val viewPager = findViewById<ViewPager>(R.id.viewPager)
        if (viewPager != null){
            val adapter = ViewPagerAdapter(supportFragmentManager)
            viewPager.adapter = adapter
        }
        */

    }

    private fun openCameraApp(){
        try {
            val imageFile = createImageFile()
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(callCameraIntent.resolveActivity(packageManager) != null) {
                val authorities = packageName + ".fileprovider"
                val imageUri = FileProvider.getUriForFile(this, authorities, imageFile)
                callCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
            }
        } catch (e : IOException) {
            Toast.makeText(this, "Could not create file!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGalleryApp(){
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"), PICK_IMAGE)
        //val intent = Intent(this@MainActivity, SecondActivity::class.java)
        //startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        when(requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    photoImageView.setImageBitmap(setScaledBitmap())
                }
            }
            PICK_IMAGE -> {
                if(resultCode == Activity.RESULT_OK)
                    if (intent != null) {
                        manageImageFromUri(intent.data)
                    }
            }
            else -> {
                Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_SHORT).show()
            }
        }

    }

     /*
     fun addDataToList(){
          listNotes.add(Note(1,"Java","Java is Programming Language"))
          listNotes.add(Note(2,"PHP","PHP is Scripting Language"))
          listNotes.add(Note(3, "Kotlin","Kotlin is the latest Programming Language"))
          listNotes.add(Note(4,"Android Studio","Android studio is the IDE used for Android development"))
       }
      */


    private fun getRealPathFromURI(contentURI: Uri): String {
        val result: String
        val cursor = contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }


    private fun manageImageFromUri(imageUri: Uri) {
        var bitmap: Bitmap? = null

        try {
            imageFilePath = getRealPathFromURI(imageUri)
            bitmap = MediaStore.Images.Media.getBitmap(
                    this.contentResolver, imageUri)

        } catch (e: Exception) {
            // Manage exception ...
            Log.i("Exception", e.printStackTrace().toString())
        }

        if (bitmap != null) {
            // Here you can use bitmap in your application ...
            photoImageView.setImageBitmap(bitmap)
        }
    }


    @Throws(IOException::class)
    fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName: String = "Picture_" + timeStamp + "_"
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if(!storageDir.exists())
            storageDir.mkdirs()
        val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
        imageFilePath = imageFile.absolutePath
        return imageFile
    }


    fun setScaledBitmap(): Bitmap {
        val imageViewWidth = photoImageView.width
        val imageViewHeight = photoImageView.height

        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageFilePath, bmOptions)
        val bitmapWidth = bmOptions.outWidth
        val bitmapHeight = bmOptions.outHeight

        val scaleFactor = Math.min(bitmapWidth/imageViewWidth, bitmapHeight/imageViewHeight)

        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor

        return BitmapFactory.decodeFile(imageFilePath, bmOptions)
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.cameraButton -> {
                openCameraApp()
            }

            R.id.openGallery -> {
                openGalleryApp()
            }
            // this else is like a default case in switch statement in java
            else -> {
                Toast.makeText(this,"functioning is not implemented", Toast.LENGTH_SHORT).show()
            }

        }
    }

}


