## spring-boot-jmh-microbechmark-performance

Purpose : Benchmark spring boot service layer methods by using jhm Java Microbenchmark Harness. <br/>
Result : Get detailed performance information about execution time of a spring service component method. <br/>

Steps to follow for creating jmh Java Microbenchmark Harness inside a spring boot junit test method. <br/>

1- Add jmh artifacts into your pom.xml file. <br/>
2- Create a new benchmark class for your service layer spring component inside test directory. <br/>
3- Add target spring service component and spring context into benchmark class. <br/>
4- Initialize spring context and get target service component. <br/>
5- Refer to your benchmark class from JUnit test method. <br/>
4- Run Junit method with @Test annotation to start benchmarking. <br/>
<br/>
### Local run steps <br/>
1- To execute jmh run the following maven command : <br/>
NOT : Execute maven command from where the pom.xml is located in the project directory. <br/>
<pre> 
$ mvn clean install <br/>
</pre>

Result "com.company.customerinfo.benchmark.impl.CustomerOrderServiceBenchMark.saveCustomerWithOrdersBenchMark": <br/>
       4097.767 ±(99.9%) 25662.331 us/op [Average] <br/>
       (min, avg, max) = (2805.100, 4097.767, 5595.800), stdev = 1406.639 <br/>
       CI (99.9%): [≈ 0, 29760.098] (assumes normal distribution) <br/>

Run complete. Total time: 00:00:12 <br/>

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on <br/>
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial <br/>
experiments, perform baseline and negative tests that provide experimental control, make sure <br/>
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts. <br/>
Do not assume the numbers tell you what you want them to tell. <br/>

Benchmark&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;  Mode Cnt Score Error Units <br/>
CustomerOrderServiceBenchMark.saveCustomerWithOrdersBenchMark  avgt    3  4097.767 ± 25662.331  us/op <br/>

Benchmark result is saved to target/jmh-result-04-26-2021-05-04-06.json <br/>

![Java Microbenchmark Harness](doc/jmh_result.png) <br/>

### Tech Stack
Java 11 <br/>
H2 Database Engine <br/>
spring boot <br/>
spring boot starter data jpa <br/>
spring boot starter web <br/>
spring boot starter test <br/>
hibernate <br/>
logback <br/>
maven <br/>
jmh-Java Microbenchmark Harness <br/>
springfox-swagger-ui <br/>
datasource-proxy <br/>
Docker <br/>
<br/>

### Docker build run steps
NOT : Execute docker commands from where the DockerFile is located. <br/>
<pre>
$ docker system prune <br/>
$ docker build . --tag demo  <br/>
$ docker run -p 8080:8080 -e "SPRING_PROFILES_ACTIVE=dev" demo:latest <br/>
</pre>

## API OPERATIONS
### Save store with products successfully to database

Method : HTTP.POST <br/>
URL : http://localhost:8080/customer-info/store/save <br/>

Request : 
<pre>
curl --location --request POST 'http://localhost:8080/customer-info/store/save' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "jeans_store",
  "products": [
    {
      "name": "prod1"
    },
    {
      "name": "prod2"
    },
    {
      "name": "prod3"
    }
  ]
}'
</pre><br/>

Response : 

HTTP response code 200 <br/>
<pre>
{
    "id": 1,
    "name": "jeans_store",
    "products": [
        {
            "id": 1,
            "name": "prod3"
        },
        {
            "id": 2,
            "name": "prod1"
        },
        {
            "id": 3,
            "name": "prod2"
        }
    ]
}
</pre>


### List Store saved to database

Method : HTTP.GET <br/>
URL : http://localhost:8080/customer-info/store/list <br/>

Request : 
<pre>
curl --location --request GET 'http://localhost:8080/customer-info/store/list'
</pre><br/>

Response : 

HTTP response code 200 <br/>
<pre>
[
    {
        "id": 1,
        "name": "jeans_store",
        "products": [
            {
                "id": 1,
                "name": "prod3"
            },
            {
                "id": 2,
                "name": "prod1"
            },
            {
                "id": 3,
                "name": "prod2"
            }
        ]
    }
]
</pre><br/>
