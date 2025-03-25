package edu.vt.ward.survey;

import org.jdom.*;
import javax.servlet.http.HttpServletRequest;

public class InputCheckbox extends OptionList {
  private int minOptions = 1;

  public InputCheckbox (String surveyId) {
    super (surveyId);
    super.setMinOptions ( this.minOptions );
    super.setInputType ( "checkbox" );
  }

  public InputCheckbox (String surveyId, Element sourceElement ) {
    super (surveyId, sourceElement, "inputCheckbox" ); // assign options and questionText
    super.setMinOptions ( this.minOptions );
    super.setInputType ( "checkbox" );
  }

  // outputs text e.g. "Multiple choice - pick one" or "Short answer - one line"
  public String getQuestionTypeTextHTML () {
    return Config.msg(256); // "Multiple choice - check all that apply";
  }

  public Element getXML ( int mode ) {
    Element inputCheckbox = new Element ( "inputCheckbox" );
    Element q = super.getXML ( mode, inputCheckbox );

    return q;
  }

  public String getEditFormHTML ( int sectionNr, int questionNr, String type, String inputErrorId ) {
    StringBuffer s = new StringBuffer ();

    s.append ( "<h2>" + this.getQuestionTypeTextHTML () + "</h2>\n" );
    s.append ( super.getEditFormHTML ( sectionNr, questionNr, type, inputErrorId ) );

    return s.toString ();
  }

  public String makeEntryChanges ( int sectionNr, int questionNr, HttpServletRequest request ) {
    try {
      String[] optionsSelected = request.getParameterValues ( "q_" + sectionNr + "_" + questionNr );

      if ( optionsSelected != null ) {
        for ( int i = 0; i < optionsSelected.length; i++ ) {
          ((Option) options.get ( Integer.parseInt ( optionsSelected [i] ) ) ).setSelected ( true );
        }
      }

      String other = (String) request.getParameter ( "q_" + sectionNr + "_" + questionNr + "_other" );
      if ( other != null )
        this.setOtherShortAnswer ( other );
    }
    catch ( Exception e ) { // response to user input error
      return "error";
    }

    return null; // no input error occurred
  }
/*
  public String getExport ( Question entryQuestion, String delimiter ) {
    StringBuffer s = new StringBuffer ("");

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

    return s.toString();
  }
*/
}
