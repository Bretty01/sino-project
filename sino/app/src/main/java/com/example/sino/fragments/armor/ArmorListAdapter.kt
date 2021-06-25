package com.example.sino.fragments.armor


import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.sino.R
import com.example.sino.data.armor.ArmorViewModel
import com.example.sino.data.armor.StatsRelation
import com.example.sino.databinding.ListRowBinding
import com.google.android.material.internal.ContextUtils.getActivity
import java.io.IOException
import java.io.InputStream

class ArmorListAdapter: RecyclerView.Adapter<ArmorListAdapter.MyViewHolder>() {
    private var armorList = emptyList<StatsRelation>()
    private var position: Int = 0
    private lateinit var mArmorViewModel: ArmorViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding = ListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        itemBinding.constraintList.setOnClickListener {
            mArmorViewModel.armorLocation.value = armorList[position].armorStats.armor_id
            //mArmorViewModel.setArmorData()
            parent.findNavController().navigate(R.id.armorInfo)
        }

        return MyViewHolder(itemBinding, parent.context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = armorList[position]
        holder.bind(currentItem)
        this.position = position
    }

    override fun getItemCount(): Int {
        return armorList.size
    }

    fun setData(armor: List<StatsRelation>) {
        this.armorList = armor
        notifyDataSetChanged()
    }

    fun setViewModel(model: ArmorViewModel)
    {
        mArmorViewModel = model
    }

    class MyViewHolder(private val itemBinding: ListRowBinding, private val context: Context) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(currentItem: StatsRelation) {
            itemBinding.nameText.text = currentItem.armor[0].name
            itemBinding.patkText.text = "MINPDEF:" + currentItem.armorStats.min_pdef
            itemBinding.matkText.text = "MINMDEF:" + currentItem.armorStats.min_mdef
            itemBinding.pdefText.text = "MAXPDEF:" + currentItem.armorStats.max_pdef
            itemBinding.mdefText.text = "MAXMDEF:" + currentItem.armorStats.max_mdef
            loadImage(currentItem.armorStats.icon, itemBinding.imageView, context)

        }

        private fun loadImage(iconLocation: String, imageHolder:ImageView, context:Context) {
            var am : AssetManager = context.assets
            var input : InputStream
            var bitmap : Bitmap? = null

            try {
                input = am.open("armor/images/$iconLocation")
                bitmap = BitmapFactory.decodeStream(input)
            }
            catch(e: IOException) {
                TODO("add a catch exception here.")
            }
            imageHolder.setImageBitmap(bitmap)
        }
    }



}