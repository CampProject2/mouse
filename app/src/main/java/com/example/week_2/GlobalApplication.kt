package com.example.week_2

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val naive_app_key = "2115599024481932941aa97f0c3ebaf9"

        // KaKao SDK  초기화
        ///
        KakaoSdk.init(this, naive_app_key)
    }
}