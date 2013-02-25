
/*
 * GET home page.
 */

exports.index = function(req, res){
  res.render('index', { title: 'Express' });
};

exports.login = function(req, res) {
  res.render('bootstrap/login');
};

exports.registration = function(req, res) {
  res.render('bootstrap/registration');
};

exports.forgotpassword = function(req, res) {
  res.render('bootstrap/forgotpassword');
};

exports.surgeon = function(req, res) {
  res.render('bootstrap/surgeon');
};

exports.asc = function(req, res) {
  res.render('bootstrap/asc');
};