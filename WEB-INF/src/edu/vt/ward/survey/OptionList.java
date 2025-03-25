package edu.vt.ward.survey;

import java.util.*;
import org.jdom.*;
import javax.servlet.http.HttpServletRequest;

public abstract class OptionList extends Question {
  private static int sizeOtherShortAnswerLabel = 30;
  private static int maxLengthOtherShortAnswerLabel = 1000;
  private int minOptions = 1;
  private String inputType = "radio"; // other valid value is "checkbox"

  private String layout = "vertical"; // other valid values are "horizontalTextRight" and "horizontalTextAbove" and "horizontalTextBelow"
  private String otherShortAnswerLabel = null; // contains null or name of label for "other:" input field
  private String otherShortAnswer = ""; // contains the value of the "other" field that the user entered
  private long otherShortAnswerCount = 0;
  protected Vector options = new Vector ();

  OptionList (String surveyId) {
    super (surveyId);
  }

  OptionList (String surveyId, String text ) {
    super (surveyId, text );
  }

  OptionList (String surveyId, Element sourceElement, String questionType ) {
    super (surveyId, sourceElement ); // assign questionText

    Element OptionListElem = sourceElement.getChild ( questionType );

    try { this.setLayout ( OptionListElem.getAttributeValue( "layout" ) ); }
    catch ( Exception e ) { }

    try {
      Element otherShortAnswerElement = OptionListElem.getChild( "otherShortAnswer" );
      this.setOtherShortAnswer ( otherShortAnswerElement.getTextTrim () );
      this.setOtherShortAnswerLabel ( otherShortAnswerElement.getAttributeValue ( "label" ) );
      this.setOtherShortAnswerCount ( Long.parseLong ( otherShortAnswerElement.getAttributeValue ( "count" ) ) );
    }
    catch ( Exception e ) { }

    List optionElems = OptionListElem.getChildren( "option" );
    if ( optionElems != null ) {
      Iterator iOptions = optionElems.iterator();
      while ( iOptions.hasNext () ) {
        Element optionElem = (Element) iOptions.next();
        Option o = new Option ();

        try { o.setLabel ( optionElem.getAttributeValue ( "label" ) ); }
        catch ( Exception e ) { }

        try { o.setExportCode ( optionElem.getAttributeValue ( "exportCode" ) ); }
        catch ( Exception e ) { }

        try { o.setSelected ( optionElem.getAttributeValue ( "selected" ).equals ( "1" ) ); }
        catch ( Exception e ) { }

        try { o.setCount ( Long.parseLong ( optionElem.getAttributeValue( "count" ) ) ); }
        catch ( Exception e ) { }

        this.addOption ( o );
      } // end: while ( iOptions.hasNext () )
    }
  }

  public Vector getOptions () {
    return this.options;
  }

  public int getNumOptions () {
    return this.options.size();
  }

  protected int getMinOptions () {
    return this.minOptions;
  }

  protected void setMinOptions ( int minOptions ) {
    this.minOptions = minOptions;
  }

  protected String getInputType () {
    return this.inputType;
  }

  protected void setInputType ( String inputType ) {
    this.inputType = inputType;
  }

  public String getLayout () {
    return this.layout;
  }

  public void setLayout ( String layout ) {
    this.layout = layout;
  }

  public String getOtherShortAnswer () {
    return this.otherShortAnswer;
  }

  public void setOtherShortAnswer ( String otherShortAnswer ) {
    this.otherShortAnswer = otherShortAnswer;
  }

  public String getOtherShortAnswerLabel () {
    return this.otherShortAnswerLabel;
  }

  public void setOtherShortAnswerLabel ( String otherShortAnswerLabel ) {
    this.otherShortAnswerLabel = otherShortAnswerLabel;
  }

  public long getOtherShortAnswerCount () {
    return this.otherShortAnswerCount;
  }

  public void setOtherShortAnswerCount ( long otherShortAnswerCount ) {
    this.otherShortAnswerCount = otherShortAnswerCount;
  }


  public void addOption ( Option option ) {
    options.add( option );
  }

  public void addOption ( int index, Option option ) {
    options.add( index, option );
  }

  public void removeOption ( int index ) {
    options.remove ( index );
  }

  private String getOtherShortAnswerLabelHTML ( int mode, int sectionNr, int questionNr ) {
    if ( this.getOtherShortAnswerLabel () != null )  {
      StringBuffer s = new StringBuffer ();
      String fieldName = "q_" + String.valueOf(sectionNr) + "_" + String.valueOf(questionNr);

      if ( this.getInputType ().equals ( "radio" ) ) {
        s.append ( "<input type=\"radio\" name=\"" + fieldName + "\" id=\"" + fieldName + "_" + options.size () + "\" value=\"" + options.size () + "\"");
        if ( mode == SurveyEntryForm.modePreview || mode == SurveyEntryForm.modeEntry ) {
          s.append ( " checked onClick=\"check_" + fieldName + "(this); document.form." + fieldName + "_other.focus();\"" );
        }
        s.append ( ">" );
      }
      s.append ( "<label for=\""+fieldName + "_" + options.size ()+"\">" );
      s.append ( this.getOtherShortAnswerLabel () );
      s.append ("</label>");
      if ( !this.getOtherShortAnswerLabel ().equals ( "" ) )
        s.append ( " " );
      s.append ( "<input type=\"text\" name=\"" + fieldName + "_other\" size=\"" + sizeOtherShortAnswerLabel + "\" maxLength=\"" + maxLengthOtherShortAnswerLabel + "\">" );
      s.append ("<br>\n");
      if ( this.getInputType ().equals ( "radio" ) && ( mode == SurveyEntryForm.modePreview || mode == SurveyEntryForm.modeEntry ) ) {
        // output Javascript that disables the "other" field if the radio button doesn't have the focus
        s.append ( "<script language=\"JavaScript\" type=\"text/javascript\"><!--\n" );
        s.append ( "function check_" + fieldName + " ( o ) {\n" );
        s.append ( "  if ( o.value == \"" + options.size() + "\" ) { enable_" + fieldName + "(); }\n" );
        s.append ( "  else { disable_" + fieldName + "(); }\n" );
        s.append ( "}\n" );
        s.append ( "function enable_" + fieldName + "() {\n" );
        s.append ( "  document.form." + fieldName + "_other.disabled = false;\n" );
        s.append ( "  document.form." + fieldName + "_other.style.backgroundColor = \"#ffffff\";\n" );
        s.append ( "}\n" );
        s.append ( "function disable_" + fieldName + "() {\n" );
        s.append ( "  document.form." + fieldName + "_other.disabled = true;\n" );
        s.append ( "  document.form." + fieldName + "_other.style.backgroundColor = \"#eeeeee\";\n" );
        s.append ( "}\n" );
        s.append ( "//-->\n" );
        s.append ( "</script>\n" );
      }

      return s.toString ();
    }
    else
      return "";
  }

  public String getHTML ( SurveyMetaData survey, int mode, int sectionNr, int questionNr ) {
    StringBuffer s = new StringBuffer ();

    s.append ( super.getHTML ( survey, mode, sectionNr, questionNr ) ); // get HTML for text of the question

    if ( this.getLayout () == null || this.getLayout ().equals ( "vertical" ) ||
         mode == SurveyEntryForm.modeResultsSummary ) {

      if ( mode == SurveyEntryForm.modeResultsSummary ) {
        s.append ( "<table cellpadding=\"2\" cellspacing=\"2\" border=\"0\">\n" );
      }

      for ( int i = 0; i < options.size (); i++ ) {
        if ( mode == SurveyEntryForm.modeEdit || mode == SurveyEntryForm.modeEntry || mode == SurveyEntryForm.modePreview || mode == SurveyEntryForm.modeResultsExport ) {
          s.append ( "<input type=\"" + this.getInputType () + "\" name=\"q_" + sectionNr + "_" + questionNr + "\" id=\"q_" + sectionNr + "_" + questionNr + "_" + i + "\" value=\"" + i + "\"" );
          if ( this.getInputType().equals("radio") && this.getOtherShortAnswerLabel () != null && (mode == SurveyEntryForm.modeEntry || mode == SurveyEntryForm.modePreview) ) {
            s.append ( " onClick=\"check_q_" + sectionNr + "_" + questionNr + "(this)\"" );
          }
          s.append( ">" );
        }

        if ( mode == SurveyEntryForm.modeResultsSummary ) {
          s.append ( "<tr>\n<td>\n" );
        }

        // output the option text
        s.append ( "<label for=\"q_" + sectionNr + "_" + questionNr + "_" + i + "\">" );
        s.append ( ((Option) options.get(i)).getLabel () );
        s.append ( "</label>" );
        s.append ( "<br>\n" );

        if ( mode == SurveyEntryForm.modeResultsSummary ) {
          s.append ( "</td>\n" );
          // output number of responses
          s.append ( "<td align=\"right\" valign=\"middle\">\n" ); //  class=\"highlightresponses\"
          if ( survey.singleEntry ) {
            if ( ((Option) options.get(i)).getCount () > 0 ) {
              s.append ( "<img alt=\"\" src=\"images/checkmark.gif\" width=\"15\" height=\"17\">" );
            }
            else {
              s.append ( "<img alt=\"\" src=\"images/trans.gif\" width=\"15\" height=\"17\">" );
            }
          }
          else {
            if ( ((Option) options.get(i)).getCount () > 0 /*&& mode == SurveyEntryForm.modeResultsSummary && !survey.singleEntry*/ )
              s.append ( "<a href=\"viewResultsDetails.jsp?surveyId=" + this.surveyId + "&section=" + sectionNr + "&question=" + questionNr + "&option=" + i + "\">" );
            s.append ( "<b>" + ((Option) options.get(i)).getCount () + "</b>" );
            if ( ((Option) options.get(i)).getCount () > 0 /*&& mode == SurveyEntryForm.modeResultsSummary && !survey.singleEntry*/ )
              s.append ( "</a>" );
          }
          s.append ( "</td>\n" );
          
          // output percentages and bar chart
          if ( !survey.singleEntry ) {
            s.append ( "<td align=\"right\">\n" ); // class=\"highlightresponses\"
            int percent = 0;
            if ( survey.getNumEntries() > 0 )
              percent = Math.round ( (float) ((Option) options.get(i)).getCount () / (float) survey.getNumEntries() * 100 );
            s.append ( " (" );
            if ( percent < 10 ) { s.append("&nbsp;"); }
            s.append ( Integer.toString(percent) + "%)" );
            s.append ( "</td>\n");
            // output bar chart
            s.append ( "<td align=\"left\">\n" );
            if ( percent > 0 ) {
              s.append ( "<img src=\"images/resultsbar.gif\" height=\"10\" width=\""+percent+"\">" );
            }
            s.append ( "</td>\n");
          }
          s.append ( "</tr>\n" );
        }
      } // end: for

      if ( mode == SurveyEntryForm.modeEdit || mode == SurveyEntryForm.modeEntry || mode == SurveyEntryForm.modePreview || mode == SurveyEntryForm.modeResultsExport )
        s.append ( getOtherShortAnswerLabelHTML ( mode, sectionNr, questionNr ) );

      if ( mode == SurveyEntryForm.modeResultsSummary && this.getOtherShortAnswerLabel () != null ) {
        s.append ( "<tr>\n" );
        s.append ( "<td>\n" );

        if ( !this.getOtherShortAnswerLabel ().equals("") ) {
          s.append ( this.getOtherShortAnswerLabel () );
        }
        else {
          s.append( Config.msg(282) );
        }

        if ( survey.singleEntry ) {
          if ( this.getOtherShortAnswerCount () > 0 ) {
//            s.append( " <span class=\"highlightresponses\">" + this.getOtherShortAnswer() + "</span>\n" );
            s.append( " " + this.getOtherShortAnswer() + "\n" );
          }
        }
        s.append ( "</td>\n" );

        s.append ( "<td align=\"right\">\n" ); // class=\"highlightresponses\"
        if ( survey.singleEntry ) {
          if ( this.getOtherShortAnswerCount() > 0 ) {
            s.append ( "<img alt=\"\" src=\"images/checkmark.gif\" width=\"15\" height=\"17\">" );
          }
          else {
            s.append ( "<img alt=\"\" src=\"images/trans.gif\" width=\"15\" height=\"17\">" );
          }
        }
        else {
          if ( this.getOtherShortAnswerCount () > 0 /* && !survey.singleEntry*/  )
            s.append ( "<a href=\"viewResultsDetails.jsp?surveyId=" + this.surveyId + "&section=" + sectionNr + "&question=" + questionNr + "&option=" + options.size () + "\">" );
          s.append ( "<b>" + this.getOtherShortAnswerCount () + "</b>" );
          if ( this.getOtherShortAnswerCount () > 0 /* && !survey.singleEntry*/ )
            s.append ( "</a>" );
        }
        s.append ( "</td>\n");
        if ( !survey.singleEntry ) {
          s.append ( "<td align=\"right\">\n" ); // end: class=\"highlightresponses\"

          int percent = 0;
          if ( survey.getNumEntries() > 0 )
            percent = Math.round ( (float) this.getOtherShortAnswerCount() / (float) survey.getNumEntries() * 100 );
          s.append ( " (" );
          if ( percent < 10 ) { s.append("&nbsp;"); }
          s.append ( Integer.toString(percent) + "%)" );
          s.append ( "</td>\n");

          // output bar chart
          s.append ( "<td align=\"left\">\n" );
          if ( percent > 0 ) {
            s.append ( "<img src=\"images/resultsbar.gif\" height=\"10\" width=\""+percent+"\">" );
          }
          s.append ( "</td>\n");
        }

        s.append ( "</tr>\n" );
      } // end: if ( mode == SurveyEntryForm.modeResultsSummary &&...

      if ( mode == SurveyEntryForm.modeResultsSummary && this.getClass().getName().equals("edu.vt.ward.survey.InputRadio") && !survey.singleEntry ) {
        s.append ( "<tr>\n" );
        s.append ( "<td>\n" );
        s.append ( "<i>"+Config.msg(263)+"</i>" );
        s.append ( "</td>\n" );
        s.append ( "<td align=\"right\">\n" ); // end: class=\"highlightresponses\"
        s.append ( "<b>" );
        int totalAnswers = 0;
        for ( int i = 0; i < options.size(); i++ ) {
          totalAnswers += ((Option) options.get(i)).getCount ();
        }
        totalAnswers += this.getOtherShortAnswerCount ();
        long numNoAnswer = survey.getNumEntries() - totalAnswers;
        s.append( numNoAnswer );
        s.append ( "</b>" );
        s.append ( "</td>\n" );
        s.append ( "<td align=\"right\">\n" ); // end: class=\"highlightresponses\"
        int percent = 0;
        if ( survey.getNumEntries() > 0 )
          percent = Math.round ( (float) numNoAnswer / (float) survey.getNumEntries() * 100 );
        s.append ( " (" );
        if ( percent < 10 ) { s.append("&nbsp;"); }
        s.append ( Integer.toString(percent) + "%)" );
        s.append ( "</td>\n" );

        // output bar chart
        s.append ( "<td align=\"left\">\n" );
        if ( percent > 0 ) {
          s.append ( "<img src=\"images/resultsbar.gif\" height=\"10\" width=\""+percent+"\">" );
        }
        s.append ( "</td>\n");

        s.append ( "</tr>\n" );
      } // end: if ( mode == SurveyEntryForm.modeResultsSummary &&...

      if ( mode == SurveyEntryForm.modeResultsSummary /*|| mode == SurveyEntryForm.modeResultsExport*/ ) {
        s.append ( "</table>\n" );
      }
    }
    else { // "horizontalTextRight"
      for ( int i = 0; i < options.size(); i++ ) {
        if ( mode == SurveyEntryForm.modeEdit || mode == SurveyEntryForm.modeEntry || mode == SurveyEntryForm.modePreview || mode == SurveyEntryForm.modeResultsExport ) {
          s.append ( "<input type=\"" + this.getInputType () + "\" name=\"q_" + sectionNr + "_" + questionNr + "\" value=\"" + i + "\">" );
        }

        s.append ( ((Option) options.get(i)).getLabel () );
        s.append ( "&nbsp;&nbsp;\n" );
      }
      s.append ( getOtherShortAnswerLabelHTML ( mode, sectionNr, questionNr ) );
      s.append ( "<br>\n" );
    }

    // javascript to disable the form elements when in edit or export mode
    if ( mode == SurveyEntryForm.modeEdit || mode == SurveyEntryForm.modeResultsExport ) {
      s.append ("<script language=\"JavaScript\" type=\"text/javascript\"><!--\n");
      for ( int i = 0; i < options.size (); i++ ) {
        s.append ("document.form.q_" + sectionNr + "_" + questionNr + "[" + i + "].disabled = true;\n" );
      }
      if ( this.getOtherShortAnswerLabel () != null )  {
        if ( this.getClass().getName().equals("edu.vt.ward.survey.InputRadio") ) {
          s.append ("document.form.q_" + sectionNr + "_" + questionNr + "[" + options.size () + "].disabled = true;\n" );
        }

        s.append ("document.form.q_" + sectionNr + "_" + questionNr + "_other.disabled = true;\n" );
      }
      s.append ("//-->\n</script>\n");
    }

    if ( mode == SurveyEntryForm.modeEdit || mode == SurveyEntryForm.modeResultsExport )
      s.append ( this.getTableCloseHTML () + this.getTableCloseHTML () );

    if ( mode == SurveyEntryForm.modeResultsSummary )
      s.append ( this.getTableCloseHTML () );

    return s.toString();
  }

  public String getEditFormHTML ( int sectionNr, int questionNr, String type, String inputErrorId ) {
    StringBuffer s = new StringBuffer ();

    s.append ( super.getEditFormBeginHTML ( sectionNr, questionNr, type ) );
    if ( inputErrorId != null && inputErrorId.equals ( "invalidOptions" ) ) {
      s.append ( "<br><font color=\"#ff0000\"><b>"+Config.msg(268)+"</b></font> "+Config.msg(283)+" " + this.getMinOptions () + " " );
      if ( this.getMinOptions () > 1 )
        s.append ( Config.msg(284) );
      else
        s.append ( Config.msg(285) );
      s.append ( ".<br>" );
    }
    s.append ( "<b>"+Config.msg(286)+"</b> <font color=\"#999999\">("+Config.msg(287)+" " + this.getMinOptions () + ")</font><br>\n" );
    s.append ( "<textarea name=\"options\" wrap=\"virtual\" cols=\"60\" rows=\"5\">" );

    if ( options.size() > 0 ) {
      for ( int i = 0; i < options.size(); i++ ) {
        s.append ( HTMLUtils.encode ( ((Option) options.get(i)).getLabel () ) + "\n" );
      }
    }
    else {
      s.append(Config.msg(288)+"\n"+Config.msg(289)+"\n"+Config.msg(290));
    }
    s.append ( "</textarea><br>\n" );

    s.append ( "<br><b>"+Config.msg(291)+"</b><br>\n" );
    s.append ( "<input type=\"radio\" name=\"otherShortAnswer\" value=\"yes\"" );
    if ( this.getOtherShortAnswerLabel () != null )
      s.append ( " checked" );
    s.append ( "> "+Config.msg(292)+" " );
    s.append ( "<input type=\"text\" size=\"30\" maxLength=\"100\" name=\"otherShortAnswerLabel\" value=\"" );
    if ( this.getOtherShortAnswerLabel () != null )
      s.append ( this.getOtherShortAnswerLabel () );
    else
      s.append ( Config.msg(293) );
    s.append ( "\"><br>\n" );
    s.append ( "<input type=\"radio\" name=\"otherShortAnswer\" value=\"no\"" );
    if ( this.getOtherShortAnswerLabel () == null )
      s.append ( " checked" );
    s.append ( "> "+Config.msg(294)+"<br>\n" );
    s.append ( "<br>\n" );

    s.append ( "<b>"+Config.msg(295)+"</b><br>\n" );
    s.append ( "<input type=\"radio\" name=\"layout\" value=\"vertical\"" );
    if ( this.getLayout () == null || this.getLayout ().equals ( "vertical" ) )
      s.append ( " checked" );
    s.append ( "> "+Config.msg(296)+"<br>\n" );
    s.append ( "<input type=\"radio\" name=\"layout\" value=\"horizontalTextRight\"" );
    if ( this.getLayout () != null && this.getLayout ().equals ( "horizontalTextRight" ) )
      s.append ( " checked" );
    s.append ( "> "+Config.msg(297)+"<br>\n" );
/*
    s.append ( "<input type=\"radio\" name=\"layout\" value=\"horizontalTextAbove\"" );
    if ( this.getLayout () != null && this.getLayout ().equals ( "horizontalTextAbove" ) )
      s.append ( " checked" );
    s.append ( "> horizontal, text above check boxes<br>\n" );
    s.append ( "<input type=\"radio\" name=\"layout\" value=\"horizontalTextBelow\"" );
    if ( this.getLayout () != null && this.getLayout ().equals ( "horizontalTextBelow" ) )
      s.append ( " checked" );
    s.append ( "> horizontal, text below check boxes<br>\n" );
*/
    s.append ( "<br><br>\n" );

    s.append ( super.getEditFormEndHTML () );

    return s.toString ();
  }

  public String makeEditFormChanges ( HttpServletRequest request ) {
    super.makeEditFormChanges ( request );

    options = new Vector ();

    try {
      int numOptions = 0;
      StringTokenizer newOptions = new StringTokenizer( (String) request.getParameter ( "options" ), "\n");
      while (newOptions.hasMoreTokens()) {
        Option newOption = new Option ( newOptions.nextToken().trim() );
        if ( !newOption.getLabel().equals("") ) {
          this.addOption ( newOption );
          numOptions++;
        }
      }

      if ( numOptions < this.getMinOptions () )
        throw new Exception ();
    }
    catch ( Exception e ) { // user input error occurred
      return "invalidOptions";
    }

    try {
      String layout = (String) request.getParameter ( "layout" );
      if ( layout == null ) {
        throw new Exception ();
      }
      else if ( layout.equals ( "vertical" ) ) {
        this.setLayout ( "vertical" );
      }
      else if ( layout.equals ( "horizontalTextRight" ) ) {
        this.setLayout ( "horizontalTextRight" );
      }
      else if ( layout.equals ( "horizontalTextAbove" ) ) {
        this.setLayout ( "horizontalTextAbove" );
      }
      else if ( layout.equals ( "horizontalTextBelow" ) ) {
        this.setLayout ( "horizontalTextBelow" );
      }
      else {
        throw new Exception ();
      }
    }
    catch ( Exception e ) { // user input error occurred
      return "invalidLayout"; // not used
    }

    try {
      String otherShortAnswer = (String) request.getParameter ( "otherShortAnswer" );
      if ( otherShortAnswer == null ) {
        throw new Exception ();
      }
      else if ( otherShortAnswer.equals ( "yes" ) ) {
        String otherShortAnswerLabel = (String) request.getParameter ( "otherShortAnswerLabel" );
        if ( otherShortAnswerLabel == null ) {
          throw new Exception ();
        }
        else {
          this.setOtherShortAnswerLabel ( otherShortAnswerLabel );
        }
      }
      else if ( otherShortAnswer.equals ( "no" ) ) {
        this.setOtherShortAnswerLabel ( null );
      }
      else {
        throw new Exception ();
      }
    }
    catch ( Exception e ) { // user input error occurred
      return "invalidOtherShortAnswer"; // not used
    }

    return null; // no user input error occurred
  }

  public Element getXML ( int mode, Element sourceElement ) {
    Element q = super.getXML ( mode );
    q.addContent ( sourceElement );

    if ( mode == SurveyEntryForm.modeEdit || mode == SurveyEntryForm.modeResultsSummary )
      sourceElement.setAttribute ( new Attribute ( "layout", HTMLUtils.xmlFilter(this.getLayout ()) ) );

    for ( int i = 0; i < options.size(); i++ ) {
      Element option = new Element ( "option" );
      if ( mode == SurveyEntryForm.modeEdit || mode == SurveyEntryForm.modeResultsSummary )
        option.setAttribute ( new Attribute ( "label", HTMLUtils.xmlFilter(((Option) options.get(i)).getLabel()) ) );

      if ( mode == SurveyEntryForm.modeEdit )
        option.setAttribute ( new Attribute ( "exportCode", HTMLUtils.xmlFilter(((Option) options.get(i)).getExportCode()) ) );

      if ( ((Option) options.get(i)).getSelected () )
        option.setAttribute ( new Attribute ( "selected", "1" ) );

      if ( mode == SurveyEntryForm.modeResultsSummary ) {
        option.setAttribute ( new Attribute ( "count", String.valueOf ( ((Option) options.get(i)).getCount () ) ) );
      }

      sourceElement.addContent ( option );
    }


    if ( mode == SurveyEntryForm.modeEdit || mode == SurveyEntryForm.modeResultsSummary ) {
      if ( this.getOtherShortAnswerLabel () != null ) {
        Element otherShortAnswer = new Element ( "otherShortAnswer" );
        otherShortAnswer.setAttribute ( new Attribute ( "label", HTMLUtils.xmlFilter(this.getOtherShortAnswerLabel ()) ) );
        if ( mode == SurveyEntryForm.modeResultsSummary )
          otherShortAnswer.setAttribute ( new Attribute ( "count", String.valueOf ( this.getOtherShortAnswerCount () ) ) );
        sourceElement.addContent ( otherShortAnswer );
      }
    }
    else if ( mode == SurveyEntryForm.modeEntry ) {
      if ( !this.getOtherShortAnswer ().equals ( "" ) ) {
        Element otherShortAnswer = new Element ( "otherShortAnswer" );
        otherShortAnswer.setText ( HTMLUtils.xmlFilter(this.getOtherShortAnswer ()) );
        sourceElement.addContent ( otherShortAnswer );
      }
    }

    return q;
  }

  public void updateResultsSummary ( Question entryQuestion ) {
    // iterate through every option and determine whether it has been selected
    for ( int i = 0; i < options.size(); i++ ) {
      Option entryOption = (Option) ((OptionList) entryQuestion).getOptions().get(i);
      ((Option) options.get(i)).updateResultsSummary ( entryOption );
    }
    if ( !((OptionList) entryQuestion).getOtherShortAnswer ().equals("") ) {
      this.otherShortAnswerCount++;
      this.setOtherShortAnswer(((OptionList) entryQuestion).getOtherShortAnswer ());
    }
  }

  public String getResultsDetailsHTML ( Question entryQuestion ) {
    StringBuffer s = new StringBuffer ("");

    for ( int i = 0; i < options.size(); i++ ) {
      String optionText = "";
      Option entryOption = (Option) ((OptionList) entryQuestion).getOptions().get(i);
      if ( entryOption.getSelected () ) {
        s.append ( ((Option) options.get(i)).getLabel() + "\n");
      }
    }
    if ( !((OptionList) entryQuestion).getOtherShortAnswer ().equals("") ) {
      s.append ( this.getOtherShortAnswerLabel() + " " + ((OptionList) entryQuestion).getOtherShortAnswer () + "\n" );
    }


    return s.toString();
  }

  public String getExport ( Question entryQuestion, String delimiter ) {
    StringBuffer s = new StringBuffer ("");

    if ( this.getExportExpand() ) {
      for ( int i = 0; i < options.size(); i++ ) {
        Option entryOption = (Option) ((OptionList) entryQuestion).getOptions().get(i);

        if ( i > 0 ) { s.append ( delimiter ); }
        if ( entryOption.getSelected () ) { s.append ( "1" ); }
        else { s.append ( "0" ); }
      }

      if ( this.getOtherShortAnswerLabel () != null ) {
        s.append ( delimiter );
        s.append ( Config.translateExportDelimiters ( ((OptionList) entryQuestion).getOtherShortAnswer (), delimiter ) );
      }
    } // end: if ( this.exportExpand() )
    else {
      boolean firstSelected = true;
      for ( int i = 0; i < options.size(); i++ ) {
        Option entryOption = (Option) ((OptionList) entryQuestion).getOptions().get(i);

        if ( entryOption.getSelected () ) {
          if ( !firstSelected ) {
            if ( delimiter.equals(",")) { s.append("; "); } else { s.append(", "); }
          }
          firstSelected = false;
          s.append ( Config.translateExportDelimiters ( ((Option) options.get(i)).getLabel (), delimiter ) );
        }
      }

      if ( this.getOtherShortAnswerLabel () != null && ((OptionList) entryQuestion).getOtherShortAnswer () != null && !((OptionList) entryQuestion).getOtherShortAnswer().equals ("") ) {
        if ( !firstSelected ) {
          if ( delimiter.equals(",")) { s.append("; "); } else { s.append(", "); }
        }
        s.append ( Config.translateExportDelimiters ( this.getOtherShortAnswerLabel () + " ", delimiter ) );
        s.append ( Config.translateExportDelimiters ( ((OptionList) entryQuestion).getOtherShortAnswer (), delimiter ) );
      }

    } // end else: if ( this.exportExpand() )

    return s.toString();
  }

}
