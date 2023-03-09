package com.example.a7minutesworkout

import android.app.Dialog
import android.content.Intent
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
import com.example.a7minutesworkout.databinding.CustomDialogBinding
import pl.droidsonroids.gif.GifDrawable
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityExerciseBinding? = null
    private var restTimer: CountDownTimer?= null
    private var restProgress = 0
    private var restDuration: Int = 11
    private var tts: TextToSpeech?= null
    private var player: MediaPlayer?= null
    private var exerciseAdapter: ExerciseStatusAdapter?= null

    private var exerciseTimer: CountDownTimer?= null
    private var exerciseProgress = 0
    private var exerciseDuration: Int = 31
    private var isRest = true
    private var isPaused = false

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
            customDialogForBackButton()       }

        setupRestView()
        setupExerciseStatusRecyclerView()
        binding?.flProgressbarExercise?.setOnClickListener { onPauseClicked() }
        binding?.flProgressbarRest?.setOnClickListener { onPauseClicked() }
        binding?.ivPrev?.setOnClickListener { onLeftClicked() }
        binding?.ivNext?.setOnClickListener { onRightClicked() }
    }

    private fun onPauseClicked(){
        if(isPaused) {
            if (isRest)
                setRestProgressBar()
            else{
                setExerciseProgressBar()
            }
        }
        else
            stop()
        isPaused = !isPaused
    }

    override fun onBackPressed() {
        customDialogForBackButton()
    }
    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        val dialogBinding = CustomDialogBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        stop()
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnConfirm?.setOnClickListener{
            this@ExerciseActivity.finish()
        }
        dialogBinding.btnCancel?.setOnClickListener{
            customDialog.dismiss()
            if(isRest){
                setRestProgressBar()
            }
            else{
            setExerciseProgressBar()
            }
        }
        customDialog.show()
    }


    private fun onLeftClicked(){
        for(item:ExerciseModel in exerciseList!!){
            if(!item.getIsCompleted()){
                item.setIsSelected(false)
                item.setIsCompleted(false)
            }
            if(item.getIsCompleted()&& item.getIsSelected()){
                item.setIsSelected(false)
                item.setIsCompleted(true)
            }
        }
        if(currentExercisePosition>0){
        currentExercisePosition-=1
        setupExerciseView()}

        exerciseList!![currentExercisePosition].setIsSelected(true)
        exerciseAdapter!!.notifyDataSetChanged()
    }
    private fun onRightClicked(){
        for(item:ExerciseModel in exerciseList!!){
            if(!item.getIsCompleted()){
                item.setIsSelected(false)
                item.setIsCompleted(false)
            }
            if(item.getIsCompleted()&& item.getIsSelected()){
                item.setIsSelected(false)
                item.setIsCompleted(true)
            }
        }
        if(currentExercisePosition< exerciseList!!.size-1){
            currentExercisePosition+=1
            setupExerciseView()
            exerciseList!![currentExercisePosition].setIsSelected(true)
            exerciseAdapter!!.notifyDataSetChanged()
        }
    }
    private fun stop(){
        if(isRest){
            restProgress = restDuration - binding?.tvTimerRest?.text.toString().toInt()
            restTimer?.cancel()
        }
        else{
            exerciseProgress = exerciseDuration - binding?.tvTimerExercise?.text.toString().toInt()
            exerciseTimer?.cancel()

        }
    }
    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.UK)
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            }

        }
        else{
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
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

    private fun setRestVisibility(){
        binding?.flProgressbarRest?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.exerciseGif?.visibility = View.INVISIBLE
        binding?.flProgressbarExercise?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvExerciseExName?.visibility = View.INVISIBLE
        binding?.ivNext?.visibility = View.INVISIBLE
        binding?.ivPrev?.visibility = View.INVISIBLE
    }
    private fun setExerciseVisibility(){
        binding?.flProgressbarRest?.visibility = View.INVISIBLE
        binding?.exerciseGif?.visibility = View.VISIBLE
        binding?.flProgressbarExercise?.visibility = View.VISIBLE
        binding?.tvExerciseExName?.visibility = View.VISIBLE
        binding?.ivPrev?.visibility = View.VISIBLE
        binding?.ivNext?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility= View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
    }
    private fun setupRestView(){
        sound(R.raw.press_start)
        setRestVisibility()
        isRest = true
        speakOut("Next exercise is "+
                exerciseList!![currentExercisePosition].getName()
        )
        if(restTimer!= null) {
            restTimer?.cancel()
            restProgress = 0
        }
        binding?.tvTitle?.text =" Get ready for "
        binding?.tvExerciseName?.text =
             exerciseList!![currentExercisePosition].getName()
        if(currentExercisePosition % 3 == 0 && currentExercisePosition > 0)
            restDuration = 26
        else
            restDuration = 11
        setRestProgressBar()

    }

    private fun setupExerciseView(){
        sound(R.raw.end_exercise)
        setExerciseVisibility()
        binding?.tvExerciseExName?.text = exerciseList!![currentExercisePosition].getName()
        isRest = false
        if(exerciseTimer!= null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        binding?.exerciseGif?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        setExerciseProgressBar()

    }

    private fun setRestProgressBar(){
        binding?.progressbarRest?.progress = restProgress
        binding?.tvTimerRest?.text = (restDuration- restProgress).toString()
        restTimer = object : CountDownTimer(((restDuration - restProgress)*1000).toLong(), 1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressbarRest?.progress = restDuration - restProgress
                binding?.tvTimerRest?.text = (restDuration - restProgress).toString()
            }

            override fun onFinish() {
                restProgress = 0
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
        binding?.exerciseGif?.visibility = View.VISIBLE
        exerciseTimer = object : CountDownTimer(((exerciseDuration - exerciseProgress)*1000).toLong(), 1000){
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
                if(currentExercisePosition < exerciseList?.size!!.minus(1) )
                    setupRestView()
                else{
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
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