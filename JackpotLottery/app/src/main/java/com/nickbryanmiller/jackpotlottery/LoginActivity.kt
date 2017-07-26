package com.nickbryanmiller.jackpotlottery

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class LoginActivity : AppCompatActivity() {

    internal var jackpotClient: JackpotClient? = null
    internal val user: User = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
    }

    fun onLoginButtonClick(v: View) {
        // login and then navigate
//        val eventsIntent = Intent(this, EventsActivity::class.java)
//        startActivity(eventsIntent)

        user.fetchEvents()
    }

    fun onSignupButtonClick(v: View) {
        val loginButton = findViewById(R.id.loginButton)
        loginButton.visibility = View.GONE
        val nameText = findViewById(R.id.nameText)
        nameText.visibility = View.VISIBLE
        val cancelButton = findViewById(R.id.cancelButton)
        cancelButton.visibility = View.VISIBLE
        val createAccountButton = findViewById(R.id.createAccountButton)
        createAccountButton.visibility = View.VISIBLE
        val signUpButton = findViewById(R.id.signupButton)
        signUpButton.visibility = View.GONE
    }

    fun onCreateAccountButtonClick(v: View) {
        // sign up
    }

    fun onSignupCancelButtonClick(v: View) {
        val loginButton = findViewById(R.id.loginButton)
        loginButton.visibility = View.VISIBLE
        val signUpButton = findViewById(R.id.signupButton)
        signUpButton.visibility = View.VISIBLE
        val nameText = findViewById(R.id.nameText)
        nameText.visibility = View.GONE
        val createAccountButton = findViewById(R.id.createAccountButton)
        createAccountButton.visibility = View.GONE
        val cancelButton = findViewById(R.id.cancelButton)
        cancelButton.visibility = View.GONE
    }

    override fun onBackPressed() {
        // Don't let them go back to the login page
    }
}