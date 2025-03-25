<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  if ( request.getParameter ( "backToExport" ) != null ) { response.sendRedirect("exportResults.jsp?surveyId="+surveyId); return; }
  if ( request.getParameter ( "backToMenu" ) != null ) { response.sendRedirect("manageSurveyMenu.jsp?surveyId="+surveyId); return; }


  SurveyEntryForm entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, true );

  String pageTitle = Config.msg(136);
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; <a href=\"manageSurveyMenu.jsp?surveyId="+surveyId+"\">" + survey.getName () + "</a> &gt; " + pageTitle );
%><html>
<head>
<title><%=Config.serviceName%> <%=Config.msg(146)%></title>
<meta http-equiv="Content-Type" content="text/html; charset=<%=Config.xmlEncoding%>">
</head>
<frameset rows="50%,50%" frameborder="yes" border="1" framespacing="0">
  <frame name="topFrame" scrolling="no" src="exportHeader.jsp?surveyId=<%=surveyId%>" >
  <frame name="mainFrame" src="export.jsp?surveyId=<%=surveyId%>">
  <noframes>
    <body bgcolor="#FFFFFF" text="#000000">
    </body>
  </noframes>
</frameset>
</html>