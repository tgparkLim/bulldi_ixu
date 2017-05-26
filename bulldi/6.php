<!DOCTYPE html>
<html>
  <head>
    <title>Custom Markers</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <style>
      /* Always set the map height explicitly to define the size of the div
       * element that contains the map. */
      #map {
        height: 100%;
      }
      /* Optional: Makes the sample page fill the window. */
      html, body {
        height: 100%;
        margin: 0;
        padding: 0;
      }
#floating-panel {
  position: absolute;
  top: 10px;
  left: 25%;
  z-index: 5;
  background-color: #fff;
  padding: 5px;
  border: 1px solid #999;
  text-align: center;
  font-family: 'Roboto','sans-serif';
  line-height: 30px;
  padding-left: 10px;
}

      #floating-panel {
        margin-left: -52px;
      }

    </style>
  </head>
  <body>
  <div id ="floating-panel">
    <button id ="Tempdrop" onclick="drop()">drop markers</button>
  </div>
    <div id="map"></div>
    <script>

    var dropTemp = [
    {lat: 37.6331683, lng: 126.7060066},
    {lat: 37.4939292, lng: 126.7220893},
    {lat: 37.517094, lng: 126.6698924}
    ];

    var dropTempmarkers = [];

      var map;
      function initMap() {
        map = new google.maps.Map(document.getElementById('map'), {
          zoom: 9,
          center: new google.maps.LatLng(37.6331, 126.7060),
          mapTypeId: 'roadmap'
        });

        function Tempdrop() {
          temp_clearMarkers();
          for (var i = 0; i < neighborhoods.length; i++) {
            addMarkerWithTimeout(neighborhoods[i], i * 200);
          }
        }

        function temp_addMarkerWithTimeout(position, timeout) {
          window.setTimeout(function() {
            markers.push(new google.maps.Marker({
              position: position,
              map: map,
              animation: google.maps.Animation.DROP
            }));
          }, timeout); 
        }

        function temp_clearMarkers() {
          for (var i = 0; i < dropTempmarkers.length; i++) {
            dropTempmarkers[i].setMap(null);
          }
          dropTempmarkers = [];
        }

        var iconBase = '/img/';
        var icons = {
          parking: {
            icon: iconBase + 'temperature_marker.png'
          },
          library: {
            icon: iconBase + 'co_marker.png'
          },
        };

        function addMarker(feature) {
          var marker = new google.maps.Marker({
            position: feature.position,
            icon: icons[feature.type].icon,
            map: map
          });
        }

        var features = [
        {
          position: new google.maps.LatLng(37.6331683, 126.7060066),
          type: 'parking'
        }, {
          position: new google.maps.LatLng(37.4939292, 126.7220893),
          type: 'parking'
        }, {
          position: new google.maps.LatLng(37.517094, 126.6698924),
          type: 'library'
        }
        ];

        // for (var i = 0, feature; feature = features[i]; i++) {
        //   addMarker(feature);
        // }
      }
    </script>
    <script async defer
    src="https://maps.googleapis.com/maps/api/js?key=AIzaSyADZhjylXxCS2VvECDv1SAC2w5kFATvdVA&callback=initMap">
    </script>
  </body>
</html>