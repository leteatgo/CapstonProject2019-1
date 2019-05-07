/* ./route/miribom/restaurant.js */
var knex = require('./../knex-mysql');
var fs = require('fs');
/* wake when user click the restaurant list item, give a detail about that restaurant */
exports.getRestInfo = (req, res) => {
    console.log('/restaurant/getRestInfo')
    const inputData = req.body;
    var resNo = inputData.resNo;
    console.log('resNo : ' + resNo);

    knex.select('*').from('restaurant')
    .leftOuterJoin('seat', 'restaurant.no', 'seat.no')
    .where('restaurant.no', resNo)
    .then((rows) => {
        console.log(rows[0]);
        var filename = "/home/ckddn9496/LetEatGo/images/" + rows[0].image + ".jpg";
        var bmpBuffer = fs.readFileSync(filename);

        var restInfo = {
            no: rows[0].no,
            name: rows[0].name,
            address: rows[0].address,
            mobile: rows[0].mobile,
            // longitude: rows[0].longitude,
            // latitude: rows[0].latitude,
            // r_type: rows[0].r_type,
            // s_type: rows[0].s_type,
            // f_type: rows[0].f_type,
            // r_num: rows[0].r_num,
            image: JSON.stringify(bmpBuffer),
            hours: rows[0].hours,
            s_total_num: rows[0].s_total_num,
            s_left_num: rows[0].s_left_num,
            s_ava_num: rows[0].s_ava_num
            // owner_request: rows[0].owner_request
        }

        res.json(restInfo);
    }).catch((err) => {
        res.send('더 이상 존재하지 않는 가게입니다.');
    })
}
/* wake when user click date on a calendar */
exports.findReservation = (req, res) => {
    console.log('/restaurant/reservation/find');
    // get restaurant no and selected date
    const inputData = req.body;
    var resNo = inputData.resNo;
    var date = inputData.date;

    knex('reservation').where({res_no: resNo, date: date}).select('time').count('time as count').groupBy('time')
    .then((rows) => {
        console.log(rows);
        var jsonArray = new Array();
        rows.forEach(element => {
            var reservationInfo = {
                time: element.time,
                count: element.count,
            }
            jsonArray.push(reservationInfo);
        });
        var sJson = JSON.stringify(jsonArray);
        res.send(sJson);     
    }).catch((err) => {
        console.log(err);
        res.send('예약 내역 조회에 실패 하였습니다.')
    })
}

exports.makeReservation = (req, res) => {
    console.log('/restaurant/reservation/make');
    const inputData = req.body;
    var u_no = inputData.uNo;
    var res_no = inputData.resNo;
    var date = inputData.date;
    var time = inputData.time;
    var deposit = inputData.deposit;
    var people_num = inputData.peopleNum;
    var user_request = inputData.userRequest;
    
    knex('reservation').returning(['date', 'time'])
    .insert({u_no: u_no, res_no: res_no, date: date, time:time, deposit:deposit, people_num: people_num, user_request: user_request})
    .then((rows) => {
        console.log(rows);
        res.send('예약되었습니다.');
    }).catch((err) => {
        console.log(err);
        res.send('예약 등록에 실패 하였습니다.');
    })
}