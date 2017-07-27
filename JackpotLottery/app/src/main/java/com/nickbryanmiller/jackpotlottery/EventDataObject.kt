package com.nickbryanmiller.jackpotlottery

import org.json.JSONArray
import org.json.JSONObject

class EventDataObject {

    internal var name: String? = ""
    internal var date: String? = ""
    internal var id: String? = ""
    internal var location: String? = ""
    internal var groupID: String? = ""
    internal var deadline: String? = ""
    internal var capacity: Long? = 0
    internal var tag: String? = ""
    internal var description: String? = ""

    internal constructor() {
    }
    internal constructor(eventJSON: JSONObject) {
        name = eventJSON.getString("name")
        date = eventJSON.getString("date")
        id = eventJSON.getString("_id")
        location = eventJSON.getString("location")
        groupID = eventJSON.getString("group")
        deadline = eventJSON.getString("deadline")
        capacity = eventJSON.getLong("capacity")
        tag = eventJSON.getString("tag")
        description = eventJSON.getString("description")
    }

    companion object {
        internal fun fromJson(jsonArray: JSONArray): ArrayList<EventDataObject> {
            val events = ArrayList<EventDataObject>(jsonArray.length())
            for (i in 0..jsonArray.length() - 1) {
                var eventJson: JSONObject? = null
                try {
                    eventJson = jsonArray.getJSONObject(i)
                }
                catch (e: Exception) {
                    e.printStackTrace()
                    continue
                }

                try {
                    val event = EventDataObject(eventJson)
                    events.add(event)
                }
                catch (e: Exception) {
                    e.printStackTrace()
                    continue
                }
            }

            return events
        }
    }
}
