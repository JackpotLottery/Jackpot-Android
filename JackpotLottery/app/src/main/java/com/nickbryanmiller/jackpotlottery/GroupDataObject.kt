package com.nickbryanmiller.jackpotlottery

import org.json.JSONObject
import org.json.JSONArray

class GroupDataObject {

    internal var name: String? = ""
    internal var id: String? = ""
    internal var password: String? = ""
    internal var description: String? = ""
    internal var admins: ArrayList<String>? = null

    internal constructor() {

    }
    internal constructor(groupJSON: JSONObject) {
        name = groupJSON.getString("name")
        id = groupJSON.getString("_id")
        password = groupJSON.getString("password")
        description = groupJSON.getString("description")
    }

    companion object {
        internal fun fromJson(jsonArray: JSONArray): ArrayList<GroupDataObject> {
            val groups = ArrayList<GroupDataObject>(jsonArray.length())
            for (i in 0..jsonArray.length() - 1) {
                var groupJson: JSONObject? = null
                try {
                    groupJson = jsonArray.getJSONObject(i)
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
