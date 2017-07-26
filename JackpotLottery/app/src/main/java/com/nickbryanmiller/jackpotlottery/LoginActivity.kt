package com.nickbryanmiller.jackpotlottery

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
    }

    fun onLoginButtonClick(v: View) {
        // login and then navigate
        val user: User = User()
        user.fetchToken(this::login)
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
        createAccount()
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

    private fun login(token: String) {
        val email: TextView = findViewById(R.id.usernameText) as TextView
        val password: TextView = findViewById(R.id.passwordText) as TextView
        JackpotClient.login(email.text.toString(), password.text.toString(), this::authenticationCompletion)
    }

    private fun createAccount() {
        val email: TextView = findViewById(R.id.usernameText) as TextView
        val password: TextView = findViewById(R.id.passwordText) as TextView
        val displayName: TextView = findViewById(R.id.nameText) as TextView
        JackpotClient.signup(email.text.toString(), password.text.toString(), displayName.text.toString(), this::authenticationCompletion)
    }

    private fun authenticationCompletion(user: User) {
        val eventsIntent = Intent(this, EventsActivity::class.java)
        startActivity(eventsIntent)
    }

    override fun onBackPressed() {
        // Don't let them go back to the login page
    }
}