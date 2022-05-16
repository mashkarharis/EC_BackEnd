package com.eldercare.rest.Elder.CareWeb.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.eldercare.rest.Elder.CareWeb.Models.CareTaker;
import com.eldercare.rest.Elder.CareWeb.Models.Coordinate;
import com.eldercare.rest.Elder.CareWeb.Models.Elder;
import com.eldercare.rest.Elder.CareWeb.Models.Home;
import com.eldercare.rest.Elder.CareWeb.Services.DistanceService;
import com.eldercare.rest.Elder.CareWeb.Services.HomeService;
import com.eldercare.rest.Elder.CareWeb.Services.IoTService;
import com.eldercare.rest.Elder.CareWeb.Services.MobileService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;

@CrossOrigin
@RestController
public class IoTController {

	@Autowired
	HomeService homeService;
	@Autowired
	MobileService mobileTokenService;
	@Autowired
	IoTService ioTService;
	@Autowired
	DistanceService distanceService;

	@PostMapping("/iot/protected/ull")
	public void updateLatLon(@RequestBody Map<String, Object> payload) throws Exception {
		String mac = payload.get("mac").toString();
		if (mac.length() < 1) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		double lat = (double) payload.get("lat");
		double lon = (double) payload.get("lon");

		Coordinate coordinate = new Coordinate();
		coordinate.setMac(mac);
		coordinate.setLat(lat);
		coordinate.setLon(lon);

		ioTService.storeCoordinates(coordinate);
		System.out.println("HERE");
		List<Home> homes = homeService.getHomes();
		for (Home home : homes) {
			for (CareTaker ct : home.getCareTakers()) {
				for (Elder elder : ct.getElders()) {
					if (mac.equals(elder.getMac())) {
						double homelat = home.getLat();
						double homelon = home.getLon();
						if (outofRange(lat, lon, homelat, homelon, 1.00)) {
							ioTService.sendNotificationToMAc(mac, "OUT OF RANGE ALERT");
							return;
						}

					}
				}
			}
		}

	}

	private boolean outofRange(double lat, double lon, double homelat, double homelon, double range) {
		System.out.println(lat + "," + lon + " | " + homelat + "," + homelon);
		double kilometer = distanceService.distance(lat, lon, homelat, homelon, "K");
		System.out.println(kilometer + " - " + range);
		return kilometer > range;
	}

	@PostMapping("/iot/protected/sos")
	public String sosTrigger(@RequestBody Map<String, Object> payload) throws Exception {
		String mac = payload.get("mac").toString();
		return ioTService.sendNotificationToMAc(mac, "SOS ALERT");
	}

	@GetMapping("/iot/protected/getmacs")
	public List<String> getMACs() throws Exception {
		List<String> macs = new ArrayList<>();
		List<Coordinate> coors = ioTService.getCoordinates();
		for (Coordinate coordinate : coors) {
			macs.add(coordinate.getMac());
		}
		return macs;

	}
}
