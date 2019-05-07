/* ./route/miribom/login.js */
var knex = require('./../knex-mysql');
var bkfd2Password = require('pbkdf2-password');
var hasher = bkfd2Password();
/* wake when user click the login button */
exports.signIn = (req, res) => {
    console.log('/login/signIn')
    const inputData = req.body;
    var id = inputData.id;
    
    console.log("id : " + id);
    knex('user').where({id: id}).select('*').then(function(rows) {
        console.log(rows[0]);

        var info = {
            no: rows[0].no,
            hash: rows[0].hash,
            salt: rows[0].salt,
            name: rows[0].name,
            mobile: rows[0].mobile
        }
        res.json(info);
    }).catch((err) => {
        console.log('로그인 실패');
        res.send('이메일과 비밀번호를 확인해주세요.')
    })
}

