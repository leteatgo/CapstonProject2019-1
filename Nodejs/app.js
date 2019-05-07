const express = require('express')

var pos = require('./routes/pos/pos')
var join = require('./routes/miribom/join')
var login = require('./routes/miribom/login')
var home = require('./routes/miribom/home')
var restaurant = require('./routes/miribom/restaurant')
var reservation_login = require('./routes/reservation/login')
var reservation_status = require('./routes/reservation/status')


const app = express()
const port = 5000

app.use(express.urlencoded({ extended: false }))
app.use(express.json())

// 이메일 인증
app.post('/join/verify', (req, res) => { join.verify(req, res)});
// 회원가입
app.post('/join/signUp', (req, res) => { join.signUp(req, res)})
app.get('/join/auth', (req, res) => { join.auth(req, res);})
// 로그인
app.post('/login/signIn', (req, res) => { login.signIn(req, res);})
// 홈 화면 구성
app.post('/home/', (req, res) => { home.home(req, res);})
// 레스토랑 정보 가져오기
app.post('/restaurant/getRestInfo', (req, res) => { restaurant.getRestInfo(req, res);})
// 선택 레스토랑 예약 내역
app.post('/restaurant/reservation/find', (req, res) => { restaurant.findReservation(req, res);})
// 선택 레스토랑 예약 하기
app.post('/restaurant/reservation/make', (req, res) => { restaurant.makeReservation(req, res);})

/* dPos application for owners */
// 잔여좌석 정보 제공
app.post('/pos/seatsChanged', (req, res) => { pos.seatsChanged(req, res)});

/* Reservation application for owners */
// 로그인
app.post('/reservation/login', (req, res) => { reservation_login.signIn(req, res);})
// 선택한 날짜에 맞는 
app.post('/reservation/list', (req, res) => { reservation_status.dateInfo(req, res);})

app.listen(port, () => { console.log(`LetEatGo Server is listening on port ${port}!`)})