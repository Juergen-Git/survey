<%@ include file="/globalNoAuthRequired.jsp" %><%
  String url = (String) request.getParameter ( "url" );
  if ( url == null ) { url = ""; }
%><html>
<head>
<title><%=Config.serviceName%> <%=Config.msg(234)%></title>
<meta http-equiv="Content-Type" content="text/html; charset=<%=Config.xmlEncoding%>">
</head>
<frameset rows="30,*" frameborder="yes" border="1" framespacing="0">
  <frame name="topFrame" scrolling="no" src="previewHeader.jsp?surveyId=<%=surveyId%>&url=<%=url%>" >
  <frame name="mainFrame" src="<%=url%>">
  <noframes>
    <body bgcolor="#FFFFFF" text="#000000">
    </body>
  </noframes>
</frameset>
</html>
