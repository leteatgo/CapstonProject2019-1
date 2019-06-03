/* ./route/reservation/seat.js */
var knex = require('./../knex-mysql');

exports.setSeats = (req, res) => {
    console.log('/reservation/seat')
    const inputData = req.body;
    var oNo = inputData.oNo;
    var date = inputData.date;
    var seat = inputData.seat;
    console.log(inputData);

    knex.from('restaurant').select('no').where({o_no:oNo}).then((rows) => {
        return knex('reservable').insert({res_no: rows[0].no, date: date, seat: seat}).then((rows)=> {
            console.log('insert seat info');
        }).catch((err) => {
            return knex('reservable').where({res_no: rows[0].no, date: date}).update({seat: seat})
            .then((rows) => {
                console.log('update seat info');
            })
        })
    
    })
    res.sendStatus(200);
}