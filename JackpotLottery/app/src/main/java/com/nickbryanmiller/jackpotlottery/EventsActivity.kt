package com.nickbryanmiller.jackpotlottery

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.LogPrinter
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TabHost
import android.widget.TabHost.OnTabChangeListener

class EventsActivity : AppCompatActivity() {
    // Explore Tab Variables
    private var mRecyclerViewExplore: RecyclerView? = null
    private var mAdapterExplore: RecyclerView.Adapter<*>? = null
    private var mLayoutManagerExplore: RecyclerView.LayoutManager? = null
    // Accepted Tab Variables
    private var mRecyclerViewAccepted: RecyclerView? = null
    private var mAdapterAccepted: RecyclerView.Adapter<*>? = null
    private var mLayoutManagerAccepted: RecyclerView.LayoutManager? = null
    // Pending Tab Variables
    private var mRecyclerViewPending: RecyclerView? = null
    private var mAdapterPending: RecyclerView.Adapter<*>? = null
    private var mLayoutManagerPending: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)

        title = "Events"

        val host = findViewById(R.id.tabHost) as TabHost
        host.setup()

        //Tab 1
        var spec: TabHost.TabSpec = host.newTabSpec("Explore")
        spec.setContent(R.id.tab1)
        spec.setIndicator("Explore")
        host.addTab(spec)

        //Tab 2
        spec = host.newTabSpec("Accepted")
        spec.setContent(R.id.tab2)
        spec.setIndicator("Accepted")
        host.addTab(spec)

        //Tab 3
        spec = host.newTabSpec("Pending")
        spec.setContent(R.id.tab3)
        spec.setIndicator("Pending")
        host.addTab(spec)

        // Cards for tab 1 - need to load or crash
        loadExploreEventsTab()

        host.setOnTabChangedListener(OnTabChangeListener {
            when (host.currentTab) {
                0 -> loadExploreEventsTab()
                1 -> loadAcceptedEventsTab()
                2 -> loadPendingEventsTab()
            }
        })

        User.sharedInstance!!.fetchEvents(this::fetchExploreEventsCompletion)
    }
    override fun onResume() {
        super.onResume()
        (mAdapterExplore as MyEventRecyclerViewAdapter).setOnItemClickListener(object : MyEventRecyclerViewAdapter.MyClickListener {
            override fun onItemClick(position: Int, v: View) {
                print("test")
            }
        })
    }

    private fun loadExploreEventsTab() {
        mRecyclerViewExplore = findViewById(R.id.recycler_view_explore) as RecyclerView
        mRecyclerViewExplore?.setHasFixedSize(true)
        mLayoutManagerExplore = LinearLayoutManager(this)
        mRecyclerViewExplore?.layoutManager = mLayoutManagerExplore
        mAdapterExplore = MyEventRecyclerViewAdapter(User.sharedInstance!!.getAllEvents())
        mRecyclerViewExplore?.adapter = mAdapterExplore
    }
    private fun loadAcceptedEventsTab() {
        mRecyclerViewAccepted = findViewById(R.id.recycler_view_accepted) as RecyclerView
        mRecyclerViewAccepted?.setHasFixedSize(true)
        mLayoutManagerAccepted = LinearLayoutManager(this)
        mRecyclerViewAccepted?.layoutManager = mLayoutManagerAccepted
        mAdapterAccepted = MyEventRecyclerViewAdapter(User.sharedInstance!!.getAcceptedEvents())
        mRecyclerViewAccepted?.adapter = mAdapterAccepted
    }
    private fun loadPendingEventsTab() {
        mRecyclerViewPending = findViewById(R.id.recycler_view_pending) as RecyclerView
        mRecyclerViewPending?.setHasFixedSize(true)
        mLayoutManagerPending = LinearLayoutManager(this)
        mRecyclerViewPending?.layoutManager = mLayoutManagerPending
        mAdapterPending = MyEventRecyclerViewAdapter(User.sharedInstance!!.getPendingEvents())
        mRecyclerViewPending?.adapter = mAdapterPending
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_profile -> {
                navigateToProfilePage()
                return true
            }
            R.id.action_add_event -> {
                createNewEvent()
                return true
            }
            else -> {
                // user's action was not recognized. Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item)
            }
        }
    }
    override fun onBackPressed() {
        // Don't let them go back to the login page
    }

    private fun createNewEvent() {
        val groups: ArrayList<GroupDataObject> = User.sharedInstance!!.getAllGroups()

        val groupNames: ArrayList<String> = User.sharedInstance!!.getAllGroupNames()
        val charSequenceItems = groupNames.toArray(arrayOfNulls<CharSequence>(groupNames.count()))

        val mSelectedItems: ArrayList<GroupDataObject> = ArrayList()
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Which Group(s)")
        alert.setIcon(R.drawable.ic_account)
        alert.setMultiChoiceItems(charSequenceItems, null, DialogInterface.OnMultiChoiceClickListener { dialogInterface, which, isChecked ->
            if (isChecked) {
                mSelectedItems.add(groups[which])
            }
            else if (mSelectedItems.contains(groups[which]))
            {
                mSelectedItems.remove(groups[which])
            }
        })
        alert.setNeutralButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->  })
        alert.setPositiveButton("Okay", DialogInterface.OnClickListener { dialogInterface, i ->
            if (mSelectedItems.isNotEmpty()) {navigateToEventCreationPage(mSelectedItems)}
        })
        alert.show()
    }

    private fun navigateToEventCreationPage(groupArray: ArrayList<GroupDataObject>) {
        val eventCreationIntent = Intent(this, EventCreationActivity::class.java)
        eventCreationIntent.putExtra("group_id", groupArray.first().id)
        startActivity(eventCreationIntent)
    }
    private fun navigateToProfilePage() {
        val profileIntent = Intent(this, ProfileActivity::class.java)
        startActivity(profileIntent)
    }

    private fun fetchExploreEventsCompletion(events: ArrayList<EventDataObject>) {
        val mainHandler = Handler(Looper.getMainLooper());
        val myRunnable = Runnable() {
            runOnUiThread {
                loadExploreEventsTab()
            }
        }
        mainHandler.post(myRunnable);
    }
}
