package com.surine.tustbox.Helper.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.surine.tustbox.App.Data.Constants;
import com.surine.tustbox.App.Data.FormData;
import com.surine.tustbox.App.Data.JCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.surine.tustbox.App.Data.FormData.JCODE;
import static com.surine.tustbox.App.Data.JCode.ERROR;
import static com.surine.tustbox.App.Data.JCode.J200;
import static com.surine.tustbox.App.Data.JCode.J400;

/**
 * Created by surine on 2017/5/14.
 */

public class JsonUtil {


    // 将Json数据解析成相应的映射对象
    public static <T> T parseJsonWithGson(String jsonData, Class<T> type) {
        Gson gson = new Gson();
        T result = gson.fromJson(jsonData, type);
        return result;
    }

    // 将Json数据解析成相应的映射列表
    public static <T> List<T> parseJsonWithGsonToList(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> mList = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for(final JsonElement elem : array){
            mList.add(gson.fromJson(elem, cls));
        }
        return mList;
    }


    /**
     * 获取请求状态
     * @param dataFromServer 从服务器返回的数据
     * @return int 服务器状态
     * */
    public static int getStatus(String dataFromServer){
        int code = ERROR;
        try {
            JSONObject jsonObject = new JSONObject(dataFromServer);
            switch (jsonObject.getInt(JCODE)){
                case 400:code = J400;break;
                case 200:code = J200;break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            code = ERROR;
        }
        return code;
    }

    /**
     * 快捷解析一个对象
     */
    public static <T> T getPojo(String dataFromServer,Class<T> cls) throws JSONException{
        JSONObject jsonObject = new JSONObject(dataFromServer);
        return parseJsonWithGson(jsonObject.getString(FormData.JDATA),cls);
    }

    /**
     * 快捷解析一个对象列表
     */
    public static <T> List<T> getPojoList(String dataFromServer,Class<T> cls) throws JSONException{
        JSONObject jsonObject = new JSONObject(dataFromServer);
        return parseJsonWithGsonToList(jsonObject.getString(FormData.JDATA),cls);
    }


}