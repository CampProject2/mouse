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

## 📁 백엔드
- ngrok를 사용해서 








## 📁 프론트엔드

# Kakao Login
![splash_AdobeExpress](https://github.com/Gloveman/CampProject1/assets/135544903/66eb195b-e91c-4ed3-8980-72eb86474a97)

- lottieanimation을 startup activity에서 실행하고 일정 시간 후에 mainactivity로 넘어가도록 했다.

# Setting

|scroll|action|group|
|------|------|------|
|![tab1_scroll](https://github.com/Gloveman/CampProject1/assets/135544903/258962f8-d640-4417-b799-80ed2181efb1)|![tab1_action](https://github.com/Gloveman/CampProject1/assets/135544903/be5cac70-7851-4175-b1ed-051ac22306e4)|![tab1_group](https://github.com/Gloveman/CampProject1/assets/135544903/687a7f6c-3a70-4813-b29b-5909e94a3dfd)


#### Major features

- 휴대폰에 저장된 연락처를 직접 불러와서 이를 스크롤하며 연락처를 확인할 수 있다.
- 연락처를 선택하면 총 3가지의 행동을 할 수 있는데,
  - 전화걸기
  - 문자 보내기
  - 그룹 추가하기가 가능하다.
- ```즐겨찾기```는 기본적으로 있는 그룹으로 언제나 연락처 가장 상단에 위치하고 있다.
- ```그룹에 추가하기```를 누르면 어떤 그룹에 추가할지 선택하게 된다. ```새 그룹```을 눌러 자유롭게 그룹을 추가할 수 있다.
- 그룹 항목을 누르면 새로운 창이 열리며 해당 그룹에 속한 연락처 목록을 보여준다. ```전화 걸기```나 ```문자 보내기```는 이전과 같으며, ```그룹에서 삭제하기```를 통해 해당 그룹에서 연락처를 제외할 수 있다. 원하면 아래 ```그룹 삭제```버튼을 통해 그룹을 삭제할 수 있다.

* * * * 
#### 기술 설명

- ```contentResolver```를 통해 휴대폰의 연락처를 탐색할 수 있는 ```cursor```를 가져와 ```이름(DISPLAY_NAME)```과 ```전화번호(NUMBER)```를 참조한다.
  - 이를 위해 ```android.permission.READ_CONTACTS``` 권한 허용을 요청한다.
  - 옆에 표시되는 이미지의 경우 9개의 icon 중 random하게 표시된다. 
- ```전화 걸기```와 ```문자 보내기```는 각각 적합한 ```Intent```를 만들고 ```data```로 전화번호를 추가하여 실행된다.
  - 전화를 걸기 위해 ```android.permission.CALL_PHONE``` 권한 허용을 요청한다.
- 그룹 데이터의 경우 앱의 내부 저장소에 JSON 파일로 저장했다. 

# Game Start
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
    
# game ing
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
