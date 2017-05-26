<?php
  $con = mysqli_connect('localhost', 'root', '', 'transfer');

?>

<html>
  <head>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
      google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawChart);

      function drawChart() {
        var data = google.visualization.arrayToDataTable([
          ['transtime', 'temp'],
          <?php
          $query = "SELECT transtime, temp FROM gimpo_3data";
          $exec = mysqli_query($con, $query);
          while ($row = mysqli_fetch_array($exec)) {
            echo "['".$row['transtime']."',".$row['temp']."],";
          }
          ?>
          // ['transtime', 'temp', 'co']
          // <?php
          // $query = "SELECT transtime, temp, co FROM gimpo_3data";
          // $exec = mysqli_query($con, $query);
          // while ($row = mysqli_fetch_array($exec)) {
          //   echo "['".$row['transtime']."',".$row['temp']."',".$row['co']."],";
          // }
          // ?>


          ]);

        var options = {
        title: 'gimpo'
        };
        var chart = new google.visualization.ColumnChart(document.getElementById("columnchart"));
        chart.draw(data, options);
      }

    </script>
  </head>
  <body>
    <div id="columnchart" style="width: 900px; height: 500px"></div>
  </body>
</html>