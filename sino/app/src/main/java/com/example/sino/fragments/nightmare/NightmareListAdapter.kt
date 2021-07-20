package com.example.sino.fragments.nightmare

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.sino.R
import com.example.sino.data.nightmare.NightmareRelation
import com.example.sino.data.nightmare.NightmareViewModel
import com.example.sino.databinding.NightmareListRowBinding
import com.example.sino.utilities.ImageHelper

class NightmareListAdapter : RecyclerView.Adapter<NightmareListAdapter.MyViewHolder>() {
    //Stores all the weapon information to be put on the adapters.
    private var nightmareList = emptyList<NightmareRelation>()

    //The viewModel for the fragment. Used to store the position on the adapter that was clicked.
    private lateinit var mNightmareViewModel: NightmareViewModel

    //Boolean variable to determine which information to display.
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
     * @param nightmare A list of all the weapon items.
     */
    fun setData(nightmare: List<NightmareRelation>) {
        this.nightmareList = nightmare
        notifyDataSetChanged()
    }

    /**
     * Function: setViewModel
     * Purpose: To grab the viewModel from the fragment to be used on the adapter.
     * @param model A reference to the viewModel.
     */
    fun setViewModel(model: NightmareViewModel) {
        mNightmareViewModel = model
    }

    /**
     * Function: setInfoDisplay
     * Purpose: To set the infoDisplay variable, which is used to display certain information on the adapter.
     * @param infoDisplay The value to be set.
     */
    fun setInfoDisplay(infoDisplay: Boolean) {
        this.infoDisplay = infoDisplay
    }

    /**
     * Class: MyViewHolder
     * Purpose: To apply all the information on to the adapter.
     * @param itemBinding The view for a specific row in the adapter.
     * @param context The current state of the fragment.
     */
    class MyViewHolder(
        private val itemBinding: NightmareListRowBinding, private val context: Context, private val infoDisplay: Boolean
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        //The text boxes of the values to be displayed
        private val nameText = itemBinding.txtName
        private val patkText = itemBinding.txtPatk
        private val matkText = itemBinding.txtMatk
        private val pdefText = itemBinding.txtPdef
        private val mdefText = itemBinding.txtMdef
        private val storyDescription = itemBinding.txtStoryDescription
        private val coloDescription = itemBinding.txtColoDescription

        /**
         * Function: bind
         * Purpose: Attaches all the proper information to an individual row in the adapter.
         * @param currentItem A list of information to be displayed.
         */
        fun bind(currentItem: NightmareRelation) {
            val helper = ImageHelper(context)
            //Set all the textViews to their respective information
            nameText.text = currentItem.name
            patkText.text = "PATK: " + currentItem.mlb_patk
            matkText.text = "MATK: " + currentItem.mlb_matk
            pdefText.text = "PDEF: " + currentItem.mlb_pdef
            mdefText.text = "MDEF: " + currentItem.mlb_mdef
            storyDescription.text = currentItem.story_name
            coloDescription.text = currentItem.colo_name
            //Set the icon to be displayed
            helper.loadImage(itemBinding.imgNightmareIcon, "nightmares/images/" + currentItem.stats_icon)
            helper.loadImage(itemBinding.imgStoryIcon, "misc/icons/battle_icon01.png")
            helper.loadImage(itemBinding.imgColoIcon, "misc/icons/battle_icon03.png")
            //Call hideData to determine which text is to be displayed on the screen
            hideData()
        }

        private fun hideData() {
            //If the passed in variable infoDisplay is true, stat information is visible and skill information is
            //  invisible. The inverse happens if infoDisplay is false
            if (infoDisplay) {
                patkText.visibility = View.INVISIBLE
                matkText.visibility = View.INVISIBLE
                pdefText.visibility = View.INVISIBLE
                mdefText.visibility = View.INVISIBLE
                storyDescription.visibility = View.VISIBLE
                coloDescription.visibility = View.VISIBLE
                itemBinding.imgColoIcon.visibility = View.VISIBLE
                itemBinding.imgStoryIcon.visibility = View.VISIBLE
            } else {
                patkText.visibility = View.VISIBLE
                matkText.visibility = View.VISIBLE
                pdefText.visibility = View.VISIBLE
                mdefText.visibility = View.VISIBLE
                storyDescription.visibility = View.INVISIBLE
                coloDescription.visibility = View.INVISIBLE
                itemBinding.imgColoIcon.visibility = View.INVISIBLE
                itemBinding.imgStoryIcon.visibility = View.INVISIBLE
            }
        }
    }
}