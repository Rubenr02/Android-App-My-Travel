<?php

include "config.inc.php";

$path_params = isset($_SERVER['PATH_INFO']) ? explode('/', trim($_SERVER['PATH_INFO'])) : [];

switch ($_SERVER['REQUEST_METHOD']) {
    case 'POST':
        $input = json_decode(file_get_contents('php://input'), true);

        $idplace = $input['idplace'];
        $iduser = $input['iduser'];
        $comment = $input['comment'];

        $sql = "INSERT INTO comments (idplace, iduser, comment) VALUES ('$idplace', '$iduser', '$comment')";

        header("Access-Control-Allow-Origin: *");
        header('Content-Type: application/json');

        if ($dbi->query($sql)) {
            $response = ['ok' => 1, 'idplace' => $idplace, 'iduser' => $iduser];
        } else {
            $response = ['ok' => 0, 'idplace' => $idplace, 'iduser' => $iduser, 'error' => $dbi->error];
        }

        echo json_encode($response);
        break;


    case 'GET':
        // Code for handling GET request to fetch comments for a place
        if (isset($path_params[1]) && is_numeric($path_params[1])) {
            $placeId = $path_params[1];

            $sql = "SELECT comments.iduser, comments.idplace, comments.comment, users.username AS userName
                    FROM comments
                    INNER JOIN users ON comments.iduser = users.id
                    WHERE comments.idplace = '$placeId'";

            header("Access-Control-Allow-Origin: *");
            header('Content-Type: application/json');

            $result = $dbi->query($sql);
            if ($result) {
                $comments = array();
                while ($row = $result->fetch_assoc()) {
                    $comment = array(
                        'idplace' => $row['idplace'],
                        'iduser' => $row['iduser'],
                        'comment' => $row['comment'],
                        'userName' => $row['userName']
                    );
                    $comments[] = $comment;
                }
                $response = array('comments' => $comments);
            } else {
                $response = array('error' => $dbi->error);
            }

            echo json_encode($response);
        } else {
            echo "error: no place ID";
        }

        break;

    default:
        echo "comments service";
}
