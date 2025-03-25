package edu.vt.ward.survey;

import java.util.*;
import org.jdom.*;
import javax.servlet.http.HttpServletRequest;

public class Section implements Cloneable {
  String title = "";
  SurveyEntryForm surveyEntryForm = null; // keeps a reference back to the survey form it belongs to
  Vector questions = new Vector();

  //public Section () {
  //}

  public Section ( SurveyEntryForm surveyEntryForm ) {
    this.surveyEntryForm = surveyEntryForm;
  }

  public Section ( SurveyEntryForm surveyEntryForm, Element sourceElement ) {
    this.surveyEntryForm = surveyEntryForm;

    try { this.setTitle ( sourceElement.getAttributeValue( "title" ) ); }
    catch ( Exception e ) { }

    List questionElems = sourceElement.getChildren( "question" );
    Iterator iQuestion = questionElems.iterator();
    while ( iQuestion.hasNext() ) {
      Element questionElem = (Element) iQuestion.next();

      Question question = null;
      List questionTypes = questionElem.getChildren ();
      Iterator iQuestionTypes = questionTypes.iterator ();

      while ( iQuestionTypes.hasNext () ) {
        Element questionType = (Element) iQuestionTypes.next ();

        if ( questionType.getName ().equals( "inputRadio" ) ) {
          this.addQuestion ( new InputRadio (surveyEntryForm.getId(), questionElem ) );
        } else if ( questionType.getName ().equals( "inputCheckbox" ) ) {
          this.addQuestion ( new InputCheckbox (surveyEntryForm.getId(), questionElem ) );
        } else if ( questionType.getName ().equals( "inputTextline" ) ) {
          this.addQuestion ( new InputTextline (surveyEntryForm.getId(), questionElem ) );
        } else if ( questionType.getName ().equals( "inputTextarea" ) ) {
          this.addQuestion ( new InputTextarea (surveyEntryForm.getId(), questionElem ) );
        } else if ( questionType.getName ().equals( "inputComment" ) ) {
          this.addQuestion ( new InputComment (surveyEntryForm.getId(), questionElem ) );
        }
      } // end: while

    }
  }

  public Section ( SurveyEntryForm surveyEntryForm, String title ) {
    this.surveyEntryForm = surveyEntryForm;
    this.title = new String (title);
  }

  public String getTitle () {
    return this.title;
  }

  public void setTitle ( String title ) {
    this.title = title;
  }

  public Section copy () {
    try {
      Section copyOfSection = (Section) super.clone ();				// return the clone
      copyOfSection.questions = new Vector();
      for ( int i = 0; i < questions.size (); i++ ) { // clone the questions the vector is pointing at
         Question copyOfquestion = getQuestion ( i ).copy ();
         copyOfSection.addQuestion ( copyOfquestion );
      }
      return copyOfSection;

    } catch ( CloneNotSupportedException e ) {
       throw new InternalError();
    }
  }

  public void addQuestion ( Question question ) {
    questions.add( question );
  }

  public void addQuestion ( int index, Question question ) {
    questions.add( index, question );
  }

  public void removeQuestion ( int index ) {
    questions.remove ( index );
  }

  public Question getQuestion ( int questionNr ) {
    return this.getQuestion ( questionNr, "" );
  }

  public Question getQuestion ( int questionNr, String type ) {
    if ( type == null || type.equals("") ) {
      return (Question) this.questions.get ( questionNr );
    }
    else {
      return Question.getNewQuestion ( this.surveyEntryForm.getId(), type );
    }
  }

  public int getNumQuestions () {
    return questions.size ();
  }

  public String getHTML ( SurveyMetaData survey, int mode, int sectionNr ) {
    StringBuffer s = new StringBuffer ();


    if ( mode == SurveyEntryForm.modeEdit ) {
      s.append ( "<a name=\"s_" + sectionNr + "\"></a>" );
    }


    for ( int i = 0; i < this.getNumQuestions (); i++ ) {

      if ( ( mode == SurveyEntryForm.modeEntry || mode == SurveyEntryForm.modePreview || mode == SurveyEntryForm.modeResultsSummary )
           && i > 0 ) {
        if ( this.getQuestion ( i ).getShowDivider () ) {
          s.append ( this.surveyEntryForm.getDivider() );
        }
      }

      s.append ( this.getQuestion ( i ).getHTML ( survey, mode, sectionNr, i ) );

    }
    if ( mode == SurveyEntryForm.modeEdit ) { // && this.getNumQuestions () > 0 ) {
      s.append ( "<br>" + Question.getAddButtonsHTML (this.surveyEntryForm.getId(), sectionNr, questions.size () ) + "<br>" );
    }

    return s.toString();
  }

  public String getExport ( Question entryQuestion, String delimiter ) {
    StringBuffer s = new StringBuffer ();

    for ( int i = 0; i < this.getNumQuestions (); i++ ) {
      s.append ( delimiter );
      s.append ( this.getQuestion ( i ).getExport ( entryQuestion, delimiter ) );
    }

    return s.toString();
  }

  public Element getXML ( int mode ) {

    Element section = new Element ( "section" );
    if ( mode == SurveyEntryForm.modeEdit )
      section.setAttribute ( new Attribute ( "title", HTMLUtils.xmlFilter(this.getTitle ()) ) );

    for ( int i = 0; i < this.getNumQuestions (); i++ ) {
      section.addContent ( getQuestion ( i ).getXML ( mode ) );
    }

    return section;
  }

  public void makeEditFormChanges ( HttpServletRequest request ) {
    try {
      this.setTitle ( (String) request.getParameter ( "sectiontitle" ) );
    }
    catch ( Exception e ) { // response to user input error
    }
  }

  public String makeEntryChanges ( int sectionNr, HttpServletRequest request ) {
    try {
      for ( int i = 0; i < this.getNumQuestions (); i++ ) {
        String errorId = ((Question) this.getQuestion ( i )).makeEntryChanges ( sectionNr, i, request );
        if ( errorId != null )
          throw new Exception ();
      }
    }
    catch ( Exception e ) { // response to user input error
      return "error";
    }

    return null; // no input error occurred
  }

  public void updateResultsSummary ( Section entrySection ) {
    for ( int i = 0; i < this.getNumQuestions (); i++ ) {
      // this works because the entry XML is supposed to have the exact same structure as the surveyEntryForm
      this.getQuestion ( i ).updateResultsSummary ( entrySection.getQuestion ( i ) );
    }
  }

}
