package com.nickbryanmiller.jackpotlottery

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*

class ProfileActivity : AppCompatActivity() {

    private var mListView: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        title = "Profile"

        val adapter = CustomAdapter(this, User.sharedInstance.getAllGroups())
        mListView = findViewById(R.id.group_list_view) as ListView
        mListView?.adapter = adapter

        mListView?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedGroup = User.sharedInstance.getAllGroups()[position]
            showGroupIDAlert(selectedGroup.name!!, "Internz2017", "secretz")
        }
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
            Toast.makeText(this, "You clicked on Create", Toast.LENGTH_SHORT).show()
        })
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "Join", {
            dialogInterface, i ->
            Toast.makeText(this, "You clicked on Join", Toast.LENGTH_SHORT).show()
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
}
