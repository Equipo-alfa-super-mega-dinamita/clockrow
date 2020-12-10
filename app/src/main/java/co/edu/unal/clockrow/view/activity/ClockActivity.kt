package co.edu.unal.clockrow.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import co.edu.unal.clockrow.R
import co.edu.unal.clockrow.logic.Task
import co.edu.unal.clockrow.logic.clock.ClockStates
import co.edu.unal.clockrow.logic.clock.TimeControlMethod
import co.edu.unal.clockrow.logic.clock.TimeListener
import co.edu.unal.clockrow.view.components.TaskListAdapter
import co.edu.unal.clockrow.viewmodel.ClockViewModel
import kotlinx.android.synthetic.main.activity_clock.*

class ClockActivity : AppCompatActivity(), View.OnClickListener {
    private var clockText: TextView? = null
    private var methodText: TextView? = null
    private var startButton: Button? = null
    private var pauseButton: Button? = null
    private var resetButton: Button? = null
    private var shiftButton: Button? = null
    private var clockViewModel: ClockViewModel? = null
    private var taskTitle: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock)
        configView()
    }

    private fun configView() {
        //view model
        clockViewModel = ViewModelProvider(this).get(ClockViewModel::class.java)
        clockViewModel!!.setCtx(applicationContext)
        //Buttons & text
        clockText = findViewById(R.id.timeText)
        methodText = findViewById(R.id.methodText)
        startButton = findViewById(R.id.startButtonClock)
        resetButton = findViewById(R.id.resetButtonClock)
        shiftButton = findViewById(R.id.shift)

        //listener
        startButton?.setOnClickListener(this as View.OnClickListener)
        pauseButton?.setOnClickListener(this as View.OnClickListener)
        resetButton?.setOnClickListener(this as View.OnClickListener)
        shiftButton?.setOnClickListener(this)

        //set Initial time
        clockText?.text = clockViewModel!!.currentTime
        methodText!!.text = translateMethodName(clockViewModel!!.method)
        setBackGColor()


        //Task
        val task = intent.getSerializableExtra(TaskListAdapter.TASK_EXTRA_ID) as Task
        taskTitle = findViewById(R.id.task_title)
        taskTitle?.text = task.name
    }

    //Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.method_menu, menu)
        return true
    }


    @SuppressLint("ResourceAsColor")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        clockViewModel!!.setmBreak(false)
        //Select method
        when (item.itemId) {
            R.id.method_pomododoro -> {
                hideButtons()
                setBackGColor()

                clockViewModel!!.method = TimeControlMethod.POMODORO
            }
            R.id.method_saving -> {
                hideButtons()
                setBackGColor()

                clockViewModel!!.method = TimeControlMethod.SAVING_METHOD
            }
            R.id.method_marathon -> {
                hideButtons()
                setBackGColor()

                clockViewModel!!.method = TimeControlMethod.MARATHON
            }
        }
        //Change text
        updateText()
        setBackGColor()
        methodText!!.text = translateMethodName(clockViewModel!!.method)
        return true
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.startButtonClock -> {
                if (clockViewModel!!.isPause) {
                    //Start
                    hideButtons()
                    startButton?.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_baseline_pause_24), null, null, null)
                    clockViewModel!!.start(object : TimeListener<String?> {
                        override fun onTimerResponse(response: String?) {
                            updateText()
                            updateProgressBar()
                        }

                        override fun onTimerFinish(response: String?) {
                            updateText()
                            updateProgressBar()
                            setBackGColor()
                            Log.d(TAG, clockViewModel!!.currentTime)
                        }
                    })
                } else {
                    //Pause
                    showButtons()
                    startButton?.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_baseline_play_arrow_2), null, null, null)
                    clockViewModel!!.pause()
                    updateText()
                    updateProgressBar()
                }
            }
            R.id.resetButtonClock -> {
                hideButtons()
                clockViewModel!!.reset()
                updateText()
                updateProgressBar()
            }
            R.id.shift -> {
                clockViewModel!!.shiftTime()
                updateText()
                updateProgressBar()
                setBackGColor()
                showButtons()
            }
        }
    }

    companion object {
        private const val TAG = "ClockActivity"
    }

    private fun translateMethodName(title: TimeControlMethod): String {
        var textTrans: String = ""
        textTrans = when (title) {
            TimeControlMethod.MARATHON -> getString(R.string.method_marathon)
            TimeControlMethod.POMODORO -> getString(R.string.method_pomododoro)
            TimeControlMethod.SAVING_METHOD -> getString(R.string.method_saving)
        }
        return textTrans
    }

    private fun updateProgressBar() {
        val timeObj: Long
        val timeLeft: Long
        val progress: Long
        when (clockViewModel!!.method) {
            TimeControlMethod.POMODORO -> {
                timeObj = if (clockViewModel!!.pomodoro.currentState == ClockStates.WORK) clockViewModel!!.pomodoro.workTime else clockViewModel!!.pomodoro.breakTime
                timeLeft = clockViewModel!!.pomodoro.timeLeftInMillis
            }
            TimeControlMethod.SAVING_METHOD -> {
                timeObj = if (clockViewModel!!.savingMethod.currentState == ClockStates.WORK) clockViewModel!!.savingMethod.workTime else clockViewModel!!.savingMethod.breakTime
                timeLeft = clockViewModel!!.savingMethod.timeLeftInMillis
            }
            else -> {
                timeObj = if (clockViewModel!!.marathon.currentState == ClockStates.WORK) clockViewModel!!.marathon.workTime else clockViewModel!!.marathon.breakTimeLeftInMillisAux
                timeLeft = clockViewModel!!.marathon.timeLeftInMillis
            }
        }
        progress = 100 - ((timeLeft * 100) / timeObj)
        progress_bar.progress = progress.toInt()
    }

    private fun updateText() {
        clockText!!.text = clockViewModel!!.currentTime
    }

    private fun setBackGColor() {
        when (clockViewModel!!.method) {
            TimeControlMethod.POMODORO -> clockActivity.setBackgroundResource(R.drawable.pomodoro_gradient)
            TimeControlMethod.SAVING_METHOD -> clockActivity.setBackgroundResource(R.drawable.saving_gradient)
            TimeControlMethod.MARATHON -> clockActivity.setBackgroundResource(R.drawable.marathon_gradient)
            else -> clockActivity.setBackgroundColor(getColor(R.color.colorSecondaryDark))
        }
        if (clockViewModel!!.ismBreak()) {
            clockActivity.setBackgroundResource(R.drawable.break_gradient)
        }
    }

    private fun showButtons() {
        if (clockViewModel!!.method != TimeControlMethod.POMODORO) {
            shiftButton?.visibility = View.VISIBLE
        } else {
            shiftButton?.visibility = View.INVISIBLE
        }
        if (!clockViewModel!!.ismBreak()) {
            resetButton?.visibility = View.VISIBLE
        } else {
            resetButton?.visibility = View.INVISIBLE
        }
    }

    private fun hideButtons() {
        if (clockViewModel!!.method != TimeControlMethod.POMODORO) {
            shiftButton?.visibility = View.INVISIBLE
        }
        resetButton?.visibility = View.INVISIBLE
    }
}