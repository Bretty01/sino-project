package com.example.sino.fragments.armor

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sino.data.armor.ArmorViewModel
import com.example.sino.data.armor.StatsRelation
import com.example.sino.databinding.FragmentListBinding

class ArmorListFragment : Fragment()
{
    private lateinit var mArmorViewModel: ArmorViewModel
    var position = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val adapter = ArmorListAdapter()
        val binding = FragmentListBinding.inflate(inflater)
        var layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        mArmorViewModel = ViewModelProvider(requireActivity()).get(ArmorViewModel::class.java)
        adapter.setViewModel(mArmorViewModel)
        mArmorViewModel.readAllData.observe(viewLifecycleOwner){
            armor -> adapter.setData(armor)
        }
        binding.recyclerView.adapter = adapter
        return binding.root
    }

}