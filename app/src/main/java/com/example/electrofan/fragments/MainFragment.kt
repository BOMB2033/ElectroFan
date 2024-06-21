package com.example.electrofan.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.electrofan.R
import com.example.electrofan.databinding.FragmentLoginBinding
import com.example.electrofan.databinding.FragmentMainBinding
import com.example.electrofan.repository.DataViewModel
import com.example.electrofan.repository.Product
import com.example.electrofan.repository.ProductsAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val viewModel:DataViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        waitingLoad()

        with(binding){



            buttonBasket.setOnClickListener {
                it.findNavController().navigate(R.id.action_mainFragment_to_basketFragment)

            }
            buttonAddProduct.setOnClickListener {
                it.findNavController().navigate(R.id.action_mainFragment_to_adminFragment)
            }
        }

    }
    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun waitingLoad(){
        viewLifecycleOwner.lifecycleScope.launch {
            while (viewModel.getUser().uid == "")
                delay(1000)
            val adapter = ProductsAdapter(requireContext(),viewModel.getUser(),object :ProductsAdapter.Listener{
                override fun addProduct(product: Product) {
                    viewModel.addProductUser(product)
                    waitingLoad()
                }
            })
            if (viewModel.getUser().isAdmin)
                binding.buttonAddProduct.visibility = View.VISIBLE
            else
                binding.buttonAddProduct.visibility = View.GONE

            binding.recyclerView.adapter = adapter

            viewModel.data.observe(viewLifecycleOwner){
                adapter.submitList(it.products)
            }

            binding.progressBar.visibility = View.GONE
        }
    }
}