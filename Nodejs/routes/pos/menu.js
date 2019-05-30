/* ./route/pos/menu.js */
var knex = require('./../knex-mysql');
var fs = require('fs');

exports.menu_insert = (req, res) => {
    console.log('/pos/restaurant/menu/insert');
    const inputData = req.body;
    var res_no = inputData.res_no; 
    var id = inputData.id;          
    var code = inputData.code;     
    var name = inputData.name;
    var price = inputData.price;   
    var image = inputData.image; 
    console.log(inputData);
    
    saveImage(res_no+"_"+id+".jpg", image);
    knex('menu').insert({res_no:res_no, id:id, code:code, name:name, price:price, image:res_no+"_"+id})
    .then((rows) => {
        res.send(rows);
    }).catch((err) => {
        res.send('메뉴 등록에 실패하였습니다.');
    })
}

exports.menu_update = (req, res) => {
    console.log('/pos/restaurant/menu/update');
    const inputData = req.body;
    var res_no = inputData.res_no;  
    var id = inputData.id;           
    var code = inputData.code;      
    var name = inputData.name;      
    var price = inputData.price;    
    var image = inputData.image;    
    console.log(inputData);
    
    saveImage(res_no+"_"+id+".jpg", image);
    knex('menu').where({ res_no: res_no, id:id})
    .update({res_no:res_no, id:id, code:code, name:name, price:price, image:res_no+"_"+id})
    .then((rows) => {
        console.log(rows);
        res.sendStatus(200);
    }).catch((err) => {
        console.log(err);
        res.send('메뉴 수정에 실패하였습니다.');
    })
}

exports.menu_delete = (req, res) => {
    console.log('/pos/restaurant/menu/delete');
    const inputData = req.body;
    var res_no = inputData.res_no; 
    var id = inputData.id;   
    // var code = inputData.code;     
    // var name = inputData.name;     
    // var price = inputData.price;   
    // var image = inputData.image;   
    console.log(inputData);
    
    knex('menu').select('image').where({res_no:res_no, id:id}).then((imagedata) => {
        removeImage(imagedata[0].image);
        return knex('menu').where({res_no:res_no, id:id}).del()
            .then((rows) => {
                console.log(rows);
                res.sendStatus(200);
            }).catch((err) => {
                console.log(err);
                res.send('메뉴 삭제에 실패하였습니다.');
            })
    }).catch((err) => {
        console.log(err);
        res.send('해당 메뉴 이미지 탐색에 실패하였습니다.')
    })
    
}

// 이미지 저장
function saveImage(filename, data) {
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
// 이미지 삭제
function removeImage(filename) {
    fs.unlink("/home/ckddn9496/LetEatGo/images/" +filename, function(err) {
        if (err) {
            console.log(err);
        } else {
            console.log('file deleted successfully');
        }
    })
}