package com.example.week_2

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.widget.Button
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
    private var choosed_card: Int = 8
    private var m_remain: Int = 4
    private var o_remain: Int = 4
    private var g = 0
    private var current_val: Int = 0
    var gNum: Int = 0

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

    val o0 = findViewById<ImageView>(R.id.oc_0)
    val o1 = findViewById<ImageView>(R.id.oc_1)
    val o2 = findViewById<ImageView>(R.id.oc_2)
    val o3 = findViewById<ImageView>(R.id.oc_3)
    val o4 = findViewById<ImageView>(R.id.oc_4)
    val o5 = findViewById<ImageView>(R.id.oc_5)
    val o6 = findViewById<ImageView>(R.id.oc_6)
    val o7 = findViewById<ImageView>(R.id.oc_7)
    val o8 = findViewById<ImageView>(R.id.oc_8)
    val o9 = findViewById<ImageView>(R.id.oc_9)
    val o10 = findViewById<ImageView>(R.id.oc_10)
    val o11 = findViewById<ImageView>(R.id.oc_11)
    val o12 = findViewById<ImageView>(R.id.oc_12)

    val mhand: MutableList<Int> = mutableListOf() // id(index) - tid 관계
    var ohand: MutableList<Int> = mutableListOf() // id(index) - tid 관계
    val down = MutableList<Int>(26) {0} // tid - open 관계

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game2)

        o0.setOnClickListener {
            if(g==1 && turn==1) {
                guess(0)
            }

        }

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
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.yourturn)
            dialog.show()
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
                        val tileid = jsonObject.getInt("tileid")
                        val open = jsonObject.getInt("down")
                        ohand.add(tileid)
                        down[tileid] = open
                        setopcard(i, tileid)
                    }
                }
            })
            if(turn == 1) {
                for(i in 0 until 4) {
                    selectCard()
                }
            }
        }
        //update my hand
        mhand.sort()
        for(i in 0 until mhand.size) {
            openmycard(i, mhand[i])
            sendmhand(i)
        }
        turnchange()
        //게임 시작
        while(m_remain!=0 && o_remain!=0) {
            if(g==0) {
                if(turn==1) {
                    if(choosed_card >= 26) {
                        selectCard()
                        choosed_card++
                        mhand.sort()
                        for(i in 0 until mhand.size) {
                            openmycard(i, mhand[i])
                            sendmhand(i)
                        }
                    }
                    g=1
                }
                else {
                    // 상대 턴임을 알려주는
                    val dialog = Dialog(this)
                    dialog.setContentView(R.layout.yourturn)
                    dialog.show()

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
                                val tileid = jsonObject.getInt("tileid")
                                val open = jsonObject.getInt("down")
                                ohand.add(tileid)
                                down[tileid] = open
                                setopcard(i, tileid)
                            }
                        }
                    })
                    if(m_remain==0 || o_remain==0) { break }
                    if(turn==1) {
                        if(choosed_card >= 26) {
                            selectCard()
                            choosed_card++
                            mhand.sort()
                            for(i in 0 until mhand.size) {
                                openmycard(i, mhand[i])
                                sendmhand(i)
                            }
                        }
                        g=1
                    }
                }
            }
        }
        //게임 종료 시 while문 탈출
        setContentView(R.layout.winlose)
        val winLoseText = findViewById<TextView>(R.id.winlose_text)
        val finishButton = findViewById<Button>(R.id.finish_button)

        if(m_remain==0) { winLoseText.text = "패배했습니다" }
        else { winLoseText.text = "승리했습니다" }
        finishButton.setOnClickListener { finish() }
    }
    class CustomDialog(context: Context) : Dialog(context) {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.checktile)

            val B0 = findViewById<Button>(R.id.B0)
            val B1 = findViewById<Button>(R.id.B1)
            val B2 = findViewById<Button>(R.id.B2)
            val B3 = findViewById<Button>(R.id.B3)
            val B4 = findViewById<Button>(R.id.B4)
            val B5 = findViewById<Button>(R.id.B5)
            val B6 = findViewById<Button>(R.id.B6)
            val B7 = findViewById<Button>(R.id.B7)
            val B8 = findViewById<Button>(R.id.B8)
            val B9 = findViewById<Button>(R.id.B9)
            val B10 = findViewById<Button>(R.id.B10)
            val B11 = findViewById<Button>(R.id.B11)
            val Bj = findViewById<Button>(R.id.Bj)

            B0.setOnClickListener { context.gNum }
        }
    }
    private fun guess(pos: Int) {
        //다이얼로그 띄우기
        val customDialog = CustomDialog(this) // this는 액티비티나 프래그먼트의 context입니다.
        customDialog.show()

        if((ohand[pos] / 2) == gNum) {
            down[ohand[pos]] = 1
            m_remain ++
            o_remain --
        }
        else {
            down[current_val] = 1
        }
        //
        turnchange()
        g=0
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
            b0.alpha = 0.5f
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
        current_val = mhand[mhand.size-1]
        dialog.show()
    }
    private fun openopcard(pos: Int, tid: Int) {
        if(pos==0) {
            if(tid==0) { o0.setImageResource(R.drawable.b0) }
            if(tid==1) { o0.setImageResource(R.drawable.w0) }
            if(tid==2) { o0.setImageResource(R.drawable.b1) }
            if(tid==3) { o0.setImageResource(R.drawable.w1) }
            if(tid==4) { o0.setImageResource(R.drawable.b2) }
            if(tid==5) { o0.setImageResource(R.drawable.w2) }
            if(tid==6) { o0.setImageResource(R.drawable.b3) }
            if(tid==7) { o0.setImageResource(R.drawable.w3) }
            if(tid==8) { o0.setImageResource(R.drawable.b4) }
            if(tid==9) { o0.setImageResource(R.drawable.w4) }
            if(tid==10) { o0.setImageResource(R.drawable.b5) }
            if(tid==11) { o0.setImageResource(R.drawable.w5) }
            if(tid==12) { o0.setImageResource(R.drawable.b6) }
            if(tid==13) { o0.setImageResource(R.drawable.w6) }
            if(tid==14) { o0.setImageResource(R.drawable.b7) }
            if(tid==15) { o0.setImageResource(R.drawable.w7) }
            if(tid==16) { o0.setImageResource(R.drawable.b8) }
            if(tid==17) { o0.setImageResource(R.drawable.w8) }
            if(tid==18) { o0.setImageResource(R.drawable.b9) }
            if(tid==19) { o0.setImageResource(R.drawable.w9) }
            if(tid==20) { o0.setImageResource(R.drawable.b10) }
            if(tid==21) { o0.setImageResource(R.drawable.w10) }
            if(tid==22) { o0.setImageResource(R.drawable.b11) }
            if(tid==23) { o0.setImageResource(R.drawable.w11) }
            if(tid==24) { o0.setImageResource(R.drawable.bj) }
            if(tid==25) { o0.setImageResource(R.drawable.wj) }
        }
        if(pos==1) {
            if(tid==0) { o1.setImageResource(R.drawable.b0) }
            if(tid==1) { o1.setImageResource(R.drawable.w0) }
            if(tid==2) { o1.setImageResource(R.drawable.b1) }
            if(tid==3) { o1.setImageResource(R.drawable.w1) }
            if(tid==4) { o1.setImageResource(R.drawable.b2) }
            if(tid==5) { o1.setImageResource(R.drawable.w2) }
            if(tid==6) { o1.setImageResource(R.drawable.b3) }
            if(tid==7) { o1.setImageResource(R.drawable.w3) }
            if(tid==8) { o1.setImageResource(R.drawable.b4) }
            if(tid==9) { o1.setImageResource(R.drawable.w4) }
            if(tid==10) { o1.setImageResource(R.drawable.b5) }
            if(tid==11) { o1.setImageResource(R.drawable.w5) }
            if(tid==12) { o1.setImageResource(R.drawable.b6) }
            if(tid==13) { o1.setImageResource(R.drawable.w6) }
            if(tid==14) { o1.setImageResource(R.drawable.b7) }
            if(tid==15) { o1.setImageResource(R.drawable.w7) }
            if(tid==16) { o1.setImageResource(R.drawable.b8) }
            if(tid==17) { o1.setImageResource(R.drawable.w8) }
            if(tid==18) { o1.setImageResource(R.drawable.b9) }
            if(tid==19) { o1.setImageResource(R.drawable.w9) }
            if(tid==20) { o1.setImageResource(R.drawable.b10) }
            if(tid==21) { o1.setImageResource(R.drawable.w10) }
            if(tid==22) { o1.setImageResource(R.drawable.b11) }
            if(tid==23) { o1.setImageResource(R.drawable.w11) }
            if(tid==24) { o1.setImageResource(R.drawable.bj) }
            if(tid==25) { o1.setImageResource(R.drawable.wj) }
        }
        if(pos==2) {
            if(tid==0) { o2.setImageResource(R.drawable.b0) }
            if(tid==1) { o2.setImageResource(R.drawable.w0) }
            if(tid==2) { o2.setImageResource(R.drawable.b1) }
            if(tid==3) { o2.setImageResource(R.drawable.w1) }
            if(tid==4) { o2.setImageResource(R.drawable.b2) }
            if(tid==5) { o2.setImageResource(R.drawable.w2) }
            if(tid==6) { o2.setImageResource(R.drawable.b3) }
            if(tid==7) { o2.setImageResource(R.drawable.w3) }
            if(tid==8) { o2.setImageResource(R.drawable.b4) }
            if(tid==9) { o2.setImageResource(R.drawable.w4) }
            if(tid==10) { o2.setImageResource(R.drawable.b5) }
            if(tid==11) { o2.setImageResource(R.drawable.w5) }
            if(tid==12) { o2.setImageResource(R.drawable.b6) }
            if(tid==13) { o2.setImageResource(R.drawable.w6) }
            if(tid==14) { o2.setImageResource(R.drawable.b7) }
            if(tid==15) { o2.setImageResource(R.drawable.w7) }
            if(tid==16) { o2.setImageResource(R.drawable.b8) }
            if(tid==17) { o2.setImageResource(R.drawable.w8) }
            if(tid==18) { o2.setImageResource(R.drawable.b9) }
            if(tid==19) { o2.setImageResource(R.drawable.w9) }
            if(tid==20) { o2.setImageResource(R.drawable.b10) }
            if(tid==21) { o2.setImageResource(R.drawable.w10) }
            if(tid==22) { o2.setImageResource(R.drawable.b11) }
            if(tid==23) { o2.setImageResource(R.drawable.w11) }
            if(tid==24) { o2.setImageResource(R.drawable.bj) }
            if(tid==25) { o2.setImageResource(R.drawable.wj) }
        }
        if(pos==3) {
            if(tid==0) { o3.setImageResource(R.drawable.b0) }
            if(tid==1) { o3.setImageResource(R.drawable.w0) }
            if(tid==2) { o3.setImageResource(R.drawable.b1) }
            if(tid==3) { o3.setImageResource(R.drawable.w1) }
            if(tid==4) { o3.setImageResource(R.drawable.b2) }
            if(tid==5) { o3.setImageResource(R.drawable.w2) }
            if(tid==6) { o3.setImageResource(R.drawable.b3) }
            if(tid==7) { o3.setImageResource(R.drawable.w3) }
            if(tid==8) { o3.setImageResource(R.drawable.b4) }
            if(tid==9) { o3.setImageResource(R.drawable.w4) }
            if(tid==10) { o3.setImageResource(R.drawable.b5) }
            if(tid==11) { o3.setImageResource(R.drawable.w5) }
            if(tid==12) { o3.setImageResource(R.drawable.b6) }
            if(tid==13) { o3.setImageResource(R.drawable.w6) }
            if(tid==14) { o3.setImageResource(R.drawable.b7) }
            if(tid==15) { o3.setImageResource(R.drawable.w7) }
            if(tid==16) { o3.setImageResource(R.drawable.b8) }
            if(tid==17) { o3.setImageResource(R.drawable.w8) }
            if(tid==18) { o3.setImageResource(R.drawable.b9) }
            if(tid==19) { o3.setImageResource(R.drawable.w9) }
            if(tid==20) { o3.setImageResource(R.drawable.b10) }
            if(tid==21) { o3.setImageResource(R.drawable.w10) }
            if(tid==22) { o3.setImageResource(R.drawable.b11) }
            if(tid==23) { o3.setImageResource(R.drawable.w11) }
            if(tid==24) { o3.setImageResource(R.drawable.bj) }
            if(tid==25) { o3.setImageResource(R.drawable.wj) }
        }
        if(pos==4) {
            if(tid==0) { o4.setImageResource(R.drawable.b0) }
            if(tid==1) { o4.setImageResource(R.drawable.w0) }
            if(tid==2) { o4.setImageResource(R.drawable.b1) }
            if(tid==3) { o4.setImageResource(R.drawable.w1) }
            if(tid==4) { o4.setImageResource(R.drawable.b2) }
            if(tid==5) { o4.setImageResource(R.drawable.w2) }
            if(tid==6) { o4.setImageResource(R.drawable.b3) }
            if(tid==7) { o4.setImageResource(R.drawable.w3) }
            if(tid==8) { o4.setImageResource(R.drawable.b4) }
            if(tid==9) { o4.setImageResource(R.drawable.w4) }
            if(tid==10) { o4.setImageResource(R.drawable.b5) }
            if(tid==11) { o4.setImageResource(R.drawable.w5) }
            if(tid==12) { o4.setImageResource(R.drawable.b6) }
            if(tid==13) { o4.setImageResource(R.drawable.w6) }
            if(tid==14) { o4.setImageResource(R.drawable.b7) }
            if(tid==15) { o4.setImageResource(R.drawable.w7) }
            if(tid==16) { o4.setImageResource(R.drawable.b8) }
            if(tid==17) { o4.setImageResource(R.drawable.w8) }
            if(tid==18) { o4.setImageResource(R.drawable.b9) }
            if(tid==19) { o4.setImageResource(R.drawable.w9) }
            if(tid==20) { o4.setImageResource(R.drawable.b10) }
            if(tid==21) { o4.setImageResource(R.drawable.w10) }
            if(tid==22) { o4.setImageResource(R.drawable.b11) }
            if(tid==23) { o4.setImageResource(R.drawable.w11) }
            if(tid==24) { o4.setImageResource(R.drawable.bj) }
            if(tid==25) { o4.setImageResource(R.drawable.wj) }
        }
        if(pos==5) {
            if(tid==0) { o5.setImageResource(R.drawable.b0) }
            if(tid==1) { o5.setImageResource(R.drawable.w0) }
            if(tid==2) { o5.setImageResource(R.drawable.b1) }
            if(tid==3) { o5.setImageResource(R.drawable.w1) }
            if(tid==4) { o5.setImageResource(R.drawable.b2) }
            if(tid==5) { o5.setImageResource(R.drawable.w2) }
            if(tid==6) { o5.setImageResource(R.drawable.b3) }
            if(tid==7) { o5.setImageResource(R.drawable.w3) }
            if(tid==8) { o5.setImageResource(R.drawable.b4) }
            if(tid==9) { o5.setImageResource(R.drawable.w4) }
            if(tid==10) { o5.setImageResource(R.drawable.b5) }
            if(tid==11) { o5.setImageResource(R.drawable.w5) }
            if(tid==12) { o5.setImageResource(R.drawable.b6) }
            if(tid==13) { o5.setImageResource(R.drawable.w6) }
            if(tid==14) { o5.setImageResource(R.drawable.b7) }
            if(tid==15) { o5.setImageResource(R.drawable.w7) }
            if(tid==16) { o5.setImageResource(R.drawable.b8) }
            if(tid==17) { o5.setImageResource(R.drawable.w8) }
            if(tid==18) { o5.setImageResource(R.drawable.b9) }
            if(tid==19) { o5.setImageResource(R.drawable.w9) }
            if(tid==20) { o5.setImageResource(R.drawable.b10) }
            if(tid==21) { o5.setImageResource(R.drawable.w10) }
            if(tid==22) { o5.setImageResource(R.drawable.b11) }
            if(tid==23) { o5.setImageResource(R.drawable.w11) }
            if(tid==24) { o5.setImageResource(R.drawable.bj) }
            if(tid==25) { o5.setImageResource(R.drawable.wj) }
        }
        if(pos==6) {
            if(tid==0) { o6.setImageResource(R.drawable.b0) }
            if(tid==1) { o6.setImageResource(R.drawable.w0) }
            if(tid==2) { o6.setImageResource(R.drawable.b1) }
            if(tid==3) { o6.setImageResource(R.drawable.w1) }
            if(tid==4) { o6.setImageResource(R.drawable.b2) }
            if(tid==5) { o6.setImageResource(R.drawable.w2) }
            if(tid==6) { o6.setImageResource(R.drawable.b3) }
            if(tid==7) { o6.setImageResource(R.drawable.w3) }
            if(tid==8) { o6.setImageResource(R.drawable.b4) }
            if(tid==9) { o6.setImageResource(R.drawable.w4) }
            if(tid==10) { o6.setImageResource(R.drawable.b5) }
            if(tid==11) { o6.setImageResource(R.drawable.w5) }
            if(tid==12) { o6.setImageResource(R.drawable.b6) }
            if(tid==13) { o6.setImageResource(R.drawable.w6) }
            if(tid==14) { o6.setImageResource(R.drawable.b7) }
            if(tid==15) { o6.setImageResource(R.drawable.w7) }
            if(tid==16) { o6.setImageResource(R.drawable.b8) }
            if(tid==17) { o6.setImageResource(R.drawable.w8) }
            if(tid==18) { o6.setImageResource(R.drawable.b9) }
            if(tid==19) { o6.setImageResource(R.drawable.w9) }
            if(tid==20) { o6.setImageResource(R.drawable.b10) }
            if(tid==21) { o6.setImageResource(R.drawable.w10) }
            if(tid==22) { o6.setImageResource(R.drawable.b11) }
            if(tid==23) { o6.setImageResource(R.drawable.w11) }
            if(tid==24) { o6.setImageResource(R.drawable.bj) }
            if(tid==25) { o6.setImageResource(R.drawable.wj) }
        }
        if(pos==7) {
            if(tid==0) { o7.setImageResource(R.drawable.b0) }
            if(tid==1) { o7.setImageResource(R.drawable.w0) }
            if(tid==2) { o7.setImageResource(R.drawable.b1) }
            if(tid==3) { o7.setImageResource(R.drawable.w1) }
            if(tid==4) { o7.setImageResource(R.drawable.b2) }
            if(tid==5) { o7.setImageResource(R.drawable.w2) }
            if(tid==6) { o7.setImageResource(R.drawable.b3) }
            if(tid==7) { o7.setImageResource(R.drawable.w3) }
            if(tid==8) { o7.setImageResource(R.drawable.b4) }
            if(tid==9) { o7.setImageResource(R.drawable.w4) }
            if(tid==10) { o7.setImageResource(R.drawable.b5) }
            if(tid==11) { o7.setImageResource(R.drawable.w5) }
            if(tid==12) { o7.setImageResource(R.drawable.b6) }
            if(tid==13) { o7.setImageResource(R.drawable.w6) }
            if(tid==14) { o7.setImageResource(R.drawable.b7) }
            if(tid==15) { o7.setImageResource(R.drawable.w7) }
            if(tid==16) { o7.setImageResource(R.drawable.b8) }
            if(tid==17) { o7.setImageResource(R.drawable.w8) }
            if(tid==18) { o7.setImageResource(R.drawable.b9) }
            if(tid==19) { o7.setImageResource(R.drawable.w9) }
            if(tid==20) { o7.setImageResource(R.drawable.b10) }
            if(tid==21) { o7.setImageResource(R.drawable.w10) }
            if(tid==22) { o7.setImageResource(R.drawable.b11) }
            if(tid==23) { o7.setImageResource(R.drawable.w11) }
            if(tid==24) { o7.setImageResource(R.drawable.bj) }
            if(tid==25) { o7.setImageResource(R.drawable.wj) }
        }
        if(pos==8) {
            if(tid==0) { o8.setImageResource(R.drawable.b0) }
            if(tid==1) { o8.setImageResource(R.drawable.w0) }
            if(tid==2) { o8.setImageResource(R.drawable.b1) }
            if(tid==3) { o8.setImageResource(R.drawable.w1) }
            if(tid==4) { o8.setImageResource(R.drawable.b2) }
            if(tid==5) { o8.setImageResource(R.drawable.w2) }
            if(tid==6) { o8.setImageResource(R.drawable.b3) }
            if(tid==7) { o8.setImageResource(R.drawable.w3) }
            if(tid==8) { o8.setImageResource(R.drawable.b4) }
            if(tid==9) { o8.setImageResource(R.drawable.w4) }
            if(tid==10) { o8.setImageResource(R.drawable.b5) }
            if(tid==11) { o8.setImageResource(R.drawable.w5) }
            if(tid==12) { o8.setImageResource(R.drawable.b6) }
            if(tid==13) { o8.setImageResource(R.drawable.w6) }
            if(tid==14) { o8.setImageResource(R.drawable.b7) }
            if(tid==15) { o8.setImageResource(R.drawable.w7) }
            if(tid==16) { o8.setImageResource(R.drawable.b8) }
            if(tid==17) { o8.setImageResource(R.drawable.w8) }
            if(tid==18) { o8.setImageResource(R.drawable.b9) }
            if(tid==19) { o8.setImageResource(R.drawable.w9) }
            if(tid==20) { o8.setImageResource(R.drawable.b10) }
            if(tid==21) { o8.setImageResource(R.drawable.w10) }
            if(tid==22) { o8.setImageResource(R.drawable.b11) }
            if(tid==23) { o8.setImageResource(R.drawable.w11) }
            if(tid==24) { o8.setImageResource(R.drawable.bj) }
            if(tid==25) { o8.setImageResource(R.drawable.wj) }
        }
        if(pos==9) {
            if(tid==0) { o9.setImageResource(R.drawable.b0) }
            if(tid==1) { o9.setImageResource(R.drawable.w0) }
            if(tid==2) { o9.setImageResource(R.drawable.b1) }
            if(tid==3) { o9.setImageResource(R.drawable.w1) }
            if(tid==4) { o9.setImageResource(R.drawable.b2) }
            if(tid==5) { o9.setImageResource(R.drawable.w2) }
            if(tid==6) { o9.setImageResource(R.drawable.b3) }
            if(tid==7) { o9.setImageResource(R.drawable.w3) }
            if(tid==8) { o9.setImageResource(R.drawable.b4) }
            if(tid==9) { o9.setImageResource(R.drawable.w4) }
            if(tid==10) { o9.setImageResource(R.drawable.b5) }
            if(tid==11) { o9.setImageResource(R.drawable.w5) }
            if(tid==12) { o9.setImageResource(R.drawable.b6) }
            if(tid==13) { o9.setImageResource(R.drawable.w6) }
            if(tid==14) { o9.setImageResource(R.drawable.b7) }
            if(tid==15) { o9.setImageResource(R.drawable.w7) }
            if(tid==16) { o9.setImageResource(R.drawable.b8) }
            if(tid==17) { o9.setImageResource(R.drawable.w8) }
            if(tid==18) { o9.setImageResource(R.drawable.b9) }
            if(tid==19) { o9.setImageResource(R.drawable.w9) }
            if(tid==20) { o9.setImageResource(R.drawable.b10) }
            if(tid==21) { o9.setImageResource(R.drawable.w10) }
            if(tid==22) { o9.setImageResource(R.drawable.b11) }
            if(tid==23) { o9.setImageResource(R.drawable.w11) }
            if(tid==24) { o9.setImageResource(R.drawable.bj) }
            if(tid==25) { o9.setImageResource(R.drawable.wj) }
        }
        if(pos==10) {
            if(tid==1) { o10.setImageResource(R.drawable.w0) }
            if(tid==2) { o10.setImageResource(R.drawable.b1) }
            if(tid==3) { o10.setImageResource(R.drawable.w1) }
            if(tid==4) { o10.setImageResource(R.drawable.b2) }
            if(tid==5) { o10.setImageResource(R.drawable.w2) }
            if(tid==6) { o10.setImageResource(R.drawable.b3) }
            if(tid==7) { o10.setImageResource(R.drawable.w3) }
            if(tid==8) { o10.setImageResource(R.drawable.b4) }
            if(tid==9) { o10.setImageResource(R.drawable.w4) }
            if(tid==10) { o10.setImageResource(R.drawable.b5) }
            if(tid==11) { o10.setImageResource(R.drawable.w5) }
            if(tid==12) { o10.setImageResource(R.drawable.b6) }
            if(tid==13) { o10.setImageResource(R.drawable.w6) }
            if(tid==14) { o10.setImageResource(R.drawable.b7) }
            if(tid==15) { o10.setImageResource(R.drawable.w7) }
            if(tid==16) { o10.setImageResource(R.drawable.b8) }
            if(tid==17) { o10.setImageResource(R.drawable.w8) }
            if(tid==18) { o10.setImageResource(R.drawable.b9) }
            if(tid==19) { o10.setImageResource(R.drawable.w9) }
            if(tid==20) { o10.setImageResource(R.drawable.b10) }
            if(tid==21) { o10.setImageResource(R.drawable.w10) }
            if(tid==22) { o10.setImageResource(R.drawable.b11) }
            if(tid==23) { o10.setImageResource(R.drawable.w11) }
            if(tid==24) { o10.setImageResource(R.drawable.bj) }
            if(tid==25) { o10.setImageResource(R.drawable.wj) }
        }
        if(pos==11) {
            if(tid==0) { o11.setImageResource(R.drawable.b0) }
            if(tid==1) { o11.setImageResource(R.drawable.w0) }
            if(tid==2) { o11.setImageResource(R.drawable.b1) }
            if(tid==3) { o11.setImageResource(R.drawable.w1) }
            if(tid==4) { o11.setImageResource(R.drawable.b2) }
            if(tid==5) { o11.setImageResource(R.drawable.w2) }
            if(tid==6) { o11.setImageResource(R.drawable.b3) }
            if(tid==7) { o11.setImageResource(R.drawable.w3) }
            if(tid==8) { o11.setImageResource(R.drawable.b4) }
            if(tid==9) { o11.setImageResource(R.drawable.w4) }
            if(tid==10) { o11.setImageResource(R.drawable.b5) }
            if(tid==11) { o11.setImageResource(R.drawable.w5) }
            if(tid==12) { o11.setImageResource(R.drawable.b6) }
            if(tid==13) { o11.setImageResource(R.drawable.w6) }
            if(tid==14) { o11.setImageResource(R.drawable.b7) }
            if(tid==15) { o11.setImageResource(R.drawable.w7) }
            if(tid==16) { o11.setImageResource(R.drawable.b8) }
            if(tid==17) { o11.setImageResource(R.drawable.w8) }
            if(tid==18) { o11.setImageResource(R.drawable.b9) }
            if(tid==19) { o11.setImageResource(R.drawable.w9) }
            if(tid==20) { o11.setImageResource(R.drawable.b10) }
            if(tid==21) { o11.setImageResource(R.drawable.w10) }
            if(tid==22) { o11.setImageResource(R.drawable.b11) }
            if(tid==23) { o11.setImageResource(R.drawable.w11) }
            if(tid==24) { o11.setImageResource(R.drawable.bj) }
            if(tid==25) { o11.setImageResource(R.drawable.wj) }
        }
        if(pos==12) {
            if(tid==0) { o12.setImageResource(R.drawable.b0) }
            if(tid==1) { o12.setImageResource(R.drawable.w0) }
            if(tid==2) { o12.setImageResource(R.drawable.b1) }
            if(tid==3) { o12.setImageResource(R.drawable.w1) }
            if(tid==4) { o12.setImageResource(R.drawable.b2) }
            if(tid==5) { o12.setImageResource(R.drawable.w2) }
            if(tid==6) { o12.setImageResource(R.drawable.b3) }
            if(tid==7) { o12.setImageResource(R.drawable.w3) }
            if(tid==8) { o12.setImageResource(R.drawable.b4) }
            if(tid==9) { o12.setImageResource(R.drawable.w4) }
            if(tid==10) { o12.setImageResource(R.drawable.b5) }
            if(tid==11) { o12.setImageResource(R.drawable.w5) }
            if(tid==12) { o12.setImageResource(R.drawable.b6) }
            if(tid==13) { o12.setImageResource(R.drawable.w6) }
            if(tid==14) { o12.setImageResource(R.drawable.b7) }
            if(tid==15) { o12.setImageResource(R.drawable.w7) }
            if(tid==16) { o12.setImageResource(R.drawable.b8) }
            if(tid==17) { o12.setImageResource(R.drawable.w8) }
            if(tid==18) { o12.setImageResource(R.drawable.b9) }
            if(tid==19) { o12.setImageResource(R.drawable.w9) }
            if(tid==20) { o12.setImageResource(R.drawable.b10) }
            if(tid==21) { o12.setImageResource(R.drawable.w10) }
            if(tid==22) { o12.setImageResource(R.drawable.b11) }
            if(tid==23) { o12.setImageResource(R.drawable.w11) }
            if(tid==24) { o12.setImageResource(R.drawable.bj) }
            if(tid==25) { o12.setImageResource(R.drawable.wj) }
        }
    }
    private fun setopcard(pos: Int, tid: Int) {
        if(down[tid]==1) { openopcard(pos, tid) }
        else {
            if(pos==0) {
                if(tid%2==0) { o0.setImageResource(R.drawable.bb) }
                else { o0.setImageResource(R.drawable.wb) }
            }
            if(pos==1) {
                if (tid % 2 == 0) { o1.setImageResource(R.drawable.bb) }
                else { o1.setImageResource(R.drawable.wb) }
            }
            if(pos==2) {
                if(tid%2==0) { o1.setImageResource(R.drawable.bb) }
                else { o1.setImageResource(R.drawable.wb) }
            }
            if(pos==3) {
                if(tid%2==0) { o1.setImageResource(R.drawable.bb) }
                else { o1.setImageResource(R.drawable.wb) }
            }
            if(pos==4) {
                if(tid%2==0) { o1.setImageResource(R.drawable.bb) }
                else { o1.setImageResource(R.drawable.wb) }
            }
            if(pos==5) {
                if(tid%2==0) { o1.setImageResource(R.drawable.bb) }
                else { o1.setImageResource(R.drawable.wb) }
            }
            if(pos==6) {
                if(tid%2==0) { o1.setImageResource(R.drawable.bb) }
                else { o1.setImageResource(R.drawable.wb) }
            }
            if(pos==7) {
                if (tid % 2 == 0) { o1.setImageResource(R.drawable.bb) }
                else { o1.setImageResource(R.drawable.wb) }
            }
            if(pos==8) {
                if (tid % 2 == 0) { o1.setImageResource(R.drawable.bb) }
                else { o1.setImageResource(R.drawable.wb) }
            }
            if(pos==9) {
                if (tid % 2 == 0) { o1.setImageResource(R.drawable.bb) }
                else { o1.setImageResource(R.drawable.wb) }
            }
            if(pos==10) {
                if (tid % 2 == 0) { o1.setImageResource(R.drawable.bb) }
                else { o1.setImageResource(R.drawable.wb) }
            }
            if(pos==11) {
                if (tid % 2 == 0) { o1.setImageResource(R.drawable.bb) }
                else { o1.setImageResource(R.drawable.wb) }
            }
            if(pos==12) {
                if (tid % 2 == 0) { o1.setImageResource(R.drawable.bb) }
                else { o1.setImageResource(R.drawable.wb) }
            }
        }
    }
    private fun openmycard(pos: Int, tid: Int) {
        if(pos==0) {
            if(tid==0) { m0.setImageResource(R.drawable.b0) }
            if(tid==1) { m0.setImageResource(R.drawable.w0) }
            if(tid==2) { m0.setImageResource(R.drawable.b1) }
            if(tid==3) { m0.setImageResource(R.drawable.w1) }
            if(tid==4) { m0.setImageResource(R.drawable.b2) }
            if(tid==5) { m0.setImageResource(R.drawable.w2) }
            if(tid==6) { m0.setImageResource(R.drawable.b3) }
            if(tid==7) { m0.setImageResource(R.drawable.w3) }
            if(tid==8) { m0.setImageResource(R.drawable.b4) }
            if(tid==9) { m0.setImageResource(R.drawable.w4) }
            if(tid==10) { m0.setImageResource(R.drawable.b5) }
            if(tid==11) { m0.setImageResource(R.drawable.w5) }
            if(tid==12) { m0.setImageResource(R.drawable.b6) }
            if(tid==13) { m0.setImageResource(R.drawable.w6) }
            if(tid==14) { m0.setImageResource(R.drawable.b7) }
            if(tid==15) { m0.setImageResource(R.drawable.w7) }
            if(tid==16) { m0.setImageResource(R.drawable.b8) }
            if(tid==17) { m0.setImageResource(R.drawable.w8) }
            if(tid==18) { m0.setImageResource(R.drawable.b9) }
            if(tid==19) { m0.setImageResource(R.drawable.w9) }
            if(tid==20) { m0.setImageResource(R.drawable.b10) }
            if(tid==21) { m0.setImageResource(R.drawable.w10) }
            if(tid==22) { m0.setImageResource(R.drawable.b11) }
            if(tid==23) { m0.setImageResource(R.drawable.w11) }
            if(tid==24) { m0.setImageResource(R.drawable.bj) }
            if(tid==25) { m0.setImageResource(R.drawable.wj) }
        }
        if(pos==1) {
            if(tid==0) { m1.setImageResource(R.drawable.b0) }
            if(tid==1) { m1.setImageResource(R.drawable.w0) }
            if(tid==2) { m1.setImageResource(R.drawable.b1) }
            if(tid==3) { m1.setImageResource(R.drawable.w1) }
            if(tid==4) { m1.setImageResource(R.drawable.b2) }
            if(tid==5) { m1.setImageResource(R.drawable.w2) }
            if(tid==6) { m1.setImageResource(R.drawable.b3) }
            if(tid==7) { m1.setImageResource(R.drawable.w3) }
            if(tid==8) { m1.setImageResource(R.drawable.b4) }
            if(tid==9) { m1.setImageResource(R.drawable.w4) }
            if(tid==10) { m1.setImageResource(R.drawable.b5) }
            if(tid==11) { m1.setImageResource(R.drawable.w5) }
            if(tid==12) { m1.setImageResource(R.drawable.b6) }
            if(tid==13) { m1.setImageResource(R.drawable.w6) }
            if(tid==14) { m1.setImageResource(R.drawable.b7) }
            if(tid==15) { m1.setImageResource(R.drawable.w7) }
            if(tid==16) { m1.setImageResource(R.drawable.b8) }
            if(tid==17) { m1.setImageResource(R.drawable.w8) }
            if(tid==18) { m1.setImageResource(R.drawable.b9) }
            if(tid==19) { m1.setImageResource(R.drawable.w9) }
            if(tid==20) { m1.setImageResource(R.drawable.b10) }
            if(tid==21) { m1.setImageResource(R.drawable.w10) }
            if(tid==22) { m1.setImageResource(R.drawable.b11) }
            if(tid==23) { m1.setImageResource(R.drawable.w11) }
            if(tid==24) { m1.setImageResource(R.drawable.bj) }
            if(tid==25) { m1.setImageResource(R.drawable.wj) }
        }
        if(pos==2) {
            if(tid==0) { m2.setImageResource(R.drawable.b0) }
            if(tid==1) { m2.setImageResource(R.drawable.w0) }
            if(tid==2) { m2.setImageResource(R.drawable.b1) }
            if(tid==3) { m2.setImageResource(R.drawable.w1) }
            if(tid==4) { m2.setImageResource(R.drawable.b2) }
            if(tid==5) { m2.setImageResource(R.drawable.w2) }
            if(tid==6) { m2.setImageResource(R.drawable.b3) }
            if(tid==7) { m2.setImageResource(R.drawable.w3) }
            if(tid==8) { m2.setImageResource(R.drawable.b4) }
            if(tid==9) { m2.setImageResource(R.drawable.w4) }
            if(tid==10) { m2.setImageResource(R.drawable.b5) }
            if(tid==11) { m2.setImageResource(R.drawable.w5) }
            if(tid==12) { m2.setImageResource(R.drawable.b6) }
            if(tid==13) { m2.setImageResource(R.drawable.w6) }
            if(tid==14) { m2.setImageResource(R.drawable.b7) }
            if(tid==15) { m2.setImageResource(R.drawable.w7) }
            if(tid==16) { m2.setImageResource(R.drawable.b8) }
            if(tid==17) { m2.setImageResource(R.drawable.w8) }
            if(tid==18) { m2.setImageResource(R.drawable.b9) }
            if(tid==19) { m2.setImageResource(R.drawable.w9) }
            if(tid==20) { m2.setImageResource(R.drawable.b10) }
            if(tid==21) { m2.setImageResource(R.drawable.w10) }
            if(tid==22) { m2.setImageResource(R.drawable.b11) }
            if(tid==23) { m2.setImageResource(R.drawable.w11) }
            if(tid==24) { m2.setImageResource(R.drawable.bj) }
            if(tid==25) { m2.setImageResource(R.drawable.wj) }
        }
        if(pos==3) {
            if(tid==0) { m3.setImageResource(R.drawable.b0) }
            if(tid==1) { m3.setImageResource(R.drawable.w0) }
            if(tid==2) { m3.setImageResource(R.drawable.b1) }
            if(tid==3) { m3.setImageResource(R.drawable.w1) }
            if(tid==4) { m3.setImageResource(R.drawable.b2) }
            if(tid==5) { m3.setImageResource(R.drawable.w2) }
            if(tid==6) { m3.setImageResource(R.drawable.b3) }
            if(tid==7) { m3.setImageResource(R.drawable.w3) }
            if(tid==8) { m3.setImageResource(R.drawable.b4) }
            if(tid==9) { m3.setImageResource(R.drawable.w4) }
            if(tid==10) { m3.setImageResource(R.drawable.b5) }
            if(tid==11) { m3.setImageResource(R.drawable.w5) }
            if(tid==12) { m3.setImageResource(R.drawable.b6) }
            if(tid==13) { m3.setImageResource(R.drawable.w6) }
            if(tid==14) { m3.setImageResource(R.drawable.b7) }
            if(tid==15) { m3.setImageResource(R.drawable.w7) }
            if(tid==16) { m3.setImageResource(R.drawable.b8) }
            if(tid==17) { m3.setImageResource(R.drawable.w8) }
            if(tid==18) { m3.setImageResource(R.drawable.b9) }
            if(tid==19) { m3.setImageResource(R.drawable.w9) }
            if(tid==20) { m3.setImageResource(R.drawable.b10) }
            if(tid==21) { m3.setImageResource(R.drawable.w10) }
            if(tid==22) { m3.setImageResource(R.drawable.b11) }
            if(tid==23) { m3.setImageResource(R.drawable.w11) }
            if(tid==24) { m3.setImageResource(R.drawable.bj) }
            if(tid==25) { m3.setImageResource(R.drawable.wj) }
        }
        if(pos==4) {
            if(tid==0) { m4.setImageResource(R.drawable.b0) }
            if(tid==1) { m4.setImageResource(R.drawable.w0) }
            if(tid==2) { m4.setImageResource(R.drawable.b1) }
            if(tid==3) { m4.setImageResource(R.drawable.w1) }
            if(tid==4) { m4.setImageResource(R.drawable.b2) }
            if(tid==5) { m4.setImageResource(R.drawable.w2) }
            if(tid==6) { m4.setImageResource(R.drawable.b3) }
            if(tid==7) { m4.setImageResource(R.drawable.w3) }
            if(tid==8) { m4.setImageResource(R.drawable.b4) }
            if(tid==9) { m4.setImageResource(R.drawable.w4) }
            if(tid==10) { m4.setImageResource(R.drawable.b5) }
            if(tid==11) { m4.setImageResource(R.drawable.w5) }
            if(tid==12) { m4.setImageResource(R.drawable.b6) }
            if(tid==13) { m4.setImageResource(R.drawable.w6) }
            if(tid==14) { m4.setImageResource(R.drawable.b7) }
            if(tid==15) { m4.setImageResource(R.drawable.w7) }
            if(tid==16) { m4.setImageResource(R.drawable.b8) }
            if(tid==17) { m4.setImageResource(R.drawable.w8) }
            if(tid==18) { m4.setImageResource(R.drawable.b9) }
            if(tid==19) { m4.setImageResource(R.drawable.w9) }
            if(tid==20) { m4.setImageResource(R.drawable.b10) }
            if(tid==21) { m4.setImageResource(R.drawable.w10) }
            if(tid==22) { m4.setImageResource(R.drawable.b11) }
            if(tid==23) { m4.setImageResource(R.drawable.w11) }
            if(tid==24) { m4.setImageResource(R.drawable.bj) }
            if(tid==25) { m4.setImageResource(R.drawable.wj) }
        }
        if(pos==5) {
            if(tid==0) { m5.setImageResource(R.drawable.b0) }
            if(tid==1) { m5.setImageResource(R.drawable.w0) }
            if(tid==2) { m5.setImageResource(R.drawable.b1) }
            if(tid==3) { m5.setImageResource(R.drawable.w1) }
            if(tid==4) { m5.setImageResource(R.drawable.b2) }
            if(tid==5) { m5.setImageResource(R.drawable.w2) }
            if(tid==6) { m5.setImageResource(R.drawable.b3) }
            if(tid==7) { m5.setImageResource(R.drawable.w3) }
            if(tid==8) { m5.setImageResource(R.drawable.b4) }
            if(tid==9) { m5.setImageResource(R.drawable.w4) }
            if(tid==10) { m5.setImageResource(R.drawable.b5) }
            if(tid==11) { m5.setImageResource(R.drawable.w5) }
            if(tid==12) { m5.setImageResource(R.drawable.b6) }
            if(tid==13) { m5.setImageResource(R.drawable.w6) }
            if(tid==14) { m5.setImageResource(R.drawable.b7) }
            if(tid==15) { m5.setImageResource(R.drawable.w7) }
            if(tid==16) { m5.setImageResource(R.drawable.b8) }
            if(tid==17) { m5.setImageResource(R.drawable.w8) }
            if(tid==18) { m5.setImageResource(R.drawable.b9) }
            if(tid==19) { m5.setImageResource(R.drawable.w9) }
            if(tid==20) { m5.setImageResource(R.drawable.b10) }
            if(tid==21) { m5.setImageResource(R.drawable.w10) }
            if(tid==22) { m5.setImageResource(R.drawable.b11) }
            if(tid==23) { m5.setImageResource(R.drawable.w11) }
            if(tid==24) { m5.setImageResource(R.drawable.bj) }
            if(tid==25) { m5.setImageResource(R.drawable.wj) }
        }
        if(pos==6) {
            if(tid==0) { m6.setImageResource(R.drawable.b0) }
            if(tid==1) { m6.setImageResource(R.drawable.w0) }
            if(tid==2) { m6.setImageResource(R.drawable.b1) }
            if(tid==3) { m6.setImageResource(R.drawable.w1) }
            if(tid==4) { m6.setImageResource(R.drawable.b2) }
            if(tid==5) { m6.setImageResource(R.drawable.w2) }
            if(tid==6) { m6.setImageResource(R.drawable.b3) }
            if(tid==7) { m6.setImageResource(R.drawable.w3) }
            if(tid==8) { m6.setImageResource(R.drawable.b4) }
            if(tid==9) { m6.setImageResource(R.drawable.w4) }
            if(tid==10) { m6.setImageResource(R.drawable.b5) }
            if(tid==11) { m6.setImageResource(R.drawable.w5) }
            if(tid==12) { m6.setImageResource(R.drawable.b6) }
            if(tid==13) { m6.setImageResource(R.drawable.w6) }
            if(tid==14) { m6.setImageResource(R.drawable.b7) }
            if(tid==15) { m6.setImageResource(R.drawable.w7) }
            if(tid==16) { m6.setImageResource(R.drawable.b8) }
            if(tid==17) { m6.setImageResource(R.drawable.w8) }
            if(tid==18) { m6.setImageResource(R.drawable.b9) }
            if(tid==19) { m6.setImageResource(R.drawable.w9) }
            if(tid==20) { m6.setImageResource(R.drawable.b10) }
            if(tid==21) { m6.setImageResource(R.drawable.w10) }
            if(tid==22) { m6.setImageResource(R.drawable.b11) }
            if(tid==23) { m6.setImageResource(R.drawable.w11) }
            if(tid==24) { m6.setImageResource(R.drawable.bj) }
            if(tid==25) { m6.setImageResource(R.drawable.wj) }
        }
        if(pos==7) {
            if(tid==0) { m7.setImageResource(R.drawable.b0) }
            if(tid==1) { m7.setImageResource(R.drawable.w0) }
            if(tid==2) { m7.setImageResource(R.drawable.b1) }
            if(tid==3) { m7.setImageResource(R.drawable.w1) }
            if(tid==4) { m7.setImageResource(R.drawable.b2) }
            if(tid==5) { m7.setImageResource(R.drawable.w2) }
            if(tid==6) { m7.setImageResource(R.drawable.b3) }
            if(tid==7) { m7.setImageResource(R.drawable.w3) }
            if(tid==8) { m7.setImageResource(R.drawable.b4) }
            if(tid==9) { m7.setImageResource(R.drawable.w4) }
            if(tid==10) { m7.setImageResource(R.drawable.b5) }
            if(tid==11) { m7.setImageResource(R.drawable.w5) }
            if(tid==12) { m7.setImageResource(R.drawable.b6) }
            if(tid==13) { m7.setImageResource(R.drawable.w6) }
            if(tid==14) { m7.setImageResource(R.drawable.b7) }
            if(tid==15) { m7.setImageResource(R.drawable.w7) }
            if(tid==16) { m7.setImageResource(R.drawable.b8) }
            if(tid==17) { m7.setImageResource(R.drawable.w8) }
            if(tid==18) { m7.setImageResource(R.drawable.b9) }
            if(tid==19) { m7.setImageResource(R.drawable.w9) }
            if(tid==20) { m7.setImageResource(R.drawable.b10) }
            if(tid==21) { m7.setImageResource(R.drawable.w10) }
            if(tid==22) { m7.setImageResource(R.drawable.b11) }
            if(tid==23) { m7.setImageResource(R.drawable.w11) }
            if(tid==24) { m7.setImageResource(R.drawable.bj) }
            if(tid==25) { m7.setImageResource(R.drawable.wj) }
        }
        if(pos==8) {
            if(tid==0) { m8.setImageResource(R.drawable.b0) }
            if(tid==1) { m8.setImageResource(R.drawable.w0) }
            if(tid==2) { m8.setImageResource(R.drawable.b1) }
            if(tid==3) { m8.setImageResource(R.drawable.w1) }
            if(tid==4) { m8.setImageResource(R.drawable.b2) }
            if(tid==5) { m8.setImageResource(R.drawable.w2) }
            if(tid==6) { m8.setImageResource(R.drawable.b3) }
            if(tid==7) { m8.setImageResource(R.drawable.w3) }
            if(tid==8) { m8.setImageResource(R.drawable.b4) }
            if(tid==9) { m8.setImageResource(R.drawable.w4) }
            if(tid==10) { m8.setImageResource(R.drawable.b5) }
            if(tid==11) { m8.setImageResource(R.drawable.w5) }
            if(tid==12) { m8.setImageResource(R.drawable.b6) }
            if(tid==13) { m8.setImageResource(R.drawable.w6) }
            if(tid==14) { m8.setImageResource(R.drawable.b7) }
            if(tid==15) { m8.setImageResource(R.drawable.w7) }
            if(tid==16) { m8.setImageResource(R.drawable.b8) }
            if(tid==17) { m8.setImageResource(R.drawable.w8) }
            if(tid==18) { m8.setImageResource(R.drawable.b9) }
            if(tid==19) { m8.setImageResource(R.drawable.w9) }
            if(tid==20) { m8.setImageResource(R.drawable.b10) }
            if(tid==21) { m8.setImageResource(R.drawable.w10) }
            if(tid==22) { m8.setImageResource(R.drawable.b11) }
            if(tid==23) { m8.setImageResource(R.drawable.w11) }
            if(tid==24) { m8.setImageResource(R.drawable.bj) }
            if(tid==25) { m8.setImageResource(R.drawable.wj) }
        }
        if(pos==9) {
            if(tid==0) { m9.setImageResource(R.drawable.b0) }
            if(tid==1) { m9.setImageResource(R.drawable.w0) }
            if(tid==2) { m9.setImageResource(R.drawable.b1) }
            if(tid==3) { m9.setImageResource(R.drawable.w1) }
            if(tid==4) { m9.setImageResource(R.drawable.b2) }
            if(tid==5) { m9.setImageResource(R.drawable.w2) }
            if(tid==6) { m9.setImageResource(R.drawable.b3) }
            if(tid==7) { m9.setImageResource(R.drawable.w3) }
            if(tid==8) { m9.setImageResource(R.drawable.b4) }
            if(tid==9) { m9.setImageResource(R.drawable.w4) }
            if(tid==10) { m9.setImageResource(R.drawable.b5) }
            if(tid==11) { m9.setImageResource(R.drawable.w5) }
            if(tid==12) { m9.setImageResource(R.drawable.b6) }
            if(tid==13) { m9.setImageResource(R.drawable.w6) }
            if(tid==14) { m9.setImageResource(R.drawable.b7) }
            if(tid==15) { m9.setImageResource(R.drawable.w7) }
            if(tid==16) { m9.setImageResource(R.drawable.b8) }
            if(tid==17) { m9.setImageResource(R.drawable.w8) }
            if(tid==18) { m9.setImageResource(R.drawable.b9) }
            if(tid==19) { m9.setImageResource(R.drawable.w9) }
            if(tid==20) { m9.setImageResource(R.drawable.b10) }
            if(tid==21) { m9.setImageResource(R.drawable.w10) }
            if(tid==22) { m9.setImageResource(R.drawable.b11) }
            if(tid==23) { m9.setImageResource(R.drawable.w11) }
            if(tid==24) { m9.setImageResource(R.drawable.bj) }
            if(tid==25) { m9.setImageResource(R.drawable.wj) }
        }
        if(pos==10) {
            if(tid==0) { m10.setImageResource(R.drawable.b0) }
            if(tid==1) { m10.setImageResource(R.drawable.w0) }
            if(tid==2) { m10.setImageResource(R.drawable.b1) }
            if(tid==3) { m10.setImageResource(R.drawable.w1) }
            if(tid==4) { m10.setImageResource(R.drawable.b2) }
            if(tid==5) { m10.setImageResource(R.drawable.w2) }
            if(tid==6) { m10.setImageResource(R.drawable.b3) }
            if(tid==7) { m10.setImageResource(R.drawable.w3) }
            if(tid==8) { m10.setImageResource(R.drawable.b4) }
            if(tid==9) { m10.setImageResource(R.drawable.w4) }
            if(tid==10) { m10.setImageResource(R.drawable.b5) }
            if(tid==11) { m10.setImageResource(R.drawable.w5) }
            if(tid==12) { m10.setImageResource(R.drawable.b6) }
            if(tid==13) { m10.setImageResource(R.drawable.w6) }
            if(tid==14) { m10.setImageResource(R.drawable.b7) }
            if(tid==15) { m10.setImageResource(R.drawable.w7) }
            if(tid==16) { m10.setImageResource(R.drawable.b8) }
            if(tid==17) { m10.setImageResource(R.drawable.w8) }
            if(tid==18) { m10.setImageResource(R.drawable.b9) }
            if(tid==19) { m10.setImageResource(R.drawable.w9) }
            if(tid==20) { m10.setImageResource(R.drawable.b10) }
            if(tid==21) { m10.setImageResource(R.drawable.w10) }
            if(tid==22) { m10.setImageResource(R.drawable.b11) }
            if(tid==23) { m10.setImageResource(R.drawable.w11) }
            if(tid==24) { m10.setImageResource(R.drawable.bj) }
            if(tid==25) { m10.setImageResource(R.drawable.wj) }
        }
        if(pos==11) {
            if(tid==0) { m11.setImageResource(R.drawable.b0) }
            if(tid==1) { m11.setImageResource(R.drawable.w0) }
            if(tid==2) { m11.setImageResource(R.drawable.b1) }
            if(tid==3) { m11.setImageResource(R.drawable.w1) }
            if(tid==4) { m11.setImageResource(R.drawable.b2) }
            if(tid==5) { m11.setImageResource(R.drawable.w2) }
            if(tid==6) { m11.setImageResource(R.drawable.b3) }
            if(tid==7) { m11.setImageResource(R.drawable.w3) }
            if(tid==8) { m11.setImageResource(R.drawable.b4) }
            if(tid==9) { m11.setImageResource(R.drawable.w4) }
            if(tid==10) { m11.setImageResource(R.drawable.b5) }
            if(tid==11) { m11.setImageResource(R.drawable.w5) }
            if(tid==12) { m11.setImageResource(R.drawable.b6) }
            if(tid==13) { m11.setImageResource(R.drawable.w6) }
            if(tid==14) { m11.setImageResource(R.drawable.b7) }
            if(tid==15) { m11.setImageResource(R.drawable.w7) }
            if(tid==16) { m11.setImageResource(R.drawable.b8) }
            if(tid==17) { m11.setImageResource(R.drawable.w8) }
            if(tid==18) { m11.setImageResource(R.drawable.b9) }
            if(tid==19) { m11.setImageResource(R.drawable.w9) }
            if(tid==20) { m11.setImageResource(R.drawable.b10) }
            if(tid==21) { m11.setImageResource(R.drawable.w10) }
            if(tid==22) { m11.setImageResource(R.drawable.b11) }
            if(tid==23) { m11.setImageResource(R.drawable.w11) }
            if(tid==24) { m11.setImageResource(R.drawable.bj) }
            if(tid==25) { m11.setImageResource(R.drawable.wj) }
        }
        if(pos==12) {
            if(tid==0) { m12.setImageResource(R.drawable.b0) }
            if(tid==1) { m12.setImageResource(R.drawable.w0) }
            if(tid==2) { m12.setImageResource(R.drawable.b1) }
            if(tid==3) { m12.setImageResource(R.drawable.w1) }
            if(tid==4) { m12.setImageResource(R.drawable.b2) }
            if(tid==5) { m12.setImageResource(R.drawable.w2) }
            if(tid==6) { m12.setImageResource(R.drawable.b3) }
            if(tid==7) { m12.setImageResource(R.drawable.w3) }
            if(tid==8) { m12.setImageResource(R.drawable.b4) }
            if(tid==9) { m12.setImageResource(R.drawable.w4) }
            if(tid==10) { m12.setImageResource(R.drawable.b5) }
            if(tid==11) { m12.setImageResource(R.drawable.w5) }
            if(tid==12) { m12.setImageResource(R.drawable.b6) }
            if(tid==13) { m12.setImageResource(R.drawable.w6) }
            if(tid==14) { m12.setImageResource(R.drawable.b7) }
            if(tid==15) { m12.setImageResource(R.drawable.w7) }
            if(tid==16) { m12.setImageResource(R.drawable.b8) }
            if(tid==17) { m12.setImageResource(R.drawable.w8) }
            if(tid==18) { m12.setImageResource(R.drawable.b9) }
            if(tid==19) { m12.setImageResource(R.drawable.w9) }
            if(tid==20) { m12.setImageResource(R.drawable.b10) }
            if(tid==21) { m12.setImageResource(R.drawable.w10) }
            if(tid==22) { m12.setImageResource(R.drawable.b11) }
            if(tid==23) { m12.setImageResource(R.drawable.w11) }
            if(tid==24) { m12.setImageResource(R.drawable.bj) }
            if(tid==25) { m12.setImageResource(R.drawable.wj) }
        }
    }
}
