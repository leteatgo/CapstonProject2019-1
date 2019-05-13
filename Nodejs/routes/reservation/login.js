/* ./route/reservation/login.js */
var knex = require('./../knex-mysql');
/* wake when owner click the login button */
exports.signIn = (req, res) => {
    console.log('/reservation/login')
    const inputData = req.body;
    var id = inputData.id;
    
    console.log("id : " + id);
    knex('owner').where({id: id}).select('*').then((rows) => {
        console.log(rows[0]);
        var info = {
            no: rows[0].no,
            hash: rows[0].hash,
            name: rows[0].name,
            mobile: rows[0].mobile
        }
        res.json(info);
    }).catch((err) => {
        console.log('로그인 실패');
        res.send('이메일과 비밀번호를 확인해주세요.')
    })
}

