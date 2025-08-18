package com.example

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class MySimulation extends Simulation {

  // 1. Configuração do Protocolo HTTP
  val httpProtocol = http
    .baseUrl("http://localhost")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  // 2. Definição dos Cenários
  val scnApp1 = scenario("Usuários da App 1")
    .exec(
      http("request_app1")
        .get("/app1/")
        .check(status.is(200))
    )

  val scnApp2 = scenario("Usuários da App 2")
    .exec(
      http("request_app2")
        .get("/app2/")
        .check(status.is(200))
    )

  val scnJavaApp = scenario("Usuários da App Java")
    .during(30.seconds) {
      exec(
        http("request_java_app")
          .get("/java-app/")
          .check(status.is(200))
      ).pause(1.second)
    }

  // 3. Configuração da Carga (Setup)
  setUp(
    scnApp1.inject(atOnceUsers(10)),
    scnApp2.inject(atOnceUsers(10)),
    scnJavaApp.inject(rampUsers(50).during(10.seconds))
  ).protocols(httpProtocol)
}