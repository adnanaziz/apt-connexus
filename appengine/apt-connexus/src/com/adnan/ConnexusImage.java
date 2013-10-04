package com.adnan;

import java.util.Date;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.common.base.Joiner;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class ConnexusImage implements Comparable<ConnexusImage> {

	@Id
	public Long id;
	public Long streamId;
	public String comments;
	public String bkUrl;
	public Date createDate;
	public Double latitude;
	public Double longitude;

	@SuppressWarnings("unused")
	private ConnexusImage() {
	}

	public ConnexusImage(Long streamId, String user, String content, String bkUrl) {
		this.streamId = streamId;
		this.bkUrl = bkUrl;
		this.comments = content;
		createDate = new Date();
	}
	
	public ConnexusImage(Long streamId, String user, String content, String bkUrl, Double latitude, Double longitude) {
		this.streamId = streamId;
		this.bkUrl = bkUrl;
		this.comments = content;
		createDate = new Date();
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	@Override
	public String toString() {
		// Joiner is from google Guava (Java utility library), makes the toString method a little cleaner
		Joiner joiner = Joiner.on(":").useForNull("NULL");
		System.out.println(id);
		System.out.println(streamId);
		System.out.println(bkUrl);
		System.out.println(createDate.toString());
		return joiner.join(id.toString(), streamId, comments, bkUrl==null ? "null" : bkUrl, createDate.toString(), latitude, longitude);
	}

	// Need this for sorting images by date
	@Override
	public int compareTo(ConnexusImage other) {
		if (createDate.after(other.createDate)) {
			return 1;
		} else if (createDate.before(other.createDate)) {
			return -1;
		}
		return 0;
	}
	
}
