package edu.vt.ward.survey;

public class Option {
  protected String label = "";
  protected String exportCode = "";
  protected boolean selected = false;
  protected long count = 0;

  Option () {
  }

  Option ( String label ) {
    this.label = new String (label);
  }

  public String getLabel () {
    return label;
  }

  public void setLabel ( String label ) {
    this.label = new String (label);
  }

  public String getExportCode () {
    return exportCode;
  }

  public void setExportCode ( String exportCode ) {
    this.exportCode = new String (exportCode);
  }

  public boolean getSelected () {
    return this.selected;
  }

  public void setSelected ( boolean selected ) {
    this.selected = selected;
  }

  public long getCount () {
    return this.count;
  }

  public void setCount ( long count ) {
    this.count = count;
  }

  public void incrementCount () {
    this.count++;
  }

  public void updateResultsSummary ( Option entryOption ) {
    if ( entryOption.getSelected () ) { // increment count for this option
      this.incrementCount ();
    };
  }
}