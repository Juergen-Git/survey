<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  int sectionNr = Integer.parseInt ((String) request.getParameter ( "section" ));
  int newSectionNr = sectionNr;
  SurveyEntryForm entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, true );

  if ( sectionNr >= 0 && sectionNr < entryForm.getNumSections () ) {
    Section newSection = (Section) entryForm.getSection ( sectionNr ).copy ();

    newSectionNr = sectionNr + 1;
    entryForm.addSection ( newSectionNr, newSection );
    entryForm.getSection ( newSectionNr ).setTitle ( Config.msg(48) + " " + newSection.getTitle () );

    entryForm.save ();
  }

  response.sendRedirect( "editEntryForm.jsp?surveyId="+surveyId+"#s_" + newSectionNr ); return;
%>