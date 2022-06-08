package com.lugares.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lugares.databinding.LugarFilaBinding
import com.lugares.model.Lugar

class LugarAdapter: RecyclerView.Adapter<LugarAdapter.LugarViewHolder>() {

    // Una lista para almacenar la información de los lugares
    private var listaLugares = emptyList<Lugar>()

    inner class LugarViewHolder(private val itemBinding: LugarFilaBinding):
    RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(lugar: Lugar) {
            itemBinding.tvNombre.text = lugar.nombre
            itemBinding.tvCorreo.text = lugar.correo
            itemBinding.tvTelefono.text = lugar.telefono
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LugarViewHolder {
        val itemBinding = LugarFilaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LugarViewHolder(itemBinding)
    }

    // Dibuja la lista de lugares
    override fun onBindViewHolder(holder: LugarViewHolder, position: Int) {
        val lugarActual = listaLugares[position]
        holder.bind(lugarActual)
    }

    override fun getItemCount(): Int {
        return listaLugares.size
    }

    fun setData(lugares: List<Lugar>) {
        this.listaLugares = lugares
        notifyDataSetChanged() // Provoca que se redibuje la lista
    }
}