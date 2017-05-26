<?php
  $con = mysqli_connect('localhost', 'root', '', 'transfer');
?>
<html>
<head>
<title>Google Charts Tutorial</title>
   <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
   <script type="text/javascript">
     google.charts.load('current', {packages: ['corechart','scatter']});     
   </script>
</head>
<body>
<div id="container" style="width: 550px; height: 400px; margin: 0 auto"></div>
<script language="JavaScript">
function drawChart() {
   // Define the chart to be drawn.
   var data = new google.visualization.DataTable();
   data.addColumn('number', 'transtime');
   data.addColumn('number', 'temp');
   data.addColumn('number', 'co');

   data.addRows([
    <?php
    $query = "SELECT transtime, temp, co FROM gimpo_3data";
    $exec = mysqli_query($con, $query);
    while ($row = mysqli_fetch_array($exec)) {
      $testvar = $row['transtime'];
      $testvar = str_replace(':','',$testvar);
      $testvar = str_replace('-','',$testvar);
      $testvar = preg_replace('/\s+/','',$testvar);
      echo "[".$testvar.",".$row['temp'].",".$row['co']."],";
    }
    ?>
      // [0, 0, 67],  [1, 1, 88],   [2, 2, 77],
      // [3, 3, 93],  [4, 4, 85],   [5, 5, 91],
      // [6, 6, 71],  [7, 7, 78],   [8, 8, 93],
      // [9, 9, 80],  [10, 10, 82], [11, 0, 75],
      // [12, 5, 80], [13, 3, 90],  [14, 1, 72],
      // [15, 5, 75], [16, 6, 68],  [17, 7, 98],
      // [18, 3, 82], [19, 9, 94],  [20, 2, 79],
      // [21, 2, 95], [22, 2, 86],  [23, 3, 67],
      // [24, 4, 60], [25, 2, 80],  [26, 6, 92],
      // [27, 2, 81], [28, 8, 79],  [29, 9, 83]
   ]);

  var options = {
     chart: {
        title: 'Students\' Final Grades',
        subtitle: 'based on hours studied'
     },
     width: 800,
     height: 500,
     series: {
        0: {axis: 'hours studied'},
        1: {axis: 'final grade'}
     },
     axes: {
         y: {
            'hours studied': {label: 'Hours Studied'},
            'final grade': {label: 'Final Exam Grade'}
         }
      }
   };
   // Instantiate and draw the chart.
   var chart = new google.charts.Scatter(document.getElementById('container'));
   chart.draw(data, google.charts.Scatter.convertOptions(options));
}
google.charts.setOnLoadCallback(drawChart);
</script>
</body>
</html>