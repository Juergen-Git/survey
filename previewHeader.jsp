<%@ include file="/globalNoAuthRequired.jsp" %><%
  String url = (String) request.getParameter ( "url" );
  if ( url == null ) { url = ""; }
%><html>
<head>
<title><%=Config.serviceName%> <%=Config.msg(234)%></title>
<meta http-equiv="Content-Type" content="text/html; charset=<%=Config.xmlEncoding%>">
<link rel="stylesheet" href="styles/survey.css" type="text/css">
<style type="text/css">
body, td {
font-size : 12px;
color : #ffffff;
font-weight : bold;
}
</style>
</head>
<body bgcolor="#000000" text="#000000" leftMargin="0" topMargin="0" marginheight="0" marginwidth="0" onLoad="this.window.focus()">
<table width="100%" border="0" cellspacing="0" cellpadding="2">
  <tr>
    <td bgcolor="#000000"><a href="JavaScript:this.parent.window.close()"><img src="images/closethiswindow.gif" width="128" height="22" border="0" alt="<%=Config.msg(235)%>"></a></td>
  </tr>
</table>
</body>
</html>
