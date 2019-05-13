/* ./route/reservation/status.js */
var knex = require('./../knex-mysql');
/* wake when owner click confirm button with specific date */
exports.dateInfo = (req, res) => {
    console.log('/reservation/list');
    const inputData = req.body;
    var res_no = inputData.rNo;
    var date = inputData.date;

    knex('reservation').innerJoin('user','reservation.u_no','=','user.no')
    .where({res_no: res_no, date: date})
    .select('*')
    .orderBy('time')
    .then((rows) => {
        // 예약 고유번호, 시간, 이름, 날짜, 인원 수, 예약 시간, 전화번호, 요청사항
        var jsonArray = new Array();
        rows.forEach(element => {
            var reservationInfo = {
                time: element.time,
                name: element.name,
                date: element.date,
                people_num: element.people_num,
                mobile: element.mobile,
                user_request: element.user_request
            }
            jsonArray.push(reservationInfo);
        });
        var sJson = JSON.stringify(jsonArray);
        res.send(sJson);
    }).catch((err) => {
        console.log(err);
        res.send('예약 정보를 가쟈오는데 실패하였습니다.')
    })
};