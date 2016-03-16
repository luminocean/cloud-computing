<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
	function formSubmit() {
		var length = document.getElementById("length").value;
		var radio;
		var status;
		var str = "";
		for (var i = 0; i < length - 1; i++) {
			radio = document.getElementsByName(i);
			for (var j = 0; j < radio.length; j++) {
				if (radio[j].checked) {
					status = radio[j].value;
					break;
				}
			}
			str += status;
		}
		document.getElementById("myFormStr").value = str;
		document.getElementById("myForm").submit();
	}
</script>


<title>Literatures</title>
</head>
<body>
	<%
		int i = 0;
	%>

	<s:iterator value="list" id='pair'>
		<div style="width: 70%">
			<div style="float: right">
				<textarea rows="10" cols="100">
		<s:property value='#pair.getKey()' />
		<s:property value='#pair.getValue()' />
		</textarea>
			</div>
			<div style="float: left">
				<br /> OK:<input type="radio" name=<%=i%> value="1"
					checked="checked" /> <br /> Like:<input type="radio" name=<%=i%>
					value="4" /> <br /> DisLike:<input type="radio" name=<%=i%>
					value="9" /> <br />
			</div>
		</div>
		<%
			i++;
		%>
	</s:iterator>
	<button id="length" value=<%=i%> style="display: none"></button>
	<form id="myForm" name="input" action="/recommend/op/likeOrNot.action"
		method="get">
		<input type="text" name="str" id="myFormStr" style="display: none" />
		<input type="button" value="submit" onclick="formSubmit()"/>
	</form>

</body>
</html>