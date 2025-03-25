<%@ include file="/globalNoAuthRequired.jsp" %><%
  if ( surveyId == null ) {
    response.sendRedirect("error.jsp?errorId=unknownSurveyId");
    return;
  }
  else {
    SurveyMetaData survey = new SurveyMetaData ( Config.appDataDir, surveyId );
    if ( !survey.exists () ) {
      response.sendRedirect("error.jsp?surveyId="+surveyId+"&errorId=unknownSurveyId");
      return;
    }
    SurveyEntryForm entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, request.getScheme(), true );

    if ( !survey.isAcceptingDataEntry () ) {
      response.sendRedirect("error.jsp?surveyId="+surveyId+"&errorId=surveyNotOpen");
      return;
    }
//    mysession.setAttribute(null,  "surveyId", surveyId );

    // this condition shouldn't occurr unless the authentication method changes after the service went live
    if (Config.authenticationMethodStandalone) {
      if (survey.getEntryRestriction().equals("vt") || survey.getEntryRestriction().equals("members")) {
        survey.setEntryRestriction("public");
        survey.setOneEntryOnly(false);
        survey.save();
      }
    } // end: if (Config.authenticationMethodStandalone)

    // process entry restrictions
    String entryPID = (String) mysession.getAttribute(surveyId, "entryPID");
    String entryPassword = (String) mysession.getAttribute(surveyId, "entryPassword");
    if (
         (
          (survey.getEntryRestriction().equals("vt") || survey.getEntryRestriction().equals("members"))
           && entryPID == null
         ) ||
         (
          survey.getEntryRestriction().equals("password") && !(entryPassword != null && entryPassword.equals(survey.getEntryPassword()))
         ) ||
         (
          survey.getEntryRestriction().equals("members") && !survey.isMember(entryPID) /* extra security */
         ) ||
         (
           (survey.getEntryRestriction().equals("vt") || survey.getEntryRestriction().equals("members")) /* extra security */
           && survey.getOneEntryOnly() && (new File ( survey.getSurveyDir() + "entryexists." + entryPID )).exists()
         )

       ) {
      response.sendRedirect( Config.urlScheme+"://" + Config.hostName + Config.rootURL + "entryLogin.jsp?surveyId="+surveyId+"&returnTo=" + java.net.URLEncoder.encode ( request.getScheme() + "://" + Config.hostName + Config.rootURL + "entry.jsp?surveyId=" + surveyId ) );
      return;
    }

    if ( request.getParameter ( "save" ) != null ) {
      // store survey results
      String inputErrorId = entryForm.makeEntryChanges ( request );

      if ( inputErrorId == null ) {
        if ( survey.getEntryRestriction().equals("vt") || survey.getEntryRestriction().equals("members") ) {
          entryForm.setEntryAuthUser ( entryPID );
        }
        String entryId = entryForm.saveEntry ();

        if ( entryPID != null ) { mysession.removeAttribute(surveyId, "entryPID"); }
        if ( entryPassword != null ) { mysession.removeAttribute(surveyId, "entryPassword"); }

        // remember if an authenticated user submitted an entry
        if ( survey.getEntryRestriction().equals("vt") || survey.getEntryRestriction().equals("members") ) {
          try {
            FileOutputStream entryExistsFileOut = new FileOutputStream ( survey.getSurveyDir() + "entryexists." + entryPID, true ); // append to the file
            entryExistsFileOut.write ( (new String (entryId + "\n")).getBytes() );
            entryExistsFileOut.close ();
          }
          catch ( Exception e ) { }
        }

        if ( !survey.getAdminEmail().equals("") && !Config.demoModeEnabled ) {
          SurveyEmail surveyEmail = new SurveyEmail (
            survey.getAdminEmail(),
            Config.msg(125)+" \"" + survey.getName() + "\"",
            Config.msg(126)+"\n" +
            Config.urlScheme+"://" + Config.hostName + Config.rootURL +
            "view.jsp?surveyid=" + surveyId + "&entryid=" + entryId );
          surveyEmail.save ();
        }

        // delete summary entry form
        SurveyEntryForm resultsSummary = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, SurveyEntryForm.resultsSummaryXML, false );
        resultsSummary.delete ();

        response.sendRedirect("exitPage.jsp?surveyId="+surveyId);
        return;
      }
    }

    out.print ( entryForm.getHTML ( survey, SurveyEntryForm.modeEntry ) );
  } // end: if ( surveyId == null )
%>