package com.nickbryanmiller.jackpotlottery

import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import org.apache.http.Header
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import com.loopj.android.http.RequestParams

class JackpotClient {

    class ClientHeader {
        val header: String
        val value: String
        internal constructor(header: String, value: String) {
            this.header = header
            this.value = value
        }
    }

    companion object {
        private val API_BASE_URL = "https://3c927f85.ngrok.io"
        private val client: AsyncHttpClient = AsyncHttpClient()

        private fun getContentFromURLExtension(urlExtension: String, extension: String, handler: JsonHttpResponseHandler) {
            val url = API_BASE_URL + urlExtension + "?" + extension
            client.get(url, handler)
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

        fun fetchToken(callbackMethod:(String, completionMethod: (String) -> Unit) -> Unit,
                       completionMethod: (String) -> Unit) {
            callbackMethod("a07e22bc18f5cb106bfe4cc1f83ad8ed", completionMethod)
        }

        fun fetchEvents(token: String,
                      callbackMethod: (ArrayList<EventDataObject>, completionMethod: (ArrayList<EventDataObject>) -> Unit) -> Unit,
                      completionMethod: (ArrayList<EventDataObject>) -> Unit) {
            getContentFromURLExtension("", token, object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<org.apache.http.Header>?, responseBody: JSONObject?) {
                    var items: JSONArray? = null
                    try {
                        // Get the posts json array
                        val contentString = responseBody.toString()
                        print(contentString)
                        items = responseBody?.getJSONArray("results")
                        val events: ArrayList<EventDataObject> = ArrayList()
                        callbackMethod(events, completionMethod)
                        //return new ArrayList<EventDataObject>
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            })
        }

        fun fetchGroups(token: String, completionMethod: (ArrayList<GroupDataObject>) -> Unit) {
            val groups: ArrayList<GroupDataObject> = ArrayList()
            completionMethod(groups)
        }
    }
}