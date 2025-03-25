<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  SurveyEntryForm entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, true );

  out.print ( entryForm.getExitPageHTML ( SurveyEntryForm.modePreview ) );
%>