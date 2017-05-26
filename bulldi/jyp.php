<?php
  $con = mysqli_connect('localhost', 'root', '','transfer');
  date_default_timezone_set('Asia/Seoul');
  $now = date("Y-m-d H:i:s");
?>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <style>
      html, body {
        height: 80%;
        margin: 0;
        padding: 0;
      }
      #map {
        height: 100%;
      }
    </style>
      <title>view through bulldi</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/bootstrap-responsive.min.css">
    <link rel="stylesheet" href="css/font-awesome.min.css">
    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/sl-slide.css">
  </head>
  <body>
    <div id="map"></div>
    <!--Header-->
    <header class="navbar navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container">
                <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </a>
                <a id="logo" class="pull-left" href="shj.php"></a>
                <div class="nav-collapse collapse pull-right">
                    <ul class="nav">
                        <li class="active"><a href="#" onClick="clearMarkers()">마커 초기화</a></li>
                        <li><a href="#" onClick = "view_tempMarkers()">온도 히스토리</a></li>
                        <li><a href="#" onClick = "view_SmokeMarkers()">연기 히스토리</a></li>
                        <li><a href="#" onClick = "view_COMarkers()">일산화탄소 히스토리</a></li> 
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

        </div>
        <!--Slider Item3-->

    </div>
    <!--/Slider Items-->

</div>
<!-- /slider-wrapper -->      

    </section>
<script>
//setTimeout("location.reload()",2000)
var map;
var TempMarkers = [];
var COMarkers = [];
var SmokeMarkers = [];

var iconBase = 'http://13.124.11.28/bulldi/images/';
        var icons = {
          temp: {
            icon: iconBase + 'temperature.png'
          },
          CO: {
            icon: iconBase + 'co.png'
          },
          smoke: {
            icon: iconBase + 'smoke.png'
          }
};

var Tempfeatures = [
  {
    position: {lat: 37.6331547, lng: 126.7060423},
    type: 'temp'
  }, {
    position: {lat: 37.4939292, lng: 126.7220893},
    type: 'temp'
  }, {
    position: {lat: 37.517094, lng: 126.6698924},
    type: 'temp'
  }
];

var COfeatures = [
  {
    position: {lat: 37.6331547, lng: 126.7060423},
    type: 'CO'
  }, {
    position: {lat: 37.4939292, lng: 126.7220893},
    type: 'CO'
  }, {
    position: {lat: 37.517094, lng: 126.6698924},
    type: 'CO'
  }
];

var Smokefeatures = [
  {
    position: {lat: 37.6331547, lng: 126.7060423},
    type: 'smoke'
  }, {
    position: {lat: 37.4939292, lng: 126.7220893},
    type: 'smoke'
  }, {
    position: {lat: 37.517094, lng: 126.6698924},
    type: 'smoke'
  }
];

function view_tempMarkers() {
  clearMarkers();
  for (var i = 0, Tempfeature; Tempfeature = Tempfeatures[i]; i++) {
    addTempMarker(Tempfeature);

    google.maps.event.addListener(TempMarkers[i], "click", function() {
      TemppopupOpen();
    });
  }
}

function view_COMarkers() {
  clearMarkers();
  for (var i = 0, COfeature; COfeature = COfeatures[i]; i++) {
    addCOMarker(COfeature);

    google.maps.event.addListener(COMarkers[i], "click", function() {
      COpopupOpen();
    });
  }
}

function view_SmokeMarkers() {
  clearMarkers();
  for (var i = 0, Smokefeature; Smokefeature = Smokefeatures[i]; i++) {
    addSmokeMarker(Smokefeature);

    google.maps.event.addListener(SmokeMarkers[i], "click", function() {
      SmokepopupOpen();
    });
  }
}

function addTempMarker(Tempfeature) {
  TempMarkers.push(new google.maps.Marker({
    position: Tempfeature.position,
    icon: icons[Tempfeature.type].icon,
    map: map
  }));
}

function addCOMarker(COfeature) {
  COMarkers.push(new google.maps.Marker({
    position: COfeature.position,
    icon: icons[COfeature.type].icon,
    map: map
  }));

  google.maps.event.addListener(COMarkers, "click", function() {
    COpopupOpen();
  });
}

function addSmokeMarker(Smokefeature) {
  SmokeMarkers.push(new google.maps.Marker({
    position: Smokefeature.position,
    icon: icons[Smokefeature.type].icon,
    map: map
  }));

  google.maps.event.addListener(SmokeMarkers, "click", function() {
    SmokepopupOpen();
  });
}

function clearMarkers() {
  for (var i = 0; i < TempMarkers.length; i++) {
    TempMarkers[i].setMap(null);
  }
  TempMarkers = [];
  for (var i = 0; i < COMarkers.length; i++) {
    COMarkers[i].setMap(null);
  }
  COMarkers = [];
  for (var i = 0; i < SmokeMarkers.length; i++) {
    SmokeMarkers[i].setMap(null);
  }
  SmokeMarkers = [];
}

function TemppopupOpen() {
  var popUrl = "http://13.124.11.28/temp/tempboard/seo_board_graph.php"; //팝업창에 출력될 페이지 URL
  var popOption = "width=1000, height=700, resizable=no, scrollbars=no, status=no;"; //팝업창 옵션(optoin)
  window.open(popUrl, "", popOption);
}

function COpopupOpen() {
  var popUrl = "http://13.124.11.28/temp/tempboard/test.php"; //팝업창에 출력될 페이지 URL
  var popOption = "width=570, height=360, resizable=no, scrollbars=no, status=no;"; //팝업창 옵션(optoin)
  window.open(popUrl, "", popOption);
}

function SmokepopupOpen() {
  var popUrl = "http://13.124.11.28/temp/tempboard/test.php"; //팝업창에 출력될 페이지 URL
  var popOption = "width=570, height=360, resizable=no, scrollbars=no, status=no;"; //팝업창 옵션(optoin)
  window.open(popUrl, "", popOption);
}

function initMap() {
  var mapCanvus = document.getElementById("map");
  var mapOption = {center : {lat:37.6331,lng:126.7060}, zoom :9, disableDefaultUI: true};
  map = new google.maps.Map(mapCanvus,mapOption);
}
    </script>
    <script async defer
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyADZhjylXxCS2VvECDv1SAC2w5kFATvdVA&libraries=visualization&callback=initMap">
    </script>
  </body>
</html>