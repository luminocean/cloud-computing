<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/bootstrap.css" rel="stylesheet" type="text/css" />
<title>Hello,world</title>
</head>
<body>

	<div class="container">
		<div class="row">
			<div class=".col-md-10">
				<h1>Recommend</h1>
			</div>
		</div>
	</div>
	<form class="form-horizontal" name="input" action="/recommend/recommend.action" method="get">
		<div class="form-group form-group-lg" style="margin-top: 250px">
			<label class="col-sm-2 control-label" for="formGroupInputLarge">Literature</label>
			<div class="col-sm-7">
				<input class="form-control" type="text" id="input" name="literature"
					placeholder="input literature to get recommend">
			</div>
			<button class="col-sm-1 btn btn-primary btn-lg" type="submit">Search</button>
		</div>
	</form>

</body>
</html>