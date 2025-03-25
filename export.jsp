<%@ include file="/includeClasses.jsp" %><%@ include file="init.jsp" %><%@ include file="/getSurveyId.jsp" %><%@ include file="/globalDoAuth.jsp" %><%
  response.setContentType("text/plain");
  response.setHeader("Content-disposition","inline; filename=" + survey.getName () + ".txt");
	  
	if ( surveyId == null ) {
    out.print ( Config.msg(132) );
  }
  else {
    SurveyEntryForm entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, true );
    Section sectionEntryForm = entryForm.getSection (0);

    // loop through all entry files
    File surveyDirFile = new File ( survey.getSurveyDir () );
//    java.io.File[] entryFileList = surveyDirFile.listFiles( new FilenameFilterEntryFile () );
    String[] entryFileList = surveyDirFile.list( new FilenameFilterEntryFile ());
    Arrays.sort( entryFileList, new FileNameComparator () );

    // write header (question texts)
    if ( entryForm.getExportIncludeQuestions() ) {
      out.print( Config.msg(133) );
      out.print( entryForm.getExportDelimiter() );
      out.print( Config.msg(134) );
      for ( int qi = 0; qi < sectionEntryForm.getNumQuestions (); qi++ ) {
        Question question = sectionEntryForm.getQuestion ( qi );

        if ( question.getExportInclude() ) {
          String text = Config.translateExportDelimiters ( HTMLUtils.newLine2Space ( question.getText() ), entryForm.getExportDelimiter() );

          if ( question.getClass().getName ().equals( "edu.vt.ward.survey.InputRadio" ) ||
              question.getClass().getName ().equals( "edu.vt.ward.survey.InputCheckbox" ) ) {
            if ( question.getExportExpand() ) {
              // have one field for each option in a multiple choice question
              for ( int oi = 0; oi < ((OptionList) question).getNumOptions(); oi++ ) {
                out.print( entryForm.getExportDelimiter() );
                Option option = (Option) ((OptionList) question).getOptions().get(oi);
                out.print( Config.translateExportDelimiters ( option.getLabel(), entryForm.getExportDelimiter() ) );
                if ( !text.equals("") )
                  out.print( " [" + text + "]" );
              }

              // have an extra field for "other"?!
              OptionList ol = (OptionList) question;
              if ( ol.getOtherShortAnswerLabel () != null ) {
                out.print( entryForm.getExportDelimiter() );
                if ( ol.getOtherShortAnswerLabel ().equals ("") ) {
                  out.print( Config.translateExportDelimiters ( ol.getOtherShortAnswerLabel (), entryForm.getExportDelimiter() ) );
                }
                else { out.print ( Config.msg(135) ); }
              }
            } // end: if ( question.getExportExpand() )
            else {
              out.print( entryForm.getExportDelimiter() );
              if ( !text.equals("") ) {  out.print( text ); }
            } // end: else: if ( question.getExportExpand() )
          }
          else if ( question.getClass().getName ().equals( "edu.vt.ward.survey.InputTextline" ) ) {
            out.print( entryForm.getExportDelimiter() );
            String label = ((InputTextline) question).getLabel();
            out.print( Config.translateExportDelimiters ( label, entryForm.getExportDelimiter() ) );
            if ( !label.equals("") && !text.equals("") ) { out.print( " [" ); }
            out.print ( text );
            if ( !label.equals("") && !text.equals("") ) { out.print( "]" ); }
          }
          else {
            out.print( entryForm.getExportDelimiter() );
            out.print( text );
          }

        } // end: if ( question.getExportInclude() )
      }
      out.print( "\n" );
    } // end: if ( entryForm.getExportIncludeQuestions() )


    // write data lines
    for ( int i = 0; i < entryFileList.length; i++ ) {
      SurveyEntryForm entry = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, entryFileList [i], true );
      Section sectionEntry = entry.getSection ( 0 );

      out.print( i + 1 );
      out.print( entryForm.getExportDelimiter() );
//      out.print( entryFileList[i].getName().substring(6) );
      out.print( DateUtil.formatDate( DateUtil.ISO4601DateFormat, new Date ( Long.parseLong(entryFileList[i].substring(6)) ) ) );

      for ( int qi = 0; qi < sectionEntryForm.getNumQuestions (); qi++ ) {
        Question questionEntryForm = sectionEntryForm.getQuestion ( qi );

        if ( questionEntryForm.getExportInclude() ) {
          String data = questionEntryForm.getExport ( sectionEntry.getQuestion ( qi ), entryForm.getExportDelimiter() );
          out.print( entryForm.getExportDelimiter() );
          out.print( HTMLUtils.newLine2Space( data ) );
        } // end: if ( question.getExportInclude() )
      } // end: for ( int qi = 0;
      out.print( "\n" );
    } // end: for ( int i=0...

  } // end: else: if ( surveyId == null )
%>