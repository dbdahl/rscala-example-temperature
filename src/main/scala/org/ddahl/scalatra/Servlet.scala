package org.ddahl.scalatra

import org.scalatra._

case class TemperatureData(location: String, year: String,
                           minValue: String, minDate: String,
                           maxValue: String, maxDate: String)

class Servlet extends WebappTemperatureTrait {

  get("/temperature/?") {
    redirect("/temperature/SFO/1973/index.html")
  }

  get("/temperature/:location/:year/?") {
    redirect("/temperature/"+params("location")+"/"+params("year")+"/index.html")
  }

  get("/temperature/:location/:year/index.html") {
    val (loc,yr) = validate(params("location"),params("year"))
    val temperatureData = getTemperatureData(loc,yr)
    org.ddahl.scalatra.html.weather.render(temperatureData)
  }

  get("/temperature/:location/:year/plot.svg") {
    val (loc,yr) = validate(params("location"),params("year"))
    val file = new java.io.File(R.synchronized{R.evalS0(s"cache('$loc','$yr')[['filename']]")})
    contentType = "image/svg+xml"
    file
  }

  private val R = org.ddahl.rscala.RClient()

  R.synchronized{ R eval """
    library(weatherData)
    library(ggplot2)
    theme_set(theme_gray(base_size=18))

    .cache <- new.env()
    cache <- function(location,year) {
      key <- paste0(location,"-",year)
      if ( exists(key,envir=.cache) ) get(key,envir=.cache)
      else {
        d <- na.omit(getWeatherForYear(location,year))

        maxT <- d[which.max(d$Max_TemperatureF), c("Max_TemperatureF", "Date")]
        minT <- d[which.min(d$Min_TemperatureF), c("Min_TemperatureF", "Date")]
        
        p <- ggplot(d, aes(x=Date)) + coord_cartesian(ylim = c(-10, 110))
        p <- p + labs(x="",y=expression(paste("Temperature (",degree,"F)")))
        p <- p + geom_line(aes(y=Max_TemperatureF))
        p <- p + stat_smooth(se=FALSE,aes(y=Max_TemperatureF),method="loess",span=0.3)
        p <- p + geom_line(aes(y=Min_TemperatureF))
        p <- p + stat_smooth(se=FALSE,aes(y=Min_TemperatureF),method="loess",span=0.3)
        p <- p + geom_ribbon(aes(ymin=Min_TemperatureF, ymax=Max_TemperatureF), fill = "tomato", alpha = 0.4)

        fn <- paste0(tempdir(),.Platform$file.sep,key,".svg")
        svg(fn,width=6,height=4)
        print(p)
        dev.off()

        fmt <- "%B %e, %Y"
        result <- list(filename=fn, minValue=minT[1, 1], minDate=format(minT[1, 2], fmt),
                                    maxValue=maxT[1, 1], maxDate=format(maxT[1, 2], fmt))
        assign(key,result,envir=.cache)
        result
      }
    }
  """}

  private val errorTemperatureData = TemperatureData(null,null,"NA","NA","NA","NA")

  private def getTemperatureData(location: String, year: String) = {
    if ( location == null ) errorTemperatureData
    else try {
      val s1 = R.synchronized{R.evalS1(s"unlist(cache('$location','$year'))")}
      TemperatureData(location,year,s1(1),s1(2),s1(3),s1(4))
    } catch {
      case _: Throwable => errorTemperatureData
    }
  }

  private def validate(location: String, year: String): (String,String) = {
    if ( location.matches("\\w{3,4}") && year.matches("\\d{4}") ) (location.toUpperCase,year)
    else (null,null)
  }

}

