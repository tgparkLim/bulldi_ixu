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
                        <li class="active"><a href="#">bulldi</a></li>
                        <li><a href="jyp.php">히스토리 보기</a></li>
                        <li><a href="jyp.php">Smoke</a></li>
                        <li><a href="jyp.php">CO</a></li>
                        <li><a href="#" onClick = "toggleHeatmap()">실시간 화재 켜기/끄기</a></li> 
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

var tempMarkers = [];
var COMarkers = [];
var SmokeMarkers = [];
var map,heatmap;


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

function initMap() {

  var mapCanvus = document.getElementById('map');

  var mapOption = {center : {lat:37.6331,lng:126.7060}, zoom :9};

  map = new google.maps.Map(mapCanvus,mapOption);

  heatmap = new google.maps.visualization.HeatmapLayer({
    data: getPoints(),
    map: map
  });

  var locations = new Array();
  //  Table에서 빈도수, 경/위도 값 가져옴
  <?php
    $query = "SELECT id,latitude,longitude,count FROM all_data ORDER BY id";
    $exec = mysqli_query($con, $query);

    while($row = mysqli_fetch_array($exec)){
      $latitude = $row['latitude'];
      $longitude = $row['longitude'];
      $count = $row['count'];
      $count = $count / 10;

      if($count > 2){
        $count = 2;
      }

      if($count >0 && $count <=1){
        $radius = 1000;
      }

      else if($count >1 && $count <=3){
        $radius = 1000;
      }

      else if($count >3 && $count <=5){
        $radius = 1000;
      }
    ?>

      for(var i =0 ; i<= <?php echo $count ?> ; i++) {
        var offset = i / 400;

       var longitude = <?php echo $longitude?>;

        longitude = longitude + offset;

        var myLatlng = new google.maps.LatLng(<?php echo $latitude ?>, longitude);

        locations.push({lat : <?php echo $latitude?>, lng :longitude });
    }


      var myCity = new google.maps.Circle({
        center: {lat:<?php echo $latitude?>,lng:<?php echo $longitude?>},
        radius: <?php echo $radius?>,
        strokeColor: "#FFff99",
        strokeOpacity: 0.3,
        strokeWeight: 2,
        fillColor: "#FFff99",
        fillOpacity: 0.5
      });
        myCity.setMap(map);
  <?php  } ?>

  var markers = locations.map(function(location, i) {
         return new google.maps.Marker({
           position: location,
           icon : iconBase + 'co.png'
         });
       });
       var markerCluster = new MarkerClusterer(map, markers,
    {imagePath: '/js/m/m'});

// initMap 종료
}

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
    <script src="/js/markerclusterer.js">
   </script>
    <script async defer
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyADZhjylXxCS2VvECDv1SAC2w5kFATvdVA&callback=initMap"></script>
  </body>
</html>
