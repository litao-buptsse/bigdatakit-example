package com.sogou.bigdatakit.example.streaming;

import com.sogou.bigdatakit.streaming.processor.JavaRDDProcessor;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;

/**
 * Created by Tao Li on 6/16/16.
 */
public class EchoJavaRDDProcessor extends JavaRDDProcessor {
  @Override
  public void process(JavaRDD<String> rdd) {
    rdd.foreach(new VoidFunction<String>() {
      @Override
      public void call(String line) throws Exception {
        System.out.println("process: " + line);
      }
    });
  }
}
