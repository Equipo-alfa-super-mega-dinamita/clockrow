package co.edu.unal.clockrow.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import co.edu.unal.clockrow.R
import co.edu.unal.clockrow.view.activity.Task.TaskActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var buttonTask: ImageButton? = null
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testFirebase()
        configView()

        // Sidebar

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.openSidebar, R.string.closeSidebar)
        drawerLayout.addDrawerListener(toggle!!)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)


        navigationView.setNavigationItemSelectedListener {
            when (it.itemId){
                R.id.project_item -> sendToTasks()
                R.id.analytics_item -> Toast.makeText(applicationContext, "Analytics", Toast.LENGTH_SHORT).show()
                R.id.login_item -> sendToLogin()
                R.id.logout_item -> logout()
            }
            true
        }


        intent?.let {
            val source = it.getStringExtra("SOURCE")
            when (source){
                "SIGNUP" -> handleSignUp(it)
                "LOGIN" -> handleLogin(it)
            }
            null
        }

    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        onBackPressed()
        val sharedPref= getSharedPreferences( getString(R.string.shrpref_file), Context.MODE_PRIVATE).edit()
        sharedPref.clear()
        sharedPref.apply()



    }


    private fun handleLogin(intent: Intent) {
        val email = intent.getStringExtra("EMAIL") ?: ""
        val provider = intent.getStringExtra("PROVIDER") ?: ""
        Toast.makeText(applicationContext, "Email $email, Provider $provider" , Toast.LENGTH_SHORT).show()

        val sharedPref= getSharedPreferences( getString(R.string.shrpref_file), Context.MODE_PRIVATE)

        with (sharedPref.edit()) {
            putString(getString(R.string.user_email_shrpref), email)
            putString(getString(R.string.user_provider_shrpref), provider)
            apply()
        }

    }

    private fun handleSignUp(intent: Intent) {
        val email = intent.getStringExtra("EMAIL") ?: ""
        val provider = intent.getStringExtra("PROVIDER") ?: ""
        Toast.makeText(applicationContext, "Email $email, Provider $provider" , Toast.LENGTH_SHORT).show()

        val sharedPref= getSharedPreferences( getString(R.string.shrpref_file), Context.MODE_PRIVATE)

        with (sharedPref.edit()) {
            putString(getString(R.string.user_email_shrpref), email)
            putString(getString(R.string.user_provider_shrpref), provider)
            apply()
        }

    }

    private fun sendToLogin() {

        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)

    }

    private fun sendToTasks() {

        val intent = Intent(this@MainActivity, TaskActivity::class.java)

        intent.putExtra(EXTRA_MESSAGE, "holo")
        startActivity(intent)

    }


    private fun testFirebase() {
        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("msg", "Conexión correcta con Firebase Analytics")
        analytics.logEvent("InitScreen", bundle)
    }

    private fun configView() {

        buttonTask = findViewById(R.id.buttonTask)
        buttonTask?.setOnClickListener(this)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonTask -> {
                Toast.makeText(applicationContext, "Hola buenas tardes", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val EXTRA_MESSAGE = "co.edu.unal.clockrow.MESSAGE"
    }
}