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

        private fun getContentFromURLExtension(urlExtension: String, token: String, handler: JsonHttpResponseHandler) {
            val url = API_BASE_URL + urlExtension + "?api_key=" + token
            client.get(url, handler)
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
                        print(items!![0])
                        // Parse the json array into array of model objects
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