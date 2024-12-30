package com.example.bugyourbrainapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bugyourbrainapp.databinding.ActivityUsernameBinding


class username : AppCompatActivity() {
    lateinit var binding: ActivityUsernameBinding
    private var quizTitle: String = " "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_username)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = ActivityUsernameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        quizTitle = intent.getStringExtra("quizTitle") ?: "Unknown Quiz"
        binding.buttonNext.setOnClickListener {
            val username = binding.editTextName.text.toString()

            if (username.isNotEmpty()) {
                val intent = Intent(this, QuizActivity::class.java)
                intent.putExtra("username", username)
                intent.putExtra("quizTitle", quizTitle)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
            }
        }
    }
}