package edu.vt.ward.survey;

import java.io.*;

public class FilenameFilterEntryFile implements FilenameFilter {

  public boolean accept (File dir, String name) {
    return ((name.length() > 6) && name.substring(0,6).equals("entry."));
  } // end: public boolean accept ...
}

