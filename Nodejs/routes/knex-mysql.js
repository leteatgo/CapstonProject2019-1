/* ./route/knex-mysql.js */
const knex = require('knex')({
  client: 'mysql',
  connection: {
    host: '127.0.0.1',
    user: 'user',
    password: 'password',
    database: 'database'
  }
});

module.exports = knex;