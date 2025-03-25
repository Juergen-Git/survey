package edu.vt.ward.survey;

import java.util.Comparator;

public class SurveysSortedByEntriesComparator implements Comparator {

  public int compare( Object o1, Object o2 ) {
    SurveyMetaData s1 = (SurveyMetaData) o1;
    SurveyMetaData s2 = (SurveyMetaData) o2;

    if ( s1.getNumEntries() > s2.getNumEntries() ) { return -1; }
    else if ( s1.getNumEntries() < s2.getNumEntries() ) { return 1; }
    else {
      // if you return 0, the TreeSet regards them as the same and discards one
      // that's why we never return 0
      int c = s1.getName().compareTo ( s2.getName() );
      if ( c == 0 ) { return 1; }
      else { return c; }
    }
  } // end: public int compare ...

}