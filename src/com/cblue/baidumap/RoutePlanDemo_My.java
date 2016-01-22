package com.cblue.baidumap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import baidumapsdk.demo.R;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

/**
 * 路线实现
 * 得到路线搜索对象，并设置路线监听搜索
 * 得到开始点，结束点，进行汽车路线搜索，在搜索结果中定义开始点和结束点的图片，并显示
 * 得到路线的节点信息，并以此打印节点
 * @author pavel
 *
 */
public class RoutePlanDemo_My extends Activity implements
		OnGetRoutePlanResultListener {

	private MapView mapView;
	private BaiduMap baiduMap;
	private String city = "北京";
	private String startPlace ="龙泽";
	private String endPlace = "西单";
	private RoutePlanSearch routePlanSearch; /* 线路平面图搜索 */
	private RouteLine routeLine; /* 线路 */
	
	
	private Button btn;

	private OverlayManager overlayManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_map);
		mapView = (MapView) findViewById(R.id.myMapView);
		btn = (Button)findViewById(R.id.myMapBtn);
		baiduMap = mapView.getMap();
        Log.i("aaa", "RoutePlanDemo_My");
        
    	// 初始化搜索模块
		routePlanSearch = RoutePlanSearch.newInstance();
		routePlanSearch.setOnGetRoutePlanResultListener(this);
        
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 起终点的信息
				PlanNode stratNode = PlanNode.withCityNameAndPlaceName(city,"西二旗");
				PlanNode endNode = PlanNode.withCityNameAndPlaceName(city,"西直门");
				
				  Log.i("aaa","--"+ stratNode.toString());
				// 起终点 开车信息
				routePlanSearch.drivingSearch(new DrivingRoutePlanOption().from(
						stratNode).to(endNode));
				
			}
		});
	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		// TODO Auto-generated method stub
		Log.i("aaa", "result="+result);
		Log.i("aaa", result.error+"===="+SearchResult.ERRORNO.NO_ERROR);
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Log.i("aaa", "驾车未找到结果");
		} else if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			Log.i("aaa", "驾车信息找到结果");
			routeLine = result.getRouteLines().get(0);
			MyDriverRouteOverlay myDriverRouteOverlay = new MyDriverRouteOverlay(
					baiduMap);   
			baiduMap.setOnMarkerClickListener(myDriverRouteOverlay);
			myDriverRouteOverlay.setData(result.getRouteLines().get(0)); // 设置线路
			myDriverRouteOverlay.addToMap(); // 添加到地图上
			myDriverRouteOverlay.zoomToSpan(); /* 缩放地图 */
			
			getNodeInfo(routeLine);
		}

	}
	// 获取节点信息
	private void getNodeInfo(RouteLine routeLine) {
		// TODO Auto-generated method stub
		for (int i = 0; i < routeLine.getAllStep().size() - 1; i++) {
			Object step = routeLine.getAllStep().get(i);
			if (step instanceof DrivingRouteLine.DrivingStep) {
				LatLng nodeLocation = ((DrivingRouteLine.DrivingStep) step)
						.getEntrance().getLocation();
				String nodeTitle = ((DrivingRouteLine.DrivingStep) step)
						.getInstructions();
				Log.i("aaa", "nodetitle=" + nodeTitle + "  经纬度"
						+ nodeLocation.latitude + "===="
						+ nodeLocation.longitude);
			}
		}

	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult arg0) {
		// TODO Auto-generated method stub

	}

	public class MyDriverRouteOverlay extends DrivingRouteOverlay {

		public MyDriverRouteOverlay(BaiduMap arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			// TODO Auto-generated method stub
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {// 末端
			// TODO Auto-generated method stub
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
		}

	}

}
