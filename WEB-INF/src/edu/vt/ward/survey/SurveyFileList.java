package edu.vt.ward.survey;

import java.io.*;
import java.util.*;
import org.jdom.*;
import org.jdom.input.*;

public class SurveyFileList {
  static final String usersDBFilename = "/users.db";
  private Vector surveyFileList = new Vector();
  
  public SurveyFileList ( javax.servlet.ServletContext servletContext, String pid ) {
    
    Element root;
    Element user = null;
    Element survey = null;
    
    try {
      SAXBuilder builder = new SAXBuilder ( true );
      Document doc = builder.build ( new File ( servletContext.getRealPath( usersDBFilename ) ) );
      root = doc.getRootElement();
      List allUsers = root.getChildren ();

      // find the user element whose id matched pid
      for (int i = 0; i < allUsers.size(); i++) {
        user = (Element) allUsers.get(i);
        if (user.getAttributeValue("id").equals(pid)) break;
      }
      
      List allSurveys = user.getChild ( "surveys" ).getChildren ();
      
      for (int i = 0; i < allSurveys.size(); i++) {
        survey = (Element) allSurveys.get(i);
        surveyFileList.add ( new SurveyFile ( survey.getChildTextTrim("id"), 
                                              survey.getChildTextTrim("date_open"), 
                                              survey.getChildTextTrim("date_close") ) );
      }
      
    }
    catch ( Exception e ) {
      e.printStackTrace();
    }
    
  }
  
  public Vector getList () {
    return surveyFileList;
  }
  
}

