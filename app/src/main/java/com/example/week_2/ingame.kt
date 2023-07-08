package com.example.week_2

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    private lateinit var imageButton1: ImageButton
    private lateinit var imageButton2: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingame)

        imageButton1 = findViewById(R.id.imageButton1)
        imageButton2 = findViewById(R.id.imageButton2)

        // 이미지 버튼 클릭 리스너 등록
        imageButton1.setOnClickListener { onImageButtonClicked(imageButton1) }
        imageButton2.setOnClickListener { onImageButtonClicked(imageButton2) }

        // 이미지 버튼 초기 상태 설정
        imageButton1.visibility = View.VISIBLE
        imageButton2.visibility = View.VISIBLE
    }

    private fun onImageButtonClicked(imageButton: ImageButton) {
        // 이미지 버튼 선택 시 처리할 로직 작성

        // 선택한 이미지 버튼 숨기기
        imageButton.visibility = View.GONE

        // 두 이미지 버튼 모두 숨겨졌는지 확인
        if (imageButton1.visibility == View.GONE && imageButton2.visibility == View.GONE) {
            // 두 이미지 버튼 모두 숨겨진 경우 처리할 로직 작성
        }
    }
}
