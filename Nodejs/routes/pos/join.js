/* ./route/pos/join.js */
var knex = require('../knex-mysql');
/* wake when owner click the join button */
exports.signUp = (req, res) => {
    console.log('/pos/join')
    const inputData = req.body;
    var id = inputData.id;
    var pw = inputData.pw;
    var name = inputData.name;
    var mobile = inputData.mobile;
    console.log(inputData);

    // 1. ID 중복조회
    // 1-1. 중복 된다면 이미 존재하는 아이디입니다.\n다른 아이디를 입력해주세요.
    knex('owner').insert({ id: id, hash: pw, salt: salt, name: name, mobile: mobile }).then((rows) => {
        res.sendStatus(200);
        console.log('create ID successfully');
    }).catch((err) => { // ID is already exist
        console.log('ID already exist');
        res.send('이미 존재하는 아이디입니다.\n다른 아이디를 입력해주세요.');
    })
}