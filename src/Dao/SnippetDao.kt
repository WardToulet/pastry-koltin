package net.toulet.ward.Dao

import net.toulet.ward.Dao.Snippets.content
import net.toulet.ward.Dao.Snippets.extension
import net.toulet.ward.Dao.Snippets.id
import net.toulet.ward.Dao.Snippets.name
import net.toulet.ward.Models.Snippet
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object Snippets : Table() {
    val id = uuid("id").primaryKey()
    val name = varchar("name", 255)
    val content = varchar("content", 4096)
    val extension = varchar("extension", 16).nullable()
}

object SnippetDao {
    fun insertSnippet(snippet: Snippet) {
        transaction {
            if (snippet.id != null) {
                Snippets.update {
                    throw IllegalArgumentException()
                }
            } else {
                Snippets.insert {
                    it[id] = UUID.randomUUID()
                    it[name] = snippet.name
                    it[content] = snippet.content
                    it[extension] = snippet.extention
                }
            }
        }
    }

    fun getAllSnippets(): List<Snippet> {
        return transaction {
            Snippets.selectAll().map() {
                Snippet(
                    id = it[id].toString(),
                    name = it[name],
                    content = it[content],
                    extention = it[extension]
                )
            }
        }
    }

    fun getSnippet(id: UUID): Snippet {
        return transaction {
            Snippets.select {
                Snippets.id eq id
            }.map {
                Snippet(
                    id = it[Snippets.id].toString(),
                    name = it[name],
                    content = it[content],
                    extention = it[extension]
                )
            }.first()
        }
    }
}
