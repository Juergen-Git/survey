<%@ include file="/globalNoAuthRequired.jsp" %><%
  SurveyMetaData survey;
  SurveyEntryForm entryForm;
  if ( surveyId == null ) {
    response.sendRedirect("error.jsp?errorId=unknownSurveyId");
    return;
  }
  else {
    survey = new SurveyMetaData ( Config.appDataDir, surveyId );
    if ( !survey.exists () ) {
      response.sendRedirect("error.jsp?surveyId="+surveyId+"&errorId=unknownSurveyId");
      return;
    }
    entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, true );

    if ( !survey.isAcceptingDataEntry () ) {
      response.sendRedirect("error.jsp?surveyId="+surveyId+"&errorId=surveyNotOpen");
      return;
    }
  }
%><%=entryForm.getExitPageHTML ( SurveyEntryForm.modeEntry )%>