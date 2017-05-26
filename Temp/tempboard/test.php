<!Doctype html>
<html>
<head>
	<title>TemperatureBoard</title>
	<script type="text/javascript">
	<!--
		setTimeout("location.reload()",2000)
	//-->
	</script>
</head>
<body>
<?php
	$con = mysql_connect('localhost', 'root', '');
	$db = mysql_select_db('transfer');

	?>
	<br />
	<br />
	<?php
		$query = mysql_query("SELECT * FROM transtemp");

		while($row = mysql_fetch_array($query)) {
			$id = $row['id'];
			$temp = $row['temp'];
			$transtime = $row['transtime'];
			$latitude = $row['latitude'];
			$longitude = $row['longitude'];

			echo $id . ':' . $temp . ':' . $transtime . ':' . $latitude . ':' . $longitude . '<br />'; 
		}
	?>
</body>
</html>