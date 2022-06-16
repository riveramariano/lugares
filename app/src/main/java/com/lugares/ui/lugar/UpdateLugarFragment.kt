package com.lugares.ui.lugar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lugares.R
import com.lugares.databinding.FragmentUpdateLugarBinding
import com.lugares.model.Lugar
import com.lugares.viewmodel.LugarViewModel

class UpdateLugarFragment : Fragment() {
    private var _binding: FragmentUpdateLugarBinding? = null
    private val binding get() = _binding!!
    private lateinit var lugarViewModel: LugarViewModel

    private val args by navArgs<UpdateLugarFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateLugarBinding.inflate(inflater, container, false)

        lugarViewModel = ViewModelProvider(this).get(LugarViewModel::class.java)

        binding.etNombre.setText(args.lugar.nombre)
        binding.etCorreo.setText(args.lugar.correo)
        binding.etWeb.setText(args.lugar.sitioWeb)
        binding.etTelefono.setText(args.lugar.telefono)

        binding.btAgregar.setOnClickListener { updateLugar() }
        // Corregir este nombre, se pone igual que en AddLugarFragment

        return binding.root
    }

    private fun updateLugar() {
        var nombre = binding.etNombre.text.toString()
        if (nombre.isNotEmpty()) { // Podemos actualizar el lugar
            var correo = binding.etCorreo.text.toString()
            var telefono = binding.etTelefono.text.toString()
            var web = binding.etWeb.toString()
            var lugar = Lugar(
                args.lugar.id,
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
            lugarViewModel.updateLugar(lugar)
            Toast.makeText(requireContext(), "Lugar Actualizado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Faltan Datos", Toast.LENGTH_SHORT).show()
        }
        findNavController().navigate(R.id.action_addLugarFragment_to_nav_lugar)
    }
}