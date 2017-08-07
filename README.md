# Temperature Web App Demonstrating rscala #

## Introduction ##

[rscala](https://github.com/dbdahl/rscala) provides a bi-directional interface
between R and Scala with callbacks.  This repository contains an example of
rscala in the context of a Scalatra (Scala based web framework) to implement a
historical weather application.

Most files were automatically generated from the Scalatra skeleton.  The most
relevant files for rscala are
[src/main/scala/org/ddahl/scalatra/WebappTemperatureTrait.scala] and
[src/main/twirl/org/ddahl/scalatra/weather.scala.html].

## Build & Run ##

```sh
$ ./sbt run
```

Launch your web browse and open [http://localhost:8080/temperature].

