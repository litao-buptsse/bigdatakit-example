package com.sogou.bigdatakit.example.hbase.etl

import com.sogou.bigdatakit.example.hbase.etl.avro.Hotwords
import com.sogou.bigdatakit.hbase.etl.processor.HbaseETLProcessor
import org.apache.avro.specific.SpecificRecordBase
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.hive.HiveContext

/**
  * Created by Tao Li on 2016/3/15.
  */
class ExampleHbaseETLProcessor extends HbaseETLProcessor {
  override def doETL(sqlContext: HiveContext, namespace: String, table: String, logdate: String): RDD[(String, Map[String, (SpecificRecordBase, Long)])] = {
    sqlContext.sql(
      s"""
    SELECT pid, query, count(*) AS pv
    FROM custom.scribe_common_wap_nginx
    WHERE logdate='$logdate' AND clean_state='OK' AND pid != '' AND query != ''
    GROUP BY pid, query
    LIMIT 100
      """).map { r =>
      val pid = r.getAs[String]("pid")
      val query = r.getAs[String]("query")
      val pv = r.getAs[Long]("pv")
      val hotwords = new Hotwords(pid, query, pv)
      (pid, (query, (hotwords, logdate.toLong)))
    }.groupByKey.mapValues(_.toMap)
  }
}
