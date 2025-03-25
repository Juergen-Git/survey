package edu.vt.ward.survey;

public class SurveyFile {
  public String id;
  public String date_open;
  public String date_close;
  
  SurveyFile ( String id, String date_open, String date_close ) {
    this.id = id;
    this.date_open = date_open;
    this.date_close = date_close;        
  }
  
}

