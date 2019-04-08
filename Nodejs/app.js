/** author ckddn
 * testing app for For checking the connection with the POS
 */

const express = require('express')
const app = express()
const port = 5000

app.use(express.urlencoded({ extended: false }))
app.use(express.json());

app.get('/seatsChanged', (req, res) => {
    res.send('Welcome to Let Eat Go!!!')
})
app.post('/seatsChanged', (req, res) => {
    console.log('test connection with dPOS');
    
    const inputData = req.body
    var totalPlace = inputData.totalPlaceNum
    var availablePlace = inputData.availablePlaceNum

    console.log('totalPlace : ' + totalPlace + ' ,availablePlace : ' + availablePlace);
    
    res.send('HI')
})


app.listen(port, () => console.log(`LetEatGo example app listening on port ${port}!`))