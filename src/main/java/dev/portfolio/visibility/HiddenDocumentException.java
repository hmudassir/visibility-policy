package dev.portfolio.visibility;

public class HiddenDocumentException extends RuntimeException {

	public HiddenDocumentException() {
		super("Document was not found");
	}
}
