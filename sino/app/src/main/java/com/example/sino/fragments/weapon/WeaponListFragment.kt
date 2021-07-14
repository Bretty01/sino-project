package com.example.sino.fragments.weapon

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sino.QueryStringBuilder
import com.example.sino.R
import com.example.sino.data.weapon.WeaponViewModel
import com.example.sino.databinding.*
import com.google.android.material.button.MaterialButtonToggleGroup
import org.w3c.dom.Text

const val WRAP = LinearLayout.LayoutParams.WRAP_CONTENT
class WeaponListFragment : Fragment() {
    //The viewModel to be used to display armor information among multiple fragments and the room database.
    private lateinit var mWeaponViewModel: WeaponViewModel
    //The adapter for the recyclerView.
    private val adapter = WeaponListAdapter()
    //Used to bind various views to the fragment.
    private lateinit var binding : FragmentWeaponListBinding
    private val queryString = QueryStringBuilder()
    private var infoDisplay = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutManager = LinearLayoutManager(requireContext())
        binding = FragmentWeaponListBinding.inflate(inflater)
        //Add the recyclerView to the layout.
        binding.recyclerView.layoutManager = layoutManager
        //Initialize the viewModel to the ArmorViewModel class.
        mWeaponViewModel = ViewModelProvider(requireActivity()).get(WeaponViewModel::class.java)
        //Add the viewModel to the adapter so the adapter can use it to store information.
        //Important so that it can show the correct information on the ArmorInfo fragment.
        adapter.setViewModel(mWeaponViewModel)
        //Grab the armor information to be displayed on the recyclerView.
        mWeaponViewModel.readAllData.observe(viewLifecycleOwner) {
                weapons -> adapter.setData(weapons)
        }
        //Set the adapters to the recyclerView.
        binding.recyclerView.adapter = adapter
        binding.wlFilterBtn.setOnClickListener {
            val popupWindow = PopupWindow(WeaponFilterPopupBinding.inflate(inflater).root,
                WRAP, WRAP, true)
            popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
            filterWeapon(popupWindow)
        }
        binding.wlSortBtn.setOnClickListener {
            val popupWindow = PopupWindow(WeaponSortPopupBinding.inflate(inflater).root,
                WRAP, WRAP, true)
            popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
            sortWeapon(popupWindow)
        }
        binding.wlSwapBtn.setOnClickListener {
            for(child in binding.recyclerView.children) {
                child.findViewById<TextView>(R.id.patkText).visibility = View.INVISIBLE
                child.findViewById<TextView>(R.id.matkText).visibility = View.INVISIBLE
                child.findViewById<TextView>(R.id.pdefText).visibility = View.INVISIBLE
                child.findViewById<TextView>(R.id.mdefText).visibility = View.INVISIBLE
            }
            infoDisplay = !infoDisplay
            createQueryData()
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun filterWeapon(popupWindow: PopupWindow) {
        val popupView = popupWindow.contentView
        val weaponElement = arrayOf(R.id.wfFireBtn, R.id.wfWaterBtn, R.id.wfWindBtn)
        val weaponType = arrayOf(R.id.wfInstBtn, R.id.wfTomeBtn, R.id.wfArtifactBtn, R.id.wfStaffBtn, R.id.wfBladeBtn,
            R.id.wfHammerBtn, R.id.wfRangedBtn, R.id.wfPolearmBtn)
        val weaponCost = arrayOf(R.id.wf12Btn, R.id.wf13Btn, R.id.wf14Btn, R.id.wf15Btn, R.id.wf16Btn, R.id.wf17Btn,
            R.id.wf18Btn, R.id.wf19Btn, R.id.wf20Btn)

        popupView.findViewById<Button>(R.id.wfSubmitBtn).setOnClickListener{
            queryString.clearQuery()
            queryString.createFilterQuery(arrayOf(weaponElement, weaponType, weaponCost), popupView, "weapon")
            queryString.parseRightBracket()
            createQueryData()
            popupWindow.dismiss()
        }
    }

    private fun sortWeapon(popupWindow: PopupWindow) {
        val popupView = popupWindow.contentView
        popupView.findViewById<Button>(R.id.wsSubmitBtn).setOnClickListener {
            val orderGroup = popupView.findViewById<MaterialButtonToggleGroup>(R.id.wfOrderGroup)
            val sortGroup = popupView.findViewById<MaterialButtonToggleGroup>(R.id.wfSortGroup)
            val selectedOrder = popupView.findViewById<Button>(orderGroup.checkedButtonId)
            val selectedSort = popupView.findViewById<Button>(sortGroup.checkedButtonId)
            if(selectedOrder != null && selectedSort != null) {
                queryString.buildSort(selectedSort.tag.toString() + " " + selectedOrder.tag.toString(),
                    "weapon")
            }
            createQueryData()
            popupWindow.dismiss()
        }
    }

    private fun createQueryData() {
        if(queryString.getString() == null) {
            mWeaponViewModel.readAllData.observe(viewLifecycleOwner) {
                weapons -> adapter.setData(weapons)
            }
        }
        else {
            Log.v("CURRENTQUERY", queryString.getString()!!)
            mWeaponViewModel.filterWeapon(queryString.getString()!!).observe(viewLifecycleOwner) {
                weapons -> adapter.setData(weapons)
            }
        }
        adapter.hideInfo(infoDisplay)
        adapter.setViewModel(mWeaponViewModel)
        binding.recyclerView.adapter = adapter
    }

}