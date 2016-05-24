package com.sogou.bigdatakit.example.etl.phoenix

import com.sogou.bigdatakit.etl.phoenix.{PhoenixETLUtils, PhoenixRunner}
import org.apache.spark.sql.hive.HiveContext

/**
  * Created by Tao Li on 2016/3/31.
  */
class ExamplePhoenixETLProcessor2 extends PhoenixRunner {
  override def run(sqlContext: HiveContext, logdate: String) = {
    val df = sqlContext.sql(
      s"""
         |select ip, count(*) as pv
         |from custom.common_wap_pv
         |where logdate='${logdate}'
         |group by ip
       """.stripMargin)
    PhoenixETLUtils.toPhoenix(df, "mytb", logdate, 50)

    val df2 = sqlContext.sql(
      s"""
         |select pid, count(*) as pv
         |from custom.common_wap_pv
         |where logdate='${logdate}'
         |group by pid
       """.stripMargin)
    PhoenixETLUtils.toPhoenix(df2, "mytb2", logdate, 20)
  }
}
