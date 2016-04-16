package com.cas.cache;

import com.cas.game.model.HapinnessStatus;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tolga on 19.03.2016.
 */
public class CacheManager {

        private static ConcurrentHashMap<String,HapinnessStatus> hapinnessStatusConcurrentHashMap = new ConcurrentHashMap<>();

        public static Map<String,HapinnessStatus> getHapinessCache(){
            return hapinnessStatusConcurrentHashMap;
        }

        public static double getHappinessRatio() {
            double ratio = 0.0;
            int happy = 0;
            int unhappy = 0;
            for(HapinnessStatus happiness : getHapinessCache().values()){
                if(happiness.getStatus().equals("HAPPY")){
                    happy++;
                }else {
                    unhappy++;
                }
            }
            if(happy != 0 && unhappy != 0){
                return happy / unhappy;
            }else {
                return 1;
            }
        }
}
