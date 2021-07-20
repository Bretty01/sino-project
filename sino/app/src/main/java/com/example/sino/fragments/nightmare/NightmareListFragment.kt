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
import com.example.sino.utilities.QueryStringBuilder
import com.example.sino.R
import com.example.sino.data.nightmare.NightmareViewModel
import com.example.sino.databinding.*
import com.google.android.material.button.MaterialButtonToggleGroup

const val WRAP = LinearLayout.LayoutParams.WRAP_CONTENT
class NightmareListFragment : Fragment() {
    //The viewModel to be used to display armor information among multiple fragments and the room database.
    private lateinit var mNightmareViewModel: NightmareViewModel
    //The adapter for the recyclerView.
    private val adapter = NightmareListAdapter()
    //Used to bind various views to the fragment.
    private lateinit var binding : FragmentItemListBinding
    //A class variable to create query strings
    private val queryString = QueryStringBuilder("nightmare")
    //Boolean variable to determine which information to display.
    private var infoDisplay = false

    /**
     * Function: onCreateView
     * Purpose: Creates all the adapters and displays them
     * @return The completed recyclerView
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutManager = LinearLayoutManager(requireContext())
        //Used to bind various views to the fragment.
        binding = FragmentItemListBinding.inflate(inflater)
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
        //When the "Filter" button is pressed, display the filter window and center it in the middle of the screen
        binding.btnFilter.setOnClickListener {
            val popupWindow = PopupWindow(NightmareFilterPopupBinding.inflate(inflater).root,
                WRAP, WRAP, true)
            popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
            //Call filterNightmare to deal with the interactions on the filter window
            filterNightmare(popupWindow)
        }
        //When the "Sort" button is pressed, display the sort window and center it in the middle of the screen
        binding.btnSort.setOnClickListener {
            val popupWindow = PopupWindow(NightmareSortPopupBinding.inflate(inflater).root,
                WRAP, WRAP, true)
            popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
            //Call sortNightmare to deal with the interactions on the sort window
            sortNightmare(popupWindow)
        }
        //When the "Skills" button is pressed, turn existing information on screen invisible
        //Currently invisible information is now visible
        binding.btnSwap.setOnClickListener {
            //Turn information on screen invisible
            for(child in binding.recyclerView.children) {
                child.findViewById<TextView>(R.id.txtPatk).visibility = View.INVISIBLE
                child.findViewById<TextView>(R.id.txtMatk).visibility = View.INVISIBLE
                child.findViewById<TextView>(R.id.txtPdef).visibility = View.INVISIBLE
                child.findViewById<TextView>(R.id.txtMdef).visibility = View.INVISIBLE
            }
            //Flip the infoDisplay between true and false
            infoDisplay = !infoDisplay
            //Determines what text the button shows based on the state of infoDisplay
            if(infoDisplay) {
                binding.btnSwap.text = "Stats"
            }
            else {
                binding.btnSwap.text = "Skills"
            }
            //call createQueryData to update the current information and its visibility
            createQueryData()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    /**
     * Function: filterNightmare
     * Purpose: Setup the information on the Filter popup window so when submitted, can create a query based on the
     *  buttons selected.
     * @param popupWindow The current state of the filter popup window.
     */
    private fun filterNightmare(popupWindow: PopupWindow) {
        //Value of the current popup view
        val popupView = popupWindow.contentView
        //Array of all the nightmare skill types
        val nightmareSkill = arrayOf(R.id.btnBuff, R.id.btnDebuff, R.id.btnSp, R.id.btnIncSkill,
            R.id.btnDecSkill, R.id.btnDisadv)
        //Array of the SP cost of the nightmare skills
        val nightmareSp = arrayOf(R.id.btn0, R.id.btn200)

        //When "Submit is pressed, the information is submitted to the QueryStringBuilder class to create a query
        //The popup window is then closed after the query is created
        popupView.findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            queryString.clearQuery()
            queryString.createFilterQuery(arrayOf(nightmareSkill, nightmareSp), popupView)
            queryString.parseRightBracket()
            createQueryData()
            popupWindow.dismiss()
        }
    }

    //TODO("Implement into querystringbuilder class")
    /**
     * Function: sortNightmare
     * Purpose: Setup the information for the Sort window so when submitted, the sort portion of the query is submitted
     *  to reorganized the currently displayed information.
     * @param popupWindow The current state of the sort popup window
     */
    private fun sortNightmare(popupWindow: PopupWindow) {
        //Value of the current popup view
        val popupView = popupWindow.contentView
        //When "Submit" is pressed, the sort information is submitted to the QueryStringBuilder class to sort
        //  the existing information
        //The popup window is closed after the query is created
        popupView.findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            //Get the button groups that encloses the ASC/DESC buttons and the group for the rest of the
            //  ways to sort the information onscreen
            val orderGroup = popupView.findViewById<MaterialButtonToggleGroup>(R.id.nfOrderGroup)
            val sortGroup = popupView.findViewById<MaterialButtonToggleGroup>(R.id.nfSortGroup)
            //Find which button is selected from each button group
            val selectedOrder = popupView.findViewById<Button>(orderGroup.checkedButtonId)
            val selectedSort = popupView.findViewById<Button>(sortGroup.checkedButtonId)
            //If no option was selected for either button group, the sort is then ignored and the default options are
            //  used
            //Otherwise if buttons were selected for each button group, a query can be made and the sort can be done
            if(selectedOrder != null && selectedSort != null) {
                queryString.buildSort(selectedSort.tag.toString() + " " + selectedOrder.tag.toString())
            }
            createQueryData()
            popupWindow.dismiss()
        }
    }

    /**
     * Function: createQueryData
     * Purpose: Grab the generated query string and use it to query the database.
     */
    private fun createQueryData() {
        //If the generated query string is null, display the default information
        if(queryString.getString() == null) {
            mNightmareViewModel.readAllData.observe(viewLifecycleOwner) {
                nightmares -> adapter.setData(nightmares)
            }
        }
        //Otherwise use the generated string to create a custom query
        else {
            Log.v("CURRENTQUERY", queryString.getString()!!)
            mNightmareViewModel.filterNightmare(queryString.getString()!!).observe(viewLifecycleOwner) {
                nightmares -> adapter.setData(nightmares)
            }
        }
        //Set the infoDisplay variable so it can display the correct information when the adapter is reset.
        adapter.setInfoDisplay(infoDisplay)
        //Reset the viewmodel on the adapter
        adapter.setViewModel(mNightmareViewModel)
        //Reset the adapter back on the recyclerview
        binding.recyclerView.adapter = adapter
    }
}