package net.toulet.ward

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.gson.gson
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.http.content.*
import io.ktor.request.isMultipart
import io.ktor.request.receive
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.response.respondTextWriter
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import net.toulet.ward.Dao.SnippetDao
import net.toulet.ward.Dao.Snippets
import net.toulet.ward.Models.Error
import net.toulet.ward.Models.Snippet
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.Exception

fun main(args: Array<String>): Unit {
    initDB();
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        gson {}
    }
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    routing {
        static("/") {
            resources("/static/")
        }

        get("/") {
            call.respond(
                FreeMarkerContent(
                    "snippetList.ftl", mapOf(
                        "snippets" to
                                SnippetDao.getAllSnippets()
                    )
                )
            )
        }

        get("/snippet/{id}") {
            try {
                val snippet = call.parameters["id"]?.let { SnippetDao.getSnippet(UUID.fromString(it)) };
                call.respond(
                    FreeMarkerContent("snippet.ftl", mapOf("snippet" to snippet))
                )
            } catch(e: Exception) {
                call.respond(
                    FreeMarkerContent("error.ftl", mapOf("error" to Error.NOT_FOUND))
                )
            }
        }

        get("/new-snippet") {
            call.respond(FreeMarkerContent("snippetForm.ftl", null))
        }

        route("/api") {
            get("/snippet/{id}") {

            }
            get("/snippets") {
                call.respond(SnippetDao.getAllSnippets())
            }
            post("/snippet") {
                var snippet = call.receive<Snippet>()
                SnippetDao.insertSnippet(snippet)
                call.respond(snippet)
            }
        }
    }
}


fun initDB() {
    val config = HikariConfig("/hikari.properties")
    Database.connect(HikariDataSource(config))
    transaction {
        create(Snippets)
    }
}