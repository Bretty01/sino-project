package com.example.sino.fragments.armor

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.example.sino.data.armor.*
import com.example.sino.databinding.FragmentArmorInfoBinding
import java.io.IOException
import java.io.InputStream

/**
 * Class: ArmorInfo
 * Purpose: To display information on a specific piece of armor
 */
class ArmorInfoFragment : Fragment() {
    //The viewModel for all the armor information
    private lateinit var mArmorViewModel : ArmorViewModel
    //Lists for the armor, names of story skills, names of armor set skills, and names of the armor type/position
    //  In the future I plan to combine all 4 variables with 1 query. I just suck at using Room.
    private var armorList = emptyList<StatsRelation>()
    private var supportList = emptyList<SkillRelation>()
    private var setList = emptyList<SetRelation>()
    private var typeList = emptyList<TypeRelation>()

    /**
     * Class: onCreateView
     * Purpose: To setup the fragment and all the underlying information on the view
     * @return The completed fragment
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        //Variable to bind all the views in the fragment
        val binding = FragmentArmorInfoBinding.inflate(inflater)

        //Reinitialize the same viewModel from ArmorListFragment
        mArmorViewModel = ViewModelProvider(requireActivity()).get(ArmorViewModel::class.java)
        //Read the queries and set the variables for each of the tables
        mArmorViewModel.readArmorSupport().observe(viewLifecycleOwner) {
            support -> supportList = support
        }
        mArmorViewModel.readArmorSet().observe(viewLifecycleOwner) {
            set -> setList = set
        }
        mArmorViewModel.readArmorType().observe(viewLifecycleOwner) {
            type -> typeList = type
        }
        mArmorViewModel.setArmorData().observe(viewLifecycleOwner) {
            //All variables will be set in descending order. There this one will be last one set
            //  Therefore a function setData, will be used for the rest of the functionality.
            armor -> setData(armor, binding)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    /**
     * Function: setData
     * Purpose: Sets the list of armor information.
     * @param armor The armor information to bind.
     * @param binding The Fragment to bind all the information to.
     */
    private fun setData(armor: List<StatsRelation>, binding: FragmentArmorInfoBinding) {
        //Set the list to the variable
        armorList = armor
        //Call displayGeneralInfo to start binding all the information to the fragment
        displayGeneralInfo(binding)
    }

    /**
     * Function: displayGeneralInfo
     * Purpose: To bind the information on the fragment that does not dynamically change
     * @param binding The Fragment to bind all the information to
     */
    private fun displayGeneralInfo(binding: FragmentArmorInfoBinding) {
        //An array of icon holders. Needed in an array to dynamically add images based on the amount of images
        //  there are.
        val imageHolders = arrayOf(binding.armorIcon1, binding.armorIcon2, binding.armorIcon3)
        //Generic int variable to iterate through imageHolders
        var i = 0

        //Bind all the text to their respective textViews
        binding.armorName.text = armorList[0].armor[0].name
        binding.armorSkillName.text = supportList[0].armorRelation.name
        binding.armorSkillDescription.text = supportList[0].armorRelation.description
        binding.armorSetName.text = setList[0].armorRelation.name
        binding.armorSetDescripton.text = setList[0].armorRelation.description
        binding.tableArmorPdef.text = "PDEF"
        binding.tableArmorMdef.text = "MDEF"
        binding.tableArmorTotal.text = "Total"
        binding.tableArmorMinLvl.text = "Lvl 1"
        //Bind the images to their respective imageViews
        displayImage(binding.typeIcon, "misc/icons/" + typeList[0].type.icon, this.context)
        for(armor in armorList) {
            val ip = ImagePosition(i)
            displayImage(imageHolders[i], "armor/images/" + armor.armorStats.icon, this.context)
            imageHolders[i].setOnClickListener {
                displaySpecificInfo(binding, ip.position)
            }
            i++
        }
        displayImage(binding.armorSkillIcon, "misc/icons/battle_icon02.png", this.context)
        displayImage(binding.armorSetIcon, "misc/icons/heading_icon.png", this.context)
        //Call displaySpecificInfo to create the default information
        displaySpecificInfo(binding, 0)
    }

    /**
     * Function: displaySpecificInfo
     * Purpose: To bind the Information that dynamically changes
     * @param binding The Fragment to bind all the information to
     * @param position The position in the armorList array that contains all the information to display
     */
    private fun displaySpecificInfo(binding: FragmentArmorInfoBinding, position: Int) {
        //Primarily used to shorten down the amount of code due to excessive use
        val armorStats = armorList[position].armorStats

        //Bind all the text to their respective views
        binding.tableArmorText.text = armorStats.rank
        binding.tableArmorMinPdef.text = armorStats.min_pdef.toString()
        binding.tableArmorMinMdef.text = armorStats.min_mdef.toString()
        binding.tableArmorMaxPdef.text = armorStats.max_pdef.toString()
        binding.tableArmorMaxMdef.text = armorStats.max_mdef.toString()
        binding.tableArmorMinTotal.text = (armorStats.min_pdef + armorStats.min_mdef).toString()
        binding.tableArmorMaxTotal.text = (armorStats.max_pdef + armorStats.max_mdef).toString()
        //Call displayMaxLvl to display to resolve a dynamic solution
        displayMaxLvl(armorStats.rank, binding)
    }

    /**
     * Function: displayMaxLvl
     * Purpose: To display text based on multiple outcomes
     * @param rank The string that is used to determine what to display
     * @param binding The Fragment to bind all the information to
     */
    private fun displayMaxLvl(rank: String, binding: FragmentArmorInfoBinding) {
        //Used to shorten down the amount of code to be written down
        val lvl = binding.tableArmorMaxLvl

        //Switch statement to determine what text to display
        when(rank) {
            "B" -> lvl.text = "Lvl 40"
            "A" -> lvl.text = "Lvl 50"
            "S" -> lvl.text = "Lvl 60"
            "SR" -> lvl.text = "Lvl 80"
        }
    }

    /**
     * Function: displayImage
     * Purpose: Grabs an image, converts the image to bitmap, and attaches the image.
     * @param iconLocation The location of the asset
     * @param imageHolder The imageView to bind the image to
     * @param context The current state of the fragment
     */
    private fun displayImage(imageHolder: ImageView, iconLocation: String, context: Context?) {
        //Manage the assets in the assets directory
        val am : AssetManager = context!!.assets
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

    /**
     * Class: ImagePosition
     * Purpose: Holds the position in the list of armor so the proper info can be displayed
     *  when the ImageView is clicked
     * @param position The position in the array
     */
    class ImagePosition(val position: Int)
}