/* ./route/miribom/join.js */
var knex = require('./../knex-mysql');
var nodemailer = require('nodemailer');
var bkfd2Password = require('pbkdf2-password');
var hasher = bkfd2Password();

exports.verify = (req, res) => {
    console.log('/join/verify');

    const inputData = req.body;
    var email = inputData.id;
    console.log('email ', email);

    // 메일 발송 객체 생성
    let transporter = nodemailer.createTransport({
        service: 'gmail',
        auth: {
            user: '20146518.sw.cau@gmail.com',  // gmail 계정 아이디를 입력
            pass: 'ehowl9496!'          // gmail 계정의 비밀번호를 입력
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
            from: '20146518.sw.cau@gmail.com',    // 발송 메일 주소 (위에서 작성한 gmail 계정 아이디)
            to: email,                     // 수신 메일 주소
            subject: 'Confirm your email from LetEatGo.',   // 제목
            // text: 'Please click this link below!'  // 내용
            html: '<p>please click the link below !!!</p>' +
                "<a href='http://" + ip + "/join/auth/?email=" + email + "&token=" + hash + "'>인증하기</a>"
        };

        transporter.sendMail(mailOptions, function (error, info) {
            if (error) {
                console.log(error);
            }
            else {
                console.log('Email sent: ' + info.response);
            }
        });
    })

    res.sendStatus(200) // OK
}

exports.signUp = (req, res) => {
    console.log('/join/signUp')
    const inputData = req.body;
    var id = inputData.id;
    var salt = inputData.salt;
    var hash = inputData.hash;
    var name = inputData.name;
    var mobile = inputData.mobile;
    console.log(inputData);

    // 1. 인증된 ip인지 확인
    knex('verify').where('id', '=', id).select('verified').then((rows) => {
        // 2. 인증이되었다면 salt, hash만들어서 ID password(hash) salt name mobile
        var verified = rows[0].verified;
        if (verified == 1) {    //  진행
            knex('user').insert({ id: id, hash: hash, salt: salt, name: name, mobile: mobile }).then((rows) => {
                knex('verify').delete({ id: id })
                console.log(`delete ID: ${id} from verify table`);
                res.sendStatus(200) // OK
            }).catch((err) => {
                console.log(err);
                console.log('fail to insert data');
                res.send('계정 추가에 실패하였습니다 잠시후 다시 시도해주세요.')
            })
        } else {    //  인증 하지 않았음
            res.send('이메일 인증 후 이용하실 수 있습니다.');
        }
    }).catch((err) => {
        res.send('이메일 인증 후 이용하실 수 있습니다.');
    })

}

exports.auth = (req, res) => {
    console.log('/join/auth')
    var email = req.query.email;
    var token = req.query.token;
    console.log("Email : " + email + " wants to verify");

    knex('verify').where({ id: email }).select('*').then(function (rows) {
        console.log('인증확인을 진행합니다.')
        var hash = replaceAll(rows[0].pw, "+", " ")
        if (hash == token) {
            res.send('인증에 성공하였습니다.')
            return knex('verify').where('id', '=', email).update({ verified: 1 })
        } else {
            res.send('인증에 실패하셨습니다.')
        }
    }).catch((err) => {
        res.send('인증번호를 부여받지 못한 계정입니다.')
    })

}

function replaceAll(str, searchStr, replaceStr) {
    return str.split(searchStr).join(replaceStr);
}