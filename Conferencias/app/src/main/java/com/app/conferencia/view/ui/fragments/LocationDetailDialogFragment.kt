package com.app.conferencia.view.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.app.conferencia.R
import com.app.conferencia.model.Location

import kotlinx.android.synthetic.main.fragment_location_detail_dialog.*

/**
 * A simple [Fragment] subclass.
 */
class LocationDetailDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_location_detail_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarLocation.navigationIcon = ContextCompat.getDrawable(view.context, R.drawable.ic_close)
        toolbarLocation.setTitleTextColor(Color.WHITE)
        toolbarLocation.setNavigationOnClickListener {
            dismiss()
        }

        val location = Location()

        toolbarLocation.title = location.name

        tvDetailNombreLugar.text = location.name
        tvLocationDireccion.text = location.address
        tvLocationTelefono.text = location.phone
        tvLocationWebsite.text = location.website

        llTelefonoLugar.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${location.phone}")
            }
            startActivity(intent)
        }

        llSitioWeb.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(location.website)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

}
