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


<img src = "https://github.com/CampProject2/mouse/assets/135544903/31df3b58-634a-42b5-9d1a-57441286eb2e" height ="400" weight = "800"/>

- 카카오 개발자에 등록한 후 카카오 SDK를 이용하여 앱 초기화면에 카카오 계정 로그인을 통해 게임을 시작하도록 구현하였다.


## Main page
<img src = "https://github.com/CampProject2/mouse/assets/135544903/77ffc469-c97b-4276-96f8-2b344357966a" height ="400" weight = "800"/>

- 카카오 로그인을 완료하면 게임 메인 페이지에 들어가게 된다. 우측 상단에 있는 ```환경설정``` 버튼과 화면 중앙에 있는 ```게임시작``` 버튼을 누를 수 있도록 만들었다.

### Setting

<img src = "https://github.com/CampProject2/mouse/assets/135544903/86230583-1847-471c-8432-454c32a9a26d" height ="400" weight = "800"/>

- 카카오 로그인을 한 계정을 불러오며 그 전에 게임을 한 적이 있다면 승, 패 전적을 DB에 업데이트 시키며 사용자의 총 승패 횟수와 승률을 볼 수 있게 구현하였다.


## Game Start

<img src = "https://github.com/CampProject2/mouse/assets/135544903/5bf99e69-7dc5-4263-b61e-16dc2290b7af" height ="400" weight = "800"/>

- 선공인 사람이 먼저 26개의 타일 중 4개의 타일을 뽑게 구현하였다. 커스텀 다이얼로그로 띄운 보드 판에서 흰, 검 상관 없이 4개의 타일을 가져간다.
이때 상대방의 화면에는 "상대방이 타일을 선택하고 있습니다." 라는 커스텀 다이얼로그가 나타나게 된다.
- 선공 USER가 타일을 모두 선택하면 상대방으로 타일을 뽑는 순서가 넘어간다. 이 때 앞서 뽑은 타일은 '''.alpha = 0.5f'''으로 지정하여서 반투명하게 보이도록 업데이트한다.
- 모두가 4개의 타일을 뽑았으면 게임이 시작된다.

- 선공 USER가 남은 타일을 1개 가져가고, 상대의 타일을 맞춘다. 이 때 추리한 타일이 틀리면 자신의 타일을 하나 보여줘야하고, 맞추게 되면 맞춘 상대의 타일을 보여준다.
- 이렇게 타일이 제일 먼저 사라지는 USER가 승리하게 된다.
- 게임이 끝나면 승리하였습니다. 혹은 패배하였습니다. 라는 문구가 뜨며 액티비티가 닫히게 된다.
- 승패 여부는 환경설정으로 바로 반영되어 업데이트 된다. 

