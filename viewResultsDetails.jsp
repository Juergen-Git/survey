<%@ include file="/globalNoAuthRequired.jsp" %><%

  if ( (String) request.getParameter ( "backToSummary" ) != null ) { 
  	 response.sendRedirect("viewResults.jsp?surveyId="+surveyId); return; 
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


  int sectionNr = -1;
  if ( request.getParameter ( "section" ) != null ) {
   sectionNr = Integer.parseInt ((String) request.getParameter ( "section" ));
  }
  int questionNr = -1;
  if ( request.getParameter ( "question" ) != null ) {
    questionNr = Integer.parseInt ((String) request.getParameter ( "question" ));
  }
  int optionNr = -1;
  if ( (String) request.getParameter ( "option" ) != null ) {
    optionNr = Integer.parseInt ((String) request.getParameter ( "option" ));
  }

  String pageTitle = Config.msg(249);
  String cookietrail = "";
  if ( pid != null ) {
    cookietrail = "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; <a href=\"manageSurveyMenu.jsp?surveyId="+surveyId+"\">" + survey.getName () + "</a> &gt; <a href=\"viewResults.jsp?surveyId="+surveyId+"\">"+Config.msg(245)+"</a> &gt; " + pageTitle;
  }
%><html>
<head>
  <title><%=pageTitle%></title>
  <LINK href="styles/survey.css" type="text/css" rel="stylesheet">
</head>
<body leftMargin="2" topMargin="0" marginheight="0" marginwidth="2">
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 bgcolor="<%=Config.bgColor%>">
  <TBODY>
  <TR>
    <TD width="60%"><IMG height=10 alt="" src="images/trans.gif"
      width=1 border=0><BR>
    </TD>
    <TD width="1%"><IMG height=1 alt="" src="images/trans.gif" width=1
      border=0><BR>
    </TD>
    <TD width="1%"><IMG height=1 alt="" src="images/trans.gif" width=1
      border=0><BR>
    </TD>
    <TD width="1%"><IMG height=1 alt="" src="images/trans.gif" width=1
      border=0><BR>
    </TD>
  </TR>
  <TR>
    <TD>&nbsp;</TD>
    <TD vAlign=bottom align=left colSpan=2><IMG height=25 alt=""
      src="images/trans.gif" width=176 border=0><BR>
    </TD>
    <TD vAlign=bottom align=left>&nbsp;</TD>
  </TR>
  <TR>
    <TD vAlign=top bgColor=#ffffff><IMG height=1 alt=""
      src="images/trans.gif" width=1 border=0><BR>
    </TD>
    <TD vAlign=top bgColor=#ffffff rowSpan=2><IMG height=14 alt=""
      src="images/trans.gif" width=91 border=0><BR>
    </TD>
    <TD vAlign=top bgColor=#ffffff><IMG height=3 alt=""
      src="images/trans.gif" width=78 border=0><BR>
    </TD>
    <TD vAlign=top bgColor=#ffffff rowSpan=2><BR>
    </TD>
  </TR>
  <TR>
    <TD class=breadcrumbtrail vAlign=top bgColor=#ffffff><%
    if ( cookietrail != null && !cookietrail.equals("")) {
      %><B><%=Config.msg(149)%></B> <%=cookietrail%><%
    }
      %>&nbsp;</TD>
    <TD vAlign=top align=right bgColor=#ffffff><%
    if ( pid != null ) {
      %><A href="<%=Config.urlScheme%>://<%=Config.hostName%><%=Config.rootURL%>logout.jsp"><%=Config.msg(150)%></A><%
    }
      %>&nbsp;</TD>
  </TR>
  <TR>
    <TD bgColor=#ffffff colspan="3">
      <h1><%=pageTitle%></h1>
      <form method="post" action="<%=HttpUtils.getRequestURL ( request )%>">
        <input type="hidden" name="surveyId" value="<%=surveyId%>">
	    <input type="submit" name="backToSummary" value="<%=Config.msg(250)%>" class="button">
      </form>
    </TD>
    <TD bgColor=#ffffff><IMG height=1 alt="" src="images/trans.gif" width=1 border=0><BR>
    </TD>
  </TR>
  </TBODY>
</TABLE>
<style>
  .data {
    font-family : Arial, Helvetica, sans-serif;
    font-size : smaller;
    background-color : <% if (entryForm.getBgColor().equals("#ffffff")) { out.print("#dddddd"); } else { out.print ( entryForm.getBgColor()); } %>;
    color : <%=entryForm.getTextColor() %>;
  }
  .dataselected {
    font-family : Arial, Helvetica, sans-serif;
    font-size : smaller;
    background-color : #eeeeee;
    color : #000000;
  }
</style>
<%
  Section sectionEntryForm = entryForm.getSection ( 0 );
  // create headline
  for ( int qi = 0; qi < sectionEntryForm.getNumQuestions (); qi++ ) {
    if ( qi == questionNr &&
         sectionEntryForm.getQuestion ( qi ).getClass().getSuperclass().getName ().equals( "edu.vt.ward.survey.OptionList" ) ) {
      if ( optionNr > -1) { // is the "option" parameter set?
        Question question = sectionEntryForm.getQuestion ( qi );

        if ( optionNr < ((OptionList) question).getNumOptions () ) {
          Option option = (Option) ((OptionList) question).getOptions().get( optionNr );
          out.print( Config.msg(251)+" &quot;<b>" + option.getLabel() + "</b>&quot; "+Config.msg(252)+" &quot;<b>" + question.getText () + "</b>&quot;");
        }
        else if ( optionNr == ((OptionList) question).getNumOptions () &&
                  ((OptionList) question).getOtherShortAnswerLabel() != null ) {
          out.print( Config.msg(251)+" &quot;<b>" + ((OptionList) question).getOtherShortAnswerLabel() + "</b>&quot; "+Config.msg(252)+" &quot;<b>" + question.getText () + "</b>&quot;");
        }

        out.print( " <a href=\"viewResultsDetails.jsp?surveyId="+surveyId+"\">"+Config.msg(244)+"</a>");
        out.println( "<br><br>" );
      }
    }
  }
%>
<table cellpadding="3" cellspacing="1" border="0">
<tr>
  <th class="data">&nbsp;</th>
  <th class="data">&nbsp;</th>
<%
  // create table header
  for ( int qi = 0; qi < sectionEntryForm.getNumQuestions (); qi++ ) {
    Question question = sectionEntryForm.getQuestion ( qi );

    if ( !question.getClass().getName ().equals( "edu.vt.ward.survey.InputComment" ) ) {
      String questionText = question.getText ();

      // if there's no question text but a label, take that instead
      if ( question.getClass().getName ().equals( "edu.vt.ward.survey.InputTextline" ) ) {
        if ( !questionText.equals("") ) {
          questionText += "<br>";
        }
        questionText += ((InputTextline) question).getLabel ();
      }

      if ( qi == questionNr ) {
        out.print( "  <th class=\"dataselected\" valign=\"bottom\" align=\"left\">" + questionText + "</th>\n" );
      } else {
        out.print( "  <th class=\"data\" valign=\"bottom\" align=\"left\">" + questionText + "</th>\n" );
      }
    } // end: if ( !question.getClass().getName ().equals( "edu.vt.ward.survey.InputComment" ) )
  }
%>
</tr>
<%
  // loop through all entry files
  File surveyDirFile = new File ( survey.getSurveyDir () );
  String[] entryFileList = surveyDirFile.list( new FilenameFilterEntryFile ());
  Arrays.sort( entryFileList, new FileNameComparator () );

  for ( int i = 0; i < entryFileList.length; i++ ) {
    SurveyEntryForm entry = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, entryFileList [i], true );

    boolean showEntry = true;
    if ( optionNr > -1 ) {
      Question question = entry.getSection ( 0 ).getQuestion ( questionNr );
      if ( optionNr < ((OptionList) question).getNumOptions () ) {
        Option option = (Option) ((OptionList) question).getOptions().get( optionNr );
        if ( !option.getSelected() ) { showEntry = false; }
      }
      else if ( optionNr == ((OptionList) question).getNumOptions () ) {
        if ( ((OptionList) question).getOtherShortAnswer().equals("") ) { showEntry = false; }
      }
    }

    if ( showEntry ) {
      out.print("<tr>\n");
      out.print("  <td class=\"data\" width=\"1%\" valign=\"top\" nowrap>" );
      out.print( i + 1 );
			out.print(" <a href=\"deleteEntry.jsp?surveyId="+surveyId+"&entryid="+entryFileList[i].substring(6)+"\">"+Config.msg(332)+"</a>");
      out.print( "</td>\n");
      out.print("  <td class=\"data\" width=\"1%\" valign=\"top\" nowrap><a href=\"view.jsp?surveyId="+surveyId+"&entryid=" + entryFileList[i].substring(6) + "\">" );
      out.print( DateUtil.formatDate( DateUtil.ISO4601DateFormat, new Date ( Long.parseLong(entryFileList[i].substring(6)) ) ) );
      out.print("</a> ");
      out.print( "</td>\n");
      Section sectionEntry = entry.getSection ( 0 );
      for ( int qi = 0; qi < sectionEntryForm.getNumQuestions (); qi++ ) {
        Question question = sectionEntryForm.getQuestion ( qi );

        if ( !question.getClass().getName ().equals( "edu.vt.ward.survey.InputComment" ) ) {
          String text = question.getResultsDetailsHTML ( sectionEntry.getQuestion ( qi ) );

          out.print( "  <td valign=\"top\" class=\"data");
          if ( qi == questionNr ) { out.print("selected"); }
          out.print ( "\">" );
            out.print( HTMLUtils.newLine2Br( HTMLUtils.encode( text ) ) );
          out.print( "</td>\n" );
        } // end: if ( !question.getClass().getName ().equals( "edu.vt.ward.survey.InputComment" ) )
      }
      out.print("</tr>\n");
    } // end: if ( showEntry )...
  }
%>
</table>
<br>
<br>
</body>
</html>