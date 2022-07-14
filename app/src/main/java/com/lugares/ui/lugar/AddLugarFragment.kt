package com.lugares.ui.lugar

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lugares.R
import com.lugares.databinding.FragmentAddLugarBinding
import com.lugares.databinding.FragmentLugarBinding
import com.lugares.model.Lugar
import com.lugares.utiles.AudioUtiles
import com.lugares.utiles.ImagenUtiles
import com.lugares.viewmodel.LugarViewModel

class AddLugarFragment : Fragment() {

    private lateinit var lugarViewModel: LugarViewModel

    private var _binding: FragmentAddLugarBinding? = null
    private val binding get() = _binding!!

    private lateinit var audioUtiles: AudioUtiles
    private lateinit var tomarFotoActivity: ActivityResultLauncher<Intent>
    private lateinit var imagenUtiles: ImagenUtiles

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddLugarBinding.inflate(inflater, container, false)

        lugarViewModel = ViewModelProvider(this).get(LugarViewModel::class.java)

        binding.btAgregar.setOnClickListener {
            if (binding.etNombre.text.toString().isNotEmpty()) {
                binding.progressBar.visibility = ProgressBar.VISIBLE
                binding.msgMensage.text = "Subiendo Nota de Audio"
                binding.msgMensaje.visibility = TextView.VISIBLE

                subeAudioNube()
            } else {
                Toast.makeText(requireContext(), "Faltan Datos", Toast.LENGTH_LONG).show()
            }
        }

        audioUtiles = AudioUtiles(
            requireActivity(),
            requireContext(),
            binding.tbAccion,
            binding.btPlay,
            binding.btDelete,
            "Grabando nota de audio",
            "Nota de audio detenida"
        )

        tomarFotoActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imagenUtiles.actualizaFoto()
            }
        }

        imagenUtiles = ImagenUtiles(
            requireContext(),
            binding.btFoto,
            binding.btRotarL,
            binding.btRotarR,
            binding.imagen,
            tomarFotoActivity
        )

        ubicaGPS()

        return binding.root
    }

    private fun subeAudioNube() {
        val audioFile = audioUtiles.audioFile
        if (audioFile.exists() && audioFile.isFile && audioFile.canRead()) {
            val ruta = Uri.fromFile(audioFile)
            val referencia: StorageReference = Firebase.storage.reference.child(
                "lugaresApp/${Firebase.auth.currentUser?.uid}/audios/${audioFile.name}"
            )
            val uploadTask = referencia.putFile(ruta)
            uploadTask
                .addOnSuccessListener {
                    val downloadUrl = referencia.downloadUrl
                    downloadUrl.addOnSuccessListener {
                        val rutaNota = it.toString()
                        subeImagenNube(rutaNota)
                    }
                }
            uploadTask
                .addOnFailureListener {
                    Toast.makeText(context, "Error subiendo nota de audio", Toast.LENGTH_SHORT).show()
                    subeImagenNube("")
                }
        } else {
            Toast.makeText(context, "No se sube audio", Toast.LENGTH_SHORT).show()
            subeImagenNube("")
        }
    }

    private fun subeImagenNube(rutaAudio: String) {
        binding.msgMensaje.text = "Subiendo Imagen"
        val imageFile = imagenUtiles.imagenFile
        if (imageFile.exists() && imageFile.isFile && imageFile.canRead()) {
            val ruta = Uri.fromFile(imageFile)
            val referencia: StorageReference = Firebase.storage.reference.child(
                "lugaresApp/${Firebase.auth.currentUser?.uid}/imagenes/${imageFile.name}"
            )
            val uploadTask = referencia.putFile(ruta)
            uploadTask
                .addOnSuccessListener {
                    val downloadUrl = referencia.downloadUrl
                    downloadUrl.addOnSuccessListener {
                        val rutaImagen = it.toString()
                        agregarLugar(rutaAudio, rutaImagen)
                    }
                }
            uploadTask
                .addOnFailureListener {
                    Toast.makeText(context, "Error subiendo la imagen", Toast.LENGTH_SHORT).show()
                    agregarLugar(rutaAudio, "")
                }
        } else {
            Toast.makeText(context, "No se sube imagen", Toast.LENGTH_SHORT).show()
            agregarLugar(rutaAudio, "")
        }
    }

    private fun agregarLugar(rutaAudio: String, rutaImagen: String) {

    }

    private fun ubicaGPS() {
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        val conPermisos = true

        if (ActivityCompat.checkSelfPermission(requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), 105)
        }

        if (conPermisos) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? -> (
                if (location != null) {
                    binding.tvLatitud.text = "${location.latitude}"
                    binding.tvLongitud.text = "${location.longitude}"
                    binding.tvAltura.text = "${location.altitude}"
                } else {
                    binding.tvLatitud.text = getString(R.string.error)
                    binding.tvLongitud.text = getString(R.string.error)
                    binding.tvAltura.text = getString(R.string.error)
                }
            ) }
        }
    }

    private fun addLugar() {
        var nombre = binding.etNombre.text.toString()
        if (nombre.isNotEmpty()) { // Podemos insertar el lugar
            var correo = binding.etCorreo.text.toString()
            var telefono = binding.etTelefono.text.toString()
            var web = binding.etWeb.toString()
            var lugar = Lugar(
                "",
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