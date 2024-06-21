package com.example.electrofan.fragments.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.electrofan.databinding.FragmentAdminBinding
import com.example.electrofan.repository.DataViewModel
import com.example.electrofan.repository.Product
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AdminFragment : Fragment() {
    private lateinit var binding: FragmentAdminBinding
    private val viewModel: DataViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            binding.buttonAdd.setOnClickListener {
                val name = editTextName.text.toString().trim()
                val about = editTextAuthor.text.toString().trim()
                val cast = editTextURL.text.toString().trim()

                if (name.isEmpty() || about.isEmpty() || cast.isEmpty()) {
                    Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                viewModel.addProduct(Product(name = name, about = about, cast = cast.toInt()))
                it.findNavController().popBackStack()
            }
        }

    }
}