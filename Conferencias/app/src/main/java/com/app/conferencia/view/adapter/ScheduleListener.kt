package com.app.conferencia.view.adapter

import com.app.conferencia.model.Conference

interface ScheduleListener {
    fun onConferenceClicked(conference: Conference, position: Int)
}