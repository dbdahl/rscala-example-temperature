package org.ddahl.scalatra

import org.scalatra._

case class TemperatureData(place: String, label: String, filename: String)

class Servlet extends WebappTemperatureTrait {

  get("/") {
    redirect("/Provo,%20UT/index.html")
  }

  get("/:place/plot.svg") {
    val place = params("place")
    val file = new java.io.File(R.evalS0(s"cache('$place')[['filename']]"))
    contentType = "image/svg+xml"
    file
  }

  get("/:place/index.html") {
    val place = params("place")
    val temperatureData = try {
      val s1 = R.invokeS1("cache",place)
      TemperatureData(place,s1(0),s1(1))
    } catch {
      case e: Throwable =>
        e.printStackTrace()
        TemperatureData(null,null,null)
    }
    org.ddahl.scalatra.html.weather.render(temperatureData)
  }

  private val R = org.ddahl.rscala.RClient()

  R eval """
    library(httr)
    googleMapsKey <- readLines("~/.googleMapsGeocodingAPI")

    library(darksky)
    darkSkyKey <- readLines("~/.darkskyKey")
    Sys.setenv(DARKSKY_API_KEY=darkSkyKey)

    library(ggplot2)
    theme_set(theme_gray(base_size=18))

    .cache <- new.env()
    cache <- function(place) {
      key <- paste0(toString(Sys.Date()),place)
      if ( exists(key,envir=.cache) ) get(key,envir=.cache)
      else {
        x <- GET(paste0("https://maps.googleapis.com/maps/api/geocode/json?address=",URLencode(place),"&key=",googleMapsKey))
        if ( http_status(x)$category != "Success" ) stop(paste0("HTTP status is :",x$status_code))
        con <- content(x)$results
        if ( length(con) == 0 ) stop(paste0("Nothing returned."))
        if ( length(con) > 1 )  stop(paste0("Too many returned."))
        xx <- con[[1]]
        label     <- xx$formatted_address
        longitude <- xx$geometry$location$lng
        latitude  <- xx$geometry$location$lat
        now <- get_current_forecast(latitude, longitude)
        d <- data.frame(
          Date=as.Date(now$daily$time),
          Min_TemperatureF=now$daily$apparentTemperatureLow,
          Max_TemperatureF=now$daily$apparentTemperatureHigh
        )

        p <- ggplot(d, aes(x=Date)) + coord_cartesian()  # ylim = c(-10, 110))
        p <- p + labs(x="",y=expression(paste("Temperature (",degree,"F)")))
        p <- p + geom_line(aes(y=Max_TemperatureF))
        p <- p + geom_line(aes(y=Min_TemperatureF))
        p <- p + geom_ribbon(aes(ymin=Min_TemperatureF, ymax=Max_TemperatureF), fill = "tomato", alpha = 0.4)

        fn <- paste0(tempfile(),".svg")
        svg(fn,width=6,height=4)
        print(p)
        dev.off()

        result <- c(label=label, filename=fn)
        assign(key,result,envir=.cache)
        result
      }
    }
  """

}
