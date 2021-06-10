package com.example.sino.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.sino.R
import com.example.sino.data.armor.ArmorSupportSkill
import com.example.sino.data.armor.ArmorViewModel
import com.example.sino.databinding.FragmentListBinding

class ListFragment : Fragment()
{
    private lateinit var mArmorViewModel: ArmorViewModel
    private var _binding: FragmentListBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentListBinding.inflate(inflater)
        _binding = binding
        mArmorViewModel = ViewModelProvider(this).get(ArmorViewModel::class.java)
        binding.submitButton.setOnClickListener{
            insertDataToDatabase()
        }
        return binding.root
    }
    //Wind Instrument Soul
    //Increases the effectiveness of wind instrument skills.
    private fun insertDataToDatabase() {
        val name = _binding?.nameText?.text.toString()
        val description = _binding?.skillText?.text.toString()

        if(inputCheck(name, description))
        {
            val armor = ArmorSupportSkill(0, name, description)
            mArmorViewModel.addArmorSupport(armor)
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()
        }
        else
        {
            Toast.makeText(requireContext(), "Invalid addition", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(name: String, description: String): Boolean
    {
        return !(TextUtils.isEmpty((name)) && TextUtils.isEmpty(description))
    }


}