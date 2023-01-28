package com.example.countryapp.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.countryapp.R
import com.example.countryapp.ViewModel.FeedViewModel
import com.example.countryapp.adapter.CountryAdapter
import com.example.countryapp.databinding.FragmentFeedBinding
import kotlinx.coroutines.flow.combine


class FeedFragment : Fragment() {
    private lateinit var viewmodel_feed: FeedViewModel
    private lateinit var binding: FragmentFeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFeedBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tempViewModel: FeedViewModel by viewModels()
        viewmodel_feed = tempViewModel

        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        observeLiveData()

        binding.swipeRefreshLayout.setOnRefreshListener {

            viewmodel_feed.getDataFromAPI()

            with(binding) {
                countryLoading.visibility = View.VISIBLE
                txtErrorMessage.visibility = View.GONE
                swipeRefreshLayout.isRefreshing = false
            }

        }

    }
    fun observeLiveData() {

        viewmodel_feed.refreshData()

        viewmodel_feed.countries.observe(viewLifecycleOwner, Observer { countries ->
            countries?.let {
                val adapter=CountryAdapter(ArrayList(countries))
                binding.recyclerView.visibility = View.VISIBLE
                binding.recyclerView.adapter=adapter
            }

        })
        viewmodel_feed.countryErrMessage.observe(viewLifecycleOwner, Observer { error ->

            error?.let {
                if (it) {
                    binding.txtErrorMessage.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                    binding.txtErrorMessage.visibility = View.GONE

                } else {
                    binding.txtErrorMessage.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.txtErrorMessage.visibility = View.GONE

                }
            }


        })
        viewmodel_feed.countryLoading.observe(viewLifecycleOwner, Observer { countryLoading ->
            countryLoading?.let {
                if (countryLoading) {
                    binding.countryLoading.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                    binding.txtErrorMessage.visibility = View.GONE

                } else {
                    binding.countryLoading.visibility = View.GONE

                }
            }
        })

    }


}