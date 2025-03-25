<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  survey.setClosed ( DateUtil.formatDate ( DateUtil.ISO4601DateFormat, new java.util.Date() ) );
  survey.save ();
  Config.surveyEntryIdUsed.remove(surveyId);

  response.sendRedirect("manageSurveyMenu.jsp?surveyId=" + surveyId); return;
%>