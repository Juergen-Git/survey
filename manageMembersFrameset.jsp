<%@ include file="/globalAuthRequired.jsp" %><%
if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }
%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<frameset rows="30,*" frameborder="yes" border="1" framespacing="0">
  <frame name="topFrame" scrolling="no" src="previewHeader.jsp?surveyId=<%=surveyId%>" >
  <frame name="mainFrame" src="manageMembersFrame.jsp?surveyId=<%=surveyId%>">
  <noframes>
    <body bgcolor="#FFFFFF" text="#000000">
    </body>
  </noframes>
</frameset>
</html>
