package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import java.util.List;


public class ViewFilter {
	
	public void filter(String startDate, String endDate, List<String> freakList) {
		/*
		 * For each freak, search the list of actions to find this freak and compose a list of cameras
		 *     that would be fetched from this freak
		 *     Then fetch the json from this freak asynchronously
		 * Filter on the current requirements
		 * wait for the results from the freak requests
		 * merge the results
		 * 
		 * return result
		 */
		
	}

}
