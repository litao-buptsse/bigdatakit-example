package com.sogou.bigdatakit.example.hive.etl

import com.sogou.bigdatakit.hive.etl.processor.ETLProcessor
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapred.TextInputFormat
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.hive.HiveContext

class ImeAndroidDBGETLProcessor extends ETLProcessor {

  case class ImeAndroidDBG(app: String, query: String, st: Int)

  override def doETL(sqlContext: HiveContext, database: String, table: String, logdate: String): DataFrame = {
    val input = s"/logdata/for_ime/AndroidDBG1week/$logdate/*.gz"

    import sqlContext.implicits._

    sqlContext.sparkContext.
      hadoopFile(input, classOf[TextInputFormat], classOf[LongWritable], classOf[Text]).
      map(pair => new String(pair._2.getBytes, "GBK")).
      filter(_.startsWith("st")).map { line =>
      val arr = line.split("\t")
      if (arr.length == 4 && Seq("0", "1", "2").contains(arr(3))) {
        new ImeAndroidDBG(arr(2), arr(1), arr(3).toInt)
      } else {
        null
      }
    }.filter(_ != null).toDF()
  }
}