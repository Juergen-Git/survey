<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  survey.setOpened ( DateUtil.formatDate ( DateUtil.ISO4601DateFormat, new java.util.Date() ) );
  survey.setClosed ( "" );
  survey.save ();
  if ( !Config.adminEmail.equals("") && Config.enableEmailAdminOnOpenSurvey ) {
    SurveyEmail surveyEmail = new SurveyEmail (
      Config.adminEmail,
      pid + " "+Config.msg(232)+" \"" + survey.getName() + "\"",
      Config.msg(233)+" "+Config.urlScheme+"://" + Config.hostName + Config.rootURL +
      "entry.jsp?id=" + surveyId );
    surveyEmail.save ();
  }

  response.sendRedirect("manageSurveyMenu.jsp?surveyId="+surveyId); return;
%>