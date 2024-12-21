package com.example.bugyourbrainapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bugyourbrainapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var quizModelList : MutableList<QuizModel>
    lateinit var adapter: QuizListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quizModelList = mutableListOf()
        getDataFromFireBase()

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        // Assuming 'quizModelList' is a list of data you want to display
        adapter = QuizListAdapter(quizModelList)
//        binding.recyclerView.layoutManager = LinearLayoutManager(this)
//        binding.recyclerView.adapter = adapter
    }


    private fun getDataFromFireBase() {
        //dummy data

        val listQuestionModel = mutableListOf<QuestionModel>()
        listQuestionModel.add(QuestionModel("What is android?", mutableListOf("Language", "OS", "Product", "None"), "OS"))
        listQuestionModel.add(QuestionModel("Who owns android", mutableListOf("Apple", "Google", "Samsung", "Microsoft"), "Google"))
        listQuestionModel.add(QuestionModel("Which assistant do android use?", mutableListOf("Siri", "Cortana", "Google Assistant", "Alexa"), "Google Assistant"))

        quizModelList.add(QuizModel("1", "HTML Terms", "All HTML Basics", "10", listQuestionModel))
//        quizModelList.add(QuizModel("2", "PHP Terms", "All PHP Basics", "15", listQuestionModel))
//        quizModelList.add(QuizModel("3", "CSS Terms", "All CSS Basics", "20", listQuestionModel))
//        quizModelList.add(QuizModel("4", "Python Terms", "All Python Basics", "15"))
//        quizModelList.add(QuizModel("5", "C# Terms", "C#", "25"))

        setupRecyclerView()
    }
}