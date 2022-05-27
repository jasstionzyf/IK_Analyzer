package org.wltea.analyzer;
import com.google.common.collect.Maps;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.wltea.analyzer.lucene.IKTokenizer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import static spark.Spark.*;

public class IKHttp {

    public static void main(String[] args) {
        int maxThreads=16;
        int port=91;
        threadPool(maxThreads);
        port(port);
        options("/*",
                (request, response) -> {


                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
                });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));


        get("/token", (request, response) -> {
            Map<String, Object> resultMap = Maps.newHashMap();
            String finalResultJson = null;
            List<String> kws=new ArrayList<>();
            String inputText=request.queryParams("inputText");
            String smart=request.queryParams("smart");
            response.type("application/json");




            if (inputText != null) {
                IKTokenizer iKTokenizer = new IKTokenizer(new StringReader(inputText), Boolean.parseBoolean(smart));
                while (iKTokenizer.incrementToken()) {
                    CharTermAttribute charTermAttribute = iKTokenizer.getAttribute(CharTermAttribute.class);
                    TypeAttribute typeAttribute = iKTokenizer.getAttribute(TypeAttribute.class);
                    String term=charTermAttribute.toString();
                    String type=typeAttribute.type();
                    if(type.equals("4_1")){
                        kws.add(term);
                    }






                }

            }
            resultMap.put("kws",kws);

            try {
                finalResultJson = JSON.toJSONString(resultMap, SerializerFeature.WriteMapNullValue);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return finalResultJson;
        });

    }


}
