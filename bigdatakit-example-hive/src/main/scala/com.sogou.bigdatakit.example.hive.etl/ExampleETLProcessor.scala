package com.sogou.bigdatakit.example.hive.etl

import com.sogou.bigdatakit.hive.etl.processor.ETLProcessor
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.hive.HiveContext

class ExampleETLProcessor extends ETLProcessor {

  override def doETL(sqlContext: HiveContext, database: String, table: String, logdate: String): DataFrame = {
    sqlContext.sql(s"select channel, count(*) as pv from custom.common_pc_pv where logdate='{$logdate}' group by channel")
  }
}