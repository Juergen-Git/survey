<%
  // verify that user has been authenticated
  String pid = (String) mysession.getAttribute(null,  "pid" );
  if ( pid == null ) { response.sendRedirect ( "login.jsp?r=1" ); return; }

  SurveyMetaData survey = null;
  if ( surveyId != null ) {
    // verify that the surveyId points to a survey owned by that user; otherwise redirect to main menu
    survey = new SurveyMetaData ( Config.appDataDir, surveyId );
    if ( !survey.isAdmin (pid) && !pid.equals("admin") ) {
      if ( mysession.getAttribute(null, "surveyId") != null ) { mysession.removeAttribute ( null, "surveyId" ); }
      response.sendRedirect ( "index.jsp" );
      return;
    }
  }
%>