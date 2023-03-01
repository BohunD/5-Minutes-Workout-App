package com.example.a7minutesworkout

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.databinding.ActivityExerciseBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityExerciseBinding? = null
    private var restTimer: CountDownTimer?= null
    private var restProgress = 0
    private val restDuration: Int = 11
    private var tts: TextToSpeech?= null
    private var player: MediaPlayer?= null
    private var exerciseAdapter: ExerciseStatusAdapter?= null

    private var exerciseTimer: CountDownTimer?= null
    private var exerciseProgress = 0
    private var exerciseDuration: Int = 31
    private var isRest = true

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        tts = TextToSpeech(this, this )
        setSupportActionBar(binding?.toolbarExercise)
        if(supportActionBar!= null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarExercise)

        exerciseList =Constants.defaultExerciseList()

        binding?.toolbarExercise?.setNavigationOnClickListener{
            onBackPressed()        }
        setupRestView()
        setupExerciseStatusRecyclerView()
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.UK)
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
            }

        }
    }

    private fun setupExerciseStatusRecyclerView(){
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun speakOut(text: String){
        tts?.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }

    private fun sound(path:Int){
        try{
            val soundURI = Uri.parse("android.resource://com.example.a7minutesworkout/" +
                    path)
            player = MediaPlayer.create(applicationContext,soundURI)
            player?.isLooping = false
            player?.start()
        }
        catch(e:Exception){
            e.printStackTrace()
        }
    }

    private fun setupRestView(){
        sound(R.raw.press_start)

        binding?.flProgressbarRest?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.exerciseGif?.visibility = View.INVISIBLE
        binding?.flProgressbarExercise?.visibility = View.INVISIBLE
        binding?.tvExerciseExName?.visibility = View.INVISIBLE



        if(restTimer!= null) {
            restTimer?.cancel()
            restProgress = 0
        }
        binding?.tvTitle?.visibility = View.VISIBLE
        if(currentExercisePosition< exerciseList!!.size-1){
            binding?.tvTitle?.text =" Get ready for "
            binding?.tvExerciseName?.text =
                    " ${exerciseList!![currentExercisePosition].getName()}"
            speakOut("Next exercise is "+ "${exerciseList!![currentExercisePosition].getName()}")
        }

        else{
            binding?.tvTitle?.text = "Last exercise"
        }

        setRestProgressBar()

    }

    private fun setupExerciseView(){
        sound(R.raw.end_exercise)
        binding?.flProgressbarRest?.visibility = View.INVISIBLE
        binding?.exerciseGif?.visibility = View.VISIBLE
        binding?.flProgressbarExercise?.visibility = View.VISIBLE
        binding?.tvExerciseExName?.text = exerciseList!![currentExercisePosition].getName()
        binding?.tvExerciseExName?.visibility = View.VISIBLE



        if(exerciseTimer!= null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        binding?.exerciseGif?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvTitle?.visibility= View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        setExerciseProgressBar()

    }

    private fun setRestProgressBar(){
        binding?.progressbarRest?.progress = restProgress
        binding?.tvTimerRest?.text = restDuration.toString()
        restTimer = object : CountDownTimer((restDuration*100).toLong(), 1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressbarRest?.progress = restDuration - restProgress
                binding?.tvTimerRest?.text = (restDuration - restProgress).toString()
            }

            override fun onFinish() {
                if(currentExercisePosition>=0){
                    exerciseList!![currentExercisePosition].setIsSelected(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                }
                if(currentExercisePosition < exerciseList?.size!!)
                    setupExerciseView()

            }

        }.start()
    }

    private fun setExerciseProgressBar(){
        binding?.progressbarExercise?.progress = exerciseProgress
        binding?.tvTimerExercise?.text = exerciseDuration.toString()

        exerciseTimer = object : CountDownTimer((exerciseDuration*100).toLong(), 1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.progressbarExercise?.progress = exerciseDuration - exerciseProgress
                binding?.tvTimerExercise?.text = (exerciseDuration - exerciseProgress).toString()
                if((exerciseDuration - exerciseProgress <=5) && (exerciseDuration - exerciseProgress >0))
                    speakOut(binding?.tvTimerExercise?.text.toString())
            }

            override fun onFinish() {
                exerciseList!![currentExercisePosition].setIsSelected(false)
                exerciseAdapter!!.notifyDataSetChanged()
                exerciseList!![currentExercisePosition].setIsCompleted(true)
                currentExercisePosition+=1

                exerciseProgress =0
                setupRestView()
            }

        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(restTimer!= null) {
            restTimer?.cancel()
            restProgress = 0
        }
        if(exerciseTimer!= null){
            exerciseTimer?.cancel()
            exerciseProgress=0
        }
        if(tts!=null){
            tts?.stop()
            tts?.shutdown()
        }
        if(player!=null)
            player!!.stop()
        binding = null
    }



}