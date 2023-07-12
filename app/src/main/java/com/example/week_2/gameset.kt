package com.example.week_2

import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.gson.Gson
import com.kakao.sdk.user.UserApiClient
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class gameset : AppCompatActivity() {

    private lateinit var settingsButton: ImageButton
    private lateinit var gamestartButton: Button
    var kid : Long? = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameset)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                // 사용자 정보 가져오기 실패 시 처리
            } else if (user != null) {
                kid = user.id // 사용자 ID
                // 필요한 정보를 변수로 저장하거나 다른 처리를 수행
                settingsButton = findViewById(R.id.settingsButton)
                settingsButton.setOnClickListener {
                    postKid()
                    getWinrate()
                }
            }
        }
        gamestartButton = findViewById(R.id.gamestartButton)
        gamestartButton.setOnClickListener {
            sendRequest("https://854c-192-249-19-234.ngrok-free.app/match")
        }
    }
    private fun postKid() {
        val client = OkHttpClient()
        // JSON 객체 생성
        val jsonObject = JSONObject()
        jsonObject.put("kakaoId", kid.toString())

        // JSON 문자열 생성
        val jsonBody = jsonObject.toString()

        val requestBody = jsonBody.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("https://854c-192-249-19-234.ngrok-free.app/kakao-account-info")
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 네트워크 요청 실패 시 처리할 작업
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    // 저장 성공 처리
                    println("Kakao account info saved to MySQL database")
                } else {
                    // 저장 실패 처리
                    println("Failed to save Kakao account info to MySQL database.")
                }
            }
        })
    }
    private fun getWinrate() {
        val kakaoId = kid.toString()
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://854c-192-249-19-234.ngrok-free.app/win-loss-count/kakaoId?kakaoId=$kakaoId")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 네트워크 요청 실패 시 처리할 작업
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()

                    if (!responseData.isNullOrBlank()) {
                        val jsonObject = JSONObject(responseData)
                        val win = jsonObject.optInt("totalWins")
                        val lose = jsonObject.optInt("totalLoses")

                        runOnUiThread {
                            // UI 업데이트 작업 수행
                            openSettingsDialog(kakaoId, win, lose)
                        }
                    } else {
                        runOnUiThread {
                            // UI 업데이트 작업 수행
                            println("Win/loss count not found for the given Kakao ID")
                        }
                    }
                } else {
                    runOnUiThread {
                        // UI 업데이트 작업 수행
                        println("Failed to fetch win/lose count from MySQL database.")
                    }
                }
            }
        })
    }

    private fun openSettingsDialog(kakaoId: String, win: Int, lose: Int) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.settings, null)
        val dialog = Dialog(this)
        val kakaoAccountText = dialogView.findViewById<TextView>(R.id.kakaoAccountText)
        val winRateText = dialogView.findViewById<TextView>(R.id.winRateText)

        kakaoAccountText.text = kakaoId

        val winRate = (win.toFloat() / (win.toFloat() + lose.toFloat())) * 100
        winRateText.text = "$win 승 / $lose 패 : $winRate%"

        dialog.setContentView(dialogView)
        dialog.show()
    }
    private fun sendRequest(url: String) {
        val client = OkHttpClient()

        val json=JSONObject()
        json.put("id", kid.toString())
        val requestBody = json.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
//        Log.d("hshs", "test 1")

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 네트워크 요청 실패 시 처리할 작업
            }
            override fun onResponse(call: Call, response: Response) {
                // 응답 받은 후 처리할 작업
//                Log.d("hshs", "test 2")
                if (response.isSuccessful) {
//                    Log.d("hshs", "test 3")
                    val responseData = response.body?.string()
                    if (!responseData.isNullOrBlank()) {
                        val jsonObject = JSONObject(responseData)
                        // 받은 데이터에서 필요한 값을 추출하여 저장
                        val opponent = jsonObject.getString("opponent")
                        val num = jsonObject.getInt("value")
                        // 저장된 값 활용
                        Log.d("ss", opponent)
                        val intent = Intent(this@gameset, GameActivity2::class.java)
                        intent.putExtra("kid", kid.toString())
                        intent.putExtra("opponent", opponent)
                        intent.putExtra("num", num)
                        startActivity(intent)
                    }
                }
            }
        })
    }

}