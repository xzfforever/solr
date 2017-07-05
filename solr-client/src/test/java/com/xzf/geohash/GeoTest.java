package com.xzf.geohash;

import ch.hsr.geohash.GeoHash;
import org.assertj.core.util.Strings;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.distance.DistanceUtils;

import java.util.*;
import java.util.stream.Collectors;

public class GeoTest {

    private static final Map<Double,Double> addressData = new HashMap<>();

    private Map<AddressDO,String> addressDOMap = null;

    // 我的坐标：114.059968,22.540446
    static {
        //500m之内
        addressData.put(114.058387,22.539277);
        addressData.put(114.062411,22.542615);
        //1km 到 5km
        addressData.put(114.047032,22.538843);
        addressData.put(114.070891,22.540045);
        addressData.put(114.052494,22.554064);
        addressData.put(114.075203,22.530965);
        addressData.put(114.077646,22.552462);
        //5km到10km
        addressData.put(114.071035,22.564477);
        addressData.put(114.076496,22.5319);
        addressData.put(114.085695,22.555666);
    }

    @Before
    public void initAddressData(){
        addressDOMap = new HashMap<>();
        addressData.forEach((lon,lat)->{
            AddressDO addressDO = new AddressDO();
            addressDO.setLongitude(lon);
            addressDO.setLatitude(lat);
            GeoHash geoHash =  GeoHash.withCharacterPrecision(lat, lon, 5);
            addressDO.setGeoCode(geoHash.toBase32());
            addressDOMap.put(addressDO,geoHash.toBase32());
         });
    }

    @Test
    public void test(){
        //定位的位置
       AddressDO myAddress = initMyPos();
        //粗略过滤出符合条件的网点地址
       List<AddressDO> filterResult = filter(myAddress);
        //排序
       sort(filterResult,myAddress);
        //精确过滤
        List<AddressDO> finalResult = getFinalResult(filterResult);
    }

    private List<AddressDO> getFinalResult(List<AddressDO> addressList){
        return  addressList.stream().filter(x->{
                     return x.getDistance() >= 0.5;
                }).collect(Collectors.toList());
    }

    private AddressDO initMyPos(){
        double my_pos_lon = 114.059968;
        double my_pos_lat = 22.540446;
        GeoHash myGeoHash =  GeoHash.withCharacterPrecision(my_pos_lat, my_pos_lon, 5);
        String my_pos_geocode = myGeoHash.toBase32();
        AddressDO myAddressDO = new AddressDO();
        myAddressDO.setGeoCode(my_pos_geocode);
        myAddressDO.setLatitude(my_pos_lat);
        myAddressDO.setLongitude(my_pos_lon);
        return myAddressDO;
    }


    //按距离进行升序排序
    private void sort(List<AddressDO> addressList,AddressDO myAddress){
        SpatialContext geo = SpatialContext.GEO;
        addressList.sort((AddressDO a1,AddressDO a2)->{
            Double distanceA1 = geo.calcDistance(
                    geo.getShapeFactory().pointXY(myAddress.getLongitude(),myAddress.getLatitude())
                    ,geo.getShapeFactory().pointXY(a1.getLongitude(),a1.getLatitude()))
                    * DistanceUtils.DEG_TO_KM;
            a1.setDistance(distanceA1);
            Double distanceA2 = geo.calcDistance(
                    geo.getShapeFactory().pointXY(myAddress.getLongitude(),myAddress.getLatitude())
                    ,geo.getShapeFactory().pointXY(a2.getLongitude(),a2.getLatitude()))
                    * DistanceUtils.DEG_TO_KM;
            a2.setDistance(distanceA2);
            return distanceA1.compareTo(distanceA2);
        });
    }


    private List<AddressDO> filter(AddressDO myAddress){
        List<AddressDO> result = new ArrayList<>();
        addressDOMap.forEach((key,value)->{
            if(value.startsWith(myAddress.getGeoCode())){
                result.add(key);
            }
        });
        return result;
    }





}
