const express = require('express');

var pos_pos = require('./routes/pos/pos');
var pos_menu = require('./routes/pos/menu');
var miribom_search = require('./routes/miribom/search');
var miribom_pay = require('./routes/miribom/pay');
var miribom_join = require('./routes/miribom/join');
var miribom_login = require('./routes/miribom/login');
var miribom_home = require('./routes/miribom/home');
var miribom_restaurant = require('./routes/miribom/restaurant');
var reservation_login = require('./routes/reservation/login');
var reservation_status = require('./routes/reservation/status');
var reservation_seat = require('./routes/reservation/seat');

const app = express();
const port = 5000;

app.use(express.urlencoded({ limit: '100mb', extended: true }));
app.use(express.json({ limit: '100mb'}));

/* Miribom application for users */
// 사용자 회원가입
app.post('/join/signUp', (req, res) => { miribom_join.signUp(req, res)});
// 이메일 인증
app.get('/join/auth', (req, res) => { miribom_join.auth(req, res);});
// 사용자 로그인
app.post('/login/signIn/nhn', (req, res) => { miribom_login.signInByNaver(req, res);})
app.post('/login/signIn', (req, res) => { miribom_login.signIn(req, res);});
// 홈 화면 구성
app.post('/home/', (req, res) => { miribom_home.home(req, res);});

/* 검색 탭 */
// 기본 검색 - 가게명
app.post('/home/search/all', (req, res) => { miribom_search.searchAll(req, res); });
// 거리 기준 검색
app.post('/home/search/distance', (req, res) => { miribom_search.searchByDistance(req, res); });
// 잔여좌석 기준 검색
app.post('/home/search/remains', (req, res) => { miribom_search.searchByRemains(req, res); });

// 레스토랑 정보 가져오기
app.post('/restaurant/getRestInfo', (req, res) => { miribom_restaurant.getRestInfo(req, res);});
// 선택 레스토랑 예약 내역
app.post('/restaurant/reservation/find', (req, res) => { miribom_restaurant.findReservation(req, res);});
// 선택 레스토랑 예약 하기
app.post('/restaurant/reservation/make', (req, res) => { miribom_restaurant.makeReservation(req, res);});

// 카카오 페이 결과 페이지
app.get('/pay/approval', (req, res) => { miribom_pay.approval(req, res);});
app.get('/pay/fail', (req, res) => { miribom_pay.fail(req, res);});
app.get('/pay/cancel', (req, res) => { miribom_pay.cancel(req, res);});

/* dPos application for owners */
// 점주 회원가입
app.post('/pos/join', (req, res) => { pos_pos.signUp(req, res);});
// 점주 로그인
app.post('/pos/login', (req, res) => { pos_pos.signIn(req, res);});
// 래스토랑 등록
app.post('/pos/restaurant', (req, res) => { pos_pos.restaurant(req, res);});
// 잔여좌석 정보 제공
app.post('/pos/seatsChanged', (req, res) => { pos_pos.seatsChanged(req, res);});

/*  menu managers for owners */
// 메뉴 추가
app.post('/pos/restaurant/menu/insert', (req, res) => { pos_menu.menu_insert(req, res);});
// 메뉴 수정
app.post('/pos/restaurant/menu/update', (req, res) => { pos_menu.menu_update(req, res);});
// 메뉴 삭제
app.post('/pos/restaurant/menu/delete', (req, res) => { pos_menu.menu_delete(req, res);});


/* Reservation application for owners */
// 점주 로그인
app.post('/reservation/login', (req, res) => { reservation_login.signIn(req, res);});
// 선택한 날짜에 맞는 
app.post('/reservation/list', (req, res) => { reservation_status.dateInfo(req, res);});
// 좌석 인원 기입
app.post('/reservation/seat', (req, res) => { reservation_seat.setSeats(req, res);});


app.listen(port, () => { console.log(`LetEatGo Server is listening on port ${port}!`)});