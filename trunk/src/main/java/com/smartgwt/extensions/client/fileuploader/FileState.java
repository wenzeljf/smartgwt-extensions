package com.smartgwt.extensions.client.fileuploader;

public enum FileState {
	/**
	 * Waiting for uploading.
	 */
	STATE_QUEUE("queue"),

	/**
	 * Uploaded successfully.
	 */
	STATE_FINISHED("finished"),

	/**
	 * Failed to upload.
	 */
	STATE_FAILED("failed"),

	/**
	 * Being uploaded.
	 */
	STATE_PROCESSING("processing");

	private String value;

	FileState(String value) {
		this.value = value;
	}

	public String toString() {
		return value;
	}

}
