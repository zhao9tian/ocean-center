package com.quxin.freshfun.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {


	public  static  final String PTP_DATA = "ptp";
	public  static  final String ONLINE_DATA = "online";
	public  static  final String OCEAN_DATA = "ocean";

	@Override
	protected Object determineCurrentLookupKey() {
		return DynamicDataSourceHolder.getDataSource();
	}
	
}
