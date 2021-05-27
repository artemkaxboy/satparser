package com.artemkaxboy.satparser.entity

import java.time.LocalDateTime
import javax.persistence.*

private const val TAGS_DELIMITER = ";"

@Entity
@Table(name = "`!_satellites`")
data class PhpSatelliteEntity(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    val id: Int = -1,

    @Column(name = "_name", unique = true, columnDefinition = "varchar(100)")
    val name: String,

    @Column(name = "_position")
    val position: Double,

    @Column(name = "_tags", columnDefinition = "text")
    val tags: String,

    @Column(name = "_url", columnDefinition = "tinytext")
    val link: String,

    @Column(name = "_band", columnDefinition = "tinytext")
    val band: String,

    @Column(name = "_added", columnDefinition = "timestamp default current_timestamp")
    val added: LocalDateTime = LocalDateTime.now(),

    @Column(name = "_closed", columnDefinition = "timestamp")
    val closed: LocalDateTime? = null,

    @Column(name = "_status", columnDefinition = "integer default 1")
    val status: Int = 1,
) {

    private fun getTagsAsList() = tags.split(TAGS_DELIMITER).filter { it.isNotEmpty() }

    fun isNeedUpdateFrom(saved: PhpSatelliteEntity?): Boolean? {

        if (saved == null) return null
        if (name != saved.name) throw IllegalArgumentException("Satellite name cannot be updated")
        if (position != saved.position) return true
        if (link != saved.link) return true
        if (band != saved.band) return true
        if (status != saved.status) return true
        if (hasNewTags(saved)) return true

        return false
    }

    private fun hasNewTags(saved: PhpSatelliteEntity): Boolean {
        return getTagsAsList().minus(saved.getTagsAsList()).isNotEmpty()
    }

    private fun mergeTags(other: PhpSatelliteEntity?): String {

        return getTagsAsList().plus(other?.getTagsAsList() ?: emptyList())
            .distinct()
            .let { makeTagsString(it) }
    }

    fun mergeWithExisting(existing: PhpSatelliteEntity): PhpSatelliteEntity {

        return copy(id = existing.id, tags = mergeTags(existing))
    }

    fun close(): PhpSatelliteEntity {

        return copy(closed = LocalDateTime.now(), status = Status.CLOSED.value)
    }

    @Suppress("DuplicatedCode")
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PhpSatelliteEntity

        if (id != other.id) return false
        if (name != other.name) return false
        if (position != other.position) return false
        if (tags != other.tags) return false
        if (link != other.link) return false
        if (band != other.band) return false
        if (added != other.added) return false
        if (closed != other.closed) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + position.hashCode()
        result = 31 * result + tags.hashCode()
        result = 31 * result + link.hashCode()
        result = 31 * result + band.hashCode()
        result = 31 * result + added.hashCode()
        result = 31 * result + (closed?.hashCode() ?: 0)
        result = 31 * result + status
        return result
    }

    enum class Status(val value: Int) {
        CLOSED(0),
        OPENED(1),
    }
}

private fun makeTagsString(tags: Collection<String>): String = tags.joinToString(TAGS_DELIMITER)
