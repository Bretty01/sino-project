package com.example.sino.fragments.nightmare

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.sino.data.nightmare.NightmareRelation
import com.example.sino.data.nightmare.NightmareViewModel
import com.example.sino.databinding.FragmentNightmareInfoBinding
import com.example.sino.utilities.ImageHelper

class NightmareInfoFragment : Fragment() {
    //The viewModel for all the weapon information
    private lateinit var mNightmareViewModel : NightmareViewModel
    //Lists for the weapon, names of story skills, names of armor set skills, and names of the armor type/position
    //  In the future I plan to combine all 4 variables with 1 query. I just suck at using Room.
    private var nightmareList = emptyList<NightmareRelation>()

    /**
     * Class: onCreateView
     * Purpose: To setup the fragment and all the underlying information on the view
     * @return The completed fragment
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        //Variable to bind all the views in the fragment
        val binding = FragmentNightmareInfoBinding.inflate(inflater)

        //Reinitialize the same viewModel from ArmorListFragment
        mNightmareViewModel = ViewModelProvider(requireActivity()).get(NightmareViewModel::class.java)
        //Read the query and set the variables for each of the tables
        mNightmareViewModel.setNightmareData().observe(viewLifecycleOwner) {
                nightmares -> setData(nightmares, binding)
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
    private fun setData(weapon: List<NightmareRelation>, binding: FragmentNightmareInfoBinding) {
        //Set the list to the variable
        nightmareList = weapon
        //Call displayGeneralInfo to start binding all the information to the fragment
        displayGeneralInfo(binding)
    }

    /**
     * Function: displayGeneralInfo
     * Purpose: To bind the information on the fragment that does not dynamically change
     * @param binding The Fragment to bind all the information to
     */
    private fun displayGeneralInfo(binding: FragmentNightmareInfoBinding) {
        val helper = ImageHelper(this.context)
        //An array of icon holders. Needed in an array to dynamically add images based on the amount of images
        //  there are.
        val imageHolders = arrayOf(binding.imgNightmareIcon1, binding.imgNightmareIcon2)
        //Generic int variable to iterate through imageHolders
        var i = 0
        val infoLocation = nightmareList[0]

        //Bind all the text to their respective textViews
        binding.txtName.text = infoLocation.name
        binding.txtStoryName.text = infoLocation.story_name
        binding.txtStoryDescription.text = infoLocation.story_description
        binding.txtColoName.text = infoLocation.colo_name
        binding.txtColoDescription.text = infoLocation.colo_description
        //Bind the images to their respective imageViews
        helper.loadImage(binding.imgNightmareType, "misc/icons/icon_013.png")
        for(weapon in nightmareList) {
            val ip = ImagePosition(i)
            helper.loadImage(imageHolders[i], "nightmares/images/" + nightmareList[i].stats_icon)
            imageHolders[i].setOnClickListener {
                displaySpecificInfo(binding, ip.position)
            }
            i++
        }
        helper.loadImage(binding.imgStoryIcon, "misc/icons/battle_icon01.png")
        helper.loadImage(binding.imgColoIcon, "misc/icons/battle_icon03.png")

        //Call displaySpecificInfo to create the default information
        displaySpecificInfo(binding, 0)
    }

    /**
     * Function: displaySpecificInfo
     * Purpose: To bind the Information that dynamically changes
     * @param binding The Fragment to bind all the information to
     * @param position The position in the armorList array that contains all the information to display
     */
    private fun displaySpecificInfo(binding: FragmentNightmareInfoBinding, position: Int) {
        //Primarily used to shorten down the amount of code due to excessive use
        val nightmareStats = nightmareList[position]

        //Bind all the text to their respective views
        binding.txtRank.text = nightmareStats.stats_rank
        binding.txtMinPdef.text = nightmareStats.min_pdef.toString()
        binding.txtMinMdef.text = nightmareStats.min_mdef.toString()
        binding.txtMinPatk.text = nightmareStats.min_patk.toString()
        binding.txtMinMatk.text = nightmareStats.min_matk.toString()
        binding.txtMaxPdef.text = nightmareStats.max_pdef.toString()
        binding.txtMaxMdef.text = nightmareStats.max_mdef.toString()
        binding.txtMaxPatk.text = nightmareStats.max_patk.toString()
        binding.txtMaxMatk.text = nightmareStats.max_matk.toString()
        binding.txtMlbPatk.text = nightmareStats.mlb_patk.toString()
        binding.txtMlbMatk.text = nightmareStats.mlb_matk.toString()
        binding.txtMlbPdef.text = nightmareStats.mlb_pdef.toString()
        binding.txtMlbMdef.text = nightmareStats.mlb_mdef.toString()

        binding.txtMinTotal.text = (nightmareStats.min_pdef + nightmareStats.min_mdef +
                nightmareStats.min_matk + nightmareStats.min_patk).toString()
        binding.txtMaxTotal.text = (nightmareStats.max_pdef + nightmareStats.max_mdef +
                nightmareStats.max_matk + nightmareStats.max_patk).toString()
        binding.txtMlbTotal.text = (nightmareStats.mlb_pdef + nightmareStats.mlb_mdef +
                nightmareStats.mlb_matk + nightmareStats.mlb_patk).toString()
        //Call displayMaxLvl to display to resolve a dynamic solution
        displayMaxLvl(nightmareStats.stats_rank, binding)
    }

    /**
     * Function: displayMaxLvl
     * Purpose: To display text based on multiple outcomes
     * @param rank The string that is used to determine what to display
     * @param binding The Fragment to bind all the information to
     */
    private fun displayMaxLvl(rank: String, binding: FragmentNightmareInfoBinding) {
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