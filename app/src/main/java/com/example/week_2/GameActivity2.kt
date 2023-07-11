package com.example.week_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class GameActivity2 : AppCompatActivity() {

    private var kid: String? = null
    private var opponent: String? = null
    var turn: Int? = null
    private var choosed_card: Int? = 8
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

    val mhand: MutableList<Int> = mutableListOf() // id(index) - tid 관계
    var ohand: MutableList<Int> = mutableListOf() // id(index) - tid 관계
    val down: List<Int> = List(26) { 0 } // tid - open 관계

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game2)

        val m0 = findViewById<ImageView>(R.id.mc_0)
        val m1 = findViewById<ImageView>(R.id.mc_1)
        val m2 = findViewById<ImageView>(R.id.mc_2)
        val m3 = findViewById<ImageView>(R.id.mc_3)
        val m4 = findViewById<ImageView>(R.id.mc_4)
        val m5 = findViewById<ImageView>(R.id.mc_5)
        val m6 = findViewById<ImageView>(R.id.mc_6)
        val m7 = findViewById<ImageView>(R.id.mc_7)
        val m8 = findViewById<ImageView>(R.id.mc_8)
        val m9 = findViewById<ImageView>(R.id.mc_9)
        val m10 = findViewById<ImageView>(R.id.mc_10)
        val m11 = findViewById<ImageView>(R.id.mc_11)
        val m12 = findViewById<ImageView>(R.id.mc_12)

        val b0 = findViewById<ImageView>(R.id.oc_0)
        val b1 = findViewById<ImageView>(R.id.oc_1)
        val b2 = findViewById<ImageView>(R.id.oc_2)
        val b3 = findViewById<ImageView>(R.id.oc_3)
        val b4 = findViewById<ImageView>(R.id.oc_4)
        val b5 = findViewById<ImageView>(R.id.oc_5)
        val b6 = findViewById<ImageView>(R.id.oc_6)
        val b7 = findViewById<ImageView>(R.id.oc_7)
        val b8 = findViewById<ImageView>(R.id.oc_8)
        val b9 = findViewById<ImageView>(R.id.oc_9)
        val b10 = findViewById<ImageView>(R.id.oc_10)
        val b11 = findViewById<ImageView>(R.id.oc_11)
        val b12 = findViewById<ImageView>(R.id.oc_12)

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
            //상대 턴임을 알려주는 무언가



            val repeatingTask = MyRepeatingTask(this)
            repeatingTask.remainTime = 30000 // 30초로 설정
            repeatingTask.startRepeatingTask()

            //yourhand update
            val Client2 = OkHttpClient() // OkHttpClient 인스턴스 생성
            val json=JSONObject()
            json.put("opponent",opponent)
            val requestBody = json.toString().toRequestBody("application/json".toMediaType())

            val Request2 = Request.Builder()
                .url("https://4278-192-249-19-234.ngrok-free.app/yourhand")
                .post(requestBody)
                .build()
            Client2.newCall(Request2).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body?.string()
                    // 응답 데이터 처리
                    val jsonArray = JSONArray(responseData)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val tileid = jsonObject.getString("tileid")
                        val down = jsonObject.getInt("down")
                        updateyourhand(i, tileid, down)
                    }
                }
            })
            if(turn == 1) {
                for(i in 0 until 4) {
                    selectCard()
                }
            }
        }
        updatemyhand()
        for(i in 0 until 4) {
            sendmhand(i)
        }
        turnchange()
        //게임 시작
        while(m_remain!=0 && o_remain!=0) {
            if(turn==1) {
                if(choosed_card!! >= 26) {
                    selectCard()
                }
                //맞히기 / 정답일 때, 오답일 때 구분
                guess()
            }
            else {

            }
        }
        //게임 종료 시 while문 탈출
        if() {
            //패배했습니다
            //확인 누르면 finish() : 액티비티 종료
        }
        else {
            //승리했습니다
            //확인 누르면 finish() : 액티비티 종료
        }

    }
    private fun updatemyhand() {
        mhand.sort()
        for(i in 0 until mhand.size) {
        }
    }
    private fun updateyourhand(i: Int, tileid: String, down: Int) {
        ohand.sort()
        for(i in 0 until ohand.size) {
        }
    }
    private fun turnchange() {
        val json = JSONObject()
        json.put("opponent", opponent)
        val requestBody = json.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("https://4278-192-249-19-234.ngrok-free.app/turnend")
            .post(requestBody)
            .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {}
        })
        turn = 0
    }
    private fun sendmhand(i: Int) {
        val mediaType = "application/json".toMediaTypeOrNull()
        val client = OkHttpClient()
        val requestBody = JSONObject().apply {
            put("kid", kid)
            put("id", i)
            put("tileid", mhand[i])
            put("down", down[mhand[i]])
        }
        val request = Request.Builder()
            .url("https://4278-192-249-19-234.ngrok-free.app/hand")
            .post(requestBody.toString().toRequestBody(mediaType))
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {}
        })
    }

    class MyRepeatingTask(gameActivity2: GameActivity2) {
        var remainTime: Long =10000
        private val url = "https://4278-192-249-19-234.ngrok-free.app/myturn" // 요청을 보낼 URL
        private val client = OkHttpClient() // OkHttpClient 인스턴스 생성
        private val handlerThread = HandlerThread("MyHandlerThread") // HandlerThread 생성
        private lateinit var handler: Handler // Handler 선언

        private val runnable = object : Runnable {
            override fun run() {
                remainTime -= 1000
                val request = Request.Builder()
                    .url(url)
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {}
                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful) {
                            val responseData = response.body?.string()
                            // 받은 데이터에서 필요한 값을 추출하여 'turn' 변수에 저장
                            val jsonObject = JSONObject(responseData)
                            val receivedTurn = jsonObject.getInt("turn")
                            // 'turn' 변수를 사용하여 원하는 작업 수행
                            if(receivedTurn == 1) {
                                gameActivity2.turn = 1
                                stopRepeatingTask()
                            }
                        }
                    }
                })
                // 다음 작업 예약
                handler.postDelayed(this, 1000)
            }
        }
        fun startRepeatingTask() {
            handlerThread.start() // HandlerThread 시작
            handler = Handler(Looper.getMainLooper()) // Main Looper 사용
            handler.post(runnable)
        }
        fun stopRepeatingTask() {
            handler.removeCallbacks(runnable)
            handlerThread.quitSafely() // HandlerThread 종료
        }
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
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
                            mhand.add(tileId)
                            dialog.dismiss()
                        }
                    }
                }
            })
        }
        dialog.show()
    }
}