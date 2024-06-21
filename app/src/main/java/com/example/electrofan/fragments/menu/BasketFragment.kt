package com.example.electrofan.fragments.menu

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.electrofan.R
import com.example.electrofan.databinding.FragmentAdminBinding
import com.example.electrofan.databinding.FragmentBasketBinding
import com.example.electrofan.repository.DataViewModel
import com.example.electrofan.repository.Product
import com.example.electrofan.repository.ProductsAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BasketFragment : Fragment() {
    private lateinit var binding: FragmentBasketBinding
    private val viewModel: DataViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        waitingLoad()

        with(binding){



            buttonBack.setOnClickListener {
                it.findNavController().popBackStack()
            }
        }

    }
    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun waitingLoad(){
        viewLifecycleOwner.lifecycleScope.launch {
            while (viewModel.getUser().uid == "")
                delay(1000)
            val adapter = ProductsAdapter(requireContext(),viewModel.getUser(),object :
                ProductsAdapter.Listener{
                override fun addProduct(product: Product) {
                    viewModel.addProductUser(product)
                    waitingLoad()
                }
            })

            binding.recyclerView.adapter = adapter

            viewModel.data.observe(viewLifecycleOwner){
                adapter.submitList(viewModel.getUser().uidProducts)
            }

            binding.progressBar.visibility = View.GONE
        }
    }
}