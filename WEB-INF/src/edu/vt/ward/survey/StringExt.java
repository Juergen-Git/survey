package edu.vt.ward.survey;

/**
 * Title:        survey
 * Description:  This web application allows users to create and run web-based surveys.
 * Copyright:    Copyright (c) 2001
 * Company:      WARD
 * @author Jochen Rode
 * @version 1.0
 */

public class StringExt {
  private String content;

  public StringExt( String content ) {
    this.content = content;
  }

  public StringExt( StringBuffer content ) {
    this.content = content.toString();
  }

  public String getContent () {
    return content;
  }

  // substitutes all occurrences of string1 with string2
  public String substitute ( String string1, String string2 ) {
    if ( !string1.equals("") ) {
      StringBuffer newString = new StringBuffer ("");
      int l1 = string1.length();
      int l2 = string2.length();
      int offset = 0;
      int i = 0;

      while ( i > -1 ) {
        i = this.content.indexOf(string1,offset);
        if ( i > -1 ) {
          newString.append ( this.content.substring(offset, i) );
          newString.append ( string2 );
          offset = i + l1;
          if ( l1 == 0 ) { offset++; }
        }
      }
      newString.append ( this.content.substring(offset) );

      this.content = newString.toString();
    }

    return this.content;
  }
}