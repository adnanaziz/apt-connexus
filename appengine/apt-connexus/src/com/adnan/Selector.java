package com.adnan;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Selector {
	private Date startDate;
	private Date endDate;
	private int maxNumberToReturn = Integer.MAX_VALUE;
	
	public Selector(Date s, Date e) {
		this.startDate = s;
		this.endDate = e;
	}
	
	public Selector() {
	}
	
	public Selector(Date s, Date e, int m) {
		this(s,e);
		this.maxNumberToReturn = m;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public List<Stream> selectStreams( List<Stream> aList ) {
		List<Stream> result = new ArrayList<Stream>();
		int count= 0;
		for ( Stream s : aList ) {
			if ( s.createDate.compareTo(startDate) < 0  || s.createDate.compareTo(endDate) > 0 ) {
				continue;
			}
			result.add(s);
			count++;
			if (count > maxNumberToReturn) {
				break;
			}
		}
		return result;
	}
	
	//TODO(AA): refactor to remove dup wrt above
	public List<ConnexusImage> selectImages( List<ConnexusImage> aList, Long streamId ) {
		List<ConnexusImage> result = new ArrayList<ConnexusImage>();
		int count= 0;
		for ( ConnexusImage s : aList ) {
			if ( !s.streamId.equals(streamId) || s.createDate.compareTo(startDate) < 0  || s.createDate.compareTo(endDate) > 0 ) {
				continue;
			}
			result.add(s);
			count++;
			if (count > maxNumberToReturn) {
				break;
			}
		}
		return result;
	}

}
