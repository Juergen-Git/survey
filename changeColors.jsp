<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  SurveyEntryForm entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, true );

  String cSurveyColor = request.getParameter ( "cSurveyColor" );
  if ( cSurveyColor != null ) {
    if ( cSurveyColor.equals ( "#clear" ) )
      cSurveyColor = "";

//    Regex r = new Regex("^#[0-9a-fA-F]{6,6}$");
    if ( cSurveyColor.equals ( "" ) || Pattern.matches( "^#[0-9a-fA-F]{6,6}$", cSurveyColor ) ) {
      entryForm.setBgColor ( cSurveyColor );
      entryForm.save ();
    }
  }
  response.sendRedirect("editEntryForm.jsp");
  return;
%>