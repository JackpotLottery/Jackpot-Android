package com.nickbryanmiller.jackpotlottery

import org.json.JSONObject

class User {
    internal var displayName: String = ""
    internal var email: String = ""
    internal var password: String = "" // should not be internal
    private var token: String = ""
    private var id: String = ""
    private var tokenExpiration: Long = 0
    private var groups: ArrayList<GroupDataObject> = ArrayList()
    private var groupNames: ArrayList<String> = ArrayList()
    private var allEvents: ArrayList<EventDataObject> = ArrayList()
    private var acceptedEvents: ArrayList<EventDataObject> = ArrayList()
    private var pendingEvents: ArrayList<EventDataObject> = ArrayList()

    internal constructor() {
        //setupUser()
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

    internal fun fetchEvents(completionMethod: (ArrayList<EventDataObject>) -> Unit) {
        fetchGroups {
            val stringBuilder: StringBuilder = StringBuilder()
            if (groups.isNotEmpty()) {
                stringBuilder.append(groups.elementAt(0).id)
            }
            for (index in 1..groups.count()-1) {
                stringBuilder.append("," + groups.elementAt(index).id)
            }
            JackpotClient.fetchEvents("explore", token, id, stringBuilder.toString(), this::fetchEventsCompletion, completionMethod)
        }
    }
    private fun fetchEventsCompletion(events: ArrayList<EventDataObject>, completionMethod: (ArrayList<EventDataObject>) -> Unit) {
        this.allEvents = events
        completionMethod(this.allEvents)
    }
    internal fun fetchAcceptedEvents(completionMethod: (ArrayList<EventDataObject>) -> Unit) {
        fetchGroups {
            val stringBuilder: StringBuilder = StringBuilder()
            if (groups.isNotEmpty()) {
                stringBuilder.append(groups.elementAt(0).id)
            }
            for (index in 1..groups.count()-1) {
                stringBuilder.append("," + groups.elementAt(index).id)
            }
            JackpotClient.fetchEvents("accepted", token, id, stringBuilder.toString(), this::fetchAcceptedEventsCompletion, completionMethod)
        }
    }
    private fun fetchAcceptedEventsCompletion(events: ArrayList<EventDataObject>, completionMethod: (ArrayList<EventDataObject>) -> Unit) {
        this.acceptedEvents = events
        completionMethod(this.acceptedEvents)
    }
    internal fun fetchPendingdEvents(completionMethod: (ArrayList<EventDataObject>) -> Unit) {
        fetchGroups {
            val stringBuilder: StringBuilder = StringBuilder()
            if (groups.isNotEmpty()) {
                stringBuilder.append(groups.elementAt(0).id)
            }
            for (index in 1..groups.count()-1) {
                stringBuilder.append("," + groups.elementAt(index).id)
            }
            JackpotClient.fetchEvents("pending", token, id, stringBuilder.toString(), this::fetchPendingEventsCompletion, completionMethod)
        }
    }
    private fun fetchPendingEventsCompletion(events: ArrayList<EventDataObject>, completionMethod: (ArrayList<EventDataObject>) -> Unit) {
        this.pendingEvents = events
        completionMethod(this.pendingEvents)
    }

    internal fun createEvent(event: EventDataObject, completionMethod: (Boolean) -> Unit) {
        JackpotClient.createEvent(token, event, this::createEventCompletion, completionMethod)
    }
    private fun createEventCompletion(success: Boolean, event: EventDataObject, completionMethod: (Boolean) -> Unit) {
        if (success) {
            allEvents.add(event)
            completionMethod(success)
        }
    }

    internal fun joinEvent(eventID: String, completionMethod: (Boolean, EventDataObject) -> Unit) {
        JackpotClient.joinEvent(token, id, eventID, this::joinEventCompletion, completionMethod)
    }
    private fun joinEventCompletion(success: Boolean, event: EventDataObject, completionMethod: (Boolean, EventDataObject) -> Unit) {
        if (success) {
            for (index in 0..allEvents.count()-1) {
                if (event.id == allEvents[index].id) {
                    allEvents.removeAt(index)
                    break
                }
            }
            pendingEvents.add(event)
            completionMethod(success, event)
        }
    }

    internal fun fetchGroups(completionMethod: (ArrayList<GroupDataObject>) -> Unit) {
        JackpotClient.fetchGroups(token, id, this::fetchGroupsCompletion, completionMethod)
    }
    private fun fetchGroupsCompletion(groups: ArrayList<GroupDataObject>, completionMethod: (ArrayList<GroupDataObject>) -> Unit) {
        this.groups = groups
        groupNames.clear()
        for (group in groups) {
            groupNames.add(group.name!!)
        }
        completionMethod(this.groups)
    }

    internal fun createGroup(group: GroupDataObject, completionMethod: (Boolean) -> Unit) {
        JackpotClient.createGroup(token, id, group, this::createGroupCompletion, completionMethod)
    }
    private fun createGroupCompletion(success: Boolean, group: GroupDataObject, completionMethod: (Boolean) -> Unit) {
        if (success) {
            groups.add(group)
            groupNames.add(group.name!!)
            completionMethod(success)
        }
    }

    internal fun joinGroup(group: GroupDataObject, completionMethod: (GroupDataObject) -> Unit) {
        JackpotClient.joinGroup(token, id, group, this::joinGroupCompletion, completionMethod)
    }
    private fun joinGroupCompletion(group: GroupDataObject, completionMethod: (GroupDataObject) -> Unit) {
        groups.add(group)
        groupNames.add(group.name!!)
        completionMethod(group)
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
            event.name = "All Event " + index
            allEvents.add(event)
        }
        for (index in 0..3) {
            val event: EventDataObject = EventDataObject()
            event.name = "Accepted Event " + (index + 17)
            acceptedEvents.add(event)
        }
        for (index in 0..4) {
            val event: EventDataObject = EventDataObject()
            event.name = "Pending Event " + (index + 42)
            pendingEvents.add(event)
        }
    }

    companion object {
        internal var sharedInstance: User? = null
    }
}