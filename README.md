# Temperature Web App Demonstrating rscala #

## Introduction ##

[rscala](https://github.com/dbdahl/rscala) provides a bi-directional interface
between R and Scala with callbacks.  This repository contains an example of
rscala in the context of a [Scalatra](http://scalatra.org/) (Scala based web
framework) to implement a weather application.

Most files were automatically generated from the
[Scalatra](http://scalatra.org/) skeleton.  The most relevant file for rscala
is
[src/main/scala/org/ddahl/scalatra/Servlet.scala](src/main/scala/org/ddahl/scalatra/Servlet.scala)
and the user interface is
[src/main/twirl/org/ddahl/scalatra/weather.scala.html](src/main/twirl/org/ddahl/scalatra/weather.scala.html).

## Build & Run ##

Install the following packages in R: <a
href="https://cran.r-project.org/package=httr">httr</a>, <a
href="https://cran.r-project.org/package=darksky">darksky</a>, <a
href="https://cran.r-project.org/package=ggplot2">ggplot2</a>, and <a
href="https://cran.r-project.org/package=rscala">rscala</a>.  Obtain a <a
href="https://developers.google.com/maps/documentation/geocoding/get-api-key">Google
Maps Geocoding API Key</a> and a <a href="https://darksky.net/dev">Dark Sky API
Key</a> and save them to "~/.googleMapsGeocodingAPI" and "~/.darkskyKey",
respectively.  Then, do:

```sh
$ ./sbt run
```

Launch your web browse and open
[http://localhost:8080/temperature](http://localhost:8080/temperature).

