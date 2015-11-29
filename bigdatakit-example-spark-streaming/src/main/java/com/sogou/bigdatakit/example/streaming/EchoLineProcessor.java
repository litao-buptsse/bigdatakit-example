package com.sogou.bigdatakit.example.streaming;

import com.sogou.bigdatakit.streaming.processor.LineProcessor;

/**
 * Created by Tao Li on 11/24/15.
 */
public class EchoLineProcessor implements LineProcessor {

  public void init() {
    System.out.println("init...");
  }

  public void process(String message) {
    System.out.println("process: " + message);

  }

  public void close() {
    System.out.println("close...");
  }
}
