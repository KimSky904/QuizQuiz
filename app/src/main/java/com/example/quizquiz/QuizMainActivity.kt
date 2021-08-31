package com.example.quizquiz

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.quizquiz.database.Quiz
import com.example.quizquiz.database.QuizDatabase
import com.google.android.material.navigation.NavigationView
import org.w3c.dom.Document
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory

class QuizMainActivity : AppCompatActivity() {
    lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var db : QuizDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_main_activity)

        db = QuizDatabase.getInstance(this)

        val sp = getSharedPreferences("pref", Context.MODE_PRIVATE)
        if(sp.getBoolean("initialized",false)){
            initQuizDataFromXMLFile()

            val editor = sp.edit()
            editor.putBoolean("initialized",false)
            editor.commit()
        }

        /* 메인스레드에서 db에 접근하면 안됨 -> anr 발생
        for(quiz in db.quizDAO().getAll()){
            Log.d("myapp",quiz.toString())
        }

         */

        AsyncTask.execute{
            for(quiz in db.quizDAO().getAll()){
                Log.d("myapp",quiz.toString())
            }
        }


        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.drawer_nav_view)

        supportFragmentManager.beginTransaction().add(R.id.frame, QuizFragment()).commit()

        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.quiz_solve -> supportFragmentManager.beginTransaction().replace(R.id.frame, QuizFragment()).commit()
                R.id.quiz_manage -> supportFragmentManager.beginTransaction().replace(R.id.frame, QuizListFragment()).commit()
            }
            drawerLayout.closeDrawers()

            true
        }

        // 햄버거 아이콘 표시하기
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.drawer_open, R.string.drawer_close) {
        }

        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        drawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(drawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    fun initQuizDataFromXMLFile(){
        AsyncTask.execute{
            val stream = assets.open("quizzes.xml")

            val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
            val doc = docBuilder.parse(stream)

            val quizzes = doc.getElementsByTagName("quiz")

            val quizList = mutableListOf<Quiz>()
            for(idx in 0 until quizzes.length){
                val e = quizzes.item(idx) as Element
                val type = e.getAttribute("type")

                val question = e.getElementsByTagName("question").item(0).textContent
                val answer = e.getElementsByTagName("answer").item(0).textContent
                val category = e.getElementsByTagName("category").item(0).textContent

                when(type){
                    "ox" -> {
                        quizList.add(Quiz(
                            type=type,
                            question = question,
                            answer=answer,
                            category=category))
                    }
                    "multiple_choice" -> {
                        val choices = e.getElementsByTagName("choice")
                        val choiceList = mutableListOf<String>()
                        for(idx in 0 until choices.length){
                            choiceList.add(choices.item(idx).textContent)
                        }
                        quizList.add(Quiz(
                            type=type,
                            question = question,
                            answer=answer,
                            category=category,
                            guesses=choiceList))
                    }
                }
            }
            for(quiz in quizList){
                db.quizDAO().insert(quiz)
            }
        }
    }
}








