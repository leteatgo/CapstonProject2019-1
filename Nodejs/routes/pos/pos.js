/* ./route/pos/pos.js */
var knex = require('./../knex-mysql');
var fs = require('fs');
// 잔여좌석 정보 전달
exports.seatsChanged = (req, res) => {
    console.log('/pos/seatsChanged');
    const inputData = req.body;
    var resNo = inputData.no;
    var totalPlace = inputData.totalPlaceNum;
    var availablePlace = inputData.availablePlaceNum;
    
    console.log('restaurant ' + resNo + 'totalPlace : ' + totalPlace + ' ,availablePlace : ' + availablePlace);
    
    // insert or update restaurant seats
    knex('seat').insert({no: resNo, s_total_num: totalPlace, s_left_num: availablePlace}).catch((err) => {
        // insert 하려는데 resNo가 이미 존재할 때
        return knex('seat').where('no','=',resNo).update({s_total_num: totalPlace, s_left_num: availablePlace})
    })

    res.sendStatus(200)
}

// 점주 회원가입
exports.signUp = (req, res) => {
    console.log('/pos/join');
    const inputData = req.body;
    var id = inputData.id;
    var pw = inputData.pw;
    var name = inputData.name;
    var mobile = inputData.mobile;
    console.log(inputData);

    knex('owner').insert({ id: id, hash: pw, name: name, mobile: mobile }).then((rows) => {
        res.sendStatus(200);
        console.log('create ID successfully');
    }).catch((err) => { // ID is already exist
        console.log('ID already exist');
        res.send('이미 존재하는 아이디입니다.\n다른 아이디를 입력해주세요.');
    })
}

// 점주 로그인
exports.signIn = (req, res) => {
    console.log('/pos/login');
    const inputData = req.body;
    var id = inputData.id;
    console.log(inputData);

    knex('owner').select("*").where({id: id}).then((owner_rows) => {
        return knex('restaurant').where({o_no: owner_rows[0].no}).select('*').then((res_rows) => {
            var info = {
                no: owner_rows[0].no,
                hash: owner_rows[0].hash,
                name: owner_rows[0].name,
                mobile: owner_rows[0].mobile,
                r_no: res_rows[0].no
            } // 비밀번호와 레스토랑 번호 전달
            res.json(info)
        }).catch((res_err) => {
            var info = {
                no: owner_rows[0].no,
                hash: owner_rows[0].hash,
                name: owner_rows[0].name,
                mobile: owner_rows[0].mobile
            }
            res.json(info)
        })
    }).catch((err) => { // ID is already exist
        res.send('존재하지 않는 ID입니다. 올바른 ID를 입력해주세요.');
    })
}

exports.restaurant = (req, res) => {
    console.log('/pos/restaurant')
    const inputData = req.body;
    var name = inputData.name;
    var address = inputData.address;
    var mobile = inputData.mobile;
    var longitude = inputData.longitude;
    var latitude = inputData.latitude;
    var r_type = inputData.r_type;
    var s_type = inputData.s_type;
    var f_type = inputData.f_type;
    var r_num = inputData.r_num;
    var image = inputData.image;
    var hours = JSON.stringify(inputData.hours);
    var owner_request = inputData.owner_request;
    var o_no = inputData.o_no;
    var initial_reservable = inputData.initial_reservable;
    console.log(inputData);

    saveImage(name+"_"+o_no+"_title.jpg", image);

    knex('restaurant').insert({name:name, address:address, mobile:mobile, longitude, longitude, latitude:latitude, r_type:r_type,s_type:s_type, f_type, f_type, r_num:r_num, image: name+"_"+o_no+"_title", hours: hours, owner_request:owner_request, o_no:o_no, initial_reservable:initial_reservable})
    .then((rows) => {
        res.send(rows);
    }).catch((err) => {
        res.send('레스토랑 등록에 실패하였습니다.')
    });
}

function saveImage(filename, data){
    var myBuffer = new Buffer(data.length);
    for (var i = 0; i < data.length; i++) {
        myBuffer[i] = data[i];
    }
    fs.writeFile("/home/ckddn9496/LetEatGo/images/" +filename, myBuffer, function(err) {
        if(err) {
            console.log(err);
        } else {
            console.log("The file was saved!");
        }
    });
}