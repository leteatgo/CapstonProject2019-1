/* ./route/miribom/home.js */
var knex = require('./../knex-mysql');
var fs = require('fs');
/* when user click the login, give information about restaurants */
exports.home = (req, res) => {
    console.log('/home')
    const inputData = req.body;
    var no = inputData.no;
    var longitude = inputData.longitude;
    var latitude = inputData.latitude;
    console.log('longitude : ' + longitude + ' latitude: ' + latitude);
    knex('user_location').insert({ u_no: no, longitude: longitude, latitude: latitude})
    .then((rows) => {
        console.log(rows[0]);
    }).catch((err) => {
        console.err(err);
    });
    knex('restaurant').select('*').then((rows) => {
        var jsonArray = new Array();
        rows.forEach(element => {
            console.log('distance from ' + element.name + ' : ' + getDistance(longitude, latitude, element.longitude, element.latitude, "kilometer"));
            var filename = "/home/ckddn9496/LetEatGo/images/" + element.image + ".jpg";
            var bmpBuffer = fs.readFileSync(filename);
            var restInfo = {
                no: element.no,
                name: element.name,
                address: element.address,
                latitude: element.latitude,
                longitude: element.longitude,
                distance: getDistance(longitude, latitude, element.longitude, element.latitude, "kilometer"),
                image: JSON.stringify(bmpBuffer)
            }
            jsonArray.push(restInfo);
        });
        var sJson = JSON.stringify(jsonArray);
        res.send(sJson);
    }).catch((err) => {
        console.error(err);
        res.send('레스토랑 정보를 가져오는것에 실패 하였습니다.');
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

