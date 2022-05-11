package com.example.submissionintermedieteakhir.ui.main

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionintermedieteakhir.R
import com.example.submissionintermedieteakhir.adapter.ListStoryAdapter
import com.example.submissionintermedieteakhir.adapter.LoadingStateAdapter
import com.example.submissionintermedieteakhir.databinding.ActivityMainBinding
import com.example.submissionintermedieteakhir.ui.login.LoginActivity
import com.example.submissionintermedieteakhir.ui.map.MapsActivity
import com.example.submissionintermedieteakhir.ui.story.StoryActivity
import com.example.submissionintermedieteakhir.ui.viewmodelfactory.StoryVMF

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvStory.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvStory.layoutManager = LinearLayoutManager(this)
        }

        title = getString(R.string.app_name)
        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        val factory: StoryVMF = StoryVMF.getInstance(this)
        mainViewModel = ViewModelProvider(
            this,
            factory
        )[MainViewModel::class.java]
        mainViewModel.getToken().observe(this){ token ->
            this.token = token
            if (token.isNotEmpty()){
                val adapter = ListStoryAdapter()
                binding.rvStory.adapter = adapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        adapter.retry()
                    }
                )
                mainViewModel.getStories(token).observe(this){result ->
                    adapter.submitData(lifecycle, result)
                }
            }
        }

        mainViewModel.isLogin().observe(this){
            if (!it){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

    }
    private fun setupAction() {
        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, StoryActivity::class.java)
            intent.putExtra(StoryActivity.EXTRA_TOKEN, token)
            startActivity(intent)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.items_menu, menu)

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                mainViewModel.logout()
                true
            }
            R.id.map_menu -> {
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra(MapsActivity.EXTRA_TOKEN, token)
                startActivity(intent)
                true
            }
            R.id.setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else -> true
        }
    }
}