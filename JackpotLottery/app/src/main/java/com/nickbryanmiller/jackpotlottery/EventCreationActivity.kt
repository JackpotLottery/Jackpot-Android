package com.nickbryanmiller.jackpotlottery

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView

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
        try {
            val groupID = this.intent.extras.getString("group_id")
            val nameTextView = findViewById(R.id.created_event_name) as TextView
            val dateTextView = findViewById(R.id.created_event_date) as TextView
            val descriptionTextView = findViewById(R.id.created_event_description) as TextView
            val locationTextView = findViewById(R.id.created_event_location) as TextView

            val event: EventDataObject = EventDataObject()
            event.name = nameTextView.text.toString()
            event.date = dateTextView.text.toString()
            event.description = descriptionTextView.text.toString()
            event.location = locationTextView.text.toString()
            event.capacity = 50
            event.tag = "demo"
            event.groupID = groupID

            User.sharedInstance!!.createEvent(event, this::createEventCompletion)
        }
        catch (e: Exception) {
            print(e.message)
        }
    }
    private fun createEventCompletion(success: Boolean) {
        this.onBackPressed()
    }
}
