package com.nickbryanmiller.jackpotlottery

import org.json.JSONObject
import org.json.JSONArray

class GroupDataObject {

    internal var name: String? = null
    internal var username: String? = null
    internal var password: String? = null
    internal var description: String? = null
    internal var imageUrl: String? = null

    internal constructor() {

    }
    internal constructor(groupJSON: JSONObject) {

    }

    companion object {
        internal fun fromJson(jsonArray: JSONArray): ArrayList<GroupDataObject> {
            val groups = ArrayList<GroupDataObject>(jsonArray.length())
            for (i in 0..jsonArray.length() - 1) {
                var groupJson: JSONObject? = null
                try {
                    groupJson = jsonArray.getJSONObject(i).getJSONObject("data")
                }
                catch (e: Exception) {
                    e.printStackTrace()
                    continue
                }

                try {
                    val group = GroupDataObject(groupJson)
                    groups.add(group)
                }
                catch (e: Exception) {
                    e.printStackTrace()
                    continue
                }
            }

            return groups
        }
    }
}
