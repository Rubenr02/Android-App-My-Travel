<?php
include "config.inc.php";

$image_url = "http:/10.99.138.167//services/";
$upload_dir = "http:/10.99.138.167//services/"; // Path de localizaçao

$path_params = explode('/', trim($_SERVER['PATH_INFO']));
$input = json_decode(file_get_contents('php://input'), true);

if (@$input['name']) {
    $name = $input['name'];
    $address = $input['address'];
    $latitude = $input['latitude'];
    $longitude = $input['longitude'];
    $description = $input['description'];
    $iduser = $input['iduser']; // Iduser correspondente
}

switch ($_SERVER['REQUEST_METHOD']) {
    case 'GET':
        $sql = "SELECT * FROM place";
        if (isset($path_params[1]) && is_numeric($path_params[1])) { 
            $sql .= " WHERE id=" . $path_params[1];
        }
        $result = $dbi->query($sql);
        while ($row = $result->fetch_assoc()) {
            $row['image'] = $image_url . $row['image'];
            $list[] = $row;
        }

        header('Content-Type: application/json');
        print json_encode($list, JSON_UNESCAPED_UNICODE);

        break;

    case 'POST':
        $sql = "INSERT INTO place(name, address, latitude, longitude, description, iduser) 
                VALUES ('$name', '$address', '$latitude', '$longitude', '$description', '$iduser') ";

        if ($dbi->query($sql)) {
            $placeId = $dbi->insert_id;

            // Handle image upload
            if (isset($_FILES['image']) && $_FILES['image']['error'] === UPLOAD_ERR_OK) {
                $tmpFilePath = $_FILES['image']['tmp_name'];
                $fileExtension = pathinfo($_FILES['image']['name'], PATHINFO_EXTENSION);
                $fileName = uniqid() . '.' . $fileExtension;
                $destination = $upload_dir . $fileName;

                if (move_uploaded_file($tmpFilePath, $destination)) {
                    // Atualiza bd com imagem
                    $updateSql = "UPDATE place SET image='$fileName' WHERE id=$placeId";
                    $dbi->query($updateSql);
                }
            }

            print "new register: $placeId";
        } else {
            
        }

        break;

    case 'PUT':
        if (isset($path_params[1]) && is_numeric($path_params[1])) { 
            $id = $path_params[1];
            $sql = "UPDATE place SET name='$name', address='$address', latitude='$latitude', longitude='$longitude', 
                    description='$description' WHERE id=$id";

            $result = $dbi->query($sql);
            print "rows changed:" . $dbi->affected_rows;
        } else {
            print "error no id";
        }

        break;

    case 'DELETE':
        if (isset($path_params[1]) && is_numeric($path_params[1])) { 
            $id = $path_params[1];

            $sql = "DELETE FROM place WHERE id=$id";
            $result = $dbi->query($sql);
            print "rows changed:" . $dbi->affected_rows;
        } else {
            print "error no id";
        }
        break;

    default:
        echo "places service";
}
?>