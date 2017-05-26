<?php 
 
    class DbOperations{
 
        private $con; 
 
        function __construct(){
 
            require_once dirname(__FILE__).'/DbConnect.php';
 
            $db = new DbConnect();
 
            $this->con = $db->connect();
 
        }
 
        /*CRUD -> C -> CREATE */
        //test code
        public function test($temp, $co){
            //$password = md5($pass);
            $stmt = $this->con->prepare("INSERT INTO `test` (`id`, `temp`, `co`) VALUES (NULL, ?, ?);");
            $stmt->bind_param("ss", $temp, $co);

            if($stmt->execute()){
                return 1; 
            }else{
                return 2; 
            }
        }


		public function createUser($temp, $transtime, $latitude, $longitude){
            //$password = md5($pass);
            $stmt = $this->con->prepare("INSERT INTO `transtemp` (`id`, `temp`, `transtime`, `latitude`, `longitude`) VALUES (NULL, ?, ?, ?, ?);");
            $stmt->bind_param("ssss", $temp, $transtime, $latitude, $longitude);

            if($stmt->execute()){
                return 1; 
            }else{
                return 2; 
            }
        }

        public function gimpo_3data($temp, $co, $smoke, $latitude, $longitude){
            //$password = md5($pass);
            $stmt = $this->con->prepare("INSERT INTO `gimpo_3data` (`id`, `temp`, `co`, `smoke`, `latitude`, `longitude`) VALUES (NULL, ?, ?, ?, ?, ?);");
            $stmt->bind_param("sssss" ,$temp, $co, $smoke, $latitude, $longitude);

            if($stmt->execute()){
                return 1; 
            }else{
                return 2; 
            }
        }
        

        public function songdo_3data($temp, $co, $smoke, $latitude, $longitude){
            //$password = md5($pass);
            $stmt = $this->con->prepare("INSERT INTO `songdo_3data` (`id`, `temp`, `co`, `smoke`, `latitude`, `longitude`) VALUES (NULL, ?, ?, ?, ?, ?);");
            $stmt->bind_param("sssss" ,$temp, $co, $smoke, $latitude, $longitude);

            if($stmt->execute()){
                return 1; 
            }else{
                return 2; 
            }
        }

        public function seogu_3data($temp, $co, $smoke, $latitude, $longitude){
            //$password = md5($pass);
            $stmt = $this->con->prepare("INSERT INTO `seogu_3data` (`id`, `temp`, `co`, `smoke`, `latitude`, `longitude`) VALUES (NULL, ?, ?, ?, ?, ?);");
            $stmt->bind_param("sssss" ,$temp, $co, $smoke, $latitude, $longitude);

            if($stmt->execute()){
                return 1; 
            }else{
                return 2; 
            }
        }     

        public function bupyeong_3data($temp, $co, $smoke, $latitude, $longitude){
            //$password = md5($pass);
            $stmt = $this->con->prepare("INSERT INTO `bupyeong_3data` (`id`, `temp`, `co`, `smoke`, `latitude`, `longitude`) VALUES (NULL, ?, ?, ?, ?, ?);");
            $stmt->bind_param("sssss" ,$temp, $co, $smoke, $latitude, $longitude);

            if($stmt->execute()){
                return 1; 
            }else{
                return 2; 
            }
        }


        public function bupyeong_transfer($temp, $transtime, $latitude, $longitude){
            //$password = md5($pass);
            $stmt = $this->con->prepare("INSERT INTO `bupyeong_temp` (`id`, `temp`, `transtime`, `latitude`, `longitude`) VALUES (NULL, ?, ?, ?, ?);");
            $stmt->bind_param("ssss", $temp, $transtime, $latitude, $longitude);

            if($stmt->execute()){
                return 1; 
            }else{
                return 2; 
            }
        }

        public function songdo_transfer($temp, $transtime, $latitude, $longitude){
            //$password = md5($pass);
            $stmt = $this->con->prepare("INSERT INTO `songdo_temp` (`id`, `temp`, `transtime`, `latitude`, `longitude`) VALUES (NULL, ?, ?, ?, ?);");
            $stmt->bind_param("ssss", $temp, $transtime, $latitude, $longitude);

            if($stmt->execute()){
                return 1; 
            }else{
                return 2; 
            }
        }

        public function seo_transfer($temp, $transtime, $latitude, $longitude){
            //$password = md5($pass);
            $stmt = $this->con->prepare("INSERT INTO `seo_temp` (`id`, `temp`, `transtime`, `latitude`, `longitude`) VALUES (NULL, ?, ?, ?, ?);");
            $stmt->bind_param("ssss", $temp, $transtime, $latitude, $longitude);

            if($stmt->execute()){
                return 1; 
            }else{
                return 2; 
            }
        }

    }