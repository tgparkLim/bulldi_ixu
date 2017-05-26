<?php
  $con = mysqli_connect('localhost', 'root', '', 'transfer');

?>
<html>
<head>
  <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
      google.charts.load('current', {'packages':['line']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {
  // Create and populate the data table.
  var data = google.visualization.arrayToDataTable([
    ['x', 'Data 1', 'Data 2', 'Data 3'],
    ['A',   1,       1,           0.5],
    ['B',   2,       0.5,         1],
    ['C',   4,       1,           0.5],
    ['D',   8,       0.5,         1],
    ['E',   7,       1,           0.5],
    ['F',   7,       0.5,         1],
    ['G',   8,       1,           0.5],
    ['H',   4,       0.5,         1],
    ['I',   2,       1,           0.5],
    ['J',   3.5,     0.5,         1],
    ['K',   3,       1,           0.5],
    ['L',   3.5,     0.5,         1],
    ['M',   1,       1,           0.5],
    ['N',   1,       0.5,         1]
  ]);

  // Create and draw the visualization.
  var chart = new google.visualization.LineChart(document.getElementById('visualization')).
    chart.draw(data, {vAxes:[
      {title: 'Title 1', titleTextStyle: {color: '#FF0000'}, maxValue: 10}, // Left axis
      {title: 'Title 2', titleTextStyle: {color: '#FF0000'}, maxValue: 20} // Right axis
    ],series:[
                {targetAxisIndex:1},
                {targetAxisIndex:0}
    ],} );


}​

  </script>
</head>
<body>
  <div id="visualization"></div>
</body>
</html>