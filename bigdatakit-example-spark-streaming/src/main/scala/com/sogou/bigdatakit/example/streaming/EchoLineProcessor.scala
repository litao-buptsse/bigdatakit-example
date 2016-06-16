package com.sogou.bigdatakit.example.streaming

import com.sogou.bigdatakit.streaming.processor.LineProcessor

/**
  * Created by Tao Li on 6/16/16.
  */
class EchoLineProcessor extends LineProcessor {
  override def beforeBatch(): Unit = {
    println("before batch ...")
  }

  override def afterBatch(): Unit = {
    println("after batch ...")
  }

  override def process(message: String): Unit = {
    println("process: " + message)
  }
}
