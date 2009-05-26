package com.smartgwt.extensions.client.fileuploader;

/**
 * Filter based on specified file extensions.
 * 
 * @author anthony.yuan@gmail.com
 */
public class ExtensionFilter implements FileNameFilter {
	private String[] permittedExtensions;

	public ExtensionFilter(String... permittedExtensions) {
		assert permittedExtensions != null && permittedExtensions.length > 0;

		this.permittedExtensions = permittedExtensions;
	}

	private String errorMessage;
	public String getErrorMessage() {
		if (errorMessage == null) {
			errorMessage = "Extension has to be: ";
			for (int i = 0; i < permittedExtensions.length; i++) {
				errorMessage += permittedExtensions[i];
				if (i < permittedExtensions.length - 1) {
					errorMessage += ", ";
				}
			}
		}
		return errorMessage;
	}

	public boolean validate(String fileName) {
		if (fileName == null) {
			return false;
		}
		String testName = fileName.trim().toLowerCase();
		for (int i = 0; i < permittedExtensions.length; i++) {
			if (testName.endsWith(permittedExtensions[i])) {
				return true;
			}
		}
		return false;
	}

}
