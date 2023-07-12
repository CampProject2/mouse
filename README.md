# 🐭 MadCampProject2_mouse

<img src = "https://github.com/CampProject2/mouse/assets/135544903/1e7be2af-b497-4b83-a703-ff6765ef9c62" height ="400" weight = "400"/>

## 🪤 프로젝트 소개
[node.js & mysql & kotlin을 이용한 다빈치코드게임]

## 🖱️ 팀원
카이스트 전산학부 19학번 이현수

숙명여자대학교 컴퓨터과학전공 21학번 조세연

## 💻 개발환경
- OS: Android
- Language: Kotlin
- IDE: Android Studio
- Target Device: Galaxy Note 10+

## 📁 자세한 설명

## Kakao Login


https://github.com/CampProject2/mouse/assets/135544903/31df3b58-634a-42b5-9d1a-57441286eb2e

- 카카오 개발자에 등록한 후 카카오 SDK를 이용하여 앱 초기화면에 카카오 계정 로그인을 통해 게임을 시작하도록 구현하였다.


## Main page


- 카카오 로그인을 완료하면 게임 메인 페이지에 들어가게 된다. 우측 상단에 있는 ```환경설정``` 버튼과 화면 중앙에 있는 ```게임시작``` 버튼을 누를 수 있도록 만들었다.

### Setting



- 카카오 로그인을 한 계정을 불러오며 그 전에 게임을 한 적이 있디면 승, 패 전적을 DB에 넣어놓아서 전체 승패횟수와 승률을 볼 수 있게 구현하였다.




* * * * 


# Game Start

https://github.com/CampProject2/mouse/assets/135544903/5bf99e69-7dc5-4263-b61e-16dc2290b7af



|scroll|share|
|------|------|
![tab2_scroll](https://github.com/Gloveman/CampProject1/assets/135544903/b82532cb-f311-41e4-a6bf-7d04d6ba2865)|![tab2_share](https://github.com/Gloveman/CampProject1/assets/135544903/74660b38-09ab-4c7b-981d-2ff67281c451)|


#### Major features
- 휴대폰에 저장된 사진들을 Grid 형식으로 보여준다.
- 이미지를 누르면 해당 이미지를 전체 화면으로 보여주며 좌우 슬라이드를 통해 다음 또는 이전 이미지로 넘어갈 수 있다.
- ```공유```버튼을 누르면 사진을 다른 앱으로 공유할 수 있다.

* * * * 
#### 기술 설명
- 연락처와 동일하게 ```contentResolver```를 이용해 사진들의 ```path```를 참조한다.
  - 이를 위해 ```android.permission.READ_EXTERNAL_STORAGE``` 권한을 이용하였다.
- 이미지를 ```View```에 표시하기 위해 ```Glide``` library를 사용했다.
- 공유 기능의 경우 이미지의 ```content URI```를 가지고  ```Share Intent```를 만들어 실행했다.
  - 이를 위해 ```android.permission.INTERNET``` 권한을 이용한다.
    
# Game -ing
|scroll|add|edit|
|------|------|------|
|![tab3_scroll](https://github.com/Gloveman/CampProject1/assets/135544903/f6efd06d-4ae5-433b-ab24-1406f5e6f855)|![tab3_add](https://github.com/Gloveman/CampProject1/assets/135544903/6aaa9fbb-828d-409c-81a6-ccf20df674e3)|![tab3_edit](https://github.com/Gloveman/CampProject1/assets/135544903/d4009b0f-d414-4aa0-aba5-a6a2f931a5b8)|

#### Major features
- Tab3에는 **캘린더 메모 기능**을 구현하였다.
- 원하는 날짜를 선택하여 ```새 메모```버튼을 눌러 메모를 추가할 수 있고, ```메모 수정```을 통해 메모 수정도 가능하다. 원하면 ```메모 삭제```를 통해 메모를 삭제할 수 있다.

* * * * 
#### 기술 설명
- 메모는 모두 앱의 내부 저장소에 JSON 파일로 저장되어 있다. 따라서 별도 권한을 필요로 하지 않는다.
- ```메모 추가```와 ```메모 수정```시 별도의 ```customdialog```가 열린다. ```dialog```와 ```fragment```간 data 이동을 위해 ```Listener```를 구현하여 사용했다.
