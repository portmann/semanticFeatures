package ch.lgt.ming.cleanup;

public class HTMLStrings {
	
	String beforeTitle ="<html><style> .textDiv { width: 900px; border: 25px solid #58D3F7; padding: 25px; margin: 25px; font-family: 'Arial', Times, serif; font-size: 16px; text-align: justify;} .title { margin: 50px 50px 50px 25px; font-family: 'Arial', Times, serif; font-size: 40px; font-weight: bold; color:  #58D3F7; text-align: left;} </style><head></head><body><div class='title'>";
	String afterTitle = "</div><div class='textDiv'>";
	String end = "</div></body></html>";
	
	
	public String getBeforeTitle() {
		return beforeTitle;
	}
	public void setBeforeTitle(String beforeTitle) {
		this.beforeTitle = beforeTitle;
	}
	public String getAfterTitle() {
		return afterTitle;
	}
	public void setAfterTitle(String afterTitle) {
		this.afterTitle = afterTitle;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	
	
	

}
