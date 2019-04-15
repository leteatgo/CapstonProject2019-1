/* ./route/knex-mysql.js */
const knex = require('knex')({
  client: 'mysql',
  connection: {
    host: '127.0.0.1',
    user: 'root',
    password: '111111', // should change
    database: 'leteatgo'
  }
});

module.exports = knex;