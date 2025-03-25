package edu.vt.ward.survey;

import java.io.*;

public class FilenameFilterEntryExistsFile implements FilenameFilter {

  public boolean accept (File dir, String name) {
    return ((name.length() > 12) && name.substring(0,12).equals("entryexists."));
  } // end: public boolean accept ...
}