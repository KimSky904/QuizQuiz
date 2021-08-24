package com.example.quizquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class QuizMainActivity : AppCompatActivity() {

    lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_main_activity)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout);
        val navView = findViewById<NavigationView>(R.id.drawer_nav_view);

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frame,QuizFragment())
        transaction.commit()




        navView.setNavigationItemSelectedListener{
            when(it.itemId){
                R.id.quiz_solve ->  supportFragmentManager.beginTransaction().replace(R.id.frame,QuizFragment()).commit()
                R.id.quiz_manage ->  supportFragmentManager.beginTransaction().replace(R.id.frame,QuizListFragment()).commit()
            }
            drawerLayout.closeDrawers()
            true
        }

        //햄버거 아이콘
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.drawer_open,
            R.string.drawer_close
        ){}

        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}