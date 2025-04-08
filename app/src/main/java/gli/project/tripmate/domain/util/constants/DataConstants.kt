package gli.project.tripmate.domain.util.constants

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import gli.project.tripmate.domain.model.PlaceCategory

object DataConstants {
    val tabName = listOf(
        "About",
        "Gallery",
        "Review"
    )

    // list for main categories
    val placeCategories = listOf(
        PlaceCategory(name = "Hotels", categoryEndpoint = HOTEL, icon = Icons.Default.LocalHotel),
        PlaceCategory(name = "Restaurant", categoryEndpoint = RESTAURANT, icon = Icons.Default.Restaurant),
        PlaceCategory(name = "Mall", categoryEndpoint = MALL, icon = Icons.Default.LocalMall),
        PlaceCategory(name = "Cafe", categoryEndpoint = CAFE, icon = Icons.Default.LocalCafe),
    )

    // List for filter
    val placeFilterCategories = listOf(
        PlaceCategory("All", TOURISM, Icons.Default.Tour),
        PlaceCategory("Hotels", HOTEL, Icons.Default.LocalHotel),
        PlaceCategory("Huts", HUT, Icons.Default.Cabin),
        PlaceCategory("Apartments", APARTMENT, Icons.Default.Apartment),
        PlaceCategory("Chalets", CHALET, Icons.Default.House),
        PlaceCategory("Guest Houses", GUEST_HOUSE, Icons.Default.BedroomParent),
        PlaceCategory("Hostels", HOSTEL, Icons.Default.HouseSiding),
        PlaceCategory("Motels", MOTEL, Icons.Default.DirectionsCar),
        PlaceCategory("Resorts", RESORT, Icons.Default.BeachAccess),
        PlaceCategory("Villas", VILLA, Icons.Default.Home),

        // Community & Sports
        PlaceCategory("Community Centers", COMMUNITY_CENTER, Icons.Default.Groups),
        PlaceCategory("Sport Clubs", SPORT_CLUB, Icons.Default.Sports),

        // Airports & Airfields
        PlaceCategory("Private Airports", PRIVATE_AIRPORT, Icons.Default.Flight),
        PlaceCategory("International Airports", INTERNATIONAL_AIRPORT, Icons.Default.FlightTakeoff),
        PlaceCategory("Military Airports", MILITARY_AIRPORT, Icons.Default.MilitaryTech),
        PlaceCategory("Gliding Airports", GLIDING_AIRPORT, Icons.Default.AirplanemodeActive),
        PlaceCategory("General Airfields", GENERAL_AIRFIELD, Icons.Default.AirplanemodeInactive),

        // Shopping & Commercial
        PlaceCategory("Supermarkets", SUPERMARKET, Icons.Default.ShoppingCart),
        PlaceCategory("Shopping Malls", MALL, Icons.Default.StoreMallDirectory),
        PlaceCategory("Department Stores", DEPARTMENT_STORE, Icons.Default.Store),
        PlaceCategory("Electronics Stores", ELECTRONICS_STORE, Icons.Default.Devices),

        // Food & Restaurants
        PlaceCategory("Restaurants", RESTAURANT, Icons.Default.Restaurant),
        PlaceCategory("Fast Food", FAST_FOOD, Icons.Default.Fastfood),
        PlaceCategory("Cafés", CAFE, Icons.Default.LocalCafe),
        PlaceCategory("Food Courts", FOOD_COURT, Icons.Default.LunchDining),
        PlaceCategory("Bars", BAR, Icons.Default.LocalBar),
        PlaceCategory("Pubs", PUB, Icons.Default.SportsBar),

        // Education
        PlaceCategory("Schools", SCHOOL, Icons.Default.School),
        PlaceCategory("Colleges", COLLEGE, Icons.Default.AccountBalance),
        PlaceCategory("Universities", UNIVERSITY, Icons.Default.CastForEducation),
        PlaceCategory("Libraries", LIBRARY, Icons.Default.LocalLibrary),

        // Entertainment & Attractions
        PlaceCategory("Zoos", ZOO, Icons.Default.Pets),
        PlaceCategory("Aquariums", AQUARIUM, Icons.Default.Water),
        PlaceCategory("Museums", MUSEUM, Icons.Default.Museum),
        PlaceCategory("Cinemas", CINEMA, Icons.Default.Movie),
        PlaceCategory("Theme Parks", THEME_PARK, Icons.Default.FamilyRestroom),
        PlaceCategory("Water Parks", WATER_PARK, Icons.Default.Pool),

        // Healthcare
        PlaceCategory("Clinics", CLINIC, Icons.Default.LocalHospital),
        PlaceCategory("Hospitals", HOSPITAL, Icons.Default.MedicalServices),
        PlaceCategory("Pharmacies", PHARMACY, Icons.Default.LocalPharmacy),
        PlaceCategory("Dentists", DENTIST, Icons.Default.Medication),

        // Natural Features
        PlaceCategory("Forests", FOREST, Icons.Default.Forest),
        PlaceCategory("Lakes", LAKE, Icons.Default.Water),
        PlaceCategory("Rivers", RIVER, Icons.Default.Waves),
        PlaceCategory("Beaches", BEACH, Icons.Default.BeachAccess),
        PlaceCategory("Mountains", MOUNTAIN, Icons.Default.Terrain),

        // Office & Business
        PlaceCategory("Government Offices", GOVERNMENT_OFFICE, Icons.Default.AccountBalance),
        PlaceCategory("Law Firms", LAW_FIRM, Icons.Default.Gavel),
        PlaceCategory("Real Estate Agencies", REAL_ESTATE, Icons.Default.HomeWork),
        PlaceCategory("Insurance Companies", INSURANCE, Icons.Default.Security),
        PlaceCategory("Banks", "office.bank", Icons.Default.AccountBalanceWallet),

        // Parking
        PlaceCategory("Car Parking", CAR_PARKING, Icons.Default.LocalParking),
        PlaceCategory("Motorcycle Parking", MOTORCYCLE_PARKING, Icons.Default.TwoWheeler),
        PlaceCategory("Bicycle Parking", BICYCLE_PARKING, Icons.AutoMirrored.Filled.DirectionsBike),

        // Pet Services
        PlaceCategory("Pet Shops", PET_SHOP, Icons.Default.ShoppingBag),
        PlaceCategory("Veterinary Clinics", VETERINARY, Icons.Default.Healing),
        PlaceCategory("Dog Parks", DOG_PARK, Icons.Default.Pets),

        // Power & Energy
        PlaceCategory("Power Stations", POWER_STATION, Icons.Default.Power),
        PlaceCategory("Solar Power Plants", SOLAR_POWER, Icons.Default.LightMode),
        PlaceCategory("Wind Farms", WIND_FARM, Icons.Default.Air),

        // Transportation
        PlaceCategory("Train Stations", TRAIN_STATION, Icons.Default.Train),
        PlaceCategory("Bus Stations", BUS_STATION, Icons.Default.DirectionsBus),
        PlaceCategory("Metro Stations", METRO_STATION, Icons.Default.Subway),
        PlaceCategory("Taxi Stands", "transport.taxi_stand", Icons.Default.LocalTaxi),
        PlaceCategory("Ferry Terminals", "transport.ferry_terminal", Icons.Default.DirectionsBoat),

        // Vehicles & Fuel
        PlaceCategory("Car Rentals", CAR_RENTAL, Icons.Default.DirectionsCar),
        PlaceCategory("Gas Stations", GAS_STATION, Icons.Default.LocalGasStation),
        PlaceCategory("EV Charging Stations", EV_CHARGING, Icons.Default.EvStation),
        PlaceCategory("Car Wash", "vehicle.car_wash", Icons.Default.LocalCarWash),
        PlaceCategory("Mechanic Shops", "vehicle.mechanic", Icons.Default.Build)
    )
}