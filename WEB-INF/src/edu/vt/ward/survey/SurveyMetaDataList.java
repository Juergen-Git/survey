package edu.vt.ward.survey;

import java.util.*;

public class SurveyMetaDataList {
  private String appDir; // absolute directory of this web application (including trailing slash)
  private Hashtable surveys = new Hashtable (); // contains references to metadata objects of all of the users surveys

  public int getNum () {
    return surveys.size ();
  }

  public SurveyMetaDataList ( String appDir ) {
    this.appDir = new String ( appDir );
  }

  public SurveyMetaDataList ( String appDir, List surveyIdList ) {
    this.appDir = new String ( appDir );

    Iterator iSurveyId = surveyIdList.iterator();
    while (iSurveyId.hasNext()) {
      String surveyId = (String) iSurveyId.next();
      this.addSurvey ( surveyId );
    }
  }

  public void addSurvey ( String surveyId ) {
    SurveyMetaData surveyMetaData = new SurveyMetaData ( this.appDir, surveyId );
    surveys.put ( surveyId, surveyMetaData );
  }

  public void addSurvey ( String surveyId, SurveyMetaData surveyMetaData ) {
    surveys.put ( surveyId, surveyMetaData );
  }

  public void removeSurvey ( String surveyId ) {
    surveys.remove ( surveyId );
  }

  public Iterator getSortedByStatus () {
    TreeSet t = new TreeSet ( new SurveysComparator () );
    t.addAll ( surveys.values () );

    return t.iterator ();
  }

  public Iterator getSortedByEntries () {
    TreeSet t = new TreeSet ( new SurveysSortedByEntriesComparator () );
    t.addAll ( surveys.values () );

    return t.iterator ();
  }

}
