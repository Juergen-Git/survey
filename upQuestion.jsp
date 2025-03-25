<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  int sectionNr = Integer.parseInt ((String) request.getParameter ( "section" ));
  int questionNr = Integer.parseInt ((String) request.getParameter ( "question" ));
  int newSectionNr = sectionNr;
  int newQuestionNr = questionNr;
  SurveyEntryForm entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, true );

  if ( ! (sectionNr == 0 && questionNr == 0 ) ) {
    Question question = entryForm.getSection ( sectionNr ).getQuestion ( questionNr );
    entryForm.getSection ( sectionNr ).removeQuestion ( questionNr );

    if ( questionNr > 0 ) {
      newSectionNr = sectionNr;
      newQuestionNr = questionNr - 1;
      entryForm.getSection ( newSectionNr ).addQuestion ( newQuestionNr, question );
    }
    else { // transfer question to previous section
      newSectionNr = sectionNr - 1;
      entryForm.getSection ( newSectionNr ).addQuestion ( question );
      newQuestionNr = entryForm.getSection ( newSectionNr ).getNumQuestions () - 1;
    }

    entryForm.save ();
  }

  response.sendRedirect( "editEntryForm.jsp?surveyId="+surveyId+"#q_" + newSectionNr + "_" + newQuestionNr ); return;
%>