package com.adnan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Date;

//AA: functionality subsumed by Selector
@Deprecated
class StreamSelector {
	public long startDate;
	public long endDate;
	StreamSelector( Date s, Date e) {
		this.startDate = s.getTime();
		this.endDate = e.getTime();
	}
	boolean matches(Stream s) {
		return ( s.createDate.getTime() >= startDate && s.createDate.getTime() < endDate);
	}
}

public class AllStreamsUtility {
  public static List<Stream> allStreams() {
		List<Stream> th = OfyService.ofy().load().type(Stream.class).list();
		Collections.sort(th);
		return th;
  }
  
  public static List<Stream> allSelectedStreams(StreamSelector so) {
	List<Stream> th = OfyService.ofy().load().type(Stream.class).list();
	Collections.sort(th);
	List<Stream> result = new ArrayList<Stream>();
	for ( Stream s : th ) {
		if ( so.matches(s)) {
		  result.add(s);
		}
	}
	return th;
  }

}
