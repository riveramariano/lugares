package com.lugares

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lugares.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        // Se define el m
        binding.btnInicioSesion.setOnClickListener() {
            haceInicioSesion()
        }

        binding.btnRegistro.setOnClickListener() {
            haceRegistro()
        }

    }

    private fun haceInicioSesion() {
        val correo = binding.etCorreoElectronico.text.toString()
        val clave = binding.etClave.text.toString()

        // Se realiza el inicio de sesión usuario
        auth.signInWithEmailAndPassword(correo, clave)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("Autenticando Usuario", "Autenticado")
                    val user = auth.currentUser
                    actualiza(user)
                } else {
                    Log.d("Autenticando Usuario", "Usuario y/o Contraseña Incorrectos")
                    Toast.makeText(baseContext, "Falló", Toast.LENGTH_LONG).show()
                    actualiza(null)
                }
            }
    }

    private fun haceRegistro() {
        val correo = binding.etCorreoElectronico.text.toString()
        val clave = binding.etClave.text.toString()

        // Se realiza el registro del nuevo usuario
        auth.createUserWithEmailAndPassword(correo, clave)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("Creando Usuario", "Registro Exitoso")
                    val user = auth.currentUser
                    actualiza(user)
                } else {
                    Log.d("Creando Usuario", "Falló")
                    Toast.makeText(baseContext, "Falló", Toast.LENGTH_LONG).show()
                    actualiza(null)
                }
            }
    }

    private fun actualiza(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, Principal::class.java)
            startActivity(intent)
        }
    }

    // Una vez autenticado, está función hace que no pida credenciales nuevamente
    public override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        actualiza(user)
    }
}