package com.sogou.bigdatakit.example.streaming;

import com.sogou.bigdatakit.streaming.processor.LineProcessor;

/**
 * Created by Tao Li on 11/24/15.
 */
public class EchoJavaLineProcessor extends LineProcessor {
  @Override
  public void beforeBatch() {
    System.out.println("before batch ...");
  }

  @Override
  public void afterBatch() {
    System.out.println("after batch ...");
  }

  public void process(String message) {
    System.out.println("process: " + message);
  }
}
