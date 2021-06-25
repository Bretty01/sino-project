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

class ArmorInfo : Fragment() {
    private lateinit var mArmorViewModel : ArmorViewModel
    private var armorList = emptyList<StatsRelation>()
    private var supportList = emptyList<SkillRelation>()
    private var setList = emptyList<SetRelation>()
    private var typeList = emptyList<TypeRelation>()
    //TODO("Try to setup armorList in adapter class if possible")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var binding = FragmentArmorInfoBinding.inflate(inflater)
        mArmorViewModel = ViewModelProvider(requireActivity()).get(ArmorViewModel::class.java)
        mArmorViewModel.setArmorData()

        mArmorViewModel.readArmorSupport().observe(viewLifecycleOwner) {
            support -> supportList = support
        }

        mArmorViewModel.readArmorSet().observe(viewLifecycleOwner) {
            set -> setList = set
        }
        mArmorViewModel.readArmorType().observe(viewLifecycleOwner) {
            type -> typeList = type
        }

        mArmorViewModel.armorData.observe(viewLifecycleOwner) {
                armor -> setData(armor, binding)
        }





        // Inflate the layout for this fragment
        return binding.root
    }


    private fun setData(armor: List<StatsRelation>, binding: FragmentArmorInfoBinding) {
        armorList = armor
        displayGeneralInfo(binding)
    }


    private fun displayGeneralInfo(binding: FragmentArmorInfoBinding) {

        val imageHolders = arrayOf(binding.armorIcon1, binding.armorIcon2, binding.armorIcon3)
        binding.armorName.text = armorList[0].armor[0].name
        var i = 0
        //TODO("Set up the element table, and connect it to ArmorType. Then set up elementText, elementIcon)
        binding.armorSkillName.text = supportList[0].armorRelation.name
        binding.armorSkillDescription.text = supportList[0].armorRelation.description
        binding.armorSetName.text = setList[0].armorRelation.name
        binding.armorSetDescripton.text = setList[0].armorRelation.description
        binding.tableArmorPdef.text = "PDEF"
        binding.tableArmorMdef.text = "MDEF"
        binding.tableArmorTotal.text = "Total"
        binding.tableArmorMinLvl.text = "Lvl 1"
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
        displaySpecificInfo(binding, 0)
    }



    private fun displaySpecificInfo(binding: FragmentArmorInfoBinding, position: Int) {
        val armorStats = armorList[position].armorStats

        binding.tableArmorText.text = armorStats.rank
        binding.tableArmorMinPdef.text = armorStats.min_pdef.toString()
        binding.tableArmorMinMdef.text = armorStats.min_mdef.toString()
        binding.tableArmorMaxPdef.text = armorStats.max_pdef.toString()
        binding.tableArmorMaxMdef.text = armorStats.max_mdef.toString()
        binding.tableArmorMinTotal.text = (armorStats.min_pdef + armorStats.min_mdef).toString()
        binding.tableArmorMaxTotal.text = (armorStats.max_pdef + armorStats.max_mdef).toString()

        displayMaxLvl(armorStats.rank, binding)
    }

    private fun displayMaxLvl(rank: String, binding: FragmentArmorInfoBinding) {
        var lvl = binding.tableArmorMaxLvl

        when(rank) {
            "B" -> lvl.text = "Lvl 40"
            "A" -> lvl.text = "Lvl 50"
            "S" -> lvl.text = "Lvl 60"
            "SR" -> lvl.text = "Lvl 80"
        }
    }

    private fun displayImage(imageHolder: ImageView, iconLocation: String, context: Context?) {
        var am : AssetManager = context!!.assets
        var input : InputStream
        var bitmap : Bitmap? = null

        try {
            input = am.open(iconLocation)
            bitmap = BitmapFactory.decodeStream(input)
        }
        catch(e: IOException) {
            //TODO("Catch the exception here as well.")
            Log.v("error", "No image was implemented " + iconLocation)
        }
        imageHolder.setImageBitmap(bitmap)
    }

    class ImagePosition(position: Int) {
        val position: Int = position
    }

}