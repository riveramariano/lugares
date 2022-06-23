package com.lugares.ui.lugar

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lugares.R
import com.lugares.databinding.FragmentUpdateLugarBinding
import com.lugares.model.Lugar
import com.lugares.viewmodel.LugarViewModel
import android.Manifest

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
        binding.etTelefono.setText(args.lugar.telefono)
        binding.etWeb.setText(args.lugar.sitioWeb)

        binding.btAgregar.setOnClickListener { updateLugar() }
        // Corregir este nombre, se pone igual que en AddLugarFragment

        binding.tvLatitud.text = args.lugar.latitud.toString()
        binding.tvLongitud.text = args.lugar.longitud.toString()
        binding.tvAltura.text = args.lugar.altura.toString()

        binding.emailBtn.setOnClickListener { escribirCorreo() }
        binding.phoneBtn.setOnClickListener { llamarLugar() }
        binding.whatsBtn.setOnClickListener { enviarWhats() }
        binding.webBtn.setOnClickListener { verWebLugar() }
        binding.locationBtn.setOnClickListener { verMapa() }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deleteLugar()
        }
        return super.onOptionsItemSelected(item)
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

    private fun deleteLugar() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.si)) { _, _ ->
            lugarViewModel.deleteLugar(args.lugar)
            Toast.makeText(requireContext(), getString(R.string.deleted) + " ${args.lugar.nombre}", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addLugarFragment_to_nav_lugar)
        }

        builder.setNegativeButton(getString(R.string.no)) { _, _ -> }
        builder.setTitle(R.string.deleted)
        builder.setMessage(getString(R.string.seguro_borrar) + " ${args.lugar.nombre}?")
        builder.create().show()
    }

    private fun llamarLugar() {
        val telefono = binding.etTelefono.text.toString()
        if (telefono.isNotEmpty()) {
            val dialIntent = Intent(Intent.ACTION_CALL)
            dialIntent.data = Uri.parse("tel:$telefono")

            if (requireActivity().checkSelfPermission(Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
                requireActivity().requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 105)
            } else {
                requireActivity().startActivity(dialIntent)
            }
        } else {
            Toast.makeText(requireContext(), getString(R.string.msg_datos), Toast.LENGTH_SHORT).show()
        }
    }

    private fun escribirCorreo() {
        val para = binding.etCorreo.text.toString()
        if (para.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "message/rfc822"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(para))
            intent.putExtra(
                Intent.EXTRA_SUBJECT, getString(R.string.msg_saludos) + " " + binding.etNombre.text
            )
            intent.putExtra(
                Intent.EXTRA_TEXT, getString(R.string.msg_mensaje_correo)
            )
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), getString(R.string.msg_datos), Toast.LENGTH_SHORT).show()
        }
    }

    private fun enviarWhats() {
        val telefono = binding.etTelefono.text.toString()
        if (telefono.isNotEmpty()) {
            val sendIntent = Intent(Intent.ACTION_VIEW)
            val uri = "whatsapp://send?phone=506$telefono&text=" + getString(R.string.msg_saludos)

            sendIntent.setPackage("com.whatsapp")
            sendIntent.data = Uri.parse(uri)
            startActivity(sendIntent)
        } else {
            Toast.makeText(requireContext(), getString(R.string.msg_datos), Toast.LENGTH_SHORT).show()
        }
    }

    private fun verWebLugar() {
        val sitio = binding.etWeb.text.toString()
        if (sitio.isNotEmpty()) {
            val webPage = Uri.parse("https://$sitio")
            val intent = Intent(Intent.ACTION_VIEW, webPage)
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), getString(R.string.msg_datos), Toast.LENGTH_SHORT).show()
        }
    }

    private fun verMapa() {
        val latitud = binding.tvLatitud.text.toString().toDouble()
        val longitud = binding.tvLongitud.text.toString().toDouble()
        if (latitud.isFinite() && longitud.isFinite()) {
            val location = Uri.parse("geo:$latitud.$longitud?z=18")
            val mapIntent = Intent(Intent.ACTION_VIEW, location)
            startActivity(mapIntent)
        } else {
            Toast.makeText(requireContext(), getString(R.string.msg_datos), Toast.LENGTH_SHORT).show()
        }
    }
}