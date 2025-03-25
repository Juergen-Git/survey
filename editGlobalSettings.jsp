<%@ include file="/globalAuthRequired.jsp" %><%
  if ( surveyId == null ) { response.sendRedirect ( "index.jsp" ); return; }

  String returnTo = (String) request.getParameter ( "returnTo" );
  if ( returnTo != null ) {
    mysession.setAttribute(surveyId,  "returnTo", returnTo );
  }

  if ( (String) request.getParameter ( "cancel" ) != null ) {
    returnTo = (String) mysession.getAttribute(surveyId,  "returnTo" );
    if ( returnTo == null ) { returnTo = "manageSurveyMenu.jsp?surveyId="+surveyId; }
    mysession.removeAttribute ( surveyId, "returnTo" );

    response.sendRedirect ( returnTo ); return;
  }


  SurveyEntryForm entryForm = new SurveyEntryForm ( Config.appDataDir, surveyId, Config.urlScheme, true );

  if ( request.getParameter ( "save" ) != null ) {
    // use new survey title from the HTML form
    String font = request.getParameter ( "font" );
    String bgColor = request.getParameter ( "bgColor" );
    String textColor = request.getParameter ( "textColor" );
    String header = request.getParameter ( "header" );
    String footer = request.getParameter ( "footer" );
    String baseHref = request.getParameter ( "baseHref" );
    if ( baseHref != null && !baseHref.equals("") ) {
      if ( !baseHref.endsWith("/") ) { baseHref += "/"; }
    }
    String userCSS = request.getParameter ( "userCSS" );
    String showBorder = request.getParameter ( "showBorder" );

    entryForm.setFont ( font );
    entryForm.setFontSize ( "" );
    entryForm.setBgColor ( bgColor );
    entryForm.setTextColor ( textColor );
    entryForm.setHeader ( header );
    entryForm.setFooter ( footer );
    entryForm.setBaseHref ( baseHref );
    entryForm.setUserCSS ( userCSS );
    entryForm.setShowBorder( showBorder.equals("1") );
    entryForm.save ();

    returnTo = (String) mysession.getAttribute(surveyId,  "returnTo" );
    if ( returnTo == null ) { returnTo = "manageSurveyMenu.jsp?surveyId="+surveyId; }
    mysession.removeAttribute ( surveyId, "returnTo" );

    response.sendRedirect ( returnTo ); return;
  }

  String pageTitle = Config.msg(95);
  mysession.setAttribute(surveyId,  "cookietrail", "<a href=\"" + Config.rootURL + "\">"+Config.msg(8)+"</a> &gt; <a href=\"manageSurveyMenu.jsp?surveyId="+surveyId+"\">" + survey.getName () + "</a> &gt; " + pageTitle );
%><survey:pageHeader title="<%=pageTitle%>" />
<form method="post" action="<%=HttpUtils.getRequestURL ( request )%>" name="globalSettings">
<input type="hidden" name="surveyId" value="<%=surveyId%>">
<script language="javascript">
function UpdateNewColor( colorVarName, colorImgName, color ) {
  document[colorImgName].src = "images/webcolors/" + color + ".gif";
  document.globalSettings[colorVarName].value = '#' + color;

  if ( (navigator.appVersion.indexOf ("MSIE 5") > 0) || (navigator.appVersion.indexOf ("MSIE 6") > 0)  ) { // has DOM support
    if ( colorVarName == 'bgColor' ) {
<%
/*
      document.getElementById('fontSelection1').style.backgroundColor = '#' + color;
      document.getElementById('fontSelection2').style.backgroundColor = '#' + color;
      document.getElementById('fontSelection3').style.backgroundColor = '#' + color;
      document.getElementById('fontSelection4').style.backgroundColor = '#' + color;
*/
%>
      document.getElementById('fontSelection').style.backgroundColor = '#' + color;
    }
    else if ( colorVarName == 'textColor' ) {
<%
/*
      document.getElementById('fontSelection1').style.color = '#' + color;
      document.getElementById('fontSelection2').style.color = '#' + color;
      document.getElementById('fontSelection3').style.color = '#' + color;
      document.getElementById('fontSelection4').style.color = '#' + color;
*/
%>
      document.getElementById('fontSelection').style.color = '#' + color;
    }
  }
}

function WebColorTable( colorVarName, colorImgName, initColor )
{
  var cur_color = initColor.substr(1,6).toLowerCase();
  var cur_image = "images/webcolors/" + cur_color + ".gif";

  // have to nest table in a shell table so netscape correctly fills in background color
  document.writeln('<table border="0" cellpadding="0" cellspacing="0"><tr><td bgcolor="#000000">');
  document.writeln('<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000">');

  var color;

  for (hgreen=0; hgreen<=153; hgreen+=153) {
    for (red=0; red<=255; red+=51) {
      document.writeln('<tr>');
      for (green=hgreen; green<=hgreen+102; green+=51) {
        for (blue=0; blue<=255; blue+=51) {
          // determine the color from the rgb value
          color = '';

          if (red<=16) color += '0' + red.toString(16);
          else color += red.toString(16);

          if (green<=16) color += '0' + green.toString(16);
          else color += green.toString(16);

          if (blue<=16) color += '0' + blue.toString(16);
          else color += blue.toString(16);

          document.write('<td align="center" bgcolor="#' + color + '">');
          document.write('<a href="javascript:UpdateNewColor(\'' + colorVarName + '\', \'' + colorImgName + '\', \'' + color + '\');">');

          if(color == cur_color) {
            if((red/51 > 3) || (green/51 > 3) || (blue/51 > 3))
              document.write('<img src="images/frame_dark.gif" width="13" height="13" border="0" alt=""></a></td>');
            else
              document.write('<img src="images/frame_lite.gif" width="13" height="13" border="0" alt=""></a></td>');
          } else {
            document.write('<img src="images/trans_pixel.gif" width="13" height="13" border="0" alt=""></a></td>');
          }
        }
      }
      document.writeln('</tr>');
    }
  }

  document.writeln('</table></td></tr>');
  document.writeln('<tr><td align="right" valign="top">');
  document.writeln('<%=Config.msg(96)%></td></tr></table>');
}
</script>

<table border="0" cellspacing="0" cellpadding="0"><tr>

<td><b><%=Config.msg(97)%></b> &nbsp;</td>
<td>
<table border="0" cellspacing="0" cellpadding="0"><tr><td bgcolor="#000000"><table cellspacing="1" cellpadding="0"><tr><td><img
name="imgBgColor" src="images/webcolors/<%=entryForm.getBgColor ().substring(1)%>.gif" height="15" width="15"><br></td>
</tr></table></td></tr></table>
</td>
<td>&nbsp;<input type="text" name="bgColor" size="8" maxlength="7" value="<%=entryForm.getBgColor ()%>"></td>
<td rowspan="2"><img src="images/trans.gif" alt="" height="1" width="10"><br></td>
<td><b><%=Config.msg(98)%></b> &nbsp;</td>
<td>
<table border="0" cellspacing="0" cellpadding="0"><tr><td bgcolor="#000000"><table cellspacing="1" cellpadding="0"><tr><td><img
name="imgTextColor" src="images/webcolors/<%=entryForm.getTextColor ().substring(1)%>.gif" height="15" width="15"><br></td>
</tr></table></td></tr></table>
</td>
<td>&nbsp;<input type="text" name="textColor" size="8" maxlength="7" value="<%=entryForm.getTextColor ()%>"></td>
</tr>

<tr><td colspan="3">
<script language="JavaScript">
  WebColorTable( 'bgColor', 'imgBgColor', '<%=entryForm.getBgColor ()%>' );
</script>
</td>
<td colspan="3">
<script language="JavaScript">
  WebColorTable( 'textColor', 'imgTextColor', '<%=entryForm.getTextColor ()%>' );
</script>
</td></tr>
</table>
  <br>
  <b><%=Config.msg(99)%></b><br>
  <div id="fontSelection" style="color : <%=entryForm.getTextColor ()%>; background-color : <%=entryForm.getBgColor ()%>">
<% for ( int i = 0; i < Config.fonts.length; i++ ) { %>
  <input type="radio" name="font" value="<%=HTMLUtils.encode ( Config.fonts [i] )%>"<%
  if ( entryForm.getFont ().equals ( Config.fonts[i] )  ) { out.print ( "checked" ); }
  %>><span style="font-family : <%=HTMLUtils.encode ( Config.fonts [i] )%>"><%=HTMLUtils.encode ( Config.fontsNames [i] )%></span><br>
<%
   } // end: for
%>
</div>
  <br>
  <b><%=Config.msg(100)%></b> <font color="#999999"><%=Config.msg(101)%></font><br>
  <textarea name="header" wrap="physical" cols="70" rows="10"><%
  if ( entryForm.getHeader () != null )
    out.print ( HTMLUtils.encode ( entryForm.getHeader () ) );
  %></textarea><br>
  <br>
  <b><%=Config.msg(102)%></b> <font color="#999999"><%=Config.msg(103)%></font><br>
  <textarea name="footer" wrap="physical" cols="70" rows="10"><%
  if ( entryForm.getFooter () != null )
    out.print ( HTMLUtils.encode ( entryForm.getFooter () ) );
  %></textarea><br>
  <br>
  <p><b><%=Config.msg(104)%></b><br>
    <%=Config.msg(105)%><br>
    <input type="text" name="baseHref" size="60" maxlength="200" value="<%=entryForm.getBaseHref()%>">
    <font color="#999999"><br>
    <%=Config.msg(106)%></font></p>

  <p><b><%=Config.msg(107)%></b><br>
    <%=Config.msg(108)%> <br>
    <input type="text" name="userCSS" size="30" maxlength="100" value="<%=entryForm.getUserCSS()%>">
    <font color="#999999"><br>
    <%=Config.msg(109)%></font></p>

  <b><%=Config.msg(110)%></b><br>
  <input type="radio" name="showBorder" value="1"<%
    if ( entryForm.getShowBorder() )
      out.print ( " checked" );
  %>> <%=Config.msg(111)%><br>
  <input type="radio" name="showBorder" value="0"<%
    if ( !entryForm.getShowBorder() )
      out.print ( " checked" );
  %>> <%=Config.msg(112)%><br>
  <br>
  <br>
  <input type="submit" name="save" value="<%=Config.msg(23)%>" class="button">&nbsp;&nbsp;<input type="submit" name="cancel" value="<%=Config.msg(24)%>" class="button">
</form>
<survey:pageFooter />