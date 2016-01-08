# BigdataKit-Example-Hive

---

## Building & Running

Please refer to [BigdataKit-Example README](http://gitlab.dev.sogou-inc.com/sogou-spark/bigdatakit-example/blob/master/README.md)


## Implementation

Write your own Processor class implements ETLProcessor class, and implements method doETL(). Package your jar and use bigdatakit etl to submit your job.

```
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
```

## Usage

### Command

```
Usage: bigdatakit etl <jar> <logdate> [options]
```

### Options

```
-Dmaster=<url>            specify the Spark run mode, "local[*]" or "yarn-client"
-Dname=<name>             specify the name of Spark application
-Dname=<database>         specify the database
-Dname=<table>            specify the table
-Dprocessor=<class>       specify the processor class
```

### Example: Running etl job

```
$ bigdatakit etl \
    bigdatakit-example-hive-1.0-SNAPSHOT.jar 20160104 \
    -Ddatabase=custom -Dtable=ime_anadroid_dbg \
    -Dprocessor=com.sogou.bigdatakit.example.hive.etl.ImeAndroidDBGETLProcessor
```
