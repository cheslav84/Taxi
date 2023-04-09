// let map, infoWindow;

function initMap() {
  const directionsService = new google.maps.DirectionsService();
  const directionsRenderer = new google.maps.DirectionsRenderer();


  map = new google.maps.Map(document.getElementById("map"), {
    center: { lat: 50.45, lng: 30.52 },
    zoom: 11,
  });

  const geocoder = new google.maps.Geocoder();
  const locationInfowindow = new google.maps.InfoWindow();
  const infowindow = new google.maps.InfoWindow();
  let originAddr = document.getElementById("originAddr").textContent;
  let destinationAddr = document.getElementById("destinationAddr").textContent;

  directionsRenderer.setMap(map);

  // const locationButton = document.createElement("button");
  // locationButton.textContent = "Pan to Current Location";
  // locationButton.classList.add("custom-map-control-button");
  // map.controls[google.maps.ControlPosition.TOP_CENTER].push(locationButton);

  // Try HTML5 geolocation.
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        const pos = {
          lat: position.coords.latitude,
          lng: position.coords.longitude,
        };

        geocoder
          .geocode({ location: pos })
          .then((response) => {
            if (response.results[0]) {
              map.setZoom(11);

              const marker = new google.maps.Marker({
                position: pos,
                map: map,
              });

              infowindow.setContent(response.results[0].formatted_address);
              infowindow.open(map, marker);
              let taxiAddr = response.results[0].formatted_address;

              calculateAndDisplayRouteWithTaxiAddr(directionsService, directionsRenderer, taxiAddr, originAddr, destinationAddr);
              document.getElementById('taxiAddr').value = taxiAddr;
            } else {
              window.alert("No results found");
            }
          })
          .catch((e) => window.alert("Geocoder failed due to: " + e));
      },
      () => {
        calculateAndDisplayRoute(directionsService, directionsRenderer, originAddr, destinationAddr);
        handleLocationError(true, locationInfowindow, map.getCenter());
      }
    );
  } else {
    // Browser doesn't support Geolocation
    calculateAndDisplayRoute(directionsService, directionsRenderer, originAddr, destinationAddr);
    handleLocationError(false, locationInfowindow, map.getCenter());
  }

  // locationButton.addEventListener("click", () => {
    // map.setCenter(pos);
  // });
  // });
}

function handleLocationError(browserHasGeolocation, locationInfowindow, pos) {
  locationInfowindow.setPosition(pos);
  locationInfowindow.setContent(
    browserHasGeolocation
      ? "Error: The Geolocation service failed."
      : "Error: Your browser doesn't support geolocation."
  );
  locationInfowindow.open(map);
}

window.initMap = initMap;



function calculateAndDisplayRoute(directionsService, directionsRenderer, originAddr, destinationAddr) {
  alert("calculateAndDisplayRoute");
  directionsService
    .route({
      origin: {
        query: originAddr,
      },
      destination: {
        query: destinationAddr,
      },
      travelMode: google.maps.TravelMode.DRIVING,
    })
    .then((response) => {
      directionsRenderer.setDirections(response);
    })
    .catch((e) => window.alert("Directions request failed due to " + status));
}


function calculateAndDisplayRouteWithTaxiAddr(directionsService, directionsRenderer, taxiAddr, originAddr, destinationAddr) {
  const waypts = [];

  waypts.push({
    location: originAddr,
    stopover: true,
  });

  directionsService
    .route({
      origin: {
        query: taxiAddr,
      },
      destination: {
        query: destinationAddr,
      },
      waypoints: waypts,
      travelMode: google.maps.TravelMode.DRIVING,
    })
    .then((response) => {
      directionsRenderer.setDirections(response);
      setDistanceFromTaxiToPassenger(response);
    })
    .catch((e) => window.alert("Directions request failed due to " + status));
}


function setDistanceFromTaxiToPassenger(response) {
  var distance = response.routes[0].legs[0].distance.text;
  document.getElementById("total").innerHTML = distance;

}


window.initMap = initMap;
