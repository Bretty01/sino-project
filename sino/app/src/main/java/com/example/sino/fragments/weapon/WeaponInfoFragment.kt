package com.example.sino.fragments.weapon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sino.data.weapon.WeaponStatsRelation
import com.example.sino.data.weapon.WeaponViewModel
import com.example.sino.databinding.FragmentWeaponInfoBinding
import com.example.sino.utilities.ImageHelper

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
        val helper = ImageHelper(this.context)
        //An array of icon holders. Needed in an array to dynamically add images based on the amount of images
        //  there are.
        val imageHolders = arrayOf(binding.imgWeaponIcon1, binding.imgWeaponIcon2, binding.imgWeaponIcon3)
        //Generic int variable to iterate through imageHolders
        var i = 0
        var infoLocation = weaponList[0].weapon[0]

        //Bind all the text to their respective textViews
        binding.txtName.text = infoLocation.name
        binding.costNumber.text = infoLocation.cost.toString()
        binding.txtStoryName.text = infoLocation.story_name
        binding.txtStoryDescription.text = infoLocation.story_description + "\nSP: " + infoLocation.sp_cost
        binding.txtColoName.text = infoLocation.colo_name
        binding.txtColoDescripton.text = infoLocation.colo_description + "\nSP: " + infoLocation.sp_cost
        binding.txtSupportName.text = infoLocation.support_name
        binding.txtSupportDescripton.text = infoLocation.support_description
        //Bind the images to their respective imageViews
        helper.loadImage(binding.imgWeaponType, "misc/icons/" + infoLocation.type_icon)
        helper.loadImage(binding.imgElement, "misc/icons/" + infoLocation.element_icon)
        for(weapon in weaponList) {
            val ip = ImagePosition(i)
            helper.loadImage(imageHolders[i], "weapons/images/" + weaponList[i].weaponStats.stats_icon)
            imageHolders[i].setOnClickListener {
                displaySpecificInfo(binding, ip.position)
            }
            i++
        }
        helper.loadImage(binding.imgStoryIcon, "misc/icons/battle_icon01.png")
        helper.loadImage(binding.imgColoIcon, "misc/icons/battle_icon03.png")
        helper.loadImage(binding.imgSupportIcon, "misc/icons/battle_icon04.png")

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
        binding.txtRank.text = weaponStats.stats_rank
        binding.txtMinPdef.text = weaponStats.min_pdef.toString()
        binding.txtMinMdef.text = weaponStats.min_mdef.toString()
        binding.txtMinPatk.text = weaponStats.min_patk.toString()
        binding.txtMinMatk.text = weaponStats.min_matk.toString()
        binding.txtMaxPdef.text = weaponStats.max_pdef.toString()
        binding.txtMaxMdef.text = weaponStats.max_mdef.toString()
        binding.txtMaxPatk.text = weaponStats.max_patk.toString()
        binding.txtMaxMatk.text = weaponStats.max_matk.toString()
        binding.txtMlbPatk.text = weaponStats.mlb_patk.toString()
        binding.txtMlbMatk.text = weaponStats.mlb_matk.toString()
        binding.txtMlbPdef.text = weaponStats.mlb_pdef.toString()
        binding.txtMlbMdef.text = weaponStats.mlb_mdef.toString()
        binding.txtMinTotal.text = (weaponStats.min_pdef + weaponStats.min_mdef +
                weaponStats.min_matk + weaponStats.min_patk).toString()
        binding.txtMaxTotal.text = (weaponStats.max_pdef + weaponStats.max_mdef +
                weaponStats.max_matk + weaponStats.max_patk).toString()
        binding.txtMlbTotal.text = (weaponStats.mlb_pdef + weaponStats.mlb_mdef +
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
        val maxLvl = binding.txtMaxLvl
        val mlbLvl = binding.txtMlbLvl

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
     * Class: ImagePosition
     * Purpose: Holds the position in the list of armor so the proper info can be displayed
     *  when the ImageView is clicked
     * @param position The position in the array
     */
    class ImagePosition(val position: Int)
}