package com.example.sino.fragments.armor

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sino.QueryStringBuilder
import com.example.sino.R
import com.example.sino.data.armor.ArmorViewModel
import com.example.sino.databinding.ArmorFilterPopupBinding
import com.example.sino.databinding.ArmorSortPopupBinding
import com.example.sino.databinding.FragmentArmorListBinding
import com.google.android.material.button.MaterialButtonToggleGroup

const val WRAP = LinearLayout.LayoutParams.WRAP_CONTENT

/**
 * Class: ArmorListFragment
 * Purpose: The class for a recyclerView referenced in R.layout.fragment_list.xml
 * This class initializes the adapters for the recyclerView.
 */
class ArmorListFragment : Fragment() {
    //The viewModel to be used to display armor information among multiple fragments and the room database.
    private lateinit var mArmorViewModel: ArmorViewModel
    private val adapter = ArmorListAdapter()
    private lateinit var binding : FragmentArmorListBinding
    private val queryString = QueryStringBuilder()
    private var infoDisplay = false
    /**
     * Function: onCreateView
     * Purpose: Creates all the adapters and displays them
     * @return The completed recyclerView
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //The adapter for the recyclerView.
        val adapter = ArmorListAdapter()
        //Used to bind various views to the fragment.
        binding = FragmentArmorListBinding.inflate(inflater)
        val layoutManager = LinearLayoutManager(requireContext())
        //Add the recyclerView to the layout.
        binding.recyclerView.layoutManager = layoutManager
        //Initialize the viewModel to the ArmorViewModel class.
        mArmorViewModel = ViewModelProvider(requireActivity()).get(ArmorViewModel::class.java)
        //Add the viewModel to the adapter so the adapter can use it to store information.
        //Important so that it can show the correct information on the ArmorInfo fragment.
        adapter.setViewModel(mArmorViewModel)
        //Grab the armor information to be displayed on the recyclerView.
        mArmorViewModel.readAllData.observe(viewLifecycleOwner) {
            armor -> adapter.setData(armor)
        }
        //Set the adapters to the recyclerView.
        binding.recyclerView.adapter = adapter
        binding.alFilterBtn.setOnClickListener {
            val popupWindow = PopupWindow(ArmorFilterPopupBinding.inflate(inflater).root,
                WRAP, WRAP, true)
            popupWindow.showAtLocation(binding.root, Gravity.CENTER , 0, 0)
            filterArmor(popupWindow)
        }
        binding.alSortBtn.setOnClickListener {
            val popupWindow = PopupWindow(ArmorSortPopupBinding.inflate(inflater).root,
                WRAP, WRAP, true)
            popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
            sortArmor(popupWindow)
        }
        binding.alSwapBtn.setOnClickListener {
            for(child in binding.recyclerView.children) {
                child.findViewById<TextView>(R.id.pdefText).visibility = View.INVISIBLE
                child.findViewById<TextView>(R.id.mdefText).visibility = View.INVISIBLE
            }
            infoDisplay = !infoDisplay
            createQueryData()
        }
        return binding.root
    }

    private fun filterArmor(popupWindow: PopupWindow)
    {
        val popupView = popupWindow.contentView
        val armorType = arrayOf(R.id.armorHeadBtn, R.id.armorBodyBtn, R.id.armorFeetBtn, R.id.armorHandsBtn)
        val armorElement = arrayOf(R.id.armorMeritBtn, R.id.armorFireBtn, R.id.armorWaterBtn, R.id.armorWindBtn)
        val armorWeapon = arrayOf(R.id.armorInstBtn, R.id.armorTomeBtn, R.id.armorArtifactBtn, R.id.armorStaffBtn,
            R.id.armorBladeBtn, R.id.armorHammerBtn, R.id.armorRangedBtn, R.id.armorPolearmBtn)
        val armorSet = arrayOf(R.id.armorBeastBtn, R.id.armorPlantBtn, R.id.wf14Btn, R.id.armorGhostBtn,
            R.id.armorDragonBtn, R.id.armorOrcBtn, R.id.armorWispBtn, R.id.armorFiendBtn, R.id.armorHumanBtn,
            R.id.armorOtherBtn)

        popupView.findViewById<Button>(R.id.armorFilterSubmitBtn).setOnClickListener {
            queryString.clearQuery()
            queryString.createFilterQuery(arrayOf(armorType, armorElement, armorWeapon, armorSet), popupView, "armor")
            queryString.parseRightBracket()
            createQueryData()
            popupWindow.dismiss()
        }
    }

    private fun sortArmor(popupWindow: PopupWindow) {
        val popupView = popupWindow.contentView
        popupView.findViewById<Button>(R.id.armorSortSubmitBtn).setOnClickListener {
            val orderGroup = popupView.findViewById<MaterialButtonToggleGroup>(R.id.armorOrderGroup)
            val sortGroup = popupView.findViewById<MaterialButtonToggleGroup>(R.id.armorSortGroup)
            val selectedOrder = popupView.findViewById<Button>(orderGroup.checkedButtonId)
            val selectedSort = popupView.findViewById<Button>(sortGroup.checkedButtonId)
            if(selectedOrder != null && selectedSort != null) {

                queryString.buildSort(selectedSort.tag.toString() + " "  + selectedOrder.tag.toString(),
                    "armor")
            }
            createQueryData()
            popupWindow.dismiss()
        }
    }
    private fun createQueryData() {
        if(queryString.getString() == null) {
            mArmorViewModel.readAllData.observe(viewLifecycleOwner) {
                    armor -> adapter.setData(armor)
            }
        }
        else {
            Log.v("CURRENTQUERY", queryString.getString()!!)
            mArmorViewModel.filterArmor(queryString.getString()!!).observe(viewLifecycleOwner) {
                    armor -> adapter.setData(armor)
            }
        }
        adapter.setInfoDisplay(infoDisplay)
        adapter.setViewModel(mArmorViewModel)
        binding.recyclerView.adapter = adapter
    }
}