<?php

    include "config.inc.php";

	
	$path_params = explode('/', trim($_SERVER['PATH_INFO']));
	
	
	switch($_SERVER['REQUEST_METHOD'] ) {

			
		case 'POST':

			$input = json_decode(file_get_contents('php://input'),true);

            //print $input;

            $idplace=$input['idplace'];
            $iduser=$input['iduser'];

            $sql="insert into likes(iduser, idplace) values ('$iduser', '$idplace') ";
			//print "$sql"; // debug
            header("Access-Control-Allow-Origin: *");  // allow cross origin
            header('Content-Type: application/json');
			if ($dbi->query($sql)){
                print "{'ok':1, 'iduser': $iduser, 'idplace': $idplace}";
            } else {
                print "{'ok':0, 'iduser': $iduser, 'idplace': $idplace}";
            }
			break;

		default: 
			echo "like service";
	}
	

?>

	