package com.paul.wallpaperapp

import android.app.Dialog
import android.app.WallpaperManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.paul.wallpaperapp.adapter.CategoriesAdapter
import com.paul.wallpaperapp.adapter.WallPaperAdapter
import com.paul.wallpaperapp.api.RetrofitInstance
import com.paul.wallpaperapp.model.CategoriesDataModel
import com.paul.wallpaperapp.model.WallPaperDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.json.JSONObject
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.widget.TextView
import android.widget.Toast
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var searchEditText :EditText
    private lateinit var searchBtn :LinearLayout
    private var wallPaperViewList:ArrayList<WallPaperDataModel> = ArrayList()
    private lateinit var mainWallPaperRecyclerView: RecyclerView
    private lateinit var wallPaperAdapter:WallPaperAdapter

    private var categoryList : ArrayList<CategoriesDataModel> = arrayListOf(
        CategoriesDataModel("Nature",(R.drawable.nature)),
        CategoriesDataModel("Love",(R.drawable.pexel)),
        CategoriesDataModel("Education",(R.drawable.education)),
        CategoriesDataModel("Tech",(R.drawable.tech)),
            CategoriesDataModel("Wildlife",(R.drawable.wildlife)),
       CategoriesDataModel("Food",(R.drawable.food))
    )
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var categoriesItemImage :RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        categoriesItemImage =findViewById(R.id.categoriesRecyclerView)
        mainWallPaperRecyclerView = findViewById(R.id.mainWallPaperRecyclerView)
        searchEditText = findViewById(R.id.searchEditText)
        searchBtn = findViewById(R.id.searchBtn)


        searchBtn.setOnClickListener {
            val query = searchEditText.text.trim().toString()
            if (query.isNotEmpty()){
                searchApiCall(query)
            }

        }
        implementCategoriesRecyclerView()
        fetchCuratedPhotos()


    }

//    private fun showCustomPopup() {
//        val dialog = Dialog(this)
//        dialog.setContentView(R.layout.custom_popup) // Use your popup layout XML file here
//        dialog.setCancelable(true)  // Set to false if you want to disable outside clicks
//
//        // Close button inside the popup
//        val closePopupButton: ImageButton = dialog.findViewById(R.id.btnClose)
//
//        closePopupButton.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        // Set up the 'Set as Home Screen' and 'Download Now' buttons if needed
//        val homeScreenButton: Button = dialog.findViewById(R.id.homeScreenPopupBtn)
//        val downloadButton: Button = dialog.findViewById(R.id.downloadPopupBtn)
//
//        // Perform actions on button clicks, e.g., downloading or setting as wallpaper
//        homeScreenButton.setOnClickListener {
//
//            // Your code here for home screen action
//        }
//        downloadButton.setOnClickListener {
//            // Your code here for download action
//        }
//
//        dialog.show() // Display the dialog
//    }


    fun searchApiCall(searchTxt:String){
        val callApiService = RetrofitInstance.api.getSearchPhotos(searchTxt)
        callApiService.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // Access the response body
                    val responseBody = response.body()
                    responseBody?.let {
                        val rawJson = it.string() // Get the raw JSON as a string
                        wallPaperItemFromJson(rawJson)
                        Log.d("###JAPAN"," $rawJson}")
                    }
                } else {
                    Log.d("###JAPAN"," ${response.code()} - ${response.message()}")
                    println("Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace() // Handle the error
            }
        })
    }
    fun fetchCuratedPhotos() {
        val call = RetrofitInstance.api.getCuratedPhotos(page = 25, perPage = 150)
        // Enqueue the call to execute asynchronously
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // Access the response body
                    val responseBody = response.body()
                    responseBody?.let {
                        val rawJson = it.string() // Get the raw JSON as a string
                        wallPaperItemFromJson(rawJson)
                        Log.d("###JAPAN"," $rawJson}")
                    }
                } else {
                    Log.d("###JAPAN"," ${response.code()} - ${response.message()}")
                    println("Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace() // Handle the error
            }
        })
    }

    private fun implementCategoriesRecyclerView(){
        categoriesItemImage.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            categoriesAdapter = CategoriesAdapter(this@MainActivity)
            adapter = categoriesAdapter
            categoriesAdapter.setCategoriesList(categoryList)

        }

    }

    private fun implementWallPaperRecyclerView(wallList: ArrayList<WallPaperDataModel>) {
        mainWallPaperRecyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity,4)
            wallPaperAdapter = WallPaperAdapter(this@MainActivity){ image ->
                showFullScreenImageDialog(image)
            }
            adapter = wallPaperAdapter
            wallPaperAdapter.setWallPaperList(wallList)
        }
    }
    private fun wallPaperItemFromJson(jsonString:String){
       /* val i: InputStream = this.assets.open("galery.json")
        val br = BufferedReader(InputStreamReader(i))
        val jsonString = br.readText()*/
        wallPaperViewList.clear()
        val jsonObject = JSONObject(jsonString)

        val photosJSONArray = jsonObject.getJSONArray("photos")

        for (len in 0 until photosJSONArray.length()){
            val wallPaperData = photosJSONArray.getJSONObject(len)
            val url :String = wallPaperData.getString("photographer")
            var srcJson:JSONObject =  wallPaperData.getJSONObject("src")
            val large :String = srcJson.getString("large")
            //Log.d("###JAPAN--------large--","${large}")
            wallPaperViewList.add(WallPaperDataModel(url,large))

        }
        //Log.d("###JAPAN","${wallPaperViewList}")
        implementWallPaperRecyclerView(wallPaperViewList)



    }

    fun showFullScreenImageDialog(imageUrl: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.activity_popup, null)
        val imageView = dialogView.findViewById<ImageView>(R.id.popUpImage)
        val closeBtn = dialogView.findViewById<ImageView>(R.id.btnClose)
        val downloadPopupBtn = dialogView.findViewById<TextView>(R.id.downloadPopupBtn)
        val homeScreenPopupBtn = dialogView.findViewById<TextView>(R.id.homeScreenPopupBtn)
        // Load image using Glide or similar library
        Glide.with(this)
            .load(imageUrl)
            .into(imageView)

        // Create and show the dialog
        val dialog = Dialog(this, android.R.style.ThemeOverlay_Material_Dark_ActionBar)
        dialog.setContentView(dialogView)
        downloadPopupBtn.setOnClickListener{
            downloadImage(this@MainActivity,imageUrl)
        }
        homeScreenPopupBtn.setOnClickListener{
            setWallpaperFromUrl(this@MainActivity,imageUrl)
        }

        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun downloadImage(context: Context, imageUrl: String) {
        val client = OkHttpClient()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = Request.Builder().url(imageUrl).build()
                val responseStore = client.newCall(request).execute()

                // Check if the response is successful
                if (responseStore.isSuccessful) {
                    val bitmap = BitmapDrawable(context.resources, responseStore.body?.byteStream())
                    showToast(context, "start downloading image.")
                    // Save the image
                    saveImageToStorage(context, bitmap)
                } else {
                    // Handle unsuccessful response
                    showToast(context, "Failed to download image.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showToast(context, "Error: ${e.message}")
            }
        }
    }

    private fun saveImageToStorage(context: Context, bitmap: BitmapDrawable?) {
        if (bitmap != null) {
            // Create a file in external storage
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "downloaded_tanmoy.png")
            val fos = FileOutputStream(file)

            // Save the bitmap to file
            bitmap.bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()

            showToast(context, "Image saved to ${file.absolutePath}")
        }
    }

    private fun showToast(context: Context, message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    fun setWallpaperFromUrl(context: Context, imageUrl: String) {
        val client = OkHttpClient()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = Request.Builder().url(imageUrl).build()
                val response = client.newCall(request).execute()

                // Check if the response is successful and body is not null
                response.body?.let { responseBody ->
                    val inputStream = responseBody.byteStream()
                    val bitmap = BitmapFactory.decodeStream(inputStream)

                    if (bitmap != null) {
                        // Set the downloaded image as wallpaper
                        setAsWallpaper(context, bitmap)
                    } else {
                        showToast(context, "Failed to decode image.")
                    }
                } ?: showToast(context, "Failed to download image: empty response.")
            } catch (e: Exception) {
                e.printStackTrace()
                showToast(context, "Error: ${e.message}")
            }
        }
    }

    private fun setAsWallpaper(context: Context, bitmap: Bitmap) {
        try {
            val wallpaperManager = WallpaperManager.getInstance(context)
            wallpaperManager.setBitmap(bitmap) // Sets the wallpaper

            showToast(context, "Wallpaper set successfully!")
        } catch (e: IOException) {
            e.printStackTrace()
            showToast(context, "Failed to set wallpaper: ${e.message}")
        }
    }


}