<?php
	$con = mysqli_connect('localhost', 'root', '', 'transfer');

?>

<html>
  <head>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {
        // var data = google.visualization.arrayToDataTable([
        //   ['Year', 'Sales', 'Expenses'],
        //   ['2004',  1000,      400],
        //   ['2005',  1170,      460],
        //   ['2006',  660,       1120],
        //   ['2007',  1030,      540]
        // ]);
        var data = google.visualization.arrayToDataTable([
        	['transtime', 'temp'],
        	<?php
        	$query = "SELECT transtime, temp FROM bupyeong_temp ORDER BY transtime";

        	$exec = mysqli_query($con, $query);
        	while($row = mysqli_fetch_assoc($exec)){
        		echo "['".$row['transtime']."',".$row['temp']."],";
        	}
        	?>
        ]);

        // var data2 = google.visualization.arrayToDataTable([
        // 	['transtime', 'temp'],
        // 	<?php
        // 	$query = "SELECT transtime, temp FROM seo_temp ORDER BY transtime";

        // 	$exec = mysqli_query($con, $query);
        // 	while($row = mysqli_fetch_assoc($exec)){
        // 		echo "['".$row['transtime']."',".$row['temp']."],";
        // 	}
        // 	?>
        // ]);


        var options = {
          title: '부평온도',
          curveType: 'function',
          legend: { position: 'bottom' }
        };

        // var options2 = {
        //   title: '서구온도',
        //   curveType: 'function',
        //   legend: { position: 'bottom' }
        // };

        var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

        chart.draw(data, options);
        // chart.draw(data2, options2);
      }
    </script>
  </head>
  <body>
    <div id="curve_chart" style="width: 900px; height: 500px"></div>
  </body>
</html>