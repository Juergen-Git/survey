package edu.vt.ward.survey;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

/** PageHeader represents a tag for printing the HTML header of a page  Use it like:
 *
 * <PRE>   &lt;survey:pageHeader title="the title of my web page" /&gt;</PRE>
 * @version $Id: PageHeader.java,v 1.1.1.1 2002/12/16 15:41:27 jrode Exp $
 */
public class PageHeader extends TagSupport {
    // title of the web page
    private String title = new String ();
    private String headerTitle = new String ();
    private String headerKeywords = new String ();
    private String onLoad = new String ();

    public PageHeader () {
      super ();
      setDefaults ();
    }

    // getter/setter methods
    public void setTitle ( String title ) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setHeaderTitle ( String headerTitle ) {
        this.headerTitle = headerTitle;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderKeywords ( String headerKeywords ) {
        this.headerKeywords = headerKeywords;
    }

    public String getHeaderKeywords() {
        return headerKeywords;
    }

    public void setOnLoad ( String onLoad ) {
        this.onLoad = onLoad;
    }

    public String getOnLoad () {
        return onLoad;
    }

    /** Set the defaults for the attribute variables.
     */
    private void setDefaults() {
      this.title = Config.serviceName;
      this.headerTitle = "";
      this.headerKeywords = "";
      this.onLoad = "";
    }

    /** Called after the tag has been evaluated.  Reset the properties to their defaults.
     */
    public void release() {
        super.release();
        setDefaults();
    }

    /** Called when the starting tag is encountered.
     * @throws JspException throw back to calling page
     * @return SKIP_BODY
     */
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        HttpServletResponse response = (HttpServletResponse)pageContext.getResponse();
//        HttpSession session = pageContext.getSession();

        try {
          out.println ( "<html>" );
          out.println ( "<head>" );
          out.print   ( "  <title>" );
          if ( !this.headerTitle.equals("") ) { out.print ( this.headerTitle ); }
          else { out.print ( this.title ); }
          out.println ( "</title>" );
          out.println ( "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + Config.xmlEncoding +"\">" );
          if ( !this.headerKeywords.equals("") ) {
            out.println ( "  <meta name=\"keywords\" content=\"" + this.headerKeywords + "\">" );
          }

          out.println ( "  <link rel=\"stylesheet\" href=\"" + Config.rootURL + "styles/survey.css\" type=\"text/css\">" );
          out.println ( "</head>" );
          out.print   ( "<body bgcolor=\"" + Config.bgColor + "\" leftMargin=\"0\" topMargin=\"0\" marginheight=\"0\" marginwidth=\"0\"" );
          if ( this.onLoad != null && !this.onLoad.equals ( "" ) )
            out.print ( " onLoad=\"" + this.onLoad + "\" " );
          out.println ( ">" );
          pageContext.include ( Config.pageHeaderFile );
          out.println ( "<h1>" + this.title + "</h1>" );
        }
        catch ( Exception ex ) {
            throw new JspTagException ( "exception " + ex.getMessage() );
        }
        return SKIP_BODY;
    }
}
