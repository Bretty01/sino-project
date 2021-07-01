package com.example.sino.fragments.nightmare

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sino.data.nightmare.NightmareViewModel
import com.example.sino.databinding.FragmentNightmareListBinding


class NightmareListFragment : Fragment() {
    //The viewModel to be used to display armor information among multiple fragments and the room database.
    private lateinit var mNightmareViewModel: NightmareViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //The adapter for the recyclerView.
        val adapter = NightmareListAdapter()
        //Used to bind various views to the fragment.
        val binding = FragmentNightmareListBinding.inflate(inflater)
        val layoutManager = LinearLayoutManager(requireContext())

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


        // Inflate the layout for this fragment
        return binding.root
    }
}