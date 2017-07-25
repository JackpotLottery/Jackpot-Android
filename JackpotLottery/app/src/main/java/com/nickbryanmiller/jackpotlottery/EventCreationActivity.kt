package com.nickbryanmiller.jackpotlottery

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View

class EventCreationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_creation)

        title = "Event Creation"
    }

    fun onEventCreationCancelButtonClick(v: View) {
        this.onBackPressed()
    }

    fun onEventCreateButtonClick(v: View) {
        createEvent()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_event_creation, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_create_event -> {
                createEvent()
                return true
            }
            else -> {
                // user's action was not recognized. Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun createEvent() {
        // do event creation
    }
}
