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
      <title>view through bulldi</t
            <div class="container">itle>
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
                <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </a>
                <a id="logo" class="pull-left" href="5.php"></a>
                <div class="nav-collapse collapse pull-right">
                    <ul class="nav">
                        <li class="active"><a href="#" onClick="clearMarkers()">bulldi</a></li>
                        <li><a href="#" onClick = "view_tempMarkers()">Temperature</a></li>
                        <li><a href="#" onClick = "view_SmokeMarkers()">Smoke</a></li>
                        <li><a href="#" onClick = "view_COMarkers()">CO</a></li>
                        <li><a href="#" onClick = "toggleHeatmap()">HEATMAP</a></li> 
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

var map, heatmap;

function initMap() {
  var mapCanvus = document.getElementById("map");
  var mapOption = {center : {lat:37.6331,lng:126.7060}, zoom :9};
  map = new google.maps.Map(mapCanvus,mapOption);

  heatmap = new google.maps.visualization.HeatmapLayer({
    data: getPoints(),
    map: map
  });
  layer.setMap(map);
}

function toggleHeatmap() {
  heatmap.setMap(heatmap.getMap() ? null : map);
}

function getPoints() {
  return [
      <?php
        $current = strtotime($now);
        $query = "SELECT * FROM seogu_3data";
        $exec = mysqli_query($con,$query);
        while($row = mysqli_fetch_assoc($exec)) {
          $transtime = strtotime(($row['transtime']));
          $diff = $current - $transtime;
          if(($diff <= 10000) && ($row['smoke']!=0)){
            echo "new google.maps.LatLng(".$row['latitude'].",".$row['longitude']."),";
          }
        }
      ?>
  ];
}

var tempMarkers = [];
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

var tempfeatures = [
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
  for (var i = 0, tempfeature; tempfeature = tempfeatures[i]; i++) {
    addTempMarker(tempfeature);
  }
}

function view_COMarkers() {
  clearMarkers();
  for (var i = 0, COfeature; COfeature = COfeatures[i]; i++) {
    addCOMarker(COfeature);
  }
}

function view_SmokeMarkers() {
  clearMarkers();
  for (var i = 0, Smokefeature; Smokefeature = Smokefeatures[i]; i++) {
    addSmokeMarker(Smokefeature);
  }
}

function addTempMarker(tempfeature) {
  tempMarkers.push(new google.maps.Marker({
    position: tempfeature.position,
    icon: icons[tempfeature.type].icon,
    map: map
  }));
}

function addCOMarker(COfeature) {
  COMarkers.push(new google.maps.Marker({
    position: COfeature.position,
    icon: icons[COfeature.type].icon,
    map: map
  }));
}

function addSmokeMarker(Smokefeature) {
  SmokeMarkers.push(new google.maps.Marker({
    position: Smokefeature.position,
    icon: icons[Smokefeature.type].icon,
    map: map
  }));
}

function clearMarkers() {
  for (var i = 0; i < tempMarkers.length; i++) {
    tempMarkers[i].setMap(null);
  }
  tempMarkers = [];
  for (var i = 0; i < COMarkers.length; i++) {
    COMarkers[i].setMap(null);
  }
  COMarkers = [];
  for (var i = 0; i < SmokeMarkers.length; i++) {
    SmokeMarkers[i].setMap(null);
  }
  SmokeMarkers = [];
}

    </script>
    <script async defer
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyADZhjylXxCS2VvECDv1SAC2w5kFATvdVA&callback=initMap"></script>
  </body>
</html>