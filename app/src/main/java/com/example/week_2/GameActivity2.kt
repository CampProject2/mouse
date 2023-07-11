package com.example.week_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.HandlerThread
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class GameActivity2 : AppCompatActivity() {

    private var kid: String? = null
    private var opponent: String? = null
    private var turn: Int? = null
    private var choosed_card: Int? = 8
//    private var bJoker: Int = 0
//    private var wJoker: Int = 0
    private var m_remain: Int = 4
    private var o_remain: Int = 4


    val b0 = findViewById<ImageView>(R.id.b_0)
    val b1 = findViewById<ImageView>(R.id.b_1)
    val b2 = findViewById<ImageView>(R.id.b_2)
    val b3 = findViewById<ImageView>(R.id.b_3)
    val b4 = findViewById<ImageView>(R.id.b_4)
    val b5 = findViewById<ImageView>(R.id.b_5)
    val b6 = findViewById<ImageView>(R.id.b_6)
    val b7 = findViewById<ImageView>(R.id.b_7)
    val b8 = findViewById<ImageView>(R.id.b_8)
    val b9 = findViewById<ImageView>(R.id.b_9)
    val b10 = findViewById<ImageView>(R.id.b_10)
    val b11 = findViewById<ImageView>(R.id.b_11)
    val b12 = findViewById<ImageView>(R.id.b_12)
    val b13 = findViewById<ImageView>(R.id.b_13)
    val b14 = findViewById<ImageView>(R.id.b_14)
    val b15 = findViewById<ImageView>(R.id.b_15)
    val b16 = findViewById<ImageView>(R.id.b_16)
    val b17 = findViewById<ImageView>(R.id.b_17)
    val b18 = findViewById<ImageView>(R.id.b_18)
    val b19 = findViewById<ImageView>(R.id.b_19)
    val b20 = findViewById<ImageView>(R.id.b_20)
    val b21 = findViewById<ImageView>(R.id.b_21)
    val b22 = findViewById<ImageView>(R.id.b_22)
    val b23 = findViewById<ImageView>(R.id.b_23)
    val b24 = findViewById<ImageView>(R.id.b_24)
    val b25 = findViewById<ImageView>(R.id.b_25)

    val hand: MutableList<Int> = mutableListOf() // id(index) - tid 관계
    val open: List<Int> = List(26) { 0 } // tid - open 관계

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game2)

        // 인텐트에서 전달받은 값 추출
        kid = intent.getStringExtra("kid")
        opponent = intent.getStringExtra("opponent")
        turn = intent.getIntExtra("num", 0)
        val opIdTextView = findViewById<TextView>(R.id.op_id)
        opIdTextView.text = opponent

        // 추출한 값 활용하여 게임 화면 구성 또는 게임 로직 처리
        if(turn==1) {
            for(i in 0 until 4) {
                selectCard()
            }
        }
        else {
            val client1 = OkHttpClient() // OkHttpClient 인스턴스 생성
            val handlerThread = HandlerThread("MyHandlerThread") // HandlerThread 생성
            lateinit var handler: android.os.Handler // Handler 선언
            val runnable = object : Runnable {
                override fun run() {
                    // GET 요청 보내기
                    val request = Request.Builder()
                        .url("https://4278-192-249-19-234.ngrok-free.app/myturn")
                        .build()

                    client1.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {}
                        override fun onResponse(call: Call, response: Response) {
                            if (response.isSuccessful) {
                                val responseData = response.body?.string()
                                // 받은 데이터에서 필요한 값을 추출하여 'turn' 변수에 저장
                                val jsonObject = JSONObject(responseData)
                                val receivedTurn = jsonObject.getString("turn")
                                if(kid == receivedTurn) {
                                    for(i in 0 until 4) {
                                        selectCard()
                                    }
                                }
                            }
                        }
                    })
                    // 다음 작업 예약
                    handler.postDelayed(this, 1000)
                }
            }
        }
        f_sorting()
        for(i in 0 until 4) {
            sendhand(i)
        }
        turnchange()
        //게임 시작
        
    }
    private fun turnchange() {

        turn = 0
    }
    private fun sendhand(i: Int) {
        val mediaType = "application/json".toMediaTypeOrNull()
        val client = OkHttpClient()
        val requestBody = JSONObject().apply {
            put("kid", kid)
            put("id", i)
            put("tileid", hand[i])
            put("down", open[hand[i]])
        }
        val request = Request.Builder()
            .url("https://4278-192-249-19-234.ngrok-free.app/hand")
            .post(requestBody.toString().toRequestBody(mediaType))
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    if (!responseData.isNullOrBlank()) {
                        val jsonObject = JSONObject(responseData)
                        val tileId = jsonObject.getInt("tileid")
                        hand.add(tileId)
                    }
                }
            }
        })

        for(i in 0 until hand.size) {

        }
    }
    private fun f_sorting() {
        for(i in 0 until 4) {

        }
    }

    private fun sorting() {

    }
    private fun selectCard() {
        val dialogView = layoutInflater.inflate(R.layout.board, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val requestBody = JSONObject()
        val mediaType = "application/json".toMediaTypeOrNull()
        val client = OkHttpClient()

        b0.setOnClickListener {
            b0.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 0)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b1.setOnClickListener {
            b1.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 1)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b2.setOnClickListener {
            b2.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 2)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b3.setOnClickListener {
            b3.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 3)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b4.setOnClickListener {
            b4.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 4)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b5.setOnClickListener {
            b5.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 5)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b6.setOnClickListener {
            b6.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 6)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b7.setOnClickListener {
            b7.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 7)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b8.setOnClickListener {
            b8.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 8)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b9.setOnClickListener {
            b9.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 9)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b10.setOnClickListener {
            b10.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 10)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b11.setOnClickListener {
            b11.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 11)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b12.setOnClickListener {
            b12.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 12)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b13.setOnClickListener {
            b13.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 13)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b14.setOnClickListener {
            b14.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 14)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b15.setOnClickListener {
            b15.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 15)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b16.setOnClickListener {
            b16.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 16)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b17.setOnClickListener {
            b17.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 17)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }
        b18.setOnClickListener {
            b18.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 18)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b19.setOnClickListener {
            b19.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 19)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b20.setOnClickListener {
            b20.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 20)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b21.setOnClickListener {
            b21.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 21)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b22.setOnClickListener {
            b22.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 22)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b23.setOnClickListener {
            b23.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 23)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b24.setOnClickListener {
            b24.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 24)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }

        b25.setOnClickListener {
            b25.setImageResource(R.drawable.firstcard)
            requestBody.put("position", 25)
            val request = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/chooseTile")
                .post(requestBody.toString().toRequestBody(mediaType))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (!responseData.isNullOrBlank()) {
                            val jsonObject = JSONObject(responseData)
                            val tileId = jsonObject.getInt("tileid")
                            hand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }
        dialog.show()
    }
}