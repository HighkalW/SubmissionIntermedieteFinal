package com.example.submissionintermedieteakhir.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.submissionintermedieteakhir.R
import com.example.submissionintermedieteakhir.ui.main.MainActivity

class SplashScreen : AppCompatActivity() {

    private lateinit var topAnimation: Animation
    private lateinit var buttonAnimation: Animation
    private lateinit var imageGithub: ImageView
    private lateinit var imageLogo : ImageView
    private lateinit var slogan : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        buttonAnimation = AnimationUtils.loadAnimation(this, R.anim.button_animation)

        imageGithub = findViewById(R.id.github_icon)
        imageLogo = findViewById(R.id.github_icon_text)
        slogan = findViewById(R.id.slogan_text)

        imageGithub.startAnimation(topAnimation)
        imageLogo.startAnimation(buttonAnimation)
        slogan.startAnimation(buttonAnimation)
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}