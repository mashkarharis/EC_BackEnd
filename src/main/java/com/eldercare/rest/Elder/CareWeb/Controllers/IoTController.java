package com.eldercare.rest.Elder.CareWeb.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.eldercare.rest.Elder.CareWeb.Models.Coordinate;
import com.eldercare.rest.Elder.CareWeb.Services.HomeService;
import com.eldercare.rest.Elder.CareWeb.Services.IoTService;
import com.eldercare.rest.Elder.CareWeb.Services.MobileService;

@CrossOrigin
@RestController
public class IoTController {

	
	@Autowired
	HomeService homeService;
	@Autowired
	MobileService mobileTokenService;
	@Autowired
	IoTService ioTService;
	
	@PostMapping("/iot/protected/ull")
	public void updateLatLon(@RequestBody Map<String, Object> payload) throws Exception {
		String mac = payload.get("mac").toString();
		if (mac.length()<1) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		double lat = (double) payload.get("lat");
		double lon = (double) payload.get("lon");
		
		Coordinate coordinate=new Coordinate();
		coordinate.setMac(mac);
		coordinate.setLat(lat);
		coordinate.setLon(lon);
		
		ioTService.storeCoordinates(coordinate);
		
		// TODO: ADD NOTIFICATION SENDING
		
	}
	
	@PostMapping("/iot/protected/sos")
	public void sosTrigger(@RequestBody Map<String, Object> payload) throws Exception {
		String mac = payload.get("mac").toString();
		
		
		// TODO: ADD NOTIFICATION SENDING
		
	}
}
