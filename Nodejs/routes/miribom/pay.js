/* ./route/miribom/pay.js */
var knex = require('./../knex-mysql');

exports.approval = (req, res) => {
    console.log('/pay/approval')
    
    res.send('approved!');
}

exports.fail = (req, res) => {
    console.log('/pay/fail')
    res.send('failed!');
}

exports.cancel = (req, res) => {
    console.log('/pay/cancel')
    res.send('canceled!');
}   