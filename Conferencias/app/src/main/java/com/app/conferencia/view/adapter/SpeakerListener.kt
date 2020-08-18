package com.app.conferencia.view.adapter

import com.app.conferencia.model.Speaker

interface SpeakerListener {
    fun onSpeakerClicked(speaker: Speaker, position: Int)
}