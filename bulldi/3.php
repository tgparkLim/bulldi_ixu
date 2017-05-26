<?php
$connect = mysqli_connect()

?>

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>Smart Fire Detector | bulldi</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width">

    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/bootstrap-responsive.min.css">
    <link rel="stylesheet" href="css/font-awesome.min.css">
    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/sl-slide.css">

    <script src="js/vendor/modernizr-2.6.2-respond-1.1.0.min.js"></script>
    <script type="text/javascript" src="js/jquery.min.js"></script>

    <script>
	  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
	  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
	  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
	  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
	
	  ga('create', 'UA-75848764-1', 'auto');
	  ga('send', 'pageview');
	
	</script>

    <!--google maps -->
    <script src="d3.v3.min.js" type="text/Javascript"></script>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyADZhjylXxCS2VvECDv1SAC2w5kFATvdVA" type="text/javascript"></script>

    <script>
        // 구글 맵 초기화
        function initialize() {
            var bupyeong = {lat: 37.4939292, lng: 126.7220893 };
            var seo = {lat: 37.517094, lng: 126.6698924 };

            //마커 위치 설정
            var markLocation = new google.maps.LatLng('37.6331547', '126.7060423');
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


            var marker = new google.maps.Marker({
                position: markLocation, // 마커가 위치할 위도와 경도(변수)
                map: map,
                icon: image, // 마커로 사용할 이미지(변수)
                title: '광화문' // 마커에 마우스 포인트를 갖다댔을 때 뜨는 타이틀
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

            function popupOpen() {
                var popUrl = "http://13.124.11.28/temp/tempboard/test.php"; //팝업창에 출력될 페이지 URL
                var popOption = "width=570, height=360, resizable=no, scrollbars=no, status=no;"; //팝업창 옵션(optoin)
                window.open(popUrl, "", popOption);
            }

            function popupOpen_bupyeong() {
                var popUrl = "http://13.124.11.28/temp/tempboard/bupyeong_board_graph.php"; //팝업창에 출력될 페이지 URL
                var popOption = "width=870, height=560, resizable=no, scrollbars=no, status=no;"; //팝업창 옵션(optoin)
                window.open(popUrl, "", popOption);
            }

            function popupOpen_seo() {
                var popUrl = "http://13.124.11.28/temp/tempboard/seo_board.php"; //팝업창에 출력될 페이지 URL
                var popOption = "width=570, height=360, resizable=no, scrollbars=no, status=no;"; //팝업창 옵션(optoin)
                window.open(popUrl, "", popOption);
            }

            google.maps.event.addListener(marker, "click", function() {
                //infowindow.open(map, marker);
                //location.replace(link);
                popupOpen();
            });

            google.maps.event.addListener(marker_bupyeong, "click", function() {
                //infowindow.open(map, marker);
                //location.replace(link);
                popupOpen_bupyeong();
            });

            google.maps.event.addListener(marker_seo, "click", function() {
                //infowindow.open(map, marker);
                //location.replace(link);
                popupOpen_seo();
            });

        }

        //페이지 로드시 initialize 함수 실행
        google.maps.event.addDomListener(window, 'load', initialize);
    </script>



    <!-- Le fav and touch icons -->
    <!-- <link rel="shortcut icon" href="images/ico/favicon.ico">
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="images/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="images/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="images/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="images/ico/apple-touch-icon-57-precomposed.png"> -->
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
                
                <!-- <div class="nav-collapse collapse pull-right">
                    <ul class="nav">
                        <li class="active"><a href="index.html">bulldi</a></li>
                        <li><a href="app.html">app</a></li>
                        <li><a href="news.html">news</a></li>
                        <li><a href="faq.html">Faq</a></li> 
                        <li><a href="contact-us.html">Contact</a></li>
                        <li class="login"><a data-toggle="modal" href="eng/index.html">English</i></a></li>
                    </ul>        
                </div><!--/.nav-collapse -->
            </div>
        </div>
    </header>
    <!-- /header -->

    <!--Slider-->
    <section id="slide-show">
     <div id="slider" class="sl-slider-wrapper">

        <!--Slider Items-->    
        <div class="sl-slider">
            
            <article>
                <figure id="googleMap" style="width:1000px;height:580px;">
                
            </article>
            

            
        </div>
        <!--Slider Item3-->

    </div>
    <!--/Slider Items-->



</div>
<!-- /slider-wrapper -->           
</section>
<!--/Slider-->

<section class="main-info">
    <div class="container">
        <div class="row-fluid">
            <div class="span9">
                <h4>bulldi는 사물인터넷(IoT)제품입니다.</h4>
                <h4>스마트 화재감지기는 일산화탄소, 연기, 온도를 감지하여 스마트폰으로 긴급상황임을 알려줍니다.</h4>
            </div>
            <!-- <div class="span3">
                <a class="btn btn-success btn-large pull-right" href="images/bulldi_K_2016.pdf" target=_blank>안내자료 내려받기</a>
            </div> -->
        </div>
    </div>
</section>

<!--Services-->
 <!-- <section id="services">
    <div class="container">
        <div class="center gap">
            <h3>스마트 화재감지기 주요기능</h3>
        </div>

        <div class="row-fluid">
            <div class="span4">
                <div class="media">
                    <div class="pull-left">
                        <img src="images/sample/icon_01.png" alt="화재감지"/>
                    </div>
                    <div class="media-body">
                        <h4 class="media-heading">화재감지</h4>
                        <p>bulldi는 스마트 화재감지기로 3 in one(일산화탄소, 연기, 온도)를 한번에 잡는 똑똑한 감지기입니다. </p><br>
                    </div>
                </div>
            </div>            

            <div class="span4">
                <div class="media">
                    <div class="pull-left">
                        <img src="images/sample/icon_02.png" alt="자체경고음"/>
                    </div>
                    <div class="media-body">
                        <h4 class="media-heading">자체경고음</h4>
                        <p>블루투스로 연동이 되어 스마트폰에서도 화재경고음이 출력이 되지만, bulldi 자체에서도 비상시 경고음을 출력시킵니다.</p><br>
                    </div>
                </div>
            </div>            

            <div class="span4">
                <div class="media">
                    <div class="pull-left">
                        <img src="images/sample/icon_03.png" alt="조명등"/>
                    </div>
                    <div class="media-body">
                        <h4 class="media-heading">대피등</h4>
                        <p>모바일로 원격제어할수 있는 대피등이 함께하고 있어, 실내.외에서 가볍게 사용할 수 있습니다.</p><br>
                    </div>
                </div>
            </div>
        </div>

        <div class="row-fluid">
            <div class="span4">
                <div class="media">
                    <div class="pull-left">
                        <img src="images/sample/icon_04.png" alt="건전지"/>
                    </div>
                    <div class="media-body">
                        <h4 class="media-heading">건전지</h4>
                        <p>AAA건전지를 교체하여 사용하는 방식입니다. 스마트폰이 연결된 상태에서 감지시 약 10개월 지속됩니다. (조명배제)</p><br>
                    </div>
                </div>
            </div>            

            <div class="span4">
                <div class="media">
                    <div class="pull-left">
                        <img src="images/sample/icon_05.png" alt="건전지"/>
                    </div>
                    <div class="media-body">
                        <h4 class="media-heading">블루투스</h4>
                        <p>스마트폰과 블루투스 BLE로 무선연동이 원격제어 할 수 있습니다. 최대거리 70m, 유효거리 35m 지원하게 됩니다.</p><br>
                    </div>
                </div>
            </div>            

            <div class="span4">
                <div class="media">
                    <div class="pull-left">
                        <img src="images/sample/icon_06.png" alt="앱"/>
                    </div>
                    <div class="media-body">
                        <h4 class="media-heading">앱</h4>
                        <p>bulldi는 스마트폰앱으로 원격제어가 되도록 설계가 되었으며, android와 iOS 모두 지원합니다.</p><br>
                    </div>
                </div>
            </div>
        </div>

        <div class="row-fluid">
            <div class="span4">
                <div class="media">
                    <div class="pull-left">
                        <img src="images/sample/icon_07.png" alt="건전지"/>
                    </div>
                    <div class="media-body">
                        <h4 class="media-heading">비상시 친구알림</h4>
                        <p>화재시 골든타임이 흐를때 가까이 있는 친구들의 도움을 받도록 하십시요. 비상소식을 함께 전달이 됩니다.</p><br>
                    </div>
                </div>
            </div>            

            <div class="span4">
                <div class="media">
                    <div class="pull-left">
                        <img src="images/sample/icon_08.png" alt="건전지"/>
                    </div>
                    <div class="media-body">
                        <h4 class="media-heading">빅데이터</h4>
                        <p>화재 위치별, 시간별, 유형별, 원인별, 대처법 등 화재통계자료를 제공합니다.</p><br>
                    </div>
                </div>
            </div>            

            <div class="span4">
                <div class="media">
                    <div class="pull-left">
                        <img src="images/sample/icon_06.png" alt="앱"/>
                    </div>
                    <div class="media-body">
                        <h4 class="media-heading">화재데모테스트</h4>
                        <p>bulldi는 원격으로 화재데모테스트, 알람테스트, 램프테스트를 진행해 볼수 있습니다.</p><br>
                    </div>
                </div>
            </div>
        </div>

    </div>
</section> -->
<!--/Services -->

<!-- <section id="recent-works">
    <div class="container">
        <div class="center">
            <h3>제품 상세보기</h3>
            <h4 class="media-heading">스마트 화재감지기는 지름 70mm, 높이 60mm(손잡이 제외)로 한손에 들어옵니다.</h4>
        </div>  
        <div class="gap"></div>
        <ul class="gallery col-4"> -->
            <!--Item 1-->
            <!-- <li>
                <div class="preview">
                    <img alt=" " src="images/portfolio/thumb/01.png">
                    <div class="overlay">
                    </div>
                    <div class="links">
                        <a data-toggle="modal" href="#modal-1"><i class="icon-eye-open"></i></a>                          
                    </div>
                </div>
                <div class="desc">
                    <h5 align=center>윗면</h5>
                </div>
                <div id="modal-1" class="modal hide fade">
                    <a class="close-modal" href="javascript:;" data-dismiss="modal" aria-hidden="true"><i class="icon-remove"></i></a>
                    <div class="modal-body">
                        <img src="images/portfolio/full/1.jpg" alt=" " width="100%" style="max-height:600px">
                    </div>
                </div>                 
            </li> -->
            <!--/Item 1--> 

            <!--Item 2-->
            <!-- <li>
                <div class="preview">
                    <img alt=" " src="images/portfolio/thumb/02.png">
                    <div class="overlay">
                    </div>
                    <div class="links">
                        <a data-toggle="modal" href="#modal-2"><i class="icon-eye-open"></i></a>                             
                    </div>
                </div>
                <div class="desc">
                    <h5 align=center>정면</h5>
                </div>
                <div id="modal-2" class="modal hide fade">
                    <a class="close-modal" href="javascript:;" data-dismiss="modal" aria-hidden="true"><i class="icon-remove"></i></a>
                    <div class="modal-body">
                        <img src="images/portfolio/full/2.jpg" alt=" " width="100%" style="max-height:600px">
                    </div>
                </div>                 
            </li> -->
            <!--/Item 2-->

            <!--Item 3-->
            <!-- <li>
                <div class="preview">
                    <img alt=" " src="images/portfolio/thumb/03.png">
                    <div class="overlay">
                    </div>
                    <div class="links">
                        <a data-toggle="modal" href="#modal-3"><i class="icon-eye-open"></i></a>                               
                    </div>
                </div>
                <div class="desc">
                    <h5 align=center>옆면</h5>
                </div>
                <div id="modal-3" class="modal hide fade">
                    <a class="close-modal" href="javascript:;" data-dismiss="modal" aria-hidden="true"><i class="icon-remove"></i></a>
                    <div class="modal-body">
                        <img src="images/portfolio/full/3.jpg" alt=" " width="100%">
                    </div>
                </div>                 
            </li> -->
            <!--/Item 3--> 

            <!--Item 4-->
            <!-- <li>
                <div class="preview">
                    <img alt=" " src="images/portfolio/thumb/04.png">
                    <div class="overlay">
                    </div>
                    <div class="links">
                        <a data-toggle="modal" href="#modal-4"><i class="icon-eye-open"></i></a>                               
                    </div>
                </div>
                <div class="desc">
                    <h5 align=center>바닥면</h5>
                </div>
                <div id="modal-4" class="modal hide fade">
                    <a class="close-modal" href="javascript:;" data-dismiss="modal" aria-hidden="true"><i class="icon-remove"></i></a>
                    <div class="modal-body">
                        <img src="images/portfolio/full/4.jpg" alt=" " width="100%">
                    </div>
                </div>                 
            </li> -->
            <!--/Item 4-->         

<!--         </ul>
    </div>
</section>
 -->

<!-- <section id="about-us" class="container main">
  <div class="row-fluid">
      <div class="center">
          <h3>서비스 개요</h3>
      </div>
  </div>
  
  <div class="row-fluid">
      <div class="span3">
          <div class="box">
              <p><img src="images/sample/team1.jpg" alt="" ></p>
              <h5>일산화탄소</h5>
              <p>침묵의 암살자로 알려진 일산화탄소를 확인감지하는데, 기준최저 50ppm에서 치사량에 이르는 300ppm까지 일산화탄소 농도와 체류시간에 따라 화재경보를 달리하게끔 최적화가 되어있다.</p>
          </div><br>
      </div>
  
      <div class="span3">
          <div class="box">
              <p><img src="images/sample/team2.jpg" alt="" ></p>
              <h5>온도</h5>
              <p>현재온도와 이전온도를 명확하게 체크하고, 섭씨57도(미국UL기준)에 도달하거나 1분에 섭씨 8도의 온도상승(화재예감)을 보일때 화재경보를 하게끔 설계되어 있다.</p>
          </div>
      </div>
  
      <div class="span3">
          <div class="box">
              <p><img src="images/sample/team3.jpg" alt="" ></p>
              <h5>연기</h5>
              <p>광전식으로 연기를 감지하는데, 발광부와 수광부로 구성된 구조로 발광부와 수광부 사이의 공간에 일정한 농도의 연기를 포함하게 되는 경우 작동하게 되어 있다.</p>
          </div>
      </div>
  
      <div class="span3">
          <div class="box">
              <p><img src="images/sample/team4.jpg" alt="" ></p>
              <h5>위험공유</h5>
              <p>화재시 단말장치에서도 화재경보를 발생시키지만, 블루투스로 연동된 스마트폰에서도 화재경보를 하게됩니다. 이때 미리등록된 지인들에게 위험문자를 자동전송하게 된다.</p>
          </div>
      </div>
  	</div>
  
  
 	 <div class="row-fluid">
      <div class="span3">
          <div class="box">
              <p><img src="images/sample/team5.jpg" alt="" ></p>
              <h5>무선통신</h5>
              <p>블루투스BLE로 스마트폰과 통신하게 되며 스마트폰마다 거리차이가 있습니다.<br>iPhone 5 - 60m(최대거리) 30(유효), <br>갤럭시 s5 - 70m(최대거리) 36m(유효), <br>넥서스 5x - 120m(최대거리) 60(유효)</p>
          </div>
      </div>
  
      <div class="span3">
          <div class="box">
              <p><img src="images/sample/team6.jpg" alt="" ></p>
              <h5>배터리</h5>
              <p>일반 AAA건전지 2개가 장착이 됩니다. <br>(스마트폰 연결된 상태에서 감지시 10개월<br>감지기만 작동시 5개월 - 조명배제)</p>
          </div>
      </div>
  
      <div class="span3">
          <div class="box">
              <p><img src="images/sample/team7.jpg" alt="" ></p>
              <h5>대피등(조명)</h5>
              <p>대피등의 밝기는 약 10lux 입니다.<br>(연속 재생시 30시간 작동하며 배터리가 30% 이하에선 화재감지 우선적용으로 램프점등을 지원하지 않습니다)</p>
          </div>
      </div>
  
      <div class="span3">
          <div class="box">
              <p><img src="images/sample/team8.jpg" alt="" ></p>
              <h5>기타</h5>
              <p>동작온도는 섭씨 -40 ~ 85도, <br>사용기간은 7년, <br>크기 70 x 높이 60mm (손잡이 제외), <br>제품무게는 110g입니다. <br>*사용환경에 따라 달라질수 있습니다.</p>
          </div>
      </div>
  </div>
</div>
</div>
</div>
</div>
</div>
</section> -->


<!-- <section id="clients" class="main">
    <div class="container">
        <div class="row-fluid">
            <div class="span2">
                <div class="clearfix">
                    <h4 class="pull-left">사용 사례들</h4>
                </div>
                <p>이동식으로 설계가 되어 야외활동에 있어 캠핑카, 텐트 등 잠시 머물러 가는 곳에서도 활용도가 뛰어납니다.</p>
            </div>
            <div class="span10">
                <div id="myCarousel" class="carousel slide clients"> -->
                  <!-- Carousel items -->
                  <!-- <div class="carousel-inner">
                    <div class="active item">
                        <div class="row-fluid">
                            <ul class="thumbnails">
                                <li class="span3"><img src="images/sample/clients/client1.png"><h6>야외활동</h6></li>
                                <li class="span3"><img src="images/sample/clients/client2.png"><h6>주택/숙박업소</h6></li>
                                <li class="span3"><img src="images/sample/clients/client3.png"><h6>텐트/글램핑</h6></li>
                                <li class="span3"><img src="images/sample/clients/client4.png"><h6>차량/캠핑카</h6></li>
                            </ul>
                        </div>
                    </div>
                </div> -->
                <!-- /Carousel items -->

<!--             </div>
        </div>
    </div>
</div>
</section> -->




<!--Footer-->
<!-- <footer id="footer">
    <div class="container">
        <div class="row-fluid">
            <div class="span5 cp">
                &copy; 2016 <a target="_blank" href="http://www.openstack.co.kr/">Open Stack, Inc.</a>. All Rights Reserved.
            </div> -->
            <!--/Copyright-->

<!--             <div class="span6">
                <ul class="social pull-right">
                    <li><a href="https://www.facebook.com/hello.bulldi" target="_blank"><i class="icon-facebook"></i></a></li>                   
                    <li><a href="https://www.youtube.com/channel/UCLY-U0qSCEQJuLAjzPf0j_A" target="_blank"><i class="icon-youtube"></i></a></li>
                    <li><a href="https://www.instagram.com/hello.bulldi" target="_blank"><i class="icon-instagram"></i></a></li>                   
                </ul>
            </div>

            <div class="span1">
                <a id="gototop" class="gototop pull-right" href="#"><i class="icon-angle-up"></i></a>
            </div> -->
            <!--/Goto Top-->
 <!--        </div>
    </div>
</footer> -->
<!--/Footer-->

<!-- <script src="js/vendor/jquery-1.9.1.min.js"></script>
<script src="js/vendor/bootstrap.min.js"></script>
<script src="js/main.js"></script> -->
<!-- Required javascript files for Slider -->
<!-- <script src="js/jquery.ba-cond.min.js"></script>
<script src="js/jquery.slitslider.js"></script> -->
<!-- /Required javascript files for Slider -->

<!-- SL Slider -->
<!-- <script type="text/javascript"> 
$(function() {
    var Page = (function() {

        var $navArrows = $( '#nav-arrows' ),
        slitslider = $( '#slider' ).slitslider( {
            autoplay : true
        } ),

        init = function() {
            initEvents();
        },
        initEvents = function() {
            $navArrows.children( ':last' ).on( 'click', function() {
                slitslider.next();
                return false;
            });

            $navArrows.children( ':first' ).on( 'click', function() {
                slitslider.previous();
                return false;
            });
        };

        return { init : init };

    })();

    Page.init();
});
</script> -->
<!-- /SL Slider -->
<!-- </body>
</html>
 -->