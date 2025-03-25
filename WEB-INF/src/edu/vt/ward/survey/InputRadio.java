package edu.vt.ward.survey;

import org.jdom.*;
import javax.servlet.http.HttpServletRequest;

public class InputRadio extends OptionList {
  private int minOptions = 2;

  public InputRadio (String surveyId) {
    super (surveyId);
    super.setMinOptions ( this.minOptions );
    super.setInputType ( "radio" );
  }

  public InputRadio (String surveyId, Element sourceElement ) {
    super (surveyId, sourceElement, "inputRadio" ); // assign options and questionText
    super.setMinOptions ( this.minOptions );
    super.setInputType ( "radio" );
  }

  // outputs text e.g. "Multiple choice - pick one" or "Short answer - one line"
  public String getQuestionTypeTextHTML () {
    return Config.msg(261);
  }

  public Element getXML ( int mode ) {
    Element inputRadio = new Element ( "inputRadio" );
    Element q = super.getXML ( mode, inputRadio );

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
      String option = (String) request.getParameter ( "q_" + sectionNr + "_" + questionNr );
      if ( option != null ) {
        int optionSelected  = Integer.parseInt ( option );
        if ( optionSelected < this.getNumOptions() )
          ((Option) options.get ( optionSelected ) ).setSelected ( true );
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
      if ( entryOption.getSelected () ) {
        s.append ( Config.translateExportDelimiters ( ((Option) options.get(i)).getExportCode(), delimiter ) );
      }
    }

    if ( this.getOtherShortAnswerLabel () != null ) {
      s.append ( delimiter );
      s.append ( Config.translateExportDelimiters ( ((OptionList) entryQuestion).getOtherShortAnswer (), delimiter ) );
    }

    return s.toString();
  }
*/
}
