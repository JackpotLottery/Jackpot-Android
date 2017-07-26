package com.nickbryanmiller.jackpotlottery

import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class JackpotClient {

    companion object {
        private val API_BASE_URL = "https://api.themoviedb.org/3/movie/now_playing"
        private val client: AsyncHttpClient = AsyncHttpClient()
        private val user: User = User()

        fun getContentFromURLExtension(urlExtension: String, token: String, handler: JsonHttpResponseHandler) {
            val url = API_BASE_URL + urlExtension + "?api_key=" + token
            client.get(url, handler)
        }

        fun getEvents(token: String, completionMethod: (ArrayList<EventDataObject>) -> Unit) {
            getContentFromURLExtension("", token, object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<org.apache.http.Header>?, responseBody: JSONObject?) {
                    var items: JSONArray? = null
                    try {
                        // Get the posts json array
                        val contentString = responseBody.toString()
                        print(contentString)
                        items = responseBody?.getJSONArray("results")
                        print(items!![0])
                        // Parse the json array into array of model objects
                        val events: ArrayList<EventDataObject> = ArrayList()
                        completionMethod(events)
                        //return new ArrayList<EventDataObject>
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            })
        }

        fun getGroups(token: String, completionMethod: (ArrayList<GroupDataObject>) -> Unit) {
            val groups: ArrayList<GroupDataObject> = ArrayList()
            completionMethod(groups)
        }
    }
}