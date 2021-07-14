package com.example.sino.fragments.weapon

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sino.data.weapon.WeaponStatsRelation
import com.example.sino.data.weapon.WeaponViewModel
import com.example.sino.databinding.FragmentWeaponInfoBinding
import java.io.IOException
import java.io.InputStream

class WeaponInfoFragment : Fragment() {
    //The viewModel for all the weapon information
    private lateinit var mWeaponViewModel : WeaponViewModel
    //Lists for the weapon, names of story skills, names of armor set skills, and names of the armor type/position
    //  In the future I plan to combine all 4 variables with 1 query. I just suck at using Room.
    private var weaponList = emptyList<WeaponStatsRelation>()

    /**
     * Class: onCreateView
     * Purpose: To setup the fragment and all the underlying information on the view
     * @return The completed fragment
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        //Variable to bind all the views in the fragment
        val binding = FragmentWeaponInfoBinding.inflate(inflater)

        //Reinitialize the same viewModel from ArmorListFragment
        mWeaponViewModel = ViewModelProvider(requireActivity()).get(WeaponViewModel::class.java)
        //Read the query and set the variables for each of the tables
        mWeaponViewModel.setWeaponData().observe(viewLifecycleOwner) {
                weapons -> setData(weapons, binding)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    /**
     * Function: setData
     * Purpose: Sets the list of armor information.
     * @param weapon The weapon information to bind.
     * @param binding The Fragment to bind all the information to.
     */
    private fun setData(weapon: List<WeaponStatsRelation>, binding: FragmentWeaponInfoBinding) {
        //Set the list to the variable
        weaponList = weapon
        //Call displayGeneralInfo to start binding all the information to the fragment
        displayGeneralInfo(binding)
    }

    /**
     * Function: displayGeneralInfo
     * Purpose: To bind the information on the fragment that does not dynamically change
     * @param binding The Fragment to bind all the information to
     */
    private fun displayGeneralInfo(binding: FragmentWeaponInfoBinding) {
        //An array of icon holders. Needed in an array to dynamically add images based on the amount of images
        //  there are.
        val imageHolders = arrayOf(binding.weaponIcon1, binding.weaponIcon2, binding.weaponIcon3)
        //Generic int variable to iterate through imageHolders
        var i = 0
        var infoLocation = weaponList[0].weapon[0]

        //Bind all the text to their respective textViews
        binding.weaponName.text = infoLocation.name
        binding.costNumber.text = infoLocation.cost.toString()
        binding.weaponStoryName.text = infoLocation.story_name
        binding.weaponStoryDescription.text = infoLocation.story_description + "\nSP: " + infoLocation.sp_cost
        binding.weaponColoName.text = infoLocation.colo_name
        binding.weaponColoDescripton.text = infoLocation.colo_description + "\nSP: " + infoLocation.sp_cost
        binding.weaponSupportName.text = infoLocation.support_name
        binding.weaponSupportDescripton.text = infoLocation.support_description
        binding.tableWeaponPdef.text = "PDEF"
        binding.tableWeaponMdef.text = "MDEF"
        binding.tableWeaponTotal.text = "Total"
        binding.tableWeaponMinLvl.text = "Lvl 1"
        //Bind the images to their respective imageViews
        displayImage(binding.typeIcon, "misc/icons/" + infoLocation.type_icon, this.context)
        displayImage(binding.elementIcon, "misc/icons/" + infoLocation.element_icon, this.context)
        for(weapon in weaponList) {
            val ip = ImagePosition(i)
            displayImage(imageHolders[i], "weapons/images/" +
                    weaponList[i].weaponStats.stats_icon, this.context)
            imageHolders[i].setOnClickListener {
                displaySpecificInfo(binding, ip.position)
            }
            i++
        }

        displayImage(binding.weaponStoryIcon, "misc/icons/battle_icon01.png", this.context)
        displayImage(binding.weaponColoIcon, "misc/icons/battle_icon03.png", this.context)
        displayImage(binding.weaponSupportIcon, "misc/icons/battle_icon04.png", this.context)

        //Call displaySpecificInfo to create the default information
        displaySpecificInfo(binding, 0)
    }

    /**
     * Function: displaySpecificInfo
     * Purpose: To bind the Information that dynamically changes
     * @param binding The Fragment to bind all the information to
     * @param position The position in the armorList array that contains all the information to display
     */
    private fun displaySpecificInfo(binding: FragmentWeaponInfoBinding, position: Int) {
        //Primarily used to shorten down the amount of code due to excessive use
        val weaponStats = weaponList[position].weaponStats

        //Bind all the text to their respective views
        binding.tableWeaponText.text = weaponStats.stats_rank
        binding.tableWeaponMinPdef.text = weaponStats.min_pdef.toString()
        binding.tableWeaponMinMdef.text = weaponStats.min_mdef.toString()
        binding.tableWeaponMinPatk.text = weaponStats.min_patk.toString()
        binding.tableWeaponMinMatk.text = weaponStats.min_matk.toString()
        binding.tableWeaponMaxPdef.text = weaponStats.max_pdef.toString()
        binding.tableWeaponMaxMdef.text = weaponStats.max_mdef.toString()
        binding.tableWeaponMaxPAtk.text = weaponStats.max_patk.toString()
        binding.tableWeaponMaxMatk.text = weaponStats.max_matk.toString()
        binding.tableWeaponMlbPatk.text = weaponStats.mlb_patk.toString()
        binding.tableWeaponMlbMatk.text = weaponStats.mlb_matk.toString()
        binding.tableWeaponMlbPdef.text = weaponStats.mlb_pdef.toString()
        binding.tableWeaponMlbMdef.text = weaponStats.mlb_mdef.toString()

        binding.tableWeaponMinTotal.text = (weaponStats.min_pdef + weaponStats.min_mdef +
                weaponStats.min_matk + weaponStats.min_patk).toString()
        binding.tableWeaponMaxTotal.text = (weaponStats.max_pdef + weaponStats.max_mdef +
                weaponStats.max_matk + weaponStats.max_patk).toString()
        binding.tableWeaponMlbTotal.text = (weaponStats.mlb_pdef + weaponStats.mlb_mdef +
                weaponStats.mlb_matk + weaponStats.mlb_patk).toString()
        //Call displayMaxLvl to display to resolve a dynamic solution
        displayMaxLvl(weaponStats.stats_rank, binding)
    }

    /**
     * Function: displayMaxLvl
     * Purpose: To display text based on multiple outcomes
     * @param rank The string that is used to determine what to display
     * @param binding The Fragment to bind all the information to
     */
    private fun displayMaxLvl(rank: String, binding: FragmentWeaponInfoBinding) {
        //Used to shorten down the amount of code to be written down
        val maxLvl = binding.tableWeaponMaxLvl
        val mlbLvl = binding.tableWeaponMlbLvl

        //Switch statement to determine what text to display
        when(rank) {
            "A" -> {
                maxLvl.text = "Lvl 40"
                mlbLvl.text = "Lvl 60"
            }
            "S" -> {
                maxLvl.text = "Lvl 50"
                mlbLvl.text = "Lvl 70"
                }
            "SR" -> {
                maxLvl.text = "Lvl 60"
                mlbLvl.text = "Lvl 80"
            }

            "L" -> {
                maxLvl.text = "Lvl 80"
                mlbLvl.text = "Lvl 120"
            }
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