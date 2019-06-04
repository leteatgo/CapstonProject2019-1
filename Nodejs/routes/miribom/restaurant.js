/* ./route/miribom/restaurant.js */
var knex = require('./../knex-mysql');
var exec = require('child_process').exec;
var fs = require('fs');
var async = require('async');

/* wake when user click the restaurant list item, give a detail about that restaurant */
exports.getRestInfo = (req, res) => {
    console.log('/restaurant/getRestInfo')
    const inputData = req.body;
    var resNo = inputData.resNo;
    console.log('resNo : ' + resNo);

    // 필요한것
    knex.select('s_left_num','image', 'menu.name as name').from('seat')
        .leftOuterJoin('menu', 'seat.no', 'menu.res_no')
        .where('seat.no', resNo)
        .then((rows) => {
            console.log(rows[0]);
            if (rows[0].image != null) {
                var filename = "/home/ckddn9496/LetEatGo/images/" + rows[0].image + ".jpg";
                var bmpBuffer = fs.readFileSync(filename);
                var restInfo = {
                    menu_name: rows[0].name,
                    image: JSON.stringify(bmpBuffer),
                    s_left_num: rows[0].s_left_num,
                }
                res.json(restInfo);
            } else {
                var restInfo = {
                    s_left_num: rows[0].s_left_num,
                }
                res.json(restInfo);
            }
        }).catch((err) => {
            res.send('더 이상 존재하지 않는 가게입니다.');
        })
}

exports.getRestInfoFromSearch = (req, res) => {
    console.log('/restaurant/search/info');
    const inputData = req.body;
    var res_no = inputData.resNo;
    console.log(inputData);
    
    knex.from('restaurant').select('*').where('no', res_no)
    .then((rows) => {
        console.log(rows);
        var filename = "/home/ckddn9496/LetEatGo/images/" + rows[0].image + ".jpg";
        var bmpBuffer = fs.readFileSync(filename);

        var info = {
            name: rows[0].name,
            address: rows[0].address,
            mobile: rows[0].mobile,
            latitude: rows[0].latitude,
            longitude: rows[0].longitude,
            image: JSON.stringify(bmpBuffer),
            hours: rows[0].hours,
            owner_request: rows[0].owner_request
        }
        res.json(info);
    }).catch((err) => {
        console.log(err);
        res.send('레스토랑 정보를 불러오는데 실패하였습니다.')
    })
}

exports.getRestInfoFromCat = (req, res) => {
    console.log('/restaurant/cat/info');
    const inputData = req.body;
    var resNo = inputData.resNo;

    knex.from('restaurant').where('no', resNo)
    .select('mobile','longitude','latitude','hours','owner_request')
    .then((rows) => {
        var info = {
            mobile: rows[0].mobile,
            longitude: rows[0].longitude,
            latitude: rows[0].latitude,
            hours: rows[0].hours,
            owner_request: rows[0].owner_request
        }
        res.json(info);
    }).catch((err) => {
        console.log(err);
        res.send('매장 정보를 불러오는데 실패했습니다.');
    });
}

exports.getTodayAvailableSeatNums = (req, res) => {
    console.log('/restaurant/reservation/seats');
    const inputData = req.body;
    var resNo = inputData.resNo;
    var date = inputData.date;
    console.log(inputData);

    knex.from('reservable').select('seat').where({res_no:resNo, date:date})
    .then((rows) => {
        var info = {
            seat: rows[0].seat
        };
        console.log('예약 가능 인원수 ',rows);
        res.json(info);
    }).catch((err) => { // seat정보가 없을 때
        return knex.from('restaurant').select('initial_reservable').where({res_no: resNo}).then((rows) => {
            var info = {
                seat: rows[0].initial_reservable
            };
            console.log('초기 설정된 예약가능 인원 수: ',rows[0].initial_reservable);
            res.json(info);
        }).catch((err) => {
            var info = {
                seat: 0
            };
            console.log('예약 가능 인원수가 등록 되지 않음');
            res.json(info);
        })
    })
}

/* wake when user click date on a calendar */
exports.findReservation = (req, res) => {
    console.log('/restaurant/reservation/find');
    // get restaurant no and selected date
    const inputData = req.body;
    var resNo = inputData.resNo;
    var date = inputData.date;

    knex('reservation').where({ res_no: resNo, date: date }).select('time').count('time as count').groupBy('time')
        .then((rows) => {
            console.log(rows);
            var jsonArray = new Array();
            rows.forEach(element => {
                var reservationInfo = {
                    time: element.time,
                    count: element.count
                }
                jsonArray.push(reservationInfo);
            });
            var sJson = JSON.stringify(jsonArray);
            res.send(sJson);
        }).catch((err) => {
            console.log(err);
            res.send('예약 내역 조회에 실패 하였습니다.')
        })
}

exports.makeReservation = (req, res) => {
    console.log('/restaurant/reservation/make');
    const inputData = req.body;
    var u_no = inputData.uNo;
    var res_no = inputData.resNo;
    var date = inputData.date;
    var time = inputData.time;
    var deposit = inputData.deposit;
    var people_num = inputData.peopleNum;
    var user_request = inputData.userRequest;

    console.log(inputData);
    
    var tasks = [
        function (callback) {
            knex('restaurant').select('name').where({no: res_no}).then((rows) => {
                callback(null, rows[0].name);
            }).catch((err) => {
                console.log(err);
                callback(err);
            })
        },
        function (rest_name, callback) {
            exec(`curl -v -X POST 'https://kapi.kakao.com/v1/payment/ready' \
                        -H 'Authorization: KakaoAK {Admin key}' \
                        --data-urlencode 'cid=TC0ONETIME' \
                        --data-urlencode 'partner_order_id=partner_order_id' \
                        --data-urlencode 'partner_user_id=partner_user_id' \
                        --data-urlencode 'item_name='${rest_name} \
                        --data-urlencode 'quantity='${people_num} \
                        --data-urlencode 'total_amount='${deposit} \
                        --data-urlencode 'vat_amount=200' \
                        --data-urlencode 'tax_free_amount=0' \
                        --data-urlencode 'approval_url=http://34.74.255.9:5000/pay/approval' \
                        --data-urlencode 'fail_url=http://34.74.255.9:5000/pay/fail' \
                        --data-urlencode 'cancel_url=http://34.74.255.9:5000/pay/cancel'`
            , function (err, stdout, stderr) {
                var jRes = JSON.parse(stdout);
                // console.log(jRes)
                
                callback(null, jRes.next_redirect_app_url);
            })
        },
        function (url, callback) {
            knex('reservation')
            .insert({u_no: u_no, res_no: res_no, date: date, time: time, deposit: deposit, people_num: people_num, user_request: user_request })
            .then((rows) => {
                console.log(rows);  //  고유키
                var result = {
                    pay_no: rows,
                    url: url
                };
                res.json(result);
            }).catch((err) => {
                console.log(err);   
                res.send('예약 등록에 실패 하였습니다.');
            })        
        }
    ]

    async.waterfall(tasks, function(err) {
        if (err)
            console.log('err')
        else
            console.log('done')
    })
}

exports.deleteReservation = (req, res) => {
    console.log('/restaurant/reservation/delete');
    const inputData = req.body;
    var payNo = inputData.payNo;

    knex.from('reservation').where({no: payNo}).del()
    .then((rows) => {
        console.log(rows);
        res.send('제거 완료');
    }).catch((err) => {
        console.log(err);
        res.send('제거 실패')
    })
}