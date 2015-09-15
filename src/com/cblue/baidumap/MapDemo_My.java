package com.cblue.baidumap;

import baidumapsdk.demo.R;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;


import android.app.Activity;
import android.os.Bundle;

public class MapDemo_My extends Activity {
	
	
	private MapView mapView;
	private BaiduMap baiduMap;
	private BaiduMapOptions baiduMapOptions;
	private MapStatus mapStatus;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_map);
		mapView = (MapView)findViewById(R.id.myMapView);
		
		//设置中心点
/*		LatLng latLng = new LatLng(10.0, 10.0);
		mapStatus = new MapStatus.Builder().target(latLng).build();
		baiduMapOptions = new BaiduMapOptions().mapStatus(mapStatus);
		mapView = new MapView(MapDemo.this, baiduMapOptions);
		setContentView(mapView);*/
		
		baiduMap =mapView.getMap();
		baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//普通地图或卫星图
		//baiduMap.setTrafficEnabled(true);//打开交通图
		baiduMap.setBaiduHeatMapEnabled(true);// 打开热力图
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		// activity 暂停时同时暂停地图控件
		mapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// activity 恢复时同时恢复地图控件
		mapView.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// activity 销毁时同时销毁地图控件
		mapView.onDestroy();
	}


}
