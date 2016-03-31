package com.sogou.bigdatakit.example.hive.serde;

import com.sogou.bigdatakit.hive.serde.TextDeserializer;
import org.apache.hadoop.io.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tao Li on 2016/1/15.
 */
public class ExampleDeserializer extends TextDeserializer {

  @Override
  public List<Object> deserialize(Text text, List<Object> reuse) {
    String line = text.toString();
    String[] array = line.split(" ");

    if (array.length != 4) {
      System.err.println("invalid line: " + line);
      return null;
    }

    int f0 = Integer.parseInt(array[0]);
    List<String> f1 = new ArrayList<>();
    for (String str : array[1].split(",")) {
      f1.add(str);
    }
    String f2 = array[2];
    Map<String, String> f3 = new HashMap<>();
    for (String kvStr : array[3].split("&")) {
      String[] kv = kvStr.split("=");
      f3.put(kv[0], kv[1]);
    }

    reuse.add(0, f0);
    reuse.add(1, f1);
    reuse.add(2, f2);
    reuse.add(3, f3);

    return reuse;
  }
}
