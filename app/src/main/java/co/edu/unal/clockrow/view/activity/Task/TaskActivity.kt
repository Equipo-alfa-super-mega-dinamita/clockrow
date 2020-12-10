    package co.edu.unal.clockrow.view.activity.Task

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.edu.unal.clockrow.R
import co.edu.unal.clockrow.data.Task.TaskViewModel
import co.edu.unal.clockrow.logic.Task
import co.edu.unal.clockrow.view.activity.LoginActivity
import co.edu.unal.clockrow.view.activity.MainActivity
import co.edu.unal.clockrow.view.activity.PremiumActivity
import co.edu.unal.clockrow.view.components.TaskListAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class TaskActivity : AppCompatActivity() {
    private var mTaskViewModel: TaskViewModel? = null
    private val db = FirebaseFirestore.getInstance()
    private var taskList : List<Task?>? = null
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview_task)
        val adapter = TaskListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        taskList = emptyList()
        mTaskViewModel!!.allTasks.observe(this, { tasks: List<Task?>? ->
            adapter.setTasks(tasks)
            tasks?.let{ taskList = it.toMutableList()
            backupTasks()
            }
        })

        // Getting the fab
        val fab = findViewById<FloatingActionButton>(R.id.fab_task)
        fab.setOnClickListener { view: View? ->
            val intent = Intent(this@TaskActivity, AddTaskActivity::class.java)
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE)
        }
        configDrawer()
        configAds()

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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            val task = Task(Objects.requireNonNull(data!!.getStringExtra(AddTaskActivity.EXTRA_REPLY_NAME)),
                    data.getStringExtra(AddTaskActivity.EXTRA_REPLY_DESCR),
                    (Objects.requireNonNull(data.getSerializableExtra(AddTaskActivity.EXTRA_REPLY_DATE)) as Date),
                    Calendar.getInstance().time,
                    data.getFloatExtra(AddTaskActivity.EXTRA_REPLY_DIFFICULTY, 0f),
                    Objects.requireNonNull(data.getStringExtra(AddTaskActivity.EXTRA_REPLY_PRIORITY)))
            mTaskViewModel!!.insert(task)
        } else {
            Toast.makeText(
                    applicationContext,
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show()
        }
    }


    private fun configDrawer(){
        val drawerLayout = findViewById<DrawerLayout>(R.id.taskDrawerLayout)

        toggle = ActionBarDrawerToggle(this@TaskActivity, drawerLayout, R.string.openSidebar, R.string.closeSidebar)
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
            menu.findItem(R.id.logout_item).isVisible = true
            menu.findItem(R.id.login_item).isVisible = false
        }
        else {
            menu.findItem(R.id.login_item).isVisible = true
            menu.findItem(R.id.logout_item).isVisible = false
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        onBackPressed()
        val sharedPref= getSharedPreferences( getString(R.string.shrpref_file), Context.MODE_PRIVATE).edit()
        sharedPref.clear()
        sharedPref.apply()

    }


    private fun sendToPremium() {
        Toast.makeText(applicationContext, "Premium", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@TaskActivity, PremiumActivity::class.java)

        startActivity(intent)
    }

    private fun sendToLogin() {

        val intent = Intent(this@TaskActivity, LoginActivity::class.java)
        startActivity(intent)

    }

    private fun sendToTasks() {

        val intent = Intent(this@TaskActivity, TaskActivity::class.java)

        intent.putExtra(MainActivity.EXTRA_MESSAGE, "holo")
        startActivity(intent)

    }


    private fun backupTasks(){

        val tasksSnapshot = taskList?.toMutableList()
        val email =  getSharedPreferences( getString(R.string.shrpref_file), Context.MODE_PRIVATE).getString( getString(R.string.user_email_shrpref), null)
        val premium =  getSharedPreferences( getString(R.string.shrpref_file), Context.MODE_PRIVATE).getBoolean( getString(R.string.user_premium_shrpref), false)



        if (!premium) return //Premium feature.
        if (email != null) {
            Log.i("TASKS", "PIPO")
            if (tasksSnapshot != null && tasksSnapshot.size > 0) {
                for (item in tasksSnapshot) {
                    Log.i("TASKS", item.toString())
                    if (item != null){
                        db.collection("tasks").document("${email}_${item.id}").set(item)
                    }
                }
            }
        }

    }


    companion object {
        const val NEW_WORD_ACTIVITY_REQUEST_CODE = 1
    }


}