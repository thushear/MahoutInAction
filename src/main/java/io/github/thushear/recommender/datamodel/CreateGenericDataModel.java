package io.github.thushear.recommender.datamodel;

import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;

/**
 * Created by kongming on 2016/7/8.
 *
 * DataMOdel 抽象推荐系统输入来源
 *
 */
public class CreateGenericDataModel {


    public static void main(String[] args) {

        FastByIDMap<PreferenceArray> preferences = new FastByIDMap<PreferenceArray>();
        PreferenceArray preferenceArray = new GenericUserPreferenceArray(10);
        preferenceArray.setUserID(0,1l);
        preferenceArray.setItemID(0,101l);
        preferenceArray.setValue(0,3.0f);

        preferenceArray.setItemID(1,102l);
        preferenceArray.setValue(1,4.5f);

        preferences.put(1l,preferenceArray);
        DataModel model = new GenericDataModel(preferences);
        System.out.println(model);

    }

}
