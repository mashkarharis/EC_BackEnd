package com.eldercare.rest.Elder.CareWeb.Controllers;

import java.io.ObjectOutputStream.PutField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.eldercare.rest.Elder.CareWeb.Models.Token;
import com.eldercare.rest.Elder.CareWeb.Services.HomeService;
import com.eldercare.rest.Elder.CareWeb.Services.IoTService;
import com.eldercare.rest.Elder.CareWeb.Services.MobileService;

@CrossOrigin
@RestController
public class MobileController {

	@Autowired
	HomeService homeService;
	@Autowired
	MobileService mobileTokenService;
	@Autowired
	IoTService ioTService;

	// **********
	// AUTH
	// **********

	@PostMapping("/mobile/protected/login")
	public String login(@RequestBody Map<String, Object> payload) {
		String email = payload.get("email").toString();
		String hashedpassword = payload.get("hashedpassword").toString();
		System.out.println(email + "-" + hashedpassword);

		try {
			List<Home> homes = homeService.getHomes();
			CareTaker loggedUser = null;

			for (Home home : homes) {
				for (CareTaker ct : home.getCareTakers()) {
					if (email.equals(ct.getEmail()) && hashedpassword.equals(ct.getPassword())) {
						loggedUser = ct;
						break;
					}
				}
			}

			if (loggedUser == null) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
			} else {
				String token = mobileTokenService.storeToken(email);
				System.out.println(token);
				return token;
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/mobile/protected/introspect")
	public boolean introspect(@RequestBody Map<String, Object> payload) throws Exception {
		String token = payload.get("token").toString();
		return mobileTokenService.introspect(token);
	}

	@PostMapping("/mobile/private/listelders")
	public List<Elder> listElders(@RequestBody Map<String, Object> payload) throws Exception {
		String token = payload.get("token").toString();
		System.out.println(token);
		List<Token> tokelist = mobileTokenService.getTokens();
		System.out.println(tokelist.toString());
		String email = null;
		for (Token storedToken : tokelist) {
			if (token.equals(storedToken.getToken())) {
				email = storedToken.getEmail();
				break;
			}
		}
		if (email == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		System.out.println(email);
		List<Home> homes = homeService.getHomes();
		for (Home home : homes) {
			for (CareTaker ct : home.getCareTakers()) {
				if (ct.getEmail().equals(email)) {
					return ct.getElders();
				}
			}
		}

		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

	}

	@PostMapping("/mobile/private/editelder")
	public void editElders(@RequestBody Map<String, Object> payload) throws Exception {

		String token = payload.get("token").toString();
		System.out.println(token);

		Elder eldertoput = new Elder();
		eldertoput.setName(payload.get("name").toString());
		eldertoput.setNic(payload.get("nic").toString());
		eldertoput.setImage(payload.get("image").toString());
		eldertoput.setAddress(payload.get("address").toString());
		eldertoput.setMac(payload.get("mac").toString());
		eldertoput.setPhone(payload.get("phone").toString());

		List<Token> tokelist = mobileTokenService.getTokens();
		System.out.println(tokelist.toString());
		String email = null;
		for (Token storedToken : tokelist) {
			if (token.equals(storedToken.getToken())) {
				email = storedToken.getEmail();
				break;
			}
		}
		if (email == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		System.out.println(email);
		List<Home> homes = homeService.getHomes();

		List<Elder> finalelders = null;
		Home finalhome = null;
		for (Home home : homes) {
			for (CareTaker ct : home.getCareTakers()) {
				if (ct.getEmail().equals(email)) {
					finalelders = ct.getElders();
					finalhome = home;
					break;
				}
			}
		}

		if (finalelders == null || finalhome == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		Elder existing = null;
		for (Elder elder : finalelders) {
			if (elder.getNic().equals(eldertoput.getNic())) {
				existing = elder;
				break;
			}
		}
		if (existing != null) {
			finalelders.remove(existing);
		}
		finalelders.add(eldertoput);
		homeService.addHome(finalhome);
		return;
	}

	@PostMapping("/mobile/private/addelder")
	public void addElders(@RequestBody Map<String, Object> payload) throws Exception {

		String token = payload.get("token").toString();
		System.out.println(token);

		Elder eldertoput = new Elder();
		eldertoput.setName(payload.get("name").toString());
		eldertoput.setNic(payload.get("nic").toString());
		eldertoput.setImage(payload.get("image").toString());
		eldertoput.setAddress(payload.get("address").toString());
		eldertoput.setMac(payload.get("mac").toString());
		eldertoput.setPhone(payload.get("phone").toString());

		List<Token> tokelist = mobileTokenService.getTokens();
		System.out.println(tokelist.toString());
		String email = null;
		for (Token storedToken : tokelist) {
			if (token.equals(storedToken.getToken())) {
				email = storedToken.getEmail();
				break;
			}
		}
		if (email == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		System.out.println(email);
		List<Home> homes = homeService.getHomes();

		List<Elder> finalelders = null;
		Home finalhome = null;
		for (Home home : homes) {
			for (CareTaker ct : home.getCareTakers()) {
				if (ct.getEmail().equals(email)) {
					finalelders = ct.getElders();
					finalhome = home;
					break;
				}
			}
		}

		if (finalelders == null || finalhome == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		for (Home home1 : homes) {
			for (CareTaker existingct : home1.getCareTakers()) {
				for (Elder exisitngelder : existingct.getElders()) {
					if (eldertoput.getNic().equals(exisitngelder.getNic())) {
						System.out.println(eldertoput.getNic());
						System.out.println(exisitngelder.getNic());
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
					}
				}

			}
		}

		finalelders.add(eldertoput);
		homeService.addHome(finalhome);
		return;
	}
	
	@PostMapping("/mobile/private/deleteelder")
	public void deleteElders(@RequestBody Map<String, Object> payload) throws Exception {

		String token = payload.get("token").toString();
		System.out.println(token);

		String nic=payload.get("nic").toString();
		System.out.println(nic);
		

		List<Token> tokelist = mobileTokenService.getTokens();
		System.out.println(tokelist.toString());
		String email = null;
		for (Token storedToken : tokelist) {
			if (token.equals(storedToken.getToken())) {
				email = storedToken.getEmail();
				break;
			}
		}
		if (email == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		System.out.println(email);
		List<Home> homes = homeService.getHomes();

		List<Elder> finalelders = null;
		Home finalhome = null;
		for (Home home : homes) {
			for (CareTaker ct : home.getCareTakers()) {
				if (ct.getEmail().equals(email)) {
					finalelders = ct.getElders();
					finalhome = home;
					break;
				}
			}
		}

		if (finalelders == null || finalhome == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		Elder existing = null;
		for (Elder elder : finalelders) {
			if (elder.getNic().equals(nic)) {
				existing = elder;
				break;
			}
		}
		if (existing != null) {
			finalelders.remove(existing);
		}
		homeService.addHome(finalhome);
		return;
	}
	
	
	@PostMapping("/mobile/private/me")
	public CareTaker myAccount(@RequestBody Map<String, Object> payload) throws Exception {

		String token = payload.get("token").toString();
		System.out.println(token);

		List<Token> tokelist = mobileTokenService.getTokens();
		System.out.println(tokelist.toString());
		String email = null;
		for (Token storedToken : tokelist) {
			if (token.equals(storedToken.getToken())) {
				email = storedToken.getEmail();
				break;
			}
		}
		if (email == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		System.out.println(email);
		List<Home> homes = homeService.getHomes();

		CareTaker account=null;
		for (Home home : homes) {
			for (CareTaker ct : home.getCareTakers()) {
				if (ct.getEmail().equals(email)) {
					account=ct;
					break;
				}
			}
		}

		if (account == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		
		return account;
	}
	
	@PostMapping("/mobile/private/elderonmap")
	public List<Map<String, Object>> onMap(@RequestBody Map<String, Object> payload) throws Exception {
		String token = payload.get("token").toString();
		System.out.println(token);
		List<Token> tokelist = mobileTokenService.getTokens();
		System.out.println(tokelist.toString());
		String email = null;
		for (Token storedToken : tokelist) {
			if (token.equals(storedToken.getToken())) {
				email = storedToken.getEmail();
				break;
			}
		}
		if (email == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		System.out.println(email);
		List<Elder> elderlist=null;
		List<Home> homes = homeService.getHomes();
		double homelat=0;
		double homelon=0;
		for (Home home : homes) {
			for (CareTaker ct : home.getCareTakers()) {
				if (ct.getEmail().equals(email)) {
					homelat=home.getLat();
					homelon=home.getLon();
					elderlist= ct.getElders();
				}
			}
		}

		if (elderlist==null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		
		List<Coordinate> coordinates=ioTService.getCoordinates();
		
		List<Map<String,Object>> map=new ArrayList<>();
		//System.out.println(coordinates);
		
		for (Elder elder : elderlist) {
			for (Coordinate coordinate : coordinates) {
				System.out.println(elder.getMac()+" - "+coordinate.getMac());
				if (elder.getMac().length()>0 && elder.getMac().equals(coordinate.getMac())) {
					HashMap<String, Object> data=new HashMap<String, Object>();
					data.put("name", elder.getName());
					data.put("nic", elder.getNic());
					data.put("mac", elder.getMac());
					data.put("lat", coordinate.getLat());
					data.put("lon", coordinate.getLon());
					data.put("homelat", homelat);
					data.put("homelon", homelon);
					map.add(data);
					continue;
				}
			}
		}
		return map;

	}

}
