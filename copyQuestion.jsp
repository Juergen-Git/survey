<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  int sectionNr = Integer.parseInt ((String) request.getParameter ( "section" ));
  int questionNr = Integer.parseInt ((String) request.getParameter ( "question" ));
  int newSectionNr = sectionNr;
  int newQuestionNr = questionNr;
  SurveyEntryForm entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, true );

  if ( sectionNr >= 0 && sectionNr < entryForm.getNumSections () && questionNr >= 0 && questionNr < entryForm.getSection ( sectionNr ).getNumQuestions () ) {
    Question question = (Question) entryForm.getSection ( sectionNr ).getQuestion ( questionNr ).copy ();

    newQuestionNr = questionNr + 1;
    entryForm.getSection ( sectionNr ).addQuestion ( newQuestionNr, question );


    if ( !question.getClass().getName ().equals( "edu.vt.ward.survey.InputComment" ) ) {
      entryForm.getSection ( sectionNr ).getQuestion ( newQuestionNr ).setText ( Config.msg(48) + " " + question.getText () );
    }

    entryForm.save ();
  }

  response.sendRedirect( "editEntryForm.jsp?surveyId="+surveyId+"#q_" + newSectionNr + "_" + newQuestionNr ); return;
%>