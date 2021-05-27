package com.artemkaxboy.satparser.entity

import org.hibernate.annotations.Immutable
import java.io.Serializable
import java.time.LocalDate
import javax.persistence.*

@Suppress("unused") // IdClass for PackEntity
class PackEntityId(
    @Column(name = "satellite_name_fk")
    val satelliteName: String = "",

    @Column(name = "pack_name")
    val packName: String = "",
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PackEntityId

        if (satelliteName != other.satelliteName) return false
        if (packName != other.packName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = satelliteName.hashCode()
        result = 31 * result + packName.hashCode()
        return result
    }
}

//@Entity
@Immutable
@Table(name = "pack")
@IdClass(PackEntityId::class)
data class PackEntity(

    @ManyToOne
    @JoinColumn(name = "satellite_name_fk", nullable = false, updatable = false, insertable = false)
    val satellite: SatelliteEntity? = null,

    @Id
    @Column(name = "satellite_name_fk")
    val satelliteName: String,

    @Id
    @Column(name = "pack_name")
    val packName: String,

    @Column(name = "pack_url")
    val packUrl: String,

    val updated: LocalDate

)
