package com.nickbryanmiller.jackpotlottery

import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import org.apache.http.Header
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import com.loopj.android.http.RequestParams

class JackpotClient {

    companion object {
        private val API_BASE_URL = "https://3c927f85.ngrok.io"
        private val client: AsyncHttpClient = AsyncHttpClient()

        private fun getContentFromURLExtension(urlExtension: String, token: String, params: RequestParams, handler: JsonHttpResponseHandler) {
            val url = API_BASE_URL + urlExtension
            client.addHeader("x-access-token", token)
            client.get(url, params, handler)
        }
        private fun postContentFromURLExtension(urlExtension: String, token: String, params: RequestParams, handler: JsonHttpResponseHandler) {
            val url = API_BASE_URL + urlExtension
            client.addHeader("x-access-token", token)
            client.post(url, params, handler)
        }
        private fun postContentFromURLExtension(urlExtension: String, params: RequestParams, handler: JsonHttpResponseHandler) {
            val url = API_BASE_URL + urlExtension
            client.post(url, params, handler)
        }

        fun login(email: String, password: String, completionMethod: (User) -> Unit) {
            val params = RequestParams()
            params.add("email", email)
            params.add("password", password)

            postContentFromURLExtension("/auth/login", params, object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<org.apache.http.Header>?, responseBody: JSONObject?) {
                    try {
                        val userJSON: JSONObject? = responseBody?.getJSONObject("user")
                        val user: User = User(userJSON!!)
                        completionMethod(user)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            })
        }
        fun signup(email: String, password: String, displayName: String, completionMethod: (User) -> Unit) {
            val params = RequestParams()
            params.add("email", email)
            params.add("password", password)
            params.add("displayName", displayName)

            postContentFromURLExtension("/auth/signup", params, object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<org.apache.http.Header>?, responseBody: JSONObject?) {
                    try {
                        val userJSON: JSONObject? = responseBody?.getJSONObject("user")
                        val user: User = User(userJSON!!)
                        completionMethod(user)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            })
        }

        fun fetchEvents(token: String, userID: String, groupsIDString: String, callbackMethod: (ArrayList<EventDataObject>, completionMethod: (ArrayList<EventDataObject>) -> Unit) -> Unit, completionMethod: (ArrayList<EventDataObject>) -> Unit) {
            val params = RequestParams()
            params.add("userID", userID)
            params.add("groups", groupsIDString)

            getContentFromURLExtension("/events/explore", token, params, object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<org.apache.http.Header>?, responseBody: JSONObject?) {
                    try {
                        val eventsJson: JSONArray? = responseBody?.getJSONArray("events")
                        val events: ArrayList<EventDataObject> = EventDataObject.fromJson(eventsJson!!)
                        callbackMethod(events, completionMethod)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            })
        }
        fun createEvent(token: String, event: EventDataObject, callbackMethod: (success: Boolean, group: EventDataObject, completionMethod: (Boolean) -> Unit) -> Unit, completionMethod: (Boolean) -> Unit) {
            val params = RequestParams()
            params.add("name", event.name)
            params.add("location", event.location)
            params.add("description", event.description)
            params.add("group", event.groupID)
            params.add("date", event.date)
            params.add("deadline", event.deadline)
            params.add("capacity", "$event.capacity")
            params.add("tag", event.tag)

            postContentFromURLExtension("/events/create", token, params, object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<org.apache.http.Header>?, responseBody: JSONObject?) {
                    try {
                        val contentString = responseBody.toString()
                        val eventJSON: JSONObject? = responseBody?.getJSONObject("event")
                        val createdEvent: EventDataObject = EventDataObject(eventJSON!!)
                        callbackMethod(true, createdEvent, completionMethod)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            })
        }
        fun joinEvent(token: String, userID: String, eventID: String, callbackMethod: (success :Boolean, event: EventDataObject, completionMethod: (Boolean, EventDataObject) -> Unit) -> Unit, completionMethod: (Boolean, EventDataObject) -> Unit) {
            val params = RequestParams()
            params.add("userID", userID)
            params.add("eventID", eventID)

            postContentFromURLExtension("/events/join", token, params, object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<org.apache.http.Header>?, responseBody: JSONObject?) {
                    try {
                        val eventJSON: JSONObject? = responseBody?.getJSONObject("event")
                        val joinedEvent: EventDataObject = EventDataObject(eventJSON!!)
                        callbackMethod(true, joinedEvent, completionMethod)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            })
        }


        fun fetchGroups(token: String, userID: String, callbackMethod: (ArrayList<GroupDataObject>, completionMethod: (ArrayList<GroupDataObject>) -> Unit) -> Unit, completionMethod: (ArrayList<GroupDataObject>) -> Unit) {
            val params = RequestParams()
            params.add("userID", userID)

            getContentFromURLExtension("/groups/", token, params, object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<org.apache.http.Header>?, responseBody: JSONObject?) {
                    try {
                        val groupsJson: JSONArray? = responseBody?.getJSONArray("groups")
                        val groups: ArrayList<GroupDataObject> = GroupDataObject.fromJson(groupsJson!!)
                        callbackMethod(groups, completionMethod)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            })
        }
        fun createGroup(token: String, userID: String, group: GroupDataObject, callbackMethod: (success: Boolean, group: GroupDataObject, completionMethod: (Boolean) -> Unit) -> Unit, completionMethod: (Boolean) -> Unit) {
            val params = RequestParams()
            params.add("userID", userID)
            params.add("name", group.name)
            params.add("password", group.password)
            params.add("description", group.description)

            postContentFromURLExtension("/groups/create", token, params, object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<org.apache.http.Header>?, responseBody: JSONObject?) {
                    try {
                        val groupJSON: JSONObject? = responseBody?.getJSONObject("group")
                        val createdGroup: GroupDataObject = GroupDataObject(groupJSON!!)
                        callbackMethod(true, createdGroup, completionMethod)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            })
        }
        fun joinGroup(token: String, userID: String, group: GroupDataObject, callbackMethod: (group: GroupDataObject, completionMethod: (GroupDataObject) -> Unit) -> Unit, completionMethod: (GroupDataObject) -> Unit) {
            val params = RequestParams()
            params.add("userID", userID)
            params.add("name", group.name)
            params.add("password", group.password)

            postContentFromURLExtension("/groups/join", token, params, object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<org.apache.http.Header>?, responseBody: JSONObject?) {
                    try {
                        val groupJSON: JSONObject? = responseBody?.getJSONObject("group")
                        val joinedGroup: GroupDataObject = GroupDataObject(groupJSON!!)
                        callbackMethod(joinedGroup, completionMethod)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            })
        }
    }
}