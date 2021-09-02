package com.example.quizquiz

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.quizquiz.database.Quiz
import com.example.quizquiz.database.QuizDatabase

class QuizFragment : Fragment(),QuizStartFragment.QuizStartListener,QuizSolveFragment.QuizSolveListener {

    lateinit var db : QuizDatabase
    lateinit var quizList : List<Quiz>
    var currentQuizIdx = 0
    var correctCount = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.quiz_fragment, container, false)

        db = QuizDatabase.getInstance(requireContext())

        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container,QuizStartFragment())
            .commit()

        return view
    }

    override fun onQuizStart(){
        AsyncTask.execute{
            currentQuizIdx = 0
            correctCount = 0

            quizList = db.quizDAO().getAll()
            childFragmentManager.beginTransaction().replace(R.id.fragment_container,
                QuizSolveFragment.newInstance(quizList[currentQuizIdx])).commit()

        }
    }

    override fun onAnswerSelected(isCorrect: Boolean) {
        if(isCorrect) {
            correctCount++
            currentQuizIdx ++
        }
        if(currentQuizIdx == quizList.size){
            Toast.makeText(activity,"맞춘 개수${correctCount}",Toast.LENGTH_SHORT).show()
        }
        else{
            childFragmentManager.beginTransaction().replace(R.id.fragment_container,QuizSolveFragment.newInstance(quizList[currentQuizIdx])).commit()
        }
    }
}
