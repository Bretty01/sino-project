package com.example.sino.fragments.nightmare

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.runtime.sourceInformation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.sino.R
import com.example.sino.data.nightmare.NightmareRelation
import com.example.sino.data.nightmare.NightmareViewModel
import com.example.sino.databinding.NightmareListRowBinding
import java.io.IOException
import java.io.InputStream

class NightmareListAdapter : RecyclerView.Adapter<NightmareListAdapter.MyViewHolder>() {
    //Stores all the weapon information to be put on the adapters.
    private var nightmareList = emptyList<NightmareRelation>()
    //The viewModel for the fragment. Used to store the position on the adapter that was clicked.
    private lateinit var mNightmareViewModel: NightmareViewModel
    private var infoDisplay = false

    /**
     * Function: onCreateViewHolder
     * Purpose: To create the adapter and return it.
     * @return Returns the adapter and binds the adapter.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding = NightmareListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding, parent.context, infoDisplay)
    }

    /**
     * Function: onBindViewHolder
     * Purpose: Sets all the weapon information on to the adapter.
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //Gets the weapon information based on the adapter position
        val currentItem = nightmareList[position]

        //Adds an onClick listener to the specific position in the adapter
        holder.itemView.setOnClickListener {
            mNightmareViewModel.nightmareLocation.value = currentItem.nightmare_id
            holder.itemView.findNavController().navigate(R.id.nightmareInfoFragment)
        }
        holder.bind(currentItem)
    }

    /**
     * Function: getItemCount
     * Purpose: Gets the amount of items in the adapter.
     * @return Total number of adapter items.
     */
    override fun getItemCount(): Int {
        return nightmareList.size
    }

    /**
     * Function: setData
     * Purpose: Sets all the weapon items from the query to be listed on the adapter.
     * @param weapon A list of all the weapon items.
     */
    fun setData(weapon: List<NightmareRelation>) {
        this.nightmareList = weapon
        notifyDataSetChanged()
    }

    /**
     * Function: setViewModel
     * Purpose: To grab the viewModel from the fragment to be used on the adapter.
     * @param model A reference to the viewModel.
     */
    fun setViewModel(model: NightmareViewModel)
    {
        mNightmareViewModel = model
    }

    fun hideInfo(infoDisplay: Boolean) {
        this.infoDisplay = infoDisplay
    }

    /**
     * Class: MyViewHolder
     * Purpose: To apply all the information on to the adapter.
     * @param itemBinding The view for a specific row in the adapter.
     * @param context The current state of the fragment.
     */
    class MyViewHolder(private val itemBinding: NightmareListRowBinding,
                       private val context: Context, private val infoDisplay: Boolean
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        private val nameText = itemBinding.nameText
        private val patkText = itemBinding.patkText
        private val matkText = itemBinding.matkText
        private val pdefText = itemBinding.pdefText
        private val mdefText = itemBinding.mdefText
        private val storyDescription = itemBinding.nlStoryDescription
        private val coloDescription = itemBinding.nlColoDescription

        /**
         * Function: bind
         * Purpose: Attaches all the proper information to an individual row in the adapter.
         * @param currentItem A list of information to be displayed.
         */
        fun bind(currentItem: NightmareRelation) {
            //Set all the textViews to their respective information
            nameText.text = currentItem.name
            patkText.text = "PATK: " + currentItem.mlb_patk
            matkText.text = "MATK: " + currentItem.mlb_matk
            pdefText.text = "PDEF: " + currentItem.mlb_pdef
            mdefText.text = "MDEF: " + currentItem.mlb_mdef
            storyDescription.text = currentItem.story_name
            coloDescription.text = currentItem.colo_name
            //Set the icon to be displayed
            loadImage("nightmares/images/" + currentItem.stats_icon, itemBinding.imageView, context)
            loadImage("misc/icons/battle_icon01.png", itemBinding.nlStoryIcon, context)
            loadImage("misc/icons/battle_icon03.png", itemBinding.nlColoIcon, context)
            hideData()
        }

        private fun hideData() {
            if(infoDisplay) {
                patkText.visibility = View.INVISIBLE
                matkText.visibility = View.INVISIBLE
                pdefText.visibility = View.INVISIBLE
                mdefText.visibility = View.INVISIBLE
                storyDescription.visibility = View.VISIBLE
                coloDescription.visibility = View.VISIBLE
                itemBinding.nlColoIcon.visibility = View.VISIBLE
                itemBinding.nlStoryIcon.visibility = View.VISIBLE
            }
            else {
                patkText.visibility = View.VISIBLE
                matkText.visibility = View.VISIBLE
                pdefText.visibility = View.VISIBLE
                mdefText.visibility = View.VISIBLE
                storyDescription.visibility = View.INVISIBLE
                coloDescription.visibility = View.INVISIBLE
                itemBinding.nlColoIcon.visibility = View.INVISIBLE
                itemBinding.nlStoryIcon.visibility = View.INVISIBLE
            }
        }

        /**
         *  Function: loadImage
         *  Purpose: Grabs an image, converts the image to bitmap, and attaches the image.
         *  @param iconLocation The name of the asset
         *  @param imageHolder The imageView to bind the image to
         *  @param context The current state of the fragment
         */
        private fun loadImage(iconLocation: String, imageHolder: ImageView, context: Context) {
            //Manage the assets in the assets directory
            val am : AssetManager = context.assets
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
                Log.v("ERRORDATA", "image was not implemented: $iconLocation")
            }
            //Set the image on the view
            imageHolder.setImageBitmap(bitmap)
        }
    }



}