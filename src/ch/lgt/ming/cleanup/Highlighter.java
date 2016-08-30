package ch.lgt.ming.cleanup;

public interface Highlighter {

	// Highlighter highlights like
	// For highlighted text: *before_highlighted* *String* *after_highlighted*
	// For standard text: *before_standard* *String* *after_standard*

	public String before_highlighted = "<b>";
	public String after_highlighted = "</b>";
	public String before_standard = "";
	public String after_standard = "";

	public String highlight(Document document);

}