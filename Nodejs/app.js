const express = require('express')

var pos_pos = require('./routes/pos/pos')
var miribom_join = require('./routes/miribom/join')
var miribom_login = require('./routes/miribom/login')
var miribom_home = require('./routes/miribom/home')
var miribom_restaurant = require('./routes/miribom/restaurant')
var reservation_login = require('./routes/reservation/login')
var reservation_status = require('./routes/reservation/status')

const app = express()
const port = 5000

app.use(express.urlencoded({ extended: false }))
app.use(express.json())

/* Miribom application for users */
// 사용자 회원가입
app.post('/join/signUp', (req, res) => { miribom_join.signUp(req, res)})
// 이메일 인증
app.get('/join/auth', (req, res) => { miribom_join.auth(req, res);})
// 사용자 로그인
app.post('/login/signIn', (req, res) => { miribom_login.signIn(req, res);})
// 홈 화면 구성
app.post('/home/', (req, res) => { miribom_home.home(req, res);})
// 레스토랑 정보 가져오기
app.post('/restaurant/getRestInfo', (req, res) => { miribom_restaurant.getRestInfo(req, res);})
// 선택 레스토랑 예약 내역
app.post('/restaurant/reservation/find', (req, res) => { miribom_restaurant.findReservation(req, res);})
// 선택 레스토랑 예약 하기
app.post('/restaurant/reservation/make', (req, res) => { miribom_restaurant.makeReservation(req, res);})

/* dPos application for owners */
// 점주 회원가입
app.post('/pos/join', (req, res) => { pos_pos.signUp(req, res)});
// 잔여좌석 정보 제공
app.post('/pos/seatsChanged', (req, res) => { pos_pos.seatsChanged(req, res)});

/* Reservation application for owners */
// 점주 로그인
app.post('/reservation/login', (req, res) => { reservation_login.signIn(req, res);})
// 선택한 날짜에 맞는 
app.post('/reservation/list', (req, res) => { reservation_status.dateInfo(req, res);})

app.listen(port, () => { console.log(`LetEatGo Server is listening on port ${port}!`)})