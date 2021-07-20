package com.example.sino.fragments.weapon

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.sino.R
import com.example.sino.data.weapon.WeaponStatsRelation
import com.example.sino.data.weapon.WeaponViewModel
import com.example.sino.databinding.WeaponListRowBinding
import com.example.sino.utilities.ImageHelper

class WeaponListAdapter : RecyclerView.Adapter<WeaponListAdapter.MyViewHolder>() {


    //Stores all the weapon information to be put on the adapters.
    private var weaponList = emptyList<WeaponStatsRelation>()
    //The viewModel for the fragment. Used to store the position on the adapter that was clicked.
    private lateinit var mWeaponViewModel: WeaponViewModel
    //Boolean variable to determine which information to display.
    private var infoDisplay: Boolean = false

    /**
     * Function: onCreateViewHolder
     * Purpose: To create the adapter and return it.
     * @return Returns the adapter and binds the adapter.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding = WeaponListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding, parent.context, infoDisplay)
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
     * Function: setInfoDisplay
     * Purpose: To set the infoDisplay variable, which is used to display certain information on the adapter.
     * @param infoDisplay The value to be set.
     */
    fun setInfoDisplay(infoDisplay:Boolean) {
        this.infoDisplay = infoDisplay
    }

    /**
     * Class: MyViewHolder
     * Purpose: To apply all the information on to the adapter.
     * @param itemBinding The view for a specific row in the adapter.
     * @param context The current state of the fragment.
     * @param infoDisplay Used to determine which text to be displayed.
     */
    class MyViewHolder(private val itemBinding: WeaponListRowBinding,
                       private val context: Context, private val infoDisplay: Boolean)
        : RecyclerView.ViewHolder(itemBinding.root) {
        //Set all the textViews to their respective information
        private val nameText = itemBinding.txtName
        private val patkText = itemBinding.txtPatk
        private val matkText = itemBinding.txtMatk
        private val pdefText = itemBinding.txtPdef
        private val mdefText = itemBinding.txtMdef
        private val wlStoryText = itemBinding.txtStory
        private val wlColoText = itemBinding.txtColo
        private val wlSupportText = itemBinding.txtSupport

        /**
         * Function: bind
         * Purpose: Attaches all the proper information to an individual row in the adapter.
         * @param currentItem A list of information to be displayed.
         */
        fun bind(currentItem: WeaponStatsRelation) {
            val handler = ImageHelper(context)
            //Set all the textViews to their respective information
            nameText.text = currentItem.weapon[0].name
            patkText.text = "PATK: " + currentItem.weaponStats.mlb_patk
            matkText.text = "MATK: " + currentItem.weaponStats.mlb_matk
            pdefText.text = "PDEF:" + currentItem.weaponStats.mlb_pdef
            mdefText.text = "MDEF:" + currentItem.weaponStats.mlb_mdef
            wlStoryText.text = currentItem.weapon[0].story_name
            wlColoText.text = currentItem.weapon[0].colo_name
            wlSupportText.text = currentItem.weapon[0].support_name
            //Set the icon to be displayed
            handler.loadImage(itemBinding.imgWeaponIcon,"weapons/images/" + currentItem.weaponStats.stats_icon)
            handler.loadImage(itemBinding.imgStoryIcon, "misc/icons/battle_icon01.png")
            handler.loadImage(itemBinding.imgColoIcon, "misc/icons/battle_icon03.png")
            handler.loadImage(itemBinding.imgSupportIcon, "misc/icons/battle_icon04.png")
            //Call hideData to determine which text is to be displayed on the screen
            hideData()
        }

        /**
         * Function: hideData
         * Purpose: Determines which TextViews and ImageViews are hidden and which are to be visible.
         */
        private fun hideData() {
            //If the passed in variable infoDisplay is true, stat information is visible and skill information is
            //  invisible. The inverse happens if infoDisplay is false
            if(infoDisplay) {
                patkText.visibility = View.INVISIBLE
                matkText.visibility = View.INVISIBLE
                pdefText.visibility = View.INVISIBLE
                mdefText.visibility = View.INVISIBLE
                wlStoryText.visibility = View.VISIBLE
                wlColoText.visibility = View.VISIBLE
                wlSupportText.visibility = View.VISIBLE
                itemBinding.imgColoIcon.visibility = View.VISIBLE
                itemBinding.imgStoryIcon.visibility = View.VISIBLE
                itemBinding.imgSupportIcon.visibility = View.VISIBLE
            }
            else {
                patkText.visibility = View.VISIBLE
                matkText.visibility = View.VISIBLE
                pdefText.visibility = View.VISIBLE
                mdefText.visibility = View.VISIBLE
                wlStoryText.visibility = View.INVISIBLE
                wlColoText.visibility = View.INVISIBLE
                wlSupportText.visibility = View.INVISIBLE
                itemBinding.imgColoIcon.visibility = View.INVISIBLE
                itemBinding.imgStoryIcon.visibility = View.INVISIBLE
                itemBinding.imgSupportIcon.visibility = View.INVISIBLE
            }
        }

    }



}