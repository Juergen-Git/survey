<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  SurveyEntryForm entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, true );
  int sectionNr = Integer.parseInt ((String) request.getParameter ( "section" ));

  if ( sectionNr >= 0 && sectionNr <= entryForm.getNumSections () ) {
    Section section = new Section ();
    entryForm.addSection ( sectionNr, section );
    entryForm.save ();

    // BUG: probably the syntax is wrong:
    %><jsp:forward page = "/editSection.jsp?surveyId=" + surveyId /><%
  }
%>