package com.sogou.bigdatakit.example.etl.phoenix

import com.sogou.bigdatakit.etl.phoenix.PhoenixTransformer
import org.apache.spark.sql.hive.HiveContext

/**
  * Created by Tao Li on 2016/3/31.
  */
class ExamplePhoenixETLProcessor extends PhoenixTransformer {
  override def transform(sqlContext: HiveContext, logdate: String) = {
    sqlContext.sql(
      s"""
         |select ip, count(*) as pv
         |from custom.common_wap_pv
         |where logdate='${logdate}'
         |group by ip
       """.stripMargin)
  }
}
