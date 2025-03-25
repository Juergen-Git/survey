<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  int sectionNr = Integer.parseInt ((String) request.getParameter ( "section" ));
  int questionNr = Integer.parseInt ((String) request.getParameter ( "question" ));
  SurveyEntryForm entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, true );

  if ( sectionNr >= 0 && sectionNr < entryForm.getNumSections () && questionNr >= 0 && questionNr < entryForm.getSection ( sectionNr ).getNumQuestions () ) {
    entryForm.getSection ( sectionNr ).removeQuestion ( questionNr );

    entryForm.save ();
  }
  response.sendRedirect( "editEntryForm.jsp?surveyId="+surveyId ); return;
%>