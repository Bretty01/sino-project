package com.example.sino.fragments.armor

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.sino.R
import com.example.sino.data.armor.ArmorViewModel
import com.example.sino.data.armor.StatsRelation
import com.example.sino.databinding.ArmorListRowBinding
import java.io.IOException
import java.io.InputStream

/**
 * Class: ArmorListAdapter
 * Purpose: To create the adapters that contain general armor information and display it on the recyclerView.
 */
class ArmorListAdapter : RecyclerView.Adapter<ArmorListAdapter.MyViewHolder>() {
    //Stores all the armor information to be put on the adapters.
    private var armorList = emptyList<StatsRelation>()
    //The viewModel for the fragment. Used to store the position on the adapter that was clicked.
    private lateinit var mArmorViewModel: ArmorViewModel

    /**
     * Function: onCreateViewHolder
     * Purpose: To create the adapter and return it.
     * @return Returns the adapter and binds the adapter.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding = ArmorListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding, parent.context)
    }

    /**
     * Function: onBindViewHolder
     * Purpose: Sets all the armor information on to the adapter.
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //Gets the armor information based on the adapter position
        val currentItem = armorList[position]

        //Adds an onClick listener to the specific position in the adapter
        holder.itemView.setOnClickListener {
            mArmorViewModel.armorLocation.value = currentItem.armorStats.armor_id
            holder.itemView.findNavController().navigate(R.id.armorInfo)
        }
        holder.bind(currentItem)
    }

    /**
     * Function: getItemCount
     * Purpose: Gets the amount of items in the adapter.
     * @return Total number of adapter items.
     */
    override fun getItemCount(): Int {
        return armorList.size
    }

    /**
     * Function: setData
     * Purpose: Sets all the armor items from the query to be listed on the adapter.
     * @param armor A list of all the armor items.
     */
    fun setData(armor: List<StatsRelation>) {
        this.armorList = armor
        notifyDataSetChanged()
    }

    /**
     * Function: setViewModel
     * Purpose: To grab the viewModel from the fragment to be used on the adapter.
     * @param model A reference to the viewModel.
     */
    fun setViewModel(model: ArmorViewModel)
    {
        mArmorViewModel = model
    }

    /**
     * Class: MyViewHolder
     * Purpose: To apply all the information on to the adapter.
     * @param itemBinding The view for a specific row in the adapter.
     * @param context The current state of the fragment.
     */
    class MyViewHolder(private val itemBinding: ArmorListRowBinding,
                       private val context: Context) : RecyclerView.ViewHolder(itemBinding.root) {
        /**
         * Function: bind
         * Purpose: Attaches all the proper information to an individual row in the adapter.
         * @param currentItem A list of information to be displayed.
         */
        fun bind(currentItem: StatsRelation) {
            //Set all the textViews to their respective information
            itemBinding.nameText.text = currentItem.armor[0].name
            itemBinding.pdefText.text = "PDEF:" + currentItem.armorStats.max_pdef
            itemBinding.mdefText.text = "MDEF:" + currentItem.armorStats.max_mdef
            //Set the icon to be displayed
            loadImage(currentItem.armorStats.icon, itemBinding.imageView, context)
        }

        /**
         *  Function: loadImage
         *  Purpose: Grabs an image, converts the image to bitmap, and attaches the image.
         *  @param iconLocation The name of the asset
         *  @param imageHolder The imageView to bind the image to
         *  @param context The current state of the fragment
         */
        private fun loadImage(iconLocation: String, imageHolder:ImageView, context:Context) {
            //Manage the assets in the assets directory
            val am : AssetManager = context.assets
            //Holds the image to be decoded
            val input : InputStream
            //Holds the decoded bitmap image. Defaults to null until image is converted
            var bitmap : Bitmap? = null

            try {
                //Attempt to grab the image from the directory
                input = am.open("armor/images/$iconLocation")
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