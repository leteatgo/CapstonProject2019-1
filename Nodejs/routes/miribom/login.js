/* ./route/miribom/login.js */
var knex = require('./../knex-mysql');

/* wake when user click the login button */
exports.signIn = (req, res) => {
    console.log('/login/signIn')
    const inputData = req.body;
    var id = inputData.id;
    
    console.log("id : " + id);
    
    knex('user').where({id: id}).select('*').then(function(rows) {
        console.log(rows[0]);
        if (rows[0].u_type === 0)
            if (rows[0].verified === 1) {
                var info = {
                    no: rows[0].no,
                    hash: rows[0].hash,
                    salt: rows[0].salt,
                    name: rows[0].name,
                    mobile: rows[0].mobile
                }
                res.json(info);
            } else
                res.send('인증되지 않은 이메일 입니다.');
        else {  //  네이버를 통해 로그인 한 경우
            res.send('아이디 중복!!! 네이버 아이디로 로그인 버튼으로 로그인 해주세요.');
        }
    }).catch((err) => {
        console.log('로그인 실패');
        res.send('이메일과 비밀번호를 확인해주세요.')
    })
}

exports.signInByNaver = (req, res) => {
    console.log('/login/signIn/nhn')
    const inputData = req.body;
    var id = inputData.id;
    var hash = inputData.hash;
    var salt = inputData.salt;
    var name = inputData.name;
    console.log(inputData);
    
    // select 가져와서 ckddn9496@naver.com으로 검색 u_type이 === 1이면 패스
    // 0이면 이메일을 통한 로그인 해라
    knex('user').where({id:id}).select('*').then((rows) => {
        var info = {
            no: rows[0].no
        }
        res.json(info);
    }).catch((err) => {
        return knex('user').insert({ id: id, hash: hash, salt: salt, name: name, verified: 1, u_type: 1 }).then((data) => {
            res.send(data);
        });
    });
}
