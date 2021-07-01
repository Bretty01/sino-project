package com.example.sino.fragments.weapon

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
import com.example.sino.data.weapon.WeaponStatsRelation
import com.example.sino.data.weapon.WeaponViewModel
import com.example.sino.databinding.WeaponListRowBinding
import java.io.IOException
import java.io.InputStream

class WeaponListAdapter : RecyclerView.Adapter<WeaponListAdapter.MyViewHolder>() {
    //Stores all the weapon information to be put on the adapters.
    private var weaponList = emptyList<WeaponStatsRelation>()
    //The viewModel for the fragment. Used to store the position on the adapter that was clicked.
    private lateinit var mWeaponViewModel: WeaponViewModel

    /**
     * Function: onCreateViewHolder
     * Purpose: To create the adapter and return it.
     * @return Returns the adapter and binds the adapter.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding = WeaponListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding, parent.context)
    }

    /**
     * Function: onBindViewHolder
     * Purpose: Sets all the weapon information on to the adapter.
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //Gets the weapon information based on the adapter position
        val currentItem = weaponList[position]

        //Adds an onClick listener to the specific position in the adapter
        holder.itemView.setOnClickListener {
            mWeaponViewModel.weaponLocation.value = currentItem.weaponStats.weapon_id
            holder.itemView.findNavController().navigate(R.id.weaponInfoFragment)
        }
        holder.bind(currentItem)
    }

    /**
     * Function: getItemCount
     * Purpose: Gets the amount of items in the adapter.
     * @return Total number of adapter items.
     */
    override fun getItemCount(): Int {
        return weaponList.size
    }

    /**
     * Function: setData
     * Purpose: Sets all the weapon items from the query to be listed on the adapter.
     * @param weapon A list of all the weapon items.
     */
    fun setData(weapon: List<WeaponStatsRelation>) {
        this.weaponList = weapon
        notifyDataSetChanged()
    }

    /**
     * Function: setViewModel
     * Purpose: To grab the viewModel from the fragment to be used on the adapter.
     * @param model A reference to the viewModel.
     */
    fun setViewModel(model: WeaponViewModel)
    {
        mWeaponViewModel = model
    }

    /**
     * Class: MyViewHolder
     * Purpose: To apply all the information on to the adapter.
     * @param itemBinding The view for a specific row in the adapter.
     * @param context The current state of the fragment.
     */
    class MyViewHolder(private val itemBinding: WeaponListRowBinding,
                       private val context: Context
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        /**
         * Function: bind
         * Purpose: Attaches all the proper information to an individual row in the adapter.
         * @param currentItem A list of information to be displayed.
         */
        fun bind(currentItem: WeaponStatsRelation) {
            //Set all the textViews to their respective information
            itemBinding.nameText.text = currentItem.weapon[0].name
            itemBinding.patkText.text = "PATK: " + currentItem.weaponStats.mlb_patk
            itemBinding.matkText.text = "MATK: " + currentItem.weaponStats.mlb_matk
            itemBinding.pdefText.text = "PDEF:" + currentItem.weaponStats.mlb_pdef
            itemBinding.mdefText.text = "MDEF:" + currentItem.weaponStats.mlb_mdef
            //Set the icon to be displayed
            loadImage(currentItem.weaponStats.stats_icon, itemBinding.imageView, context)
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
                input = am.open("weapons/images/$iconLocation")
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