<?php
    $db = new mysqli("localhost", "root", "", "placesdb");
    $db->set_charset("utf8");
    $path_params = explode('/', trim($_SERVER['PATH_INFO']));
    $input = json_decode(file_get_contents('php://input'),true);
    
    
    switch($_SERVER['REQUEST_METHOD'] ) {
        case 'GET':
            get();
            break;
        case 'POST':
            echo 'POST metodo: ';
            post();
            //not implemented
            break;
        case 'PUT':
            echo 'PUT metodo: ';
            put();
            // not implemented
            break;
        case 'DELETE':
            echo 'DELETE metodo: ';
            del();
            // not implemented
        default: 
            echo "lisbon places service";
        }
    ///////////////////// functions //////////////////////////////////
    function get(){
        global $db, $path_params;
        
        if(isset($path_params[1])){
            if($path_params[1] == "places"){
                $sql="select * from place";
                if (isset($path_params[2]) && 
is_numeric($path_params[2])){ // id
                                    $sql.= " where id=" . $path_params[2];
                            } 
                            $result=$db->query($sql);
                            while($row = $result->fetch_assoc()){
                                    $list[]=$row;
                            }
                            
                            header("Access-Control-Allow-Origin: *"); /* allow cross 
origin */
        
                            header('Content-Type: application/json');
                            print json_encode($list, JSON_UNESCAPED_UNICODE);
                    } else {
                            print "needs parameter 1";
                    
                    }
        
            } 
        }
        
        function post(){}
        function put(){}
        function del(){}
?>
    