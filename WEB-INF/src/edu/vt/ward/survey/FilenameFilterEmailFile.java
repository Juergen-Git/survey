package edu.vt.ward.survey;

import java.io.*;

public class FilenameFilterEmailFile implements FilenameFilter {

  public boolean accept (File dir, String name) {
    return ((name.length() > 6) && name.substring(0,6).equals("email."));
  } // end: public boolean accept ...
}