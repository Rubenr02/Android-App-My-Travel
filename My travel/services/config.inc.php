<?php
$dbi = new mysqli("localhost", "root", "", "placesdb");

$dbi->set_charset("utf8");

function mypass($pass){
    $salt="sdkgfwgew";
    return md5(sha1($pass . $salt));
}