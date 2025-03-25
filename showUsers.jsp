<%@ include file="/globalAuthRequired.jsp" %><%

  // loop through all user directories
  File userDirFile = new File ( Config.appDataDir + "users" + Config.FILE_SEPARATOR );
  java.io.File[] userFileList = userDirFile.listFiles( );

  for ( int i = 0; i < userFileList.length; i++ ) {
    if ( userFileList [i].isDirectory() )
      out.print( userFileList [i].getName() + "@vt.edu; " );
  }

%>