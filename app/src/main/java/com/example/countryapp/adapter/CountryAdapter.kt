package com.example.countryapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.countryapp.R
import com.example.countryapp.View.FeedFragmentDirections
import com.example.countryapp.model.Country
import com.example.countryapp.databinding.CountryRowBinding
import kotlinx.android.synthetic.main.country_row.view.*

class CountryAdapter(val countryArray: ArrayList<Country>) :
    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>(),CountryClickListener {
    class CountryViewHolder( var binding: CountryRowBinding) : ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {

        val inflater=LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<CountryRowBinding>(inflater,R.layout.country_row,parent,false)
        return CountryViewHolder(view)
    }


    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
      //  holder.binding.txtCountryName.text = countryArray.get(position).countryName
      //  holder.binding.txtCountryRegion.text = countryArray.get(position).countryRegio
        holder.binding.country=countryArray.get(position)
        holder.binding.listener=this

//        holder.itemView.setOnClickListener {
//            Navigation.findNavController(it).navigate(FeedFragmentDirections.actionFeedFragmentToCountryFragment(countryArray.get(position).uuid))
//        }
//        holder.binding.imgCountryFlag.downloadFromUrl(countryArray.get(position).flagUrl.toString(),
//            placeHolderProgressBar(holder.itemView.context)
//        )
    }

    override fun getItemCount(): Int {
        return countryArray.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCountryList(CountryLİst: List<Country>) {
        countryArray.clear()
        countryArray.addAll(CountryLİst)
        notifyDataSetChanged()
    }

    override fun onCountryClick(v: View) {
        var uuid=v.txtUuid.text.toString().toInt()

        Navigation.findNavController(v).navigate(FeedFragmentDirections.actionFeedFragmentToCountryFragment(uuid))
    }
}


