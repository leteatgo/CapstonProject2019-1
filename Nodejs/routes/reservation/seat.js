/* ./route/reservation/seat.js */
var knex = require('./../knex-mysql');

exports.setSeats = (req, res) => {
    console.log('/reservation/seat')
    const inputData = req.body;
    var res_no = inputData.resNo;
    var date = inputData.date;
    var seat = inputData.seat;
    console.log(inputData);

    knex('reservable').insert({res_no: res_no, date: date, seat: seat}).then((rows)=> {
        console.log('insert seat info');
    }).catch((err) => {
        return knex('reservable').where({res_no: res_no, date: date}).update({seat: seat})
        .then((rows) => {
            console.log('update seat info');
        })
    })

    res.sendStatus(200);
}