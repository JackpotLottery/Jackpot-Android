package com.nickbryanmiller.jackpotlottery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*

class ProfileActivity : AppCompatActivity() {

    private var customAdapter: CustomAdapter? = null
    private var mListView: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        title = "Profile"

        val profileNameTextView = findViewById(R.id.profile_name) as TextView
        profileNameTextView.text = User.sharedInstance?.displayName
        val emailTextView = findViewById(R.id.profile_email) as TextView
        emailTextView.text = User.sharedInstance?.email

        customAdapter = CustomAdapter(this, User.sharedInstance!!.getAllGroups())
        mListView = findViewById(R.id.group_list_view) as ListView
        mListView?.adapter = customAdapter

        mListView?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedGroup = User.sharedInstance!!.getAllGroups()[position]
            showGroupIDAlert(selectedGroup.name!!, "Internz2017", "secretz")
        }

        User.sharedInstance?.fetchGroups(this::fetchGroupsCompletion)
    }

    fun onCreateGroupButtonClick(v: View) {
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(12, 12, 12, 12)
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        val groupNameEditText = EditText(linearLayout.context)
        groupNameEditText.hint = "name"
        linearLayout.addView(groupNameEditText, layoutParams)
        val groupPasswordEditText = EditText(linearLayout.context)
        groupPasswordEditText.hint = "password"
        linearLayout.addView(groupPasswordEditText, layoutParams)

        val alert = AlertDialog.Builder(this).create()
        alert.setTitle("Group Manager")
        alert.setView(linearLayout)
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", {
            dialogInterface, i ->
        })
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Create", {
            dialogInterface, i ->
            val group: GroupDataObject = GroupDataObject()
            group.name = groupNameEditText.text.toString()
            group.password = groupPasswordEditText.text.toString()
            group.description = "descriptions for days. I really love descriptions"
            User.sharedInstance!!.createGroup(group, this::createGroupCompletion)
        })
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "Join", {
            dialogInterface, i ->
            val group: GroupDataObject = GroupDataObject()
            group.name = groupNameEditText.text.toString()
            group.password = groupPasswordEditText.text.toString()
            User.sharedInstance!!.joinGroup(group, this::joinGroupCompletion)
        })
        alert.show()
    }

    private fun showGroupIDAlert(name: String, username: String, password: String) {
        val message: String = "Username: $username\nPassword: $password"
        val alert = AlertDialog.Builder(this).create()
        alert.setTitle(name)
        alert.setMessage(message)
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "Edit", {
            dialogInterface, i ->
            Toast.makeText(this, "You clicked on Edit", Toast.LENGTH_SHORT).show()
        })
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "Done", {
            dialogInterface, i ->
            Toast.makeText(this, "You clicked on Done", Toast.LENGTH_SHORT).show()
        })
        alert.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                User.sharedInstance = null
                navigateToLoginPage()
                return true
            }
            else -> {
                // user's action was not recognized. Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun navigateToLoginPage() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
    }

    private fun fetchGroupsCompletion(groups: ArrayList<GroupDataObject>) {
        refreshGroups()
    }
    private fun createGroupCompletion(success: Boolean) {
        if (success) {
            refreshGroups()
            Toast.makeText(this, "Created Group!", Toast.LENGTH_SHORT).show()
        }
    }
    private fun joinGroupCompletion(group: GroupDataObject) {
        refreshGroups()
        Toast.makeText(this, "Created Group!", Toast.LENGTH_SHORT).show()
    }

    private fun refreshGroups() {
        customAdapter = CustomAdapter(this, User.sharedInstance!!.getAllGroups())
        mListView?.adapter = customAdapter
    }
}
