package com.nickbryanmiller.jackpotlottery

import org.json.JSONObject

class User {
    internal var displayName: String = ""
    internal var email: String = ""
    private var token: String = ""
    private var password: String = ""
    private var id: String = ""
    private var tokenExpiration: Long = 0
    private var groups: ArrayList<GroupDataObject> = ArrayList()
    private var groupNames: ArrayList<String> = ArrayList()
    private var allEvents: ArrayList<EventDataObject> = ArrayList()
    private var acceptedEvents: ArrayList<EventDataObject> = ArrayList()
    private var pendingEvents: ArrayList<EventDataObject> = ArrayList()

    internal constructor() {
        setupUser()
    }
    internal constructor(token: String) {
        setupUser()
        this.token = token
    }
    internal constructor(userJSON: JSONObject) {
        email = userJSON.getString("email")
        displayName = userJSON.getString("displayName")
        password = userJSON.getString("password")
        id = userJSON.getString("_id")
        token = userJSON.getString("token")
        tokenExpiration = userJSON.getLong("tokenExpiration")
    }

    internal fun getAllGroups(): ArrayList<GroupDataObject> {
        return groups
    }
    internal fun getAllGroupNames() : ArrayList<String> {
        return groupNames
    }

    internal fun getAllEvents() : ArrayList<EventDataObject> {
        return allEvents
    }
    internal fun getAcceptedEvents() : ArrayList<EventDataObject> {
        return acceptedEvents
    }
    internal fun getPendingEvents() : ArrayList<EventDataObject> {
        return pendingEvents
    }

    internal fun login() {

    }
    internal fun fetchToken(completionMethod: (String) -> Unit) {
        JackpotClient.fetchToken(this::fetchTokenCompletion, completionMethod)
    }
    private fun fetchTokenCompletion(token: String, completionMethod: (String) -> Unit) {
        this.token = token
        completionMethod(this.token)
    }
    internal fun fetchEvents(completionMethod: (ArrayList<EventDataObject>) -> Unit) {
        JackpotClient.fetchEvents(token, this::fetchEventsCompletion, completionMethod)
    }
    private fun fetchEventsCompletion(events: ArrayList<EventDataObject>, completionMethod: (ArrayList<EventDataObject>) -> Unit) {
        this.allEvents = events
        completionMethod(this.allEvents)
    }
    internal fun fetchGroups() {
        JackpotClient.fetchGroups(token, this::fetchGroupsCompletion)
    }
    private fun fetchGroupsCompletion(groups: ArrayList<GroupDataObject>) {
        this.groups = groups
    }

    private fun setupUser() {
        displayName = "Display Name"
        email = "username@example.com"
        //token = "a07e22bc18f5cb106bfe4cc1f83ad8ed"
        loadGroups()
        loadEvents()
    }
    private fun loadGroups() {
        for (index in 0..4) {
            val group: GroupDataObject = GroupDataObject()
            group.name = "Group Name " + index
            group.description = "Group Description " + index
            groups.add(group)
            groupNames.add("Group Name " + index)
        }
    }
    private fun loadEvents() {
        for (index in 0..8) {
            val event: EventDataObject = EventDataObject()
            event.mEventNameText = "All Event " + index
            allEvents.add(event)
        }
        for (index in 0..3) {
            val event: EventDataObject = EventDataObject()
            event.mEventNameText = "Accepted Event " + (index + 17)
            acceptedEvents.add(event)
        }
        for (index in 0..4) {
            val event: EventDataObject = EventDataObject()
            event.mEventNameText = "Pending Event " + (index + 42)
            pendingEvents.add(event)
        }
    }
}