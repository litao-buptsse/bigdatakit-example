# BigdataKit-Example-Spark-Streaming

---

## Building & Running

Please refer to [BigdataKit-Example README](http://gitlab.dev.sogou-inc.com/sogou-spark/bigdatakit-example/blob/master/README.md)


## Implementation

Write your own Processor class implements LineProcessor interface, and implements method init(), process(String message), close(). Package your jar and use bigdatakit spark-streaming to submit your job.

```
public class EchoLineProcessor implements LineProcessor {

  public void init() {
    System.out.println("init...");
  }

  public void process(String message) {
    System.out.println("process: " + message);

  }

  public void close() {
    System.out.println("close...");
  }
}
```

## Usage

### Command

```
Usage: bigdatakit spark-streaming <jar> [options]
```

### Options

```
-Dmaster=<url>            specify the Spark run mode, "local[*]" or "yarn-client"
-Dname=<name>             specify the name of Spark application
-Dapproach=<Dapproach>    specify the Kafka approach, "receiver-based" or "direct-based"
-DzkConnString=<url>      specify the ZooKeeper connection to use
-Dtopics=<topics>         specify the Kafka topics with comma seperated
-DgroupId=<group>         specify the Kafka consumer group id
-DbatchDuration=<seconds> specify the Spark Streaming batch process duration
-DthreadNum=<num>         specify the Kafka consumer thread num (for "receiver-based")
-DbrokerList=<url>        specify the kafka broker list (for "direct-based")
-DoffsetsCommitBatchInterval=<interval>  specify the Kafka offsets commit batch interval  (for "direct-based")
-D<property>=<value>      specify any Java system property value
```

### Example-1: Running with receiver-based approach

```
$ bigdatakit spark-streaming \
    bigdatakit-example-spark-streaming-1.0-SNAPSHOT.jar \
    -Dapproach=receiver-based \
    -Dtopics=raw.MobileWeb-zw-wapoplog \
    -Dprocessor=com.sogou.bigdatakit.example.streaming.EchoLineProcessor
```

### Example-2: Running with direct-based approach

```
$ bigdatakit spark-streaming \
    bigdatakit-example-spark-streaming-1.0-SNAPSHOT.jar \
    -Dapproach=direct-based \
    -Dtopics=raw.MobileWeb-zw-wapoplog \
    -Dprocessor=com.sogou.bigdatakit.example.streaming.EchoLineProcessor
```

## Configuration
| Option | Default | Meaning |
|:---|:---|:---|
| -Dmaster | local[*] | The Spark run mode, "local[*]" means run on local, "yarn-client" means run on hadoop cluster. |
| -Dname | bigdatakit-spark-streaming | The Spark application name. |
| -Dapproach | receiver-based | The Spark Streaming and Kafka integrate mode, "receiver-based" and "direct-based". Please refer to [Spark Streaming + Kafka Integration Guide](http://spark.apache.org/docs/latest/streaming-kafka-integration.html) |
| -DzkConnString | (cloud_dev managed) | The Kafka ZooKeeper connection to use, please contact administrator. |
| -Dtopics | (none) | The Kafka topics with comma seperated. |
| -DgroupId | default-$topics | The Kafka consumer group id. |
| -DbatchDuration | 10 | The Spark Streaming batch process duration |
| -DthreadNum | 1 | Kafka consumer thread num (for "receiver-based") |
| -DbrokerList | (cloud_dev managed) | The kafka broker list (for "direct-based") |
| -DoffsetsCommitBatchInterval | 6 | The Kafka offsets commit batch interval  (for "direct-based") |
| -Dspark.streaming.backpressure.enabled | false | Enables or disables Spark Streaming's internal backpressure mechanism (since 1.5). This enables the Spark Streaming to control the receiving rate based on the current batch scheduling delays and processing times so that the system receives only as fast as the system can process. Internally, this dynamically sets the maximum receiving rate of receivers. This rate is upper bounded by the values `spark.streaming.receiver.maxRate` and `spark.streaming.kafka.maxRatePerPartition` if they are set (see below). |
| -Dspark.streaming.receiver.maxRate | (none) | Maximum rate (number of records per second) at which each receiver will receive data. Effectively, each stream will consume at most this number of records per second. Setting this configuration to 0 or a negative number will put no limit on the rate. (for "receiver-based") |
| -Dspark.streaming.blockInterval | 200ms | Interval at which data received by Spark Streaming receivers is chunked into blocks of data before storing them in Spark. Minimum recommended - 50 ms. (for "receiver-based") |
| -Dspark.streaming.kafka.maxRatePerPartition | (none) | Maximum rate (number of records per second) at which data will be read from each Kafka partition when using the new Kafka direct stream API. (for "direct-based") |
| -Dspark.streaming.kafka.maxRetries | 1 | Maximum number of consecutive retries the driver will make in order to find the latest offsets on the leader of each partition (a default value of 1 means that the driver will make a maximum of 2 attempts). Only applies to the new Kafka direct stream API. (for "direct-based") |
| -Dspark.streaming.ui.retainedBatches | 1000 | 	How many batches the Spark Streaming UI and status APIs remember before garbage collecting. |

## About Spark Streaming

Please refer to the [streaming programming guide](http://spark.apache.org/docs/latest/streaming-programming-guide.html) for more informatioin

