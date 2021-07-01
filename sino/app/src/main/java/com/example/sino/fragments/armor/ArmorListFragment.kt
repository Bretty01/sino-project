package com.example.sino.fragments.armor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sino.data.armor.ArmorViewModel
import com.example.sino.databinding.FragmentArmorListBinding

/**
 * Class: ArmorListFragment
 * Purpose: The class for a recyclerView referenced in R.layout.fragment_list.xml
 * This class initializes the adapters for the recyclerView.
 */
class ArmorListFragment : Fragment() {
    //The viewModel to be used to display armor information among multiple fragments and the room database.
    private lateinit var mArmorViewModel: ArmorViewModel

    /**
     * Function: onCreateView
     * Purpose: Creates all the adapters and displays them
     * @return The completed recyclerView
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //The adapter for the recyclerView.
        val adapter = ArmorListAdapter()
        //Used to bind various views to the fragment.
        val binding = FragmentArmorListBinding.inflate(inflater)
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
        return binding.root
    }

}