package com.nickbryanmiller.jackpotlottery

class User {
    internal var displayName: String = ""
    internal var username: String = ""
    private var token: String = ""
    private var groups: ArrayList<GroupDataObject> = ArrayList()
    private var groupNames: ArrayList<String> = ArrayList()
    private var allEvents: ArrayList<EventDataObject> = ArrayList()
    private var acceptedEvents: ArrayList<EventDataObject> = ArrayList()
    private var pendingEvents: ArrayList<EventDataObject> = ArrayList()

    internal constructor() {
        setupUser()
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

    internal fun fetchEvents() {
        JackpotClient.getEvents(token, this::fetchEventsCompletion)
    }
    private fun fetchEventsCompletion(events: ArrayList<EventDataObject>) {
        this.allEvents = events
    }
    internal fun fetchGroups() {
        JackpotClient.getGroups(token, this::fetchGroupsCompletion)
    }
    private fun fetchGroupsCompletion(groups: ArrayList<GroupDataObject>) {
        this.groups = groups
    }

    private fun setupUser() {
        displayName = "Display Name"
        username = "username@example.com"
        token = "a07e22bc18f5cb106bfe4cc1f83ad8ed"
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