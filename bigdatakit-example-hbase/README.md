# BigdataKit-Example-Hbase

---

## Implementation

Write your own Processor class implements HbaseETLProcessor interface, and implements method doETL. Package your jar and use bigdatakit hbase-etl to submit your job.

```
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
```

## Usage

### Command

```
Usage: hbase-etl [-options] <jar> <logdate>
```

### Options

```
-Dmaster=<url>            specify the Spark run mode, "local[*]" or "yarn-client"
-Dname=<name>             specify the name of Spark application
-Dnamespace=<namespace>   specify the database, default default
-Dtable=<table>           specify the table
-DcolumnFamily=<columnFamily>  specify the DcolumnFamily
-Dprocessor=<class>       specify the processor class
-Dapproach=<approach>     specify the hbase import approach, "put" or "bulkload"
-Dparallelism=<num>       specify the parallelism which will influence orc file num and write parallelism, default 1
```

### Example

```
$ bigdatakit hbase-etl \
    -Dtable=hotwords_demo_315 \
    -DcolumnFamily=cf \
    -Dprocessor=com.sogou.bigdatakit.example.hbase.etl.ExampleHbaseETLProcessor \
    bigdatakit-example-hbase-1.0-SNAPSHOT.jar 201603150000
```