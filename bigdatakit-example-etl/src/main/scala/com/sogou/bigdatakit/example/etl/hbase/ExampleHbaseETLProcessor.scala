package com.sogou.bigdatakit.example.etl.hbase

import com.sogou.bigdatakit.etl.hbase.HBaseCFAvroTransformer
import com.sogou.bigdatakit.example.etl.hbase.avro.Hotwords
import org.apache.spark.sql.hive.HiveContext

/**
  * Created by Tao Li on 2016/3/15.
  */
class ExampleHbaseETLProcessor extends HBaseCFAvroTransformer {
  override def transform(sqlContext: HiveContext, logdate: String) = {
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
