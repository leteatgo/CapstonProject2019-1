/* ./route/miribom/home.js */
var knex = require('./../knex-mysql');
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
            distance = getDistance(longitude, latitude, element.longitude, element.latitude, "kilometer");
            console.log('distance from ' + element.name + ' : ' + distance);
            if (distance < 1) {
                var restInfo = {
                    no: element.no,
                    name: element.name,
                    address: element.address,
                    mobile: element.mobile,
                    latitude: element.latitude,
                    longitude: element.longitude,
                    distance: getDistance(longitude, latitude, element.longitude, element.latitude, "kilometer"),
                    hours: element.hours,
                    image: element.image,
                    owner_request: element.owner_request
                } 
                jsonArray.push(restInfo);
            }
        });
        jsonArray.sort(function(a, b) {
            return a.distance - b.distance;
        })
        var sJson = JSON.stringify(jsonArray);
        // console.log(sJson);
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

