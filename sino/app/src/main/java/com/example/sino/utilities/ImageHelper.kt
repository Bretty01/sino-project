package com.example.sino.utilities

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import java.io.IOException
import java.io.InputStream

/**
 * Class: ImageHelper
 * Purpose: To handle all instances of grabbing images
 * @param context The current state of the fragment
 */
class ImageHelper(private val context: Context?){
    //Manages the assets in the assets directory
    val am : AssetManager = context!!.assets
    /**
     * Function: displayImage
     * Purpose: Grabs an image, converts the image to bitmap, and attaches the image.
     * @param iconLocation The location of the asset
     * @param imageHolder The imageView to bind the image to
     */
    fun loadImage(imageHolder: ImageView, iconLocation: String) {
        //Holds the image to be decoded
        val input : InputStream
        //Holds the decoded bitmap image. Defaults to null until image is converted
        var bitmap : Bitmap? = null

        try {
            //Attempt to grab the image from the directory
            input = am.open(iconLocation)
            //Convert the image if the image is successfully grabbed
            bitmap = BitmapFactory.decodeStream(input)
        }
        catch(e: IOException) {
            //Log the icon name if it didn't properly implement
            Log.v("error", "No image was implemented $iconLocation")
        }
        //Set the image on the view
        imageHolder.setImageBitmap(bitmap)
    }

}