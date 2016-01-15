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
    List<String> f4 = new ArrayList<String>();
    for (String e : array[1].split(",")) {
      f4.add(e);
    }
    Map<String, String> f3 = new HashMap<String, String>();
    for (String kvStr : array[3].split("&")) {
      String[] kv = kvStr.split("=");
      f3.put(kv[0], kv[1]);
    }

    reuse.add(0, Integer.parseInt(array[0]));
    reuse.add(1, f4);
    reuse.add(2, array[2]);
    reuse.add(3, f3);

    return reuse;
  }
}
