package com.example.bugyourbrainapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bugyourbrainapp.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        var questionModelList : List<QuestionModel> = listOf()



        //BACKEND 36:15-39:52

    }

    lateinit var binding: ActivityQuizBinding

    var currentQuestionIndex = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
           //BACKEND STARTING AT 40:22 - 46:28
        }
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loadQuestions() {
        binding.apply {
//            33:03
            questionIndicatorTextView.text = "Question ${currentQuestionIndex+1}/ ${questionModelList.size} "
            questionProgressIndicator.progress =
                (currentQuestionIndex.toFloat() / questionModelList.size.toFloat() * 100).toInt()
            questionTextview.text = questionModelList[currentQuestionIndex].question

            btn0.text = questionModelList[currentQuestionIndex].options[0]
            btn1.text = questionModelList[currentQuestionIndex].options[1]
            btn3.text = questionModelList[currentQuestionIndex].options[3]
            btn4.text = questionModelList[currentQuestionIndex].options[4]
        }

    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

    // BACKEND Starting from 49:30
}