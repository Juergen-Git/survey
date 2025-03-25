<%@ include file="/globalAuthRequired.jsp" %><%
  if ( (String) request.getParameter ( "cancel" ) != null ) { response.sendRedirect ( "index.jsp" ); return; }

  String surveyName;
  if ( request.getParameter ( "surveyName" ) != null ) {
    surveyName = request.getParameter ( "surveyName" );
  }
  else surveyName = "";

  boolean invalidSurveyName = false;

  // if "templateSurveyId" is passed as a param it copies an existing survey
  String templateSurveyId = request.getParameter ( "templateSurveyId" );
  SurveyMetaData templateSurvey = null;
  if ( templateSurveyId != null ) {
    // read all survey meta data (parameter "true")
    templateSurvey = new SurveyMetaData ( Config.appDataDir, templateSurveyId );
    if ( request.getParameter ( "surveyName" ) == null )
      surveyName = Config.shortenText( Config.msg(49)+" " + templateSurvey.getName (), Config.maxLenSurveyName - 3 );
  }

  if ( request.getParameter ( "createSurvey" ) != null ) {
    // check if name is valid (valid characters & not taken yet) otherwise forward back to survey creation UI
//    Regex r = new Regex( Config.regexValidSurveyName );

    if ( Pattern.matches ( Config.regexValidSurveyName, surveyName ) && !surveyName.trim().equals("") ) { // create survey
      // generate new survey Id
      java.util.Date date = new java.util.Date ();
      String newSurveyId = String.valueOf ( date.getTime() );

      SurveyMetaData newSurvey;
      if ( templateSurveyId != null ) { // copy existing survey
        templateSurvey.createCopy ( newSurveyId, surveyName );
        newSurvey = new SurveyMetaData ( Config.appDataDir, newSurveyId );

        // remove all admins except the current person logged on
        Iterator adminPIDs = newSurvey.getAdminsSorted ();
        while ( adminPIDs.hasNext () ) {
          String adminPID = (String) adminPIDs.next ();
          if ( !adminPID.equals(pid)) {
            newSurvey.removeAdminPID(adminPID);
          }
        } // end: while
      }
      else {
        // create new survey metadata file
        newSurvey = new SurveyMetaData ( Config.appDataDir, newSurveyId, pid );
      }

      newSurvey.setName ( surveyName );
      newSurvey.save ();

      // register this survey in the appropriate user file
      User user = new User ( Config.appDataDir, pid );
      user.addSurvey ( newSurveyId );
      user.save ();

//      mysession.setAttribute(null, "surveyId", newSurveyId );
      response.sendRedirect ( "manageSurveyMenu.jsp?surveyId="+newSurveyId );
      return;
    }
    else {
      invalidSurveyName = true;
    }
  }

  String pageTitle = Config.msg(50);
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; " + pageTitle );
%><survey:pageHeader title="<%=pageTitle%>" onLoad="document.form.surveyName.focus()" />
<%
  if ( templateSurveyId != null ) {
    out.println ( Config.msg(51) + " &quot;" + templateSurvey.getName () + "&quot; " + Config.msg(52) + "<br>" );
  }
  if ( invalidSurveyName ) {
    out.println ( "<font color=\"red\"><b>" + Config.msg(53) +"</b></font><br>" );
  }
%>
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>" name="form">
<b><%=Config.msg(54)%></b>
<font color="#999999"><%=Config.msg(55)%></font><br>
<input type="text" name="surveyName" value="<%=surveyName%>" size="<%=Config.maxLenSurveyName+10%>" maxlength="<%=Config.maxLenSurveyName%>" tabindex="1">
<font color="#999999">(<%=Config.minLenSurveyName%> <%=Config.msg(56)%> <%=Config.maxLenSurveyName%> <%=Config.msg(57)%>)</font>
<br>
<br>
<%
  if ( templateSurveyId != null )
    out.print ( "<input type=\"hidden\" name=\"templateSurveyId\" value=\"" + templateSurveyId + "\">" );
%>
<input type="submit" name="createSurvey" value="<%=Config.msg(23)%>" class="button">&nbsp;&nbsp;<input type="submit" name="cancel" value="<%=Config.msg(24)%>" class="button">
</form>
<survey:pageFooter />
