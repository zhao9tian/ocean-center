package com.quxin.freshfun.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {


	public  static  final String PTP_DATA = "ptp";
	public  static  final String ONLINE_DATA = "online";

	@Override
	protected Object determineCurrentLookupKey() {
		return DynamicDataSourceHolder.getDataSource();
	}
	
}
