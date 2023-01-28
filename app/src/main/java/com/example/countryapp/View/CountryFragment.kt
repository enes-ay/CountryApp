package com.example.countryapp.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.countryapp.R
import com.example.countryapp.ViewModel.CountryViewModel
import com.example.countryapp.databinding.FragmentCountryBinding
import com.example.countryapp.util.downloadFromUrl
import com.example.countryapp.util.placeHolderProgressBar

class CountryFragment : Fragment() {
    private lateinit var binding: FragmentCountryBinding
    private var countryId=0
    private lateinit var viewmodel_country:CountryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCountryBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tempViewModel:CountryViewModel by viewModels()
        viewmodel_country=tempViewModel

        arguments?.let {

           countryId= CountryFragmentArgs.fromBundle(it).countryId
            viewmodel_country.getSpecificDataFromRoom(countryId)
        }

        observeSpecificCountry()
    }
  private  fun observeSpecificCountry()
    {
        viewmodel_country.country.observe(viewLifecycleOwner, Observer { country->
            country?.let {
                binding.txtCountryCapital.text=country.countryCapital
                binding.txtCountryCurrency.text=country.countryCurrency
                binding.txtCountryName.text=country.countryName
                binding.txtCountryRegion.text=country.countryRegion
                binding.txtCountryLanguage.text=country.countryLanguage
                context?.let {
                    binding.imgCountryImage.downloadFromUrl(country.flagUrl.toString(),
                        placeHolderProgressBar(it)
                    )
                }

            }
        })
    }


}