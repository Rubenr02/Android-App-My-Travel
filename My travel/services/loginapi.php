<?php
    include "config.inc.php";	
	$path_params = explode('/', trim($_SERVER['PATH_INFO']));


	if ($_SERVER['REQUEST_METHOD'] == 'POST'){
        if (isset($path_params[1])){
            $cmd=$path_params[1];
            if ($cmd == "login"){
                login($_POST['user'], $_POST['pass']);
            } else if ($cmd == "register"){
                register($_POST['email'], $_POST['user'], $_POST['pass']);
                
            } else{
                print "nothing to do";
            }
        }

    }

    function login($user, $pass ){
        
        global $dbi;

        $user=$dbi->real_escape_string($user);
        $pass=mypass($pass);

        $sql="SELECT id FROM users WHERE password='$pass' AND username='$user'";

        //print $sql;
        
        $result = $dbi->query( $sql);
        $num = $result->num_rows;

        if ($num === 1) {
            $row = $result->fetch_assoc();
            $iduser=$row['id'];
            print $iduser;
        } else {
            print "LOGIN ERROR";
        }

    }


    function register($email, $user, $pass){
        global $dbi;

        $email=$dbi->real_escape_string($email);
        $user=$dbi->real_escape_string($user);
        $pass=mypass($pass);

        $sql="select username from users where username='$user' ";
        $result = $dbi->query($sql);
        if ($result->num_rows)
            die("USER EXISTS");

        $sql="insert into users(email, username, password) 
        values ('$email', '$user', '$pass') ";

        
        if ($dbi->query($sql)){
            print $dbi->insert_id;
        } else {
            print "REGISTER ERROR";
        }
    }


