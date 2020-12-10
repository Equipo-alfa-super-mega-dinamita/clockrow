package co.edu.unal.clockrow.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import co.edu.unal.clockrow.R
import co.edu.unal.clockrow.view.activity.Task.TaskActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var buttonTask: ImageButton? = null
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testFirebase()
        configView()

        // Sidebar
        configDrawer()

        //
        configAds()


        intent?.let {
            val source = it.getStringExtra("SOURCE")
            when (source){
                "SIGNUP" -> handleSignUp(it)
                "LOGIN" -> handleLogin(it)
            }
            null
        }

    }

    private fun configAds() {
        MobileAds.initialize(this)

        val adBuilder = AdRequest.Builder().build()
        adView.loadAd(adBuilder)

        if (getSharedPreferences( getString(R.string.shrpref_file), Context.MODE_PRIVATE).getBoolean(getString(R.string.user_premium_shrpref), false)) {
            adView.visibility = View.VISIBLE
        }
        else adView.visibility = View.GONE


        getSharedPreferences( getString(R.string.shrpref_file), Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener { sharedPreferences, s ->
            if (s == getString(R.string.user_premium_shrpref)) {
                adView.visibility = if (sharedPreferences.getBoolean(s, false)) View.VISIBLE else View.GONE
            }
        }
    }


    private fun configDrawer(){


        val drawerLayout = findViewById<DrawerLayout>(R.id.mainDrawerLayout)

        toggle = ActionBarDrawerToggle(this@MainActivity, drawerLayout, R.string.openSidebar, R.string.closeSidebar)
        drawerLayout.addDrawerListener(toggle!!)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)


        navigationView.setNavigationItemSelectedListener {
            when (it.itemId){
                R.id.project_item -> sendToTasks()
                R.id.premium_item -> sendToPremium()
                R.id.login_item -> sendToLogin()
                R.id.logout_item -> logout()
            }
            true
        }

        val menu = navigationView.menu
        if ( getSharedPreferences( getString(R.string.shrpref_file), Context.MODE_PRIVATE).getString(getString(R.string.user_email_shrpref), null) != null )
        {
            menu.findItem(R.id.logout_item).isVisible = false
            menu.findItem(R.id.login_item).isVisible = true
        }
        else {
            menu.findItem(R.id.login_item).isVisible = false
            menu.findItem(R.id.logout_item).isVisible = true
        }

        getSharedPreferences( getString(R.string.shrpref_file), Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener { sharedPreferences, s ->

            Log.i("PREFERENCIAS", "$s")
            if (  sharedPreferences.getBoolean(getString(R.string.user_logged_shrpref), false )) {
                if (sharedPreferences.getString(s, null) != null) {
                    Log.i("PREFERENCIAS", "Login habilitado")
                    menu.findItem(R.id.logout_item).isVisible = false
                    menu.findItem(R.id.login_item).isVisible = true
                } else {
                    Log.i("PREFERENCIAS", "Login deshabilitado")
                    menu.findItem(R.id.login_item).isVisible = false
                    menu.findItem(R.id.logout_item).isVisible = true
                }
            }
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
        val username = intent.getStringExtra("USERNAME") ?: ""
        val provider = intent.getStringExtra("PROVIDER") ?: ""
        Toast.makeText(applicationContext, "$username, Email $email, Provider $provider" , Toast.LENGTH_SHORT).show()

        val sharedPref= getSharedPreferences( getString(R.string.shrpref_file), Context.MODE_PRIVATE)

        with (sharedPref.edit()) {
            putString(getString(R.string.user_email_shrpref), email)
            putString(getString(R.string.user_provider_shrpref), provider)
            putString(getString(R.string.user_username_shrpref), username)
            putBoolean(getString(R.string.user_logged_shrpref), true)
            apply()
        }

    }

    private fun handleSignUp(intent: Intent) {
        val email = intent.getStringExtra("EMAIL") ?: ""
        val username = intent.getStringExtra("USERNAME") ?: ""
        val provider = intent.getStringExtra("PROVIDER") ?: ""
        Toast.makeText(applicationContext, "$username, Email $email, Provider $provider" , Toast.LENGTH_SHORT).show()

        val sharedPref= getSharedPreferences( getString(R.string.shrpref_file), Context.MODE_PRIVATE)

        with (sharedPref.edit()) {
            putString(getString(R.string.user_email_shrpref), email)
            putString(getString(R.string.user_provider_shrpref), provider)
            putString(getString(R.string.user_username_shrpref), username)
            putBoolean(getString(R.string.user_logged_shrpref), true)
            apply()
        }

    }

    private fun sendToPremium() {
        Toast.makeText(applicationContext, "Premium", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@MainActivity, PremiumActivity::class.java)

        startActivity(intent)
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
