/* ./route/miribom/login.js */
var knex = require('./../knex-mysql');

exports.getRestInfo = (req, res) => {
    console.log('/login/getRestInfo')
    const inputData = req.body;
    var resNo = inputData.resNo;
    console.log('resNo : ' + resNo);

    knex.select('*').from('restaurant').leftOuterJoin('seat', 'restaurant.no', 'seat.no').then((rows) => {
        console.log(rows[0]);
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
            // image: rows[0].image,
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