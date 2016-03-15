package com.sogou.bigdatakit.example.hive.etl

import com.sogou.bigdatakit.hive.etl.processor.HiveETLProcessor
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.hive.HiveContext

class ExampleHiveETLProcessor extends HiveETLProcessor {

  override def doETL(sqlContext: HiveContext, database: String, table: String, logdate: String): DataFrame = {
    sqlContext.sql(s"select channel, count(*) as pv from custom.common_pc_pv where logdate='{$logdate}' group by channel")
  }
}