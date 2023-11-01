<?php
include "config.inc.php";

$image_url = "http://10.99.138.167/services/";

$path_params = explode('/', trim($_SERVER['PATH_INFO']));
$input = json_decode(file_get_contents('php://input'), true);

if (@$input['name']) {
    $name = $input['name'];
    $address = $input['address'];
    $latitude = $input['latitude'];
    $longitude = $input['longitude'];
    $description = $input['description'];
}

switch ($_SERVER['REQUEST_METHOD']) {

    case 'GET':
        $userId = null;

        if (isset($path_params[1]) && is_numeric($path_params[1])) {
            $userId = $path_params[1];
        }

        $sql = "SELECT * FROM place";
        
        if ($userId) {
            $sql .= " WHERE iduser = " . $userId;
        }

        $result = $dbi->query($sql);
        $list = [];

        while ($row = $result->fetch_assoc()) {
            $row['image'] = $image_url . $row['image'];
            $list[] = $row;
        }

        header('Content-Type: application/json');
        print json_encode($list, JSON_UNESCAPED_UNICODE);

        break;


    }
    

