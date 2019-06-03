/* ./route/miribom/image.js */
var knex = require('./../knex-mysql');
var fs = require('fs');

exports.loadImage = (req, res) => {
    const inputData = req.body;
    var imagePath = inputData.imageUrl;
    console.log('/image/load', imagePath);

    var filename = "/home/ckddn9496/LetEatGo/images/" + imagePath + ".jpg";
    var bmpBuffer = fs.readFileSync(filename);
    var restInfo = {
        image: JSON.stringify(bmpBuffer)
    }
    res.send(restInfo);
}