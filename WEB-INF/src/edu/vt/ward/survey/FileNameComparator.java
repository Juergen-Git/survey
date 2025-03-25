package edu.vt.ward.survey;

import java.util.Comparator;

public class FileNameComparator implements Comparator {

  public int compare( Object o1, Object o2 ) {
    if ( o1 == null || o2 == null ) { return 0; }
    else {
      String e1 = (String) o1;
      String e2 = (String) o2;
      int c = 0;

      c = e1.compareTo ( e2 );

      return c;
    }
  } // end: public int compare ...

}