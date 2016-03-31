package com.sogou.bigdatakit.example.etl.hive

import com.sogou.bigdatakit.etl.hive.HiveTransformer
import org.apache.spark.sql.hive.HiveContext

class ExampleHiveETLProcessor extends HiveTransformer {

  override def transform(sqlContext: HiveContext, logdate: String) = {
    sqlContext.sql(
      s"""
         |select channel, count(*) as pv
         |from custom.common_pc_pv
         |where logdate='{$logdate}'
         |group by channel
       """.stripMargin)
  }
}