package edu.vt.ward.survey;

// adapted from Ryan Schutt's XMLUtils
public class HTMLUtils {

  public static String encode ( String s ) {
  	StringBuffer buf = new StringBuffer();

  	if ( s != null ) {
 	    for ( int i = 0; i < s.length(); i++ ) {
    		char c = s.charAt ( i );

    		switch ( c ) {
    		case '&':
    		    buf.append ( "&amp;" );
    		    break;

    		case '<':
    		    buf.append ( "&lt;" );
    		    break;

    		case '>':
    		    buf.append ( "&gt;" );
    		    break;

    		case '"':
    		    buf.append ( "&quot;" );
    		    break;

    		default:
              buf.append ( c );
    		    break;
    		} // end: switch
      } // end: for
  	} // end: if

    return buf.toString();
  }

  // converts new lines (13) into "\n"
  public static String newLineEncode ( String s ) {
  	StringBuffer buf = new StringBuffer();

  	if ( s != null ) {
 	    for ( int i = 0; i < s.length(); i++ ) {

        String c = s.substring( i, i+1 );
        int hashCode = c.hashCode ();
		    if ( hashCode == 10 ) {
          buf.append ( "\\n" );
        }
        else if ( hashCode != 13 ) {
          buf.append ( c );
        }
      } // end: for
  	} // end: if

    return buf.toString();
  }

  // converts "\n" into new lines (13)
  public static String newLineDecode ( String s ) {
  	StringBuffer buf = new StringBuffer();

  	if ( s != null ) {
 	    for ( int i = 0; i < s.length(); i++ ) {

        String c = s.substring( i, i+1 );
		    if ( c.equals ( "\\" ) ) {
          if ( i < s.length()-1 && s.substring ( i+1, i+2 ).equals ( "n" ) ) {
            buf.append ( "\n" );
            i++;
          }
          else
            buf.append ( c );
        }
        else
          buf.append ( c );
      } // end: for
  	} // end: if

    return buf.toString();
  }

  // converts new lines (13) into "<br>"
  public static String newLine2Br ( String s ) {
  	StringBuffer buf = new StringBuffer();

  	if ( s != null ) {
 	    for ( int i = 0; i < s.length(); i++ ) {

        String c = s.substring( i, i+1 );
        int hashCode = c.hashCode ();
		    if ( hashCode == 10 ) {
          buf.append ( "<br>" );
        }
        else
          buf.append ( c );
      } // end: for
  	} // end: if

    return buf.toString();
  }

  // converts new lines (13) into spaces (" ")
  public static String newLine2Space ( String s ) {
    StringBuffer buf = new StringBuffer();

    if ( s != null ) {
      for ( int i = 0; i < s.length(); i++ ) {
        String c = s.substring( i, i+1 );
        int hashCode = c.hashCode ();
        if ( hashCode == 10 ) {
          buf.append ( " " );
        }
        else
          buf.append ( c );
      } // end: for
    } // end: if

    return buf.toString();
  }

  // filters out characters that are invalid in an XML file
  public static String xmlFilter ( String s ) {
    if ( s != null ) {
      StringBuffer buf = new StringBuffer("");
      for ( int i=0; i < s.length(); i++ ) {
        char c = s.charAt(i);
        if ( c >= '\u0020' || c == '\u0009' || c == '\n' ) {
          buf.append(c);
        }
      }
      return buf.toString();
    }
    else { return null; }
  }
}