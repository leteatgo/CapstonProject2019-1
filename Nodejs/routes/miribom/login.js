/* ./route/miribom/login.js */
var knex = require('./../knex-mysql');
var bkfd2Password = require('pbkdf2-password');
var hasher = bkfd2Password();
/* wake when user click the login button */
exports.signIn = (req, res) => {
    console.log('/login/signIn')
    const inputData = req.body;
    var id = inputData.id;
    var pw = inputData.pw;
    
    console.log("id, pw : " + id + ", " + pw);
    knex('user').where({id: id}).select('*').then(function(rows) {
        console.log(rows[0]);
        
        hasher({password: pw, salt: rows[0].salt}, (err, pass, salt, hash) => {
            console.log(hash);
            if (hash == rows[0].pw) {
                console.log('로그인 성공');
                res.sendStatus(200);       
            } else {
                console.log('로그인 실패')
                res.send('비밀번호를 확인해주세요.')
            }
        })
    }).catch((err) => {
        console.log('로그인 실패');
        res.send('이메일과 비밀번호를 확인해주세요.')
    })
}

