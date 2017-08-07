# Temperature Web App Demonstrating rscala #

## Introduction ##

[rscala](https://github.com/dbdahl/rscala) provides a bi-directional interface
between R and Scala with callbacks.  This repository contains an example of
rscala in the context of a [Scalatra](http://scalatra.org/) (Scala based web
framework) to implement a historical weather application.

Most files were automatically generated from the
[Scalatra](http://scalatra.org/) skeleton.  The most relevant file for rscala
is
[src/main/scala/org/ddahl/scalatra/Servlet.scala](src/main/scala/org/ddahl/scalatra/Servlet.scala)
and the user interface is
[src/main/twirl/org/ddahl/scalatra/weather.scala.html](src/main/twirl/org/ddahl/scalatra/weather.scala.html).

## Build & Run ##

```sh
$ ./sbt run
```

Launch your web browse and open
[http://localhost:8080/temperature](http://localhost:8080/temperature).

