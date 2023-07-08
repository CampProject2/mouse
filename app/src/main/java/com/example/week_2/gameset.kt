package com.example.week_2

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import com.example.week_2.R

class gameset : AppCompatActivity() {

    private lateinit var settingsButton: ImageButton
    private lateinit var gamestartButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameset)

        settingsButton = findViewById(R.id.settingsButton)
        settingsButton.setOnClickListener {
            openSettingsDialog()
        }
        gamestartButton = findViewById(R.id.gamestartButton)
        gamestartButton.setOnClickListener {
            showGameMatchDialog()
        }
    }

    private fun openSettingsDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.settings, null)
        val dialog = Dialog(this)
        dialog.setContentView(dialogView)
        dialog.show()
    }
    private fun showGameMatchDialog() {
        val gameMatchDialog = Dialog(this)
        gameMatchDialog.setContentView(R.layout.gamematch)

        val matchButton: Button = gameMatchDialog.findViewById(R.id.match_button)
        matchButton.setOnClickListener {
            showFriendsIdDialog()
        }

        gameMatchDialog.show()
    }

    private fun showFriendsIdDialog() {
        val friendsIdDialog = Dialog(this)
        friendsIdDialog.setContentView(R.layout.friends_id)
        friendsIdDialog.show()
    }
}