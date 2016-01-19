# Build Hive Data Warehouse with BigdataKit

---

## Type-1: Load Data By ETL Job

### 1. Create Table

#### a. write table spec file

```
$ cat mytb.conf
table = mytb
fields = [
  { name: channel, type: string }
  { name: pv, type: long }
]
```

#### b. create table

```
$ bigdatakit create-table mytb.conf
```

### 2. Build ETL Process

#### a. generate project

```
$ bigdatakit generate-project mytb-etl
```

#### b. extend ETLProcessor class

```
package com.sogou.bigdatakit.example.hive.etl

class ExampleETLProcessor extends ETLProcessor {
  override def doETL(sqlContext: HiveContext, database: String, table: String, logdate: String): DataFrame = {
    sqlContext.sql(s"select channel, count(*) as pv from custom.common_pc_pv where logdate='{$logdate}' group by channel")
  }
}
```

#### c. make docker image

```
$ make docker-push
```

#### d. docker run command (timely scheduled by DTE)
```
$ docker run --rm --net=host -v /root/ugi_config:/root/ugi_config \
    registry.docker.dev.sogou-inc.com:5000/bigdatakitapp/mytb-etl:1.0 bigdatakit etl \
    -Dtable=mytb -Dprocessor=com.sogou.bigdatakit.example.hive.etl.ExampleETLProcessor \
    mytb-etl-1.0.jar <logdate>
```

---

## Type-2: Add Serde On Existing Raw Log (no data copy)

### 1. Build Serde Package

#### a. generate project

```
$ bigdatakit generate-project mytb2-serde
```

#### b. extend TextDeserializer class

```
package com.sogou.bigdatakit.example.hive.serde;

public class ExampleDeserializer extends TextDeserializer {
  @Override
  public List<Object> deserialize(String line, List<Object> reuse) {
    String[] array = line.split(" ");
    reuse.add(0, array[0]);
    reuse.add(1, Integer.parseInt(array[1]));
    return reuse;
  }
}
```

#### c. build serde package

```
$ make
```

#### d. publish serde package

```
$ bigdatakit publish-package com.sogou.bigdatakit.app:mytb2-serde:1.0 target/mytb2-serde-1.0.jar
```

### 2. Create Table

#### a. write table spec file

```
$ cat mytb2.conf
table = mytb2
fields = [
  { name: f1, type: string }
  { name: f2, type: int }
]
serde: com.sogou.bigdatakit.example.hive.serde.ExampleDeserializer
```

#### b. create table

```
$ bigdatakit create-table mytb2.conf -Dpackages=com.sogou.bigdatakit.app:mytb2-serde:1.0
```

### 3. auto timely add partition by DTE