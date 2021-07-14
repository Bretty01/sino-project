package com.example.sino.fragments.nightmare

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
import com.example.sino.data.nightmare.NightmareViewModel
import com.example.sino.databinding.FragmentNightmareListBinding
import com.example.sino.databinding.NightmareFilterPopupBinding
import com.example.sino.databinding.NightmareSortPopupBinding
import com.example.sino.fragments.armor.ArmorListAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup

const val WRAP = LinearLayout.LayoutParams.WRAP_CONTENT
class NightmareListFragment : Fragment() {
    //The viewModel to be used to display armor information among multiple fragments and the room database.
    private lateinit var mNightmareViewModel: NightmareViewModel
    private val adapter = NightmareListAdapter()
    private lateinit var binding : FragmentNightmareListBinding
    private val queryString = QueryStringBuilder()
    private var infoDisplay = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutManager = LinearLayoutManager(requireContext())
        //Used to bind various views to the fragment.
        binding = FragmentNightmareListBinding.inflate(inflater)
        //Add the recyclerView to the layout.
        binding.recyclerView.layoutManager = layoutManager
        //Initialize the viewModel to the ArmorViewModel class.
        mNightmareViewModel = ViewModelProvider(requireActivity()).get(NightmareViewModel::class.java)
        //Add the viewModel to the adapter so the adapter can use it to store information.
        //Important so that it can show the correct information on the ArmorInfo fragment.
        adapter.setViewModel(mNightmareViewModel)
        //Grab the armor information to be displayed on the recyclerView.
        mNightmareViewModel.readAllData.observe(viewLifecycleOwner) {
                nightmares -> adapter.setData(nightmares)
        }
        //Set the adapters to the recyclerView.
        binding.recyclerView.adapter = adapter
        binding.nlFilterBtn.setOnClickListener {
            val popupWindow = PopupWindow(NightmareFilterPopupBinding.inflate(inflater).root,
                WRAP, WRAP, true)
            popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
            filterNightmare(popupWindow)
        }
        binding.nlSortBtn.setOnClickListener {
            val popupWindow = PopupWindow(NightmareSortPopupBinding.inflate(inflater).root,
                WRAP, WRAP, true)
            popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
            sortNightmare(popupWindow)
        }
        binding.nlSwapBtn.setOnClickListener {
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

    private fun filterNightmare(popupWindow: PopupWindow) {
        val popupView = popupWindow.contentView
        val nightmareSkill = arrayOf(R.id.nfBuffBtn, R.id.nfDebuffBtn, R.id.nfSpBtn, R.id.nfIncskillBtn,
            R.id.nfDecskillBtn, R.id.nfDisadvBtn)
        val nightmareSp = arrayOf(R.id.nf0spBtn, R.id.nf200spBtn)

        popupView.findViewById<Button>(R.id.nfSubmitBtn).setOnClickListener {
            queryString.clearQuery()
            queryString.createFilterQuery(arrayOf(nightmareSkill, nightmareSp), popupView, "nightmare")
            queryString.parseRightBracket()
            createQueryData()
            popupWindow.dismiss()
        }
    }

    //TODO("Implement into querystringbuilder class")
    private fun sortNightmare(popupWindow: PopupWindow) {
        val popupView = popupWindow.contentView
        popupView.findViewById<Button>(R.id.nsSubmitBtn).setOnClickListener {
            val orderGroup = popupView.findViewById<MaterialButtonToggleGroup>(R.id.nfOrderGroup)
            val sortGroup = popupView.findViewById<MaterialButtonToggleGroup>(R.id.nfSortGroup)
            val selectedOrder = popupView.findViewById<Button>(orderGroup.checkedButtonId)
            val selectedSort = popupView.findViewById<Button>(sortGroup.checkedButtonId)
            if(selectedOrder != null && selectedSort != null) {
                queryString.buildSort(selectedSort.tag.toString() + " " + selectedOrder.tag.toString(),
                    "nightmare")
            }
            createQueryData()
            popupWindow.dismiss()
        }
    }

    private fun createQueryData() {
        if(queryString.getString() == null) {
            mNightmareViewModel.readAllData.observe(viewLifecycleOwner) {
                nightmares -> adapter.setData(nightmares)
            }
        }
        else {
            Log.v("CURRENTQUERY", queryString.getString()!!)
            mNightmareViewModel.filterNightmare(queryString.getString()!!).observe(viewLifecycleOwner) {
                nightmares -> adapter.setData(nightmares)
            }
        }
        adapter.hideInfo(infoDisplay)
        adapter.setViewModel(mNightmareViewModel)
        binding.recyclerView.adapter = adapter
    }
}