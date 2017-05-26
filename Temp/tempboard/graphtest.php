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

      var data = new google.visualization.DataTable();
      data.addColumn('number', 'Day');
      data.addColumn('number', 'temperature');
      data.addColumn('number', 'co');
      //data.addColumn('number', 'Transformers: Age of Extinction');

      data.addRows([
        <?php
          $query = "SELECT id, temp, co FROM gimpo_3data";
          $exec = mysqli_query($con, $query);
           while ($row = mysqli_fetch_array($exec)) {
            echo "[".$row['id'].",".$row['temp'].",".$row['co']."],";
           }
        ?>
        // [1,  37.8, 80.8],
        // [2, 30.9, 69.5],
        // [3,  25.4,   57],
        // [4,  11.7, 18.8],
        // [5,  11.9, 17.],
        // [6,   8.8, 13.6],
        // [7,   7.6, 12.3],
        // [8,  12.3, 29.2],
        // [9,  16.9, 42.9],
        // [10, 12.8, 30.9],
        // [11,  5.3,  7.9],
        // [12,  6.6,  8.4],
        // [13,  4.8,  6.3],
        // [14,  4.2,  6.2]
      ]);

      var options = {
        chart: {
          title: 'Box Office Earnings in First Two Weeks of Opening',
          subtitle: 'in millions of dollars (USD)'
        },
        width: 900,
        height: 500,
        axes: {
          x: {
            0: {side: 'top'}
          }
        }
      };

      var chart = new google.charts.Line(document.getElementById('line_top_x'));

      chart.draw(data, options);
    }
  </script>
</head>
<body>
  <div id="line_top_x"></div>
</body>
</html>