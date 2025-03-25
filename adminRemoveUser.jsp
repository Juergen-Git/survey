<%@ include file="/globalAuthRequired.jsp" %><%
  if ( !pid.equals("admin") ) { response.sendRedirect ( "index.jsp" ); return; }

  if ( (String) request.getParameter ( "cancel" ) != null ) { response.sendRedirect("index.jsp"); return; }

  String userPID = "";
  if ( request.getParameter ( "userPID" ) != null ) { userPID = request.getParameter ( "userPID" ); }
  String movesurveystoadmin = request.getParameter ( "movesurveystoadmin" );


  String onLoad = "document.form.userPID.focus();";
  String errorMessage = "";

  if ( request.getParameter ( "remove" ) != null ) {
    User user = new User ( Config.appDataDir, userPID );
    if ( user.exists () ) {
      if ( !userPID.equals("admin") ) {
        // remove all surveys belonging to this user
        SurveyMetaDataList surveyMetaDataList = new SurveyMetaDataList ( Config.appDataDir, user.getSurveyIdList () );
        Iterator surveys = surveyMetaDataList.getSortedByStatus ();
        User userAdmin = new User ( Config.appDataDir, "admin" );

        while (surveys.hasNext () ) {
          SurveyMetaData s = (SurveyMetaData) surveys.next ();
          // check if there are any other admins defined for this survey
          if ( s.getNumAdmins() == 1 ) { // just delete the survey
            if ( movesurveystoadmin != null ) {
              s.addAdminPID("admin");
              s.removeAdminPID(userPID);
              s.save();
              userAdmin.addSurvey(s.getId());
            }
            else {
              s.delete();
            }
          }
          else { // just remove this admin
            s.removeAdminPID(userPID);
            s.save();
          }
        } // end: while (surveys.hasNext () )

        if ( movesurveystoadmin != null ) {
          userAdmin.save();
        }

        // remove the user data
        user.remove ();

        response.sendRedirect("index.jsp");
        return;
      }
      else {
        errorMessage = Config.msg(29);
      }
    } // end:  if ( user.exists () )
    else {
      errorMessage = Config.msg(25);
    }
  } // end: if ( request.getParameter ( "remove" ) != null )

  String pageTitle = Config.msg(30);
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; " + pageTitle );
%><survey:pageHeader title="<%=pageTitle%>" onLoad="<%=onLoad%>" />
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>" name="form">
<%
  if ( !errorMessage.equals("") ) {
    out.print ( Config.formatFeedback("error", errorMessage ) );
  }
%>
<table border="0" cellspacing="3" cellpadding="3">
  <tr>
    <td align="right" width="5%" nowrap><b><%=Config.msg(20)%></b></td>
    <td width="95%">
      <input type="text" name="userPID" value="<%=userPID%>" size="15" maxlength="15">
    </td>
  </tr>
  <tr>
    <td align="right">&nbsp;</td>
    <td>
      <input type="checkbox" name="movesurveystoadmin" value="1"<%
  if ( movesurveystoadmin!= null ) { out.print(" checked"); }
      %>> <%=Config.msg(31)%>
    </td>
  </tr>
  <tr>
    <td align="left" colspan="2">
    <span style="color: #ff0000"><b><%=Config.msg(32)%></b></span> <%=Config.msg(33)%>
    </td>
  </tr>
  <tr>
    <td align="right">&nbsp;</td>
    <td>
      <input type="submit" name="remove" value="<%=Config.msg(23)%>" class="button">&nbsp;&nbsp;<input type="submit" name="cancel" value="<%=Config.msg(24)%>" class="button">
    </td>
  </tr>
</table>
<br>
</form>
<survey:pageFooter />