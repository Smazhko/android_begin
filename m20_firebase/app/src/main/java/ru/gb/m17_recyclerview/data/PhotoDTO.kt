package ru.gb.m17_recyclerview.data

import com.google.gson.annotations.SerializedName

data class ListPhotoDTO (
    val photos: List<PhotoDTO>
)

data class PhotoDTO (
    val id: Int,
    val sol: Int,
    val camera: RoverCamera,
    @SerializedName("img_src")
    val imgSrcHttp: String,
    @SerializedName("earth_date")
    val earthDate: String,
    val rover: Rover
) {
    val imgSrcHttps
        get() = imgSrcHttp.replace("http://", "https://")
}

data class RoverCamera(
    val id: Int,
    val name: String,
    @SerializedName("rover_id")
    val roverId: Int,
    @SerializedName("full_name")
    val fillName: String
)

data class Rover(
    val id: Int,
    val name: String,
    val status: String
)
/*
{
  "photos": [
    {
      "id": 912766,
      "sol": 3344,
      "camera": {
        "id": 20,
        "name": "FHAZ",
        "rover_id": 5,
        "full_name": "Front Hazard Avoidance Camera"
      },
      "img_src": "https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/03344/opgs/edr/fcam/FLB_694368899EDR_F0921230FHAZ00337M_.JPG",
      "earth_date": "2022-01-01",
      "rover": {
        "id": 5,
        "name": "Curiosity",
        "landing_date": "2012-08-06",
        "launch_date": "2011-11-26",
        "status": "active",
        "max_sol": 4102,
        "max_date": "2024-02-19",
        "total_photos": 695670,
        "cameras": [
          {
            "name": "FHAZ",
            "full_name": "Front Hazard Avoidance Camera"
          },
          {
            "name": "NAVCAM",
            "full_name": "Navigation Camera"
          },
          {
            "name": "MAST",
            "full_name": "Mast Camera"
          },
          {
            "name": "CHEMCAM",
            "full_name": "Chemistry and Camera Complex"
          },
          {
            "name": "MAHLI",
            "full_name": "Mars Hand Lens Imager"
          },
          {
            "name": "MARDI",
            "full_name": "Mars Descent Imager"
          },
          {
            "name": "RHAZ",
            "full_name": "Rear Hazard Avoidance Camera"
          }
        ]
      }
    }
  ]
}
 */