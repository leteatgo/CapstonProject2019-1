/* ./route/reservation/status.js */
var knex = require('./../knex-mysql');
/* wake when owner click confirm button with specific date */
exports.dateInfo = (req, res) => {
    console.log('/reservation/list');
    const inputData = req.body;
    var o_no = inputData.oNo;
    var date = inputData.date;

    knex.from('restaurant').select('no').where({o_no: o_no})
    .then((rows) => {
        knex('reservation').innerJoin('user','reservation.u_no','=','user.no')
        .where({res_no: rows[0].no, date: date})
        .select('name','time','date','people_num','mobile','user_request')
        .orderBy('time')
        .then((rows) => {
            console.log(rows);
            // 예약 고유번호, 시간, 이름, 날짜, 인원 수, 예약 시간, 전화번호, 요청사항
            var jsonArray = new Array();
            rows.forEach(element => {
                if (element.mobile == null) {
                    var reservationInfo = {
                        time: element.time,
                        name: element.name,
                        date: element.date,
                        people_num: element.people_num,
                        mobile: '***********',
                        user_request: element.user_request
                    }
                    jsonArray.push(reservationInfo);
                }
                else {
                    var reservationInfo = {
                        time: element.time,
                        name: element.name,
                        date: element.date,
                        people_num: element.people_num,
                        mobile: element.mobile,
                        user_request: element.user_request
                    }
                    jsonArray.push(reservationInfo);
                }
            });
            var sJson = JSON.stringify(jsonArray);
            res.send(sJson);
        }).catch((err) => {
            console.log(err);
            res.send('예약 정보를 가쟈오는데 실패하였습니다.')
        })
    }).catch((err) => {
        console.log(err);
        res.send('등록된 레스토랑이 없습니다.')
    })
    
};