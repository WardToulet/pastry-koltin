package net.toulet.ward

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.html.*
import kotlinx.html.*
import kotlinx.css.*
import freemarker.cache.*
import io.ktor.freemarker.*
import io.ktor.content.*
import io.ktor.http.content.*
import io.ktor.sessions.*
import io.ktor.features.*
import io.ktor.auth.*
import kotlin.test.*
import io.ktor.server.testing.*

class ApplicationTest {
//    @Test
//    fun testRoot() {
//        withTestApplication({ module(testing = true) }) {
//            handleRequest(HttpMethod.Get, "/").apply {
//                assertEquals(HttpStatusCode.OK, response.status())
//                assertEquals("HELLO WORLD!", response.content)
//            }
//        }
//    }
}
