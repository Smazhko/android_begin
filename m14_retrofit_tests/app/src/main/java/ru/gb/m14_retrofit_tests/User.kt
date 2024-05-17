package ru.gb.m14_retrofit_tests

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

data class UserResponse(
    val results: List<User>
)

data class User(
    val gender: String,
    val name: Name,
    val location: Location,
    val email: String,
    val login: Login,
    val dob: Dob,
    val registered: Registered,
    val phone: String,
    val cell: String,
    val id: Id,
    val picture: Picture,
    val pictureComposite: String = picture.medium,
    val nat: String
) {
    val locationComposite: String
        get() = "Street: ${location.street.number}, ${location.street.name}\nCity: ${location.city}\n" +
                "State: ${location.state}\nCountry: ${location.country}\nPostcode: ${location.postcode}\n" +
                "Coordinates: ${location.coordinates.latitude}, ${location.coordinates.longitude}\n" +
                "Timezone: (UTC ${location.timezone.offset}) ${location.timezone.description}"

    val nameComposite: String
        get() = "${name.title} ${name.first} ${name.last}"

    val dobComposite: String
        get() = "${dob.date.take(10)} (${dob.age} years)"

}

data class Name(val title: String,
                val first: String,
                val last: String)

data class Location(
    val street: Street,
    val city: String,
    val state: String,
    val country: String,
    val postcode: Int,
    val coordinates: Coordinates,
    val timezone: Timezone
)

data class Street(val number: Int,
                  val name: String)

data class Coordinates(val latitude: String,
                       val longitude: String)

data class Timezone(val offset: String,
                    val description: String)

data class Login(val uuid: String,
                 val username: String,
                 val password: String,
                 val salt: String,
                 val md5: String,
                 val sha1: String,
                 val sha256: String)

data class Dob(val date: String,
               val age: Int)

data class Registered(val date: String,
                      val age: Int)

data class Id(val name: String,
              val value: String)

data class Picture(val large: String,
                   val medium: String,
                   val thumbnail: String)
data class Info(
    val seed: String?,
    val results: Int?,
    val page: Int?,
    val version: String?
)