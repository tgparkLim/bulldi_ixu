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

	// if($con) {
	// 	echo 'Successfully connected to the database';
	// } else {
	// 	die('Error');
	// }

	// if($db) {
	// 	echo 'Successfully found the database';
	// } else {
	// 	die('Error');
	// }

	?>
	<br />
	<br />
	<?php
		$query = mysql_query("SELECT * FROM seo_temp");

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