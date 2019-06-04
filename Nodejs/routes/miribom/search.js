/* ./route/miribom/search.js */
var knex = require('./../knex-mysql');

// 가져와야 할것, restaurant no, name
exports.searchAll = (req, res) => {
    console.log('/home/search/all')

    knex('restaurant').select('no', 'name').then((rows) => {
        var jsonArray = new Array();
        rows.forEach(row => {
            var rest = {
                no: row.no,
                name: row.name
            };
            jsonArray.push(rest);
        });
        var sJson = JSON.stringify(jsonArray);
        res.send(sJson);
    }).catch((err) => {
        console.log(err);
        res.send('fail to load restaurant lists...');
    });
} // returning no, name

// max distance보다 적은 거리의 restaurant 가져오기
exports.searchByDistance = (req, res) => {
    console.log('/home/search/distance')
    const inputData = req.body;
    var u_lon = inputData.longitude;
    var u_lat = inputData.latitude;
    var max_dis = inputData.distance;  //  몇 km이내: default
    console.log(inputData);

    if (max_dis === false)
        max_dis = 1;

    knex('restaurant').select('no', 'name', 'image','address','longitude', 'latitude').then((rows) => {
        var jsonArray = new Array();
        rows.forEach(row => {
            var distance = getDistance(u_lon, u_lat, row.longitude, row.latitude, "kilometer");
            if (distance < 1) {
                var rest = {
                    no: row.no,
                    name: row.name,
                    image: row.image,
                    address: row.address,
                    distance: distance
                };
                jsonArray.push(rest);
            }
        });
        var sJson = JSON.stringify(jsonArray);
        res.send(sJson);
    }).catch((err) => {
        console.log(err);
        res.send('fail to load restaurant lists...');
    });
} // returning no, name, distance

// 잔여좌석순 레스토랑
exports.searchByRemains = (req, res) => {
    console.log('/home/search/remains')

    knex.select('restaurant.no', 'name', 'image','address', 's_total_num', 's_left_num').from('restaurant').innerJoin('seat', 'restaurant.no', 'seat.no')
        .then((rows) => {
            var jsonArray = new Array();
            rows.forEach(row => {
                var rest = {
                    no: row.no,
                    name: row.name,
                    image: row.image,
                    address: row.address,
                    remains: row.s_left_num
                };
                jsonArray.push(rest);
            });
            var sJson = JSON.stringify(jsonArray);
            res.send(sJson);
        }).catch((err) => {
            console.log(err);
            res.send('fail to load restaurant lists...');
        });
} // returning no, name, remains(seats)

// 음식 종류
exports.searchByCategory = (req, res) => {
    var f_type = req.params.type;
    const inputData = req.body;
    var u_lon = inputData.longitude;
    var u_lat = inputData.latitude;
    console.log('/home/search/category/',f_type);
    console.log(inputData);
    

    knex.from('restaurant').select('no','name','image','address', 'longitude', 'latitude').where('f_type', f_type)
    .then((rows) => {
        var jsonArray = new Array();
        rows.forEach(row => {
            var distance = getDistance(u_lon, u_lat, row.longitude, row.latitude, "kilometer");
            var rest = {
                no: row.no,
                name: row.name,
                address: row.address,
                distance: distance,
                image: row.image
            };
            jsonArray.push(rest);
        });
        var sJson = JSON.stringify(jsonArray);
        res.send(sJson);
    }).catch((err) => {
        console.log(err);
        res.send('fail to load restaurant lists...');
    })
}

function getDistance(lon1, lat1, lon2, lat2, unit) {
    theta = lon1 - lon2;
    dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

    dist = Math.acos(dist);
    dist = rad2deg(dist);
    dist = dist * 60 * 1.1515;

    if (unit == "kilometer") {
        dist = dist * 1.609344;
    } else if (unit == "meter") {
        dist = dist * 1609.344;
    }

    return (dist);
}
function deg2rad(deg) {
    return (deg * Math.PI / 180.0);
}

function rad2deg(rad) {
    return (rad * 180 / Math.PI);
}