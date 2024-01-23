package com.example.myapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapp.databinding.FragmentFirstBinding
import android.widget.ArrayAdapter
import android.util.Log

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbHandler: DatabaseHandler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHandler = DatabaseHandler(requireContext())
        updateListView()

        binding.buttonAdd.setOnClickListener {
            val userInputType = binding.editTextType.text.toString()
            val userInputBrand = binding.editTextBrand.text.toString()
            val userInputModel = binding.editTextModel.text.toString()
            val userInputCountry = binding.editTextCountry.text.toString()
            val userInputPrice = binding.editTextPrice.text.toString().toDoubleOrNull() ?: 0.0

            Log.d("FirstFragment", "User Input: $userInputType, $userInputBrand, $userInputModel, $userInputCountry, $userInputPrice")

            // Валідація даних...

            val newInstrument = Instruments(
                userInputType,
                userInputBrand,
                userInputModel,
                userInputCountry,
                userInputPrice
            )
            dbHandler.addInstrument(newInstrument)

            // Очистка полів вводу
            binding.editTextType.text.clear()
            binding.editTextBrand.text.clear()
            binding.editTextModel.text.clear()
            binding.editTextCountry.text.clear()
            binding.editTextPrice.text.clear()

            updateListView()
        }

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    private fun updateListView() {
        try {
            val instrumentsList = dbHandler.getAllInstruments()
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                instrumentsList.map { "${it.type} - ${it.brand} - ${it.model} - ${it.price} - ${it.countryOfOrigin}" })
            binding.listViewUserInput.adapter = adapter
        } catch (e: Exception) {
            Log.e("FirstFragment", "Error updating list view", e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
