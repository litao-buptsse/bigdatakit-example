package com.sogou.bigdatakit.example.hive.serde;

import com.sogou.bigdatakit.hive.serde.TextDeserializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tao Li on 2016/1/15.
 */
public class ExampleDeserializer extends TextDeserializer {
  @Override
  public List<Object> deserialize(String line, List<Object> reuse) {
    String[] array = line.split(" ");

    // f0: string
    String f0 = array[0];

    // f1: int
    int f1 = Integer.parseInt(array[1]);

    // f2: array<string>
    List<String> f2 = new ArrayList<>();
    for (String e : array[2].split(",")) {
      f2.add(e);
    }

    // f3: map<string,string>
    Map<String, String> f3 = new HashMap<>();
    for (String kvStr : array[3].split("&")) {
      String[] kv = kvStr.split("=");
      f3.put(kv[0], kv[1]);
    }

    // f4: array<struct<pos:int,url:string>>
    List<Object> f4 = new ArrayList<>();
    for (String str : array[4].split(",")) {
      String[] kv = str.split(":");
      f4.add(new Object[]{Integer.parseInt(kv[0]), kv[1]});
    }

    reuse.add(0, f0);
    reuse.add(1, f1);
    reuse.add(2, f2);
    reuse.add(3, f3);
    reuse.add(3, f4);

    return reuse;
  }
}
