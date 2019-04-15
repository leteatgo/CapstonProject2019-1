const express = require('express')

var pos = require('./routes/pos/pos')
var join = require('./routes/miribom/join')
var login = require('./routes/miribom/login')
var restaurant = require('./routes/miribom/restaurant')

const app = express()
const port = 5000

app.use(express.urlencoded({ extended: false }))
app.use(express.json())

// 잔여좌석 정보 제공
app.post('/pos/seatsChanged', (req, res) => { pos.seatsChanged(req, res)});
// 이메일 인증
app.post('/join/verify', (req, res) => { join.verify(req, res)});
// 회원가입
app.post('/join/signUp', (req, res) => { join.signUp(req, res)})
app.get('/join/auth', (req, res) => { join.auth(req, res);})
// 로그인
app.post('/login/signIn', (req, res) => { login.signIn(req, res);})
// 레스토랑 정보 가져오기
app.post('/restaurant/getRestInfo', (req, res) => { restaurant.getRestInfo(req, res);})

app.listen(port, () => { console.log(`LetEatGo Server is listening on port ${port}!`)})