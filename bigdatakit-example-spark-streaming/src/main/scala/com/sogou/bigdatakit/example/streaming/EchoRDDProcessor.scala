package com.sogou.bigdatakit.example.streaming

import com.sogou.bigdatakit.streaming.processor.RDDProcessor
import org.apache.spark.rdd.RDD

/**
  * Created by Tao Li on 6/16/16.
  */
class EchoRDDProcessor extends RDDProcessor {
  override def process(rdd: RDD[String]): Unit = {
    rdd.foreach(println(_))
  }
}
