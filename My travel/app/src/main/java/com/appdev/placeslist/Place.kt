package com.appdev.placeslist

data class Place(
    var id: Int,
    var name: String,
    var address: String,
    var latitude: String?,
    var longitude: String?,
    var description: String,
    var image: String?
)


