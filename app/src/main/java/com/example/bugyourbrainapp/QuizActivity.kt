package com.example.bugyourbrainapp

import android.content.IntentSender.OnFinished
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bugyourbrainapp.databinding.ActivityQuizBinding
import com.example.bugyourbrainapp.databinding.ScoreDialogBinding
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.min
import android.media.MediaPlayer
class QuizActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        var questionModelList : List<QuestionModel> = listOf()
        var time : String = ""
    }

    lateinit var binding: ActivityQuizBinding
    var mediaPlayer: MediaPlayer? = null
    var username = ""
    var quizTitle = ""
    var currentQuestionIndex = 0;
    var selectedAnswer = ""
    var score = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mediaPlayer = MediaPlayer.create(this, R.raw.quiz_start)
        mediaPlayer?.start()
        binding.apply {
            btn0.setOnClickListener(this@QuizActivity)
            btn1.setOnClickListener(this@QuizActivity)
            btn2.setOnClickListener(this@QuizActivity)
            btn3.setOnClickListener(this@QuizActivity)
            nextBtn.setOnClickListener(this@QuizActivity)
        }
        quizTitle = intent.getStringExtra("quizTitle") ?: "Unknown Quiz"
        username = intent.getStringExtra("username") ?: "Guest"
        Log.d("QuizActivity", "Received username: $username")
        loadQuestions()
        startTimer()
    }

    private fun startTimer() {
        val totalTimeInMillis = time.toInt() * 60 * 1000L
        object : CountDownTimer(totalTimeInMillis,1000L){
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished /1000
                val minutes = seconds/60
                val remainingSeconds = seconds % 60
                binding.timerIndicatorTextview.text = String.format("%02d:%02d", minutes,remainingSeconds)

            }
            override fun onFinish() {
                //Finish the quiz
            }
        }.start()
    }
    private fun loadQuestions(){
        selectedAnswer = ""
        if(currentQuestionIndex == questionModelList.size){
            finishQuiz()
            return
        }

        binding.apply {
            questionIndicatorTextView.text = "Question ${currentQuestionIndex+1}/ ${questionModelList.size} "
            questionProgressIndicator.progress =
                ( currentQuestionIndex.toFloat() / questionModelList.size.toFloat() * 100 ).toInt()
            questionTextview.text = questionModelList[currentQuestionIndex].question
            btn0.text = questionModelList[currentQuestionIndex].options[0]
            btn1.text = questionModelList[currentQuestionIndex].options[1]
            btn2.text = questionModelList[currentQuestionIndex].options[2]
            btn3.text = questionModelList[currentQuestionIndex].options[3]
        }
    }
    override fun onClick(view : View?) {
        binding.apply {
            btn0.setBackgroundColor(getColor(R.color.pink))
            btn1.setBackgroundColor(getColor(R.color.pink))
            btn2.setBackgroundColor(getColor(R.color.pink))
            btn3.setBackgroundColor(getColor(R.color.pink))
        }

        val clickedBtn = view as Button
        if(clickedBtn.id==R.id.next_btn){
            if(selectedAnswer.isEmpty()){
                Toast.makeText(applicationContext,"Please select answer to continue",Toast.LENGTH_SHORT).show()
                return;
            }
            if(selectedAnswer == questionModelList[currentQuestionIndex].correct){
                score++
                Log.i("Score of quiz",score.toString())
            }
            currentQuestionIndex++
            loadQuestions()
        }else{
            //options button is clicked
            selectedAnswer = clickedBtn.text.toString()
            clickedBtn.setBackgroundColor(getColor(R.color.orange))
        }
    }
    private fun finishQuiz() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        val totalQuestions = questionModelList.size
        val percentage = ((score.toFloat() / totalQuestions.toFloat() ) *100 ).toInt()

        val status = if (percentage > 60) "pass" else "fail"
        saveQuizResultToFirebase(username, score, percentage, status)

        val dialogBinding  = ScoreDialogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            scoreProgressIndicator.progress = percentage
            scoreProgressText.text = "$percentage %"
            if(percentage>60){
                scoreTitle.text = "Congrats! You have passed"
                scoreTitle.setTextColor(Color.BLUE)
            }else{
                scoreTitle.text = "Sorry, but you have failed"
                scoreTitle.setTextColor(Color.RED)
            }
            scoreSubtitle.text = "$score out of $totalQuestions are correct"
            finishBtn.setOnClickListener {
                finish()
            }
        }
        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()

    }
    private fun saveQuizResultToFirebase(username: String, score: Int, percentage: Int, status:String) {
        val database = FirebaseDatabase.getInstance()
        val quizResultsRef = database.getReference("quizResults")

        // Create a new QuizResult object
        val quizResult = QuizResult(username, score, percentage, status, quizTitle)

        // Push the result to the Firebase Realtime Database
        val newResultRef = quizResultsRef.push()
        newResultRef.setValue(quizResult).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("QuizActivity", "Quiz result saved to Firebase.")
            } else {
                Log.d("QuizActivity", "Failed to save quiz result: ${task.exception}")
            }
        }
    }
    override fun onPause() {
        super.onPause()
        mediaPlayer?.release() // Release MediaPlayer resources
        mediaPlayer = null
    }
}