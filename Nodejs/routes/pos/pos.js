/* ./route/pos/pos.js */
var knex = require('./../knex-mysql');

/* wake when the number of available seats is changed */
exports.seatsChanged = (req, res) => {
    console.log('/pos/seatsChanged');
    const inputData = req.body;
    var resNo = inputData.no;
    var totalPlace = inputData.totalPlaceNum;
    var availablePlace = inputData.availablePlaceNum;
    
    console.log('totalPlace : ' + totalPlace + ' ,availablePlace : ' + availablePlace);
    
    // insert or update restaurant seats
    knex('seat').insert({no: resNo, s_total_num: totalPlace, s_left_num: availablePlace, s_ava_num: 0}).catch((err) => {
        // insert 하려는데 resNo가 이미 존재할 때
        return knex('seat').where('no','=',resNo).update({s_total_num: totalPlace, s_left_num: availablePlace})
    })

    res.sendStatus(200)
}