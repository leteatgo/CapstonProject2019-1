/* ./route/miribom/join.js */
var knex = require('./../knex-mysql');
var nodemailer = require('nodemailer');
var bkfd2Password = require('pbkdf2-password');
var hasher = bkfd2Password();

// 회원가입
exports.signUp = (req, res) => {
    console.log('/join/signUp')
    const inputData = req.body;
    var id = inputData.id;
    var salt = inputData.salt;
    var hash = inputData.hash;
    var name = inputData.name;
    var mobile = inputData.mobile;
    console.log(inputData);

    // 1. ID 중복조회
    // 1-1. 중복 된다면 이미 존재하는 아이디입니다.\n다른 아이디를 입력해주세요.
    // 1-2. verify table에 추가, user table에 추가
    knex('user').insert({ id: id, hash: hash, salt: salt, name: name, mobile: mobile }).then((rows) => {
        sendMail(id);
        res.sendStatus(200);
    }).catch((err) => { // ID is already exist
        console.log('ID already exist');
        res.send('이미 존재하는 아이디입니다.\n다른 아이디를 입력해주세요.');
    })
}

// 인증 url 연결
exports.auth = (req, res) => {
    console.log('/join/auth')
    var email = req.query.email;
    var token = req.query.token;
    console.log("Email : " + email + " wants to verify");

    knex('verify').where({ id: email }).select('*').then(function (rows) {
        console.log('인증확인을 진행합니다.')
        var hash = replaceAll(rows[0].pw, "+", " ")
        if (hash == token) {
            console.log('token matched')
            res.send('인증에 성공하였습니다.')
            return knex('user').where('id', '=', email).update({ verified: 1 }).then((rows) => {
                return knex('verify').where('id', '=', email).delete();
            })
        } else {
            console.log('match failed')
            res.send('인증에 실패하셨습니다.')
        }
    }).catch((err) => {
        res.send('인증번호를 부여받지 못한 계정입니다.')
    })

}

function sendMail(email) {
    // 메일 발송 객체 생성
    let transporter = nodemailer.createTransport({
        service: 'gmail',
        auth: {
            user: 'ID',  // gmail 계정 아이디를 입력
            pass: 'PW'          // gmail 계정의 비밀번호를 입력
        }
    });
    var ip = '35.243.118.35:5000';    //  server ip

    var opts = { password: email };
    hasher(opts, function (err, pass, salt, hash) {

        /** verify TABLE
         * id : id
         * pw : hash
         * verified : 0 (default)
         */

        knex('verify').where({ id: email }).select('id').then(function (rows) {
            console.log('Inserted Account ' + rows[0].id)
            return knex('verify').where('id', '=', email).update({ pw: hash, verified: 0 })
        }).catch(function (err) {
            console.log('account that not inserted yet');
            return knex('verify').insert({ id: email, pw: hash, verified: 0 })
        })

        let mailOptions = {
            from: '20146518.sw.cau@gmail.com',              // 발송 메일 주소 (위에서 작성한 gmail 계정 아이디)
            to: email,                                      // 수신 메일 주소
            subject: 'Confirm your email from LetEatGo.',   // 제목
            html: '<p>please click the link below !!!</p>' +// 내용
                "<a href='http://" + ip + "/join/auth/?email=" + email + "&token=" + hash + "'>이메일 인증하기</a>"
        };

        transporter.sendMail(mailOptions, function (error, info) {
            if (error)
                console.log(error);
            else
                console.log('Email sent: ' + info.response);
        });
    })
}

function replaceAll(str, searchStr, replaceStr) {
    return str.split(searchStr).join(replaceStr);
}
