package edu.vt.ward.survey;

import java.util.Comparator;

public class SurveysComparator implements Comparator {

  public int compare( Object o1, Object o2 ) {
    SurveyMetaData s1 = (SurveyMetaData) o1;
    SurveyMetaData s2 = (SurveyMetaData) o2;
    int c = 0;
    String cs1 = new String ();
    String cs2 = new String ();

    // build text strings that are used for comparison
    // they have the format e.g. "2001-01-17 2000-12-24 34589739475345" (closed, opened, id)
    // or e.g. "           2000-12-24 34589739475345" if the closed date is empty
/*
    if ( s1.getClosed () != null ) { cs1 += s1.getClosed (); } else { cs1 += "          "; }
    cs1 += " ";
    if ( s1.getOpened () != null ) { cs1 += s1.getOpened (); } else { cs1 += "          "; }
    cs1 += " " + s1.getId ();

    if ( s2.getClosed () != null ) { cs2 += s2.getClosed (); } else { cs2 += "          "; }
    cs2 += " ";
    if ( s2.getOpened () != null ) { cs2 += s2.getOpened (); } else { cs2 += "          "; }
    cs2 += " " + s2.getId ();
*/
    cs1 = s1.getName();
    cs2 = s2.getName();

    // if you return 0, the TreeSet regards them as the same and discards one
    // that's why we never return 0
    c = cs1.compareTo ( cs2 );
    if ( c == 0 ) { return 1; }
    else { return c; }
  } // end: public int compare ...

}