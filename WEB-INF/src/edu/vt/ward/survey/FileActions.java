package edu.vt.ward.survey;

import java.io.*; 

public abstract class FileActions {
  public static void delete ( java.io.File p_file ) { 
    String FILE_SEPARATOR = System.getProperty("file.separator"); 

    // If it is a directory, empty it first 
    if ( p_file.isDirectory() ) { 
      String[] dirList = p_file.list(); 
      for ( int i=0; i < dirList.length; i++) {
        java.io.File aFile = new java.io.File( p_file.getPath () + FILE_SEPARATOR + dirList[i] ); 
        if ( aFile.isDirectory () ) { 
          delete ( aFile ); 
        } 
   
        aFile.delete (); 
      } 
    } 
    
    p_file.delete (); 
  } 

  // copied and adapted from http://www.sys-con.com/java/source/4-2/code.cfm?Page=48
  public static void copy ( java.io.File src, java.io.File dst ) 
  throws IOException { 
  
    if ( src.isDirectory () ) { 
      dst.mkdir (); 
      String[] dirList = src.list (); 

      for ( int i = 0; i < dirList.length; i++ ) { 
        String entry = dirList [i]; 
        copy ( new java.io.File ( src, entry ), new java.io.File ( dst, entry ) ); 
      } 
    } 
    else { // assume it's a file 
      FileInputStream  in  = new FileInputStream (src); 
      FileOutputStream out = new FileOutputStream (dst); 
     
      int c; 
      byte [] buf = new byte [32768]; 
   
      while ( ( c = in.read (buf) ) > 0 ) 
        out.write(buf, 0, c); 
   
      in.close(); 
      out.close(); 
    } 
  }
  
}