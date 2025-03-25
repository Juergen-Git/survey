<%
  request.setCharacterEncoding(Config.xmlEncoding);
  String surveyId = null;
  if ( request.getParameter ( "surveyId" ) != null ) {
    surveyId = (String) request.getParameter ( "surveyId" );
  }
  else if ( request.getParameter ( "surveyid" ) != null ) {
    surveyId = (String) request.getParameter ( "surveyid" );
  }
  else if ( request.getParameter ( "id" ) != null ) {
    surveyId = (String) request.getParameter ( "id" );
  }
/*
  else {
    surveyId = (String) mysession.getAttribute(null,  "surveyId" );
  }
*/  
  
  // make sure the surveyId has a correct format, otherwise reset
  if ( surveyId != null && !Pattern.matches ( Config.regexValidSurveyId, surveyId ) ) {
  	surveyId = null;
  }
%>