package com.lugares.ui.lugar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.lugares.R
import com.lugares.databinding.FragmentAddLugarBinding
import com.lugares.databinding.FragmentLugarBinding
import com.lugares.model.Lugar
import com.lugares.viewmodel.LugarViewModel

class AddLugarFragment : Fragment() {

    private lateinit var lugarViewModel: LugarViewModel

    private var _binding: FragmentAddLugarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddLugarBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun addLugar() {
        var nombre = binding.etNombre.text.toString()
        if (nombre.isNotEmpty()) { // Podemos insertar el lugar
            var correo = binding.etCorreo.text.toString()
            var telefono = binding.etTelefono.text.toString()
            var web = binding.etWeb.toString()
            var lugar = Lugar(
                0,
                nombre,
                correo,
                telefono,
                web,
                0.0,
                0.0,
                0.0,
                "",
                ""
            )
            lugarViewModel.addLugar(lugar)
            Toast.makeText(requireContext(), "Lugar Agregado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Faltan Datos", Toast.LENGTH_SHORT).show()
        }
        findNavController().navigate(R.id.action_addLugarFragment_to_nav_lugar)
    }
}