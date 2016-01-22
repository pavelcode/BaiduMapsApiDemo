package com.cblue.baidumap;

import baidumapsdk.demo.R;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.location.BDLocationListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * 得到百度地图对象  （从官网拷贝）
 * 开启监听模式
 * 设置定位参数（监听模式，定位显示的图片）
 * 设置定位客户端  给定位客户端设置定位选项（打开gps，经纬度坐标系，扫描间隔，地址信息，设置定位提示）
 * 设置客户端的定位监听   （得到定位数据之后，把定位数据显示在地图上。如果是第一次，根据经纬度更新当前状态，并打印定位信息）
 * 
 * 注意：显示地址，在LocationClientOption设置显示地址
 * 
 * 定位必须使用真机测试
 * @author pavel
 *
 */
public class LocationDemo_My extends Activity {
	
	
	private MapView mapView;
	private BaiduMap baiduMap;
	private LocationClient locationClient;
	private MyLocationListener myLocationListener = new MyLocationListener();
	private boolean isFirstLoc = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_map);
		mapView = (MapView)findViewById(R.id.myMapView);
		baiduMap = mapView.getMap();
		baiduMap.setMyLocationEnabled(true); //开始定位模式
		
		//设置定位标记图标
		//BitmapDescriptor currrentPic = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
		//第二个参数：是否显示方向信息
		baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(LocationMode.NORMAL,true,null));
		
		
		locationClient = new LocationClient(this);
	    LocationClientOption locationClientOption = new LocationClientOption();
	    locationClientOption.setOpenGps(true);
	    locationClientOption.setCoorType("bd09ll");  //经纬度坐标系
	    locationClientOption.setScanSpan(1000); //设置扫描间隔 默认是一次，既只定位一次
	    locationClientOption.setIsNeedAddress(true); //需要地址信息
	  //  locationClientOption.setIsNeedLocationDiscrpt(true);  设置是否需要位置语义化结果
	  //  locationClientOption.setIsNeedLocationPoiList(true);  设置是否需要POI
	    
	    locationClientOption.setLocationNotify(true);//设置是否当gps有效的时候按照1秒1次频率输出
		locationClient.setLocOption(locationClientOption);
		
		locationClient.registerLocationListener(myLocationListener);  //注册定位监听
		locationClient.start();
		
	}
	
	@Override
	protected void onPause() {
		mapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		locationClient.stop();
		// 关闭定位图层
		baiduMap.setMyLocationEnabled(false);
		mapView.onDestroy();
		mapView = null;
		super.onDestroy();
	}
	
	
	class MyLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation dbLocation) {
			// TODO Auto-generated method stub
			if(dbLocation==null||mapView==null){
				return;
			}
			//得到定位数据
			MyLocationData myLoationData = new MyLocationData.Builder()
			.accuracy(dbLocation.getRadius())  //设置精度
			.direction(dbLocation.getDirection())      //定位数据的方向信息
			.latitude(dbLocation.getLatitude())
			.longitude(dbLocation.getLongitude())
			.build();
			baiduMap.setMyLocationData(myLoationData);
			//如果是第一次登陆
			if(isFirstLoc){
				isFirstLoc = false;
				LatLng latLng = new LatLng(dbLocation.getLatitude(),dbLocation.getLongitude());
				MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
				baiduMap.animateMapStatus(mapStatusUpdate);
			}
			Log.i("aaa", "-----");
			showInfo(dbLocation);
			Log.i("aaa", "-----");
		}
		
		
		private void showInfo(BDLocation location){
			
		StringBuffer sb = new StringBuffer(256);
        sb.append("time");
        sb.append(location.getTime());
        sb.append("\nerror code : ");
        sb.append(location.getLocType());
        sb.append("\nlatitude : ");
        sb.append(location.getLatitude());
        sb.append("\nlontitude : ");
        sb.append(location.getLongitude());
        sb.append("\nradius : ");
        sb.append(location.getRadius());
        if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
            sb.append("\nspeed : ");
            sb.append(location.getSpeed());// 单位：公里每小时
            sb.append("\nsatellite : ");
            sb.append(location.getSatelliteNumber());
            sb.append("\nheight : ");
            sb.append(location.getAltitude());// 单位：米l
            sb.append("\ndirection : ");
            sb.append(location.getDirection());// 单位度
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());
            sb.append("\ndescribe : ");
            sb.append("gps定位成功");

        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());
            //运营商信息
            sb.append("\noperationers : ");
            sb.append(location.getOperators());
            sb.append("\ndescribe : ");
            sb.append("网络定位成功");
        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
            sb.append("\ndescribe : ");
            sb.append("离线定位成功，离线定位结果也是有效的");
        } else if (location.getLocType() == BDLocation.TypeServerError) {
            sb.append("\ndescribe : ");
            sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
            sb.append("\ndescribe : ");
            sb.append("网络不同导致定位失败，请检查网络是否通畅");
        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
            sb.append("\ndescribe : ");
            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
        }
        /*  sb.append("\nlocationdescribe : ");
            sb.append(location.get());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }*/
        Log.i("aaa", sb.toString());
	
		}
	}
	
	
	

}
