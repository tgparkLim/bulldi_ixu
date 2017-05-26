<?php
$connect = mysqli_connect()
?>

    <!--google maps -->
    <script src="d3.v3.min.js" type="text/Javascript"></script>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyADZhjylXxCS2VvECDv1SAC2w5kFATvdVA" type="text/javascript"></script>

    <script>
        // 구글 맵 초기화
        function initialize() {
            var gimpo = {lat: 37.6331683, lng: 126.7060066 };
            var bupyeong = {lat: 37.4939292, lng: 126.7220893 };
            var seo = {lat: 37.517094, lng: 126.6698924 };

            //마커 위치 설정
            //var markLocation = new google.maps.LatLng('37.6331547', '126.7060423');
            var mapProp = {
                center: new google.maps.LatLng(37.6331, 126.7060), // 지도 중간위치 설정
                zoom: 9, //zoom단계 설정
                mapTypeId: google.maps.MapTypeId.ROADMAP //Roadmap타입, 가장 기본적 타입
            };
            //div 아이디 설정
            var map = new google.maps.Map(document.getElementById("googleMap"), mapProp);
            var size_x = 20; // 마커로 사용할 이미지의 가로 크기
            var size_y = 20; // 마커로 사용할 이미지의 세로 크기

            // 마커로 사용할 이미지 주소
            var image = new google.maps.MarkerImage('http://www.larva.re.kr/home/img/boximage3.png',
                new google.maps.Size(size_x, size_y),
                '',
                '',
                new google.maps.Size(size_x, size_y));


            // var marker = new google.maps.Marker({
            //     position: markLocation, // 마커가 위치할 위도와 경도(변수)
            //     map: map,
            //     icon: image, // 마커로 사용할 이미지(변수)
            //     title: '광화문' // 마커에 마우스 포인트를 갖다댔을 때 뜨는 타이틀
            // });

            var marker_gimpo = new google.maps.Marker({
                map : map,
                position: gimpo,
                icon : image,
                title: '김포'
            });

            var marker_bupyeong = new google.maps.Marker({
                map : map,
                position: bupyeong,
                icon : image,
                title: '부평'
            });

            var marker_seo = new google.maps.Marker({
                map : map,
                position: seo,
                icon : image,
                title: '서구'
            });

            var content = "광화문 화재 현황";
            // 말풍선 안에 들어갈 내용

            // var link = "https://13.124.11.28/Temp/tempboard/test.php";
            // 마커 클릭했을때 들어갈 링크

            // 마커를 클릭했을 때의 이벤트. 말풍선
            var infowindow = new google.maps.InfoWindow({
                content: content
            });

            // function goReplace(link) {
            //     location.replace(link);
            // }


            function popupOpen_gimpo() {
                var popUrl = "http://13.124.11.28/temp/tempboard/gimpo_board_graph.php"; //팝업창에 출력될 페이지 URL
                var popOption = "width=870, height=560, resizable=no, scrollbars=no, status=no;"; //팝업창 옵션(optoin)
                window.open(popUrl, "", popOption);
            }
            google.maps.event.addListener(marker_gimpo, "click", function() {
                //infowindow.open(map, marker);
                //location.replace(link);
                popupOpen_gimpo();
            });    

            function popupOpen_bupyeong() {
                var popUrl = "http://13.124.11.28/temp/tempboard/bupyeong_board_graph.php"; //팝업창에 출력될 페이지 URL
                var popOption = "width=870, height=560, resizable=no, scrollbars=no, status=no;"; //팝업창 옵션(optoin)
                window.open(popUrl, "", popOption);
            }
            google.maps.event.addListener(marker_bupyeong, "click", function() {
                //infowindow.open(map, marker);
                //location.replace(link);
                popupOpen_bupyeong();
            });

            function popupOpen_seo() {
                var popUrl = "http://13.124.11.28/temp/tempboard/seo_board_graph.php"; //팝업창에 출력될 페이지 URL
                var popOption = "width=870, height=560, resizable=no, scrollbars=no, status=no;"; //팝업창 옵션(optoin)
                window.open(popUrl, "", popOption);
            }
            google.maps.event.addListener(marker_seo, "click", function() {
                //infowindow.open(map, marker);
                //location.replace(link);
                popupOpen_seo();
            });
        }

        //페이지 로드시 initialize 함수 실행
        google.maps.event.addDomListener(window, 'load', initialize);
    </script>

<!DOCTYPE html>
<head>
	<title>view through bulldi</title>
	<link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/bootstrap-responsive.min.css">
    <link rel="stylesheet" href="css/font-awesome.min.css">
    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/sl-slide.css">
</head>

<body>
	<!--Header-->
    <header class="navbar navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container">
                <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </a>
                <a id="logo" class="pull-left" href="index.html"></a>
                <div class="nav-collapse collapse pull-right">
                    <ul class="nav">
                        <li class="active"><a href="index.html">bulldi</a></li>
                        <!--<li><a href="http://13.124.11.28/temp/tempboard/allareatemp.php">Temperature</a></li>-->
                        <!-- <li><a href="#" onclick="MyWindow=window.open('http://13.124.11.28/temp/tempboard/allareatemp.php', 'MyWindow', "width=300, height=300"); return false;">Temperature</a></li> -->
                        <li><a href="#" onClick = "window.open('http://13.124.11.28/temp/tempboard/allareatemp.php', 'My window', 'height=600,width=800, scrollbars=no, resizable=no')" >Temperature</a></li>
                        <li><a href="news.html">Smoke</a></li>
                        <li><a href="faq.html">CO</a></li> 
                        <!-- <li><a href="contact-us.html">Contact</a></li>
                        <li class="login"><a data-toggle="modal" href="eng/index.html">English</i></a></li> -->
                    </ul>        
                </div><!--/.nav-collapse -->
            </div>
        </div>
    </header>
    <!-- /header -->

    <!-- google maps -->
    <section id = "googlemaps">
     <div id="slider" class="sl-slider-wrapper">

        <!--Slider Items-->    
        <div class="sl-slider">
            
            <article>
                <figure id="googleMap" style="width:1200px;height:580px;">
                
            </article>
            

            
        </div>
        <!--Slider Item3-->

    </div>
    <!--/Slider Items-->



</div>
<!-- /slider-wrapper -->    	

    </section>