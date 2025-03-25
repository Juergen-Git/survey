<%@ include file="/globalNoAuthRequired.jsp" %><%

  if ( (String) request.getParameter ( "backToMenu" ) != null ) { 
  	response.sendRedirect("manageSurveyMenu.jsp?surveyId="+surveyId); return; 
  }

  if ( surveyId == null ) {
    response.sendRedirect("error.jsp?errorId=unknownSurveyId");
    return;
  }

  SurveyMetaData survey = new SurveyMetaData ( Config.appDataDir, surveyId );
  SurveyEntryForm entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, true );
  // make sure survey exists and had been open at some point in time
  if ( !survey.exists() || !entryForm.exists () || survey.getOpened ().equals("") ) {
    response.sendRedirect("error.jsp?surveyId="+surveyId);
    return;
  }
//  else {
//    mysession.setAttribute(null,  "surveyId", surveyId );
//  }

  String pid = (String) mysession.getAttribute(null,  "pid" );
  // is the person that is currently logged on the owner of this survey? (if so, don't require password)
  if ( !survey.getAccessResultsRestriction().equals("anyone") ) {
    if ( survey.getAccessResultsRestriction().equals("password") ) {
      // test if password is in the session
      String surveyPassword = (String) mysession.getAttribute(surveyId,  "surveyPassword" );
      if ( surveyPassword == null || !surveyPassword.equals (survey.getResultsPassword()) ) {
        response.sendRedirect("askSurveyPassword.jsp?surveyId="+surveyId+"&returnTo="+java.net.URLEncoder.encode("viewResultsDetails.jsp?surveyId=" + surveyId) );
        return;
      }
    }
    else {
      if ( pid == null || !survey.isAdmin (pid) ) {
        response.sendRedirect ( "login.jsp?r=1" );
        return;
      }
    }
  } // end: if ( !survey.getAccessResultsRestriction().equals("anyone") )

  String pageTitle = Config.msg(148);
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; <a href=\"manageSurveyMenu.jsp?surveyId="+surveyId+"\">" + survey.getName () + "</a> &gt; " + pageTitle );
%><survey:pageHeader title="<%=pageTitle%>" />
<%
  File surveyDirFile = new File ( survey.getSurveyDir () );
  java.io.File[] entryFileList = surveyDirFile.listFiles( new FilenameFilterEntryFile () );

  Calendar calendar = Calendar.getInstance();
  Random random = new Random ( calendar.getTime().getTime() );

  for ( int i=1; i < 11; i++ ) {
    int rint = random.nextInt( entryFileList.length );
    out.print( entryFileList [rint].getName() + "<br>" );
  }
%>
<survey:pageFooter />