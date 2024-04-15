const express = require('express');
const mysql = require('mysql');

const app = express();

const connection = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: '9465',
  database: 'mydb'
});

connection.connect((err) => {
  if (err) {
    console.error('Failed to connect to MySQL database.', err);
    return;
  }
  console.log('Connected to MySQL database');
});
app.set('port', process.env.PORT||80);

app.get('/',(req,res) => {
    res.send('Root');
});

// 카카오 계정 정보
app.post('/kakao-account-info', (req, res) => {
  let body = '';
  
  req.on('data', (data) => {
    body += data;
  });
  
  req.on('end', () => {
    const kakaoAccountInfo = JSON.parse(body);
    
    // MySQL에 카카오 정보 저장
    const query = 'INSERT INTO USERS (kakao_id) VALUES (?)';
    const values = [kakaoAccountInfo.kakaoId];
    
    connection.query(query, values, (err, result) => {
      if (err) {
        console.error('Failed to save Kakao account info to MySQL database.', err);
        res.sendStatus(500);
        return;
      }
      
      console.log('Kakao account info saved to MySQL database');
      res.sendStatus(200);
    });
  });
});


app.get('/win-loss-count/kakaoId', (req, res) => {
  //console.log(req.params)
  const kakaoId = req.query.kakaoId; 
  console.log(kakaoId);
  // MySQL에서 총 승리 및 총 패배 횟수 조회
  const query = 'SELECT SUM(win) AS totalWins, SUM(lose) AS totalLoses FROM USERS WHERE kakaoId = ?';
  const values = [kakaoId];
  
  connection.query(query, values, (err, results) => {
    if (err) {
      console.error('Failed to fetch win/lose count from MySQL database.', err);
      res.sendStatus(500);
      return;
    }

    if (results.length === 0) {
      res.status(404).send('Win/lose count not found for the given Kakao ID');
      return;
    }
    const totalWins = results[0].totalWins;
    const totalLoses = results[0].totalLoses;
    res.json({ totalWins, totalLoses });
  });
});


const bodyParser = require('body-parser');
app.use(bodyParser.json());
let waitingPlayers = []; // 대기 중인 플레이어 목록h

app.post('/match', (req, res) => {
  const player = req.body.id; // 매칭 요청 보낸 플레이어 정보
  
 // console.log(player);
  // console.log(waitingPlayers);
  
  if (waitingPlayers.length === 1) {
    const opponent = waitingPlayers[0];
    waitingPlayers.push(player);
    console.log(opponent);

    res.status(200).json({ "opponent" : opponent, "value" : 0 });

  } else {
    waitingPlayers.push(player);
  
    setTimeout(() => {
      if (waitingPlayers.length >= 2) {
        const opponent = waitingPlayers[1];

        res.status(200).json({ "opponent" : opponent, "value" : 1 });
        waitingPlayers = []; // 대기 중인 플레이어 목록 초기화
      }
    }, 3000); // 3초 후에 실행
  }
});



app.post('/chooseTile', (req, res) => {
  const chosenPosition = req.body.position;
  
  console.log('chosenPosition:', chosenPosition);

  //const query = `UPDATE BOARD SET choose = 1, tileid = (SELECT tileid FROM BOARD WHERE loc = ?) AS subquery WHERE loc = ?`;

  const query = `UPDATE BOARD SET choose = 1, tileid = (SELECT tileid FROM BOARD WHERE loc = ?)  WHERE loc = ?`;


  connection.query(query, [chosenPosition, chosenPosition], (error, results) => {
    if (error) {
      console.error('Error executing MySQL query:', error);
      res.status(500).send('Internal Server Error');
    } else {
      const tileid = results[0] ? results[0].tileid : null; //요소의 tileid 값 가져오고 없으면 null
      console.log(tileid)
      res.status(200).json({ "tileid" : tileid });
      
    }
  });
});

app.post('/hand', (req, res) => {
  const { kakaoID, id, tileid, down } = req.body;
  console.log('kakaoID:', kakaoID, 'id:', id, 'tileid:', tileid, 'down:', down);


  if (kakaoID === "2901593585") { //현수쓰
    console.log("입력 HAND1 DB로 처리");
    // HAND1 DB에 입력하는 로직
    const query = `UPDATE HAND1 SET titleid = ?, down = ? WHERE id = ?`;
    const values = [titleid, down, id];

    connection.query(query, values, (error, results) => {
      if (error) {
        console.error("입력 실패", error);
      } else {
        console.log("입력 성공");
      }
    });

  } else if (kakaoID === "2901844148") { //세연쓰

    console.log("입력 HAND2 DB로 처리");
    // HAND2 DB에 입력하는 로직
    const query = `UPDATE HAND2 SET titleid = ?, down = ? WHERE id = ?`;
    const values = [titleid, down, id];

    connection.query(query, values, (error, results) => {
      if (error) {
        console.error("입력 실패", error);
      } else {
        console.log("입력 성공");
      }
    });
  } 
  res.sendStatus(200);
});


let turn = "2901593585"; //5

app.post('/turnend', (req, res) => {
  const opponentID  = req.body.opponent;
  console.log('opponentID:', opponentID );
  turn = opponentID;

  res.status(200).send('턴 변경');//보내야하나

});

app.post('/myturn', (req, res)=> {
  const kakaoID = req.body.kid;
  //console.log('kakaoID:', kakaoID);
  
  if (turn === kakaoID) {
    res.status(200).json({ turn: 1 });
  }
});



app.get('/myturn', (req, res) => {
  const  kakaoID  = req.query.kakaoId
  console.log('kakaoID:', kakaoID );  

  if (turn === kakaoID) {
    res.status(200).json({ "turn": 1 });
  }
});

app.post('/check', (req, res) => {
  const { kakaoID, tileid, id } = {}
  console.log('kakaoID:', kakaoID, 'tileid:', tileid , 'id:', id);


  if (kakaoID === "2901593585") { // 현수쓰
    console.log("입력 HAND1 DB로 처리");
    // HAND1 DB에 입력
    const query = 'SELECT * FROM HAND1 WHERE tileid = ? AND id = ?';
    const values = [tileid, id];
  
    connection.query(query, values, (error, results) => {
      if (error) {
        console.log('에러 발생:', error);
        res.status(500).send('내부 서버 오류');

      } else {
        if (results.length > 0) {
          console.log('HAND1에서 일치하는 값 찾음');
          res.send('1');

        } else {
          console.log('HAND1에서 일치하는 값 없음');
          res.send('0');
        }
      }
    });
  } else if (kakaoID === "2901844148") {  // 세연쓰
    console.log("입력 HAND2 DB로 처리");
    // HAND2 DB에 입력하는 로직

    const query = 'SELECT * FROM HAND2 WHERE tileid = ? AND id = ?';
    const values = [tileid, id];
  
    connection.query(query, values, (error, results) => {
      if (error) {
        console.log('에러 발생:', error);
        res.status(500).send('내부 서버 오류');

      } else {
        if (results.length > 0) {
          console.log('HAND2에서 일치하는 값 찾음');
          res.send('1');

        } else {
          console.log('HAND2에서 일치하는 값 없음');
          res.send('0');
        }
      }
    });
  } else {
    console.log('유효하지 않은 kakaoID');
    res.send('0');
  }
})
  
  
app.post('/yourhand', (req, res) => {
  const kakaoID = req.body.opponent;
  console.log('kakaoID:', kakaoID);

  if (kakaoID === "2901593585") { // 현수쓰
    console.log("입력 HAND1 DB로 처리");

    // tileid와 down 값을 HAND1에서 조회
    const query = 'SELECT tileid, down FROM HAND1';
    connection.query(query, (error, results) => {
      if (error) {
        console.log('에러 발생:', error);
        res.status(500).send('내부 서버 오류');
      } else {
        const { tileid, down } = results[0];
        console.log('tileid:', tileid);
        console.log('down:', down);
        res.status(200).json({"tileid" : tileid, "down": down});
      }
    });
  } else if (kakaoID === "2901844148") { // 세연쓰
    console.log("입력 HAND1 DB로 처리");
    // tileid와 down 값을 HAND1에서 조회
    const query = 'SELECT tileid, down FROM HAND1';
    connection.query(query, (error, results) => {
      if (error) {
        console.log('에러 발생:', error);
        res.status(500).send('내부 서버 오류');
      } else {
        const { tileid, down } = results[0];
        console.log('tileid:', tileid);
        console.log('down:', down);
        res.status(200).json({"tileid" : tileid, "down": down});
      }
    });
  }
});


//app.post('/correct') (req, res) => {

//}
  
app.listen(app.get('port'), () => {
  console.log('Server is running on port ' + app.get('port'));
});
