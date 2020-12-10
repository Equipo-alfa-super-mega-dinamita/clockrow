package co.edu.unal.clockrow.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import co.edu.unal.clockrow.R
import co.edu.unal.clockrow.data.Task.TaskViewModel
import co.edu.unal.clockrow.logic.Task
import co.edu.unal.clockrow.view.activity.Task.TaskActivity
import co.edu.unal.clockrow.view.components.TaskListAdapter
import co.edu.unal.clockrow.viewmodel.ClockViewModel
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    lateinit var toggle: ActionBarDrawerToggle
    private var mTaskViewModel: TaskViewModel? = null
    private var taskList: List<Task?>? = null
    private  val TAG = "MainActivity"
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
            when (source) {
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

        if (getSharedPreferences(getString(R.string.shrpref_file), Context.MODE_PRIVATE).getBoolean(getString(R.string.user_premium_shrpref), false)) {
            adView.visibility = View.VISIBLE
        } else adView.visibility = View.GONE


        getSharedPreferences(getString(R.string.shrpref_file), Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener { sharedPreferences, s ->
            if (s == getString(R.string.user_premium_shrpref)) {
                adView.visibility = if (sharedPreferences.getBoolean(s, false)) View.VISIBLE else View.GONE
            }
        }
    }


    private fun configDrawer() {


        val drawerLayout = findViewById<DrawerLayout>(R.id.mainDrawerLayout)

        toggle = ActionBarDrawerToggle(this@MainActivity, drawerLayout, R.string.openSidebar, R.string.closeSidebar)
        drawerLayout.addDrawerListener(toggle!!)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)


        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.project_item -> sendToTasks()
                R.id.premium_item -> sendToPremium()
                R.id.login_item -> sendToLogin()
                R.id.logout_item -> logout()
            }
            true
        }

    }


    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        onBackPressed()
        val sharedPref = getSharedPreferences(getString(R.string.shrpref_file), Context.MODE_PRIVATE).edit()
        sharedPref.clear()
        sharedPref.apply()

    }


    private fun handleLogin(intent: Intent) {
        val email = intent.getStringExtra("EMAIL") ?: ""
        val username = intent.getStringExtra("USERNAME") ?: ""
        val provider = intent.getStringExtra("PROVIDER") ?: ""
        Toast.makeText(applicationContext, "$username, Email $email, Provider $provider", Toast.LENGTH_SHORT).show()

        val sharedPref = getSharedPreferences(getString(R.string.shrpref_file), Context.MODE_PRIVATE)

        with(sharedPref.edit()) {
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
        Toast.makeText(applicationContext, "$username, Email $email, Provider $provider", Toast.LENGTH_SHORT).show()

        val sharedPref = getSharedPreferences(getString(R.string.shrpref_file), Context.MODE_PRIVATE)

        with(sharedPref.edit()) {
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
        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        val compactCalendarView: CompactCalendarView =  findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true)
        //val e: Event = Event(Color.GREEN,1607685860000L,"info")
       // compactcalendar_view.addEvent(e)

        val adapter = TaskListAdapter(this)
        taskList = emptyList()
        mTaskViewModel!!.allTasks.observe(this) { tasks: List<Task?>? ->
            adapter.setTasks(tasks)
            tasks?.let {
                taskList = it.toMutableList()
                backupTasks()
            }
        }

        for (task in taskList!!){
            compactcalendar_view.addEvent(task?.let { createEvent(it) })
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }


    companion object {
        const val EXTRA_MESSAGE = "co.edu.unal.clockrow.MESSAGE"
    }

    private fun createEvent(task: Task): Event {
        val name = task.name
        val date = task.dueDate.time * 1000
        Log.e(TAG, "$name $date")
        return Event(Color.BLUE, date, name)
    }

    private fun backupTasks() {

        val tasksSnapshot = taskList?.toMutableList()
        val email = getSharedPreferences(getString(R.string.shrpref_file), Context.MODE_PRIVATE).getString(getString(R.string.user_email_shrpref), null)
        val premium = getSharedPreferences(getString(R.string.shrpref_file), Context.MODE_PRIVATE).getBoolean(getString(R.string.user_premium_shrpref), false)



        if (!premium) return //Premium feature.
        if (email != null) {
            Log.i("TASKS", "PIPO")
            if (tasksSnapshot != null && tasksSnapshot.size > 0) {
                for (item in tasksSnapshot) {
                    Log.i("TASKS", item.toString())
                    if (item != null) {
                        db.collection("tasks").document("${email}_${item.id}").set(item)
                    }
                }
            }
        }

    }
}

private fun Any.observe(mainActivity: MainActivity, function: (List<Task?>?) -> Unit?) {

}
