<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  int sectionNr = Integer.parseInt ((String) request.getParameter ( "section" ));
  int questionNr = Integer.parseInt ((String) request.getParameter ( "question" ));
  int newSectionNr = sectionNr;
  int newQuestionNr = questionNr;
  SurveyEntryForm entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, true );

  if ( sectionNr < entryForm.getNumSections () ) {

    if ( sectionNr < entryForm.getNumSections ()-1 || questionNr < entryForm.getSection ( sectionNr ).getNumQuestions ()-1 ) {
      Question question = entryForm.getSection ( sectionNr ).getQuestion ( questionNr );
      entryForm.getSection ( sectionNr ).removeQuestion ( questionNr );

      if ( questionNr < entryForm.getSection ( sectionNr ).getNumQuestions () ) {
        newSectionNr = sectionNr;
        newQuestionNr = questionNr + 1;
        entryForm.getSection ( newSectionNr ).addQuestion ( newQuestionNr, question );
      }
      else { // transfer question to next section
        newSectionNr = sectionNr + 1;
        newQuestionNr = 0;
        entryForm.getSection ( newSectionNr ).addQuestion ( 0, question );
      }

      entryForm.save ();
    } // end:  if ( sectionNr < ...
  } // end: if ( sectionNr < entryForm.getNumSections () )

  response.sendRedirect( "editEntryForm.jsp?surveyId="+surveyId+"#q_" + newSectionNr + "_" + newQuestionNr ); return;
%>