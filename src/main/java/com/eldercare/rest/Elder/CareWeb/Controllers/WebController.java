package com.eldercare.rest.Elder.CareWeb.Controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.eldercare.rest.Elder.CareWeb.Models.CareTaker;
import com.eldercare.rest.Elder.CareWeb.Models.Elder;
import com.eldercare.rest.Elder.CareWeb.Models.Home;
import com.eldercare.rest.Elder.CareWeb.Services.AdminService;
import com.eldercare.rest.Elder.CareWeb.Services.HomeService;

@CrossOrigin
@RestController
public class WebController {

	@Autowired
	AdminService adminService;
	@Autowired
	HomeService homeService;

	// **********
	// AUTH
	// **********

	@PostMapping("/web/protected/login")
	public String login(@RequestBody Map<String, Object> payload) {
		String username = payload.get("username").toString();
		String password = payload.get("password").toString();

		String token;
		try {
			token = adminService.loginAdmin(username, password);
			if (token == null)
				throw new Exception();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}

		return token;
	}

	@PostMapping("web/protected/introspect")
	public boolean introspect(@RequestBody Map<String, Object> payload) {
		// System.out.println(payload);
		String token = payload.get("token").toString();
		try {
			return adminService.introspect(token);
		} catch (Exception e) {
			return false;
		}
	}

	// **********
	// HOME
	// **********

	@PostMapping("web/private/home/add")
	public String addHome(@RequestBody Home home) {
		// System.out.println(home.toString());
		try {
			Home existinghome = homeService.getHomeDetail(home.getHomeId());
			if (existinghome != null) {
				throw new Exception();
			}
			return homeService.addHome(home);
		} catch (Exception e) {
			// System.out.println(e.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("web/private/home/update")
	public String updateHome(@RequestBody Home home) {
		// System.out.println(home.toString());
		try {
			return homeService.updateHome(home);
		} catch (Exception e) {
			// System.out.println(e.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("web/private/home/delete")
	public String deleteHome(@RequestBody Map<String, String> data) {
		String homeId = data.get("homeId");
		// System.out.println(homeId.toString());
		try {
			return homeService.deleteHome(homeId);
		} catch (Exception e) {
			// System.out.println(e.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("web/private/home/list")
	public List<Home> getHomes() {
		try {
			return homeService.getHomes();
		} catch (Exception e) {
			// System.out.println(e.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	// **********
	// CARETAKERS
	// **********

	@PostMapping("web/private/caretaker/add")
	public void addCareTaker(@RequestBody Map<String, Object> careTaker) {
		// System.out.println(careTaker.toString());
		try {

			String homeId = (String) careTaker.get("homeId");
			CareTaker ct = new CareTaker();
			ct.setNic((String) careTaker.get("nic"));
			ct.setName((String) careTaker.get("name"));
			ct.setAddress((String) careTaker.get("address"));
			ct.setPhone((String) careTaker.get("phone"));
			ct.setEmail((String) careTaker.get("email"));
			ct.setPassword((String) careTaker.get("password"));

			Home home = homeService.getHomeDetail(homeId);
			// System.out.println(home.toString());

			List<Home> homes = homeService.getHomes();

			for (Home home1 : homes) {
				for (CareTaker existingct : home1.getCareTakers()) {
					if (ct.getNic().equals(existingct.getNic()) || ct.getEmail().equals(existingct.getEmail())) {
						throw new Exception();
					}
				}
			}

			home.addCareTaker(ct);
			homeService.addHome(home);

		} catch (Exception e) {
			// System.out.println(e.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("web/private/caretaker/update")
	public void updateCareTaker(@RequestBody Map<String, Object> careTaker) {
		// System.out.println(careTaker.toString());
		try {

			String homeId = (String) careTaker.get("homeId");

			Home home = homeService.getHomeDetail(homeId);
			// System.out.println(home.toString());
			for (CareTaker ct : home.getCareTakers()) {
				if (careTaker.get("email").equals(ct.getEmail())) {
					ct.setNic((String) careTaker.get("nic"));
					ct.setName((String) careTaker.get("name"));
					ct.setAddress((String) careTaker.get("address"));
					ct.setPhone((String) careTaker.get("phone"));
					break;
				}
			}
			homeService.addHome(home);

		} catch (Exception e) {
			// System.out.println(e.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("web/private/caretaker/delete")
	public void deleteCareTaker(@RequestBody Map<String, Object> careTaker) {
		// System.out.println(careTaker.toString());
		try {

			String homeId = (String) careTaker.get("homeId");
			String email = (String) careTaker.get("email");
			Home home = homeService.getHomeDetail(homeId);

			// System.out.println(home.toString());
			for (CareTaker ct : home.getCareTakers()) {
				if (email.equals(ct.getEmail())) {
					home.getCareTakers().remove(ct);
					break;
				}
			}
			homeService.addHome(home);

		} catch (Exception e) {
			// System.out.println(e.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("web/private/caretaker/list")
	public List<Map<String, Object>> listCareTaker() {
		try {

			List<Home> homes = homeService.getHomes();
			List<Map<String, Object>> cts = new ArrayList<>();

			for (Home home : homes) {
				for (CareTaker ct : home.getCareTakers()) {
					Map<String, Object> ctdetailstosend = new HashMap<>();
					ctdetailstosend.put("nic", ct.getNic());
					ctdetailstosend.put("name", ct.getName());
					ctdetailstosend.put("address", ct.getAddress());
					ctdetailstosend.put("phone", ct.getPhone());
					ctdetailstosend.put("email", ct.getEmail());
					ctdetailstosend.put("houseid", home.getHomeId());
					ctdetailstosend.put("noofelders", ct.getElders().size());
					cts.add(ctdetailstosend);
				}
			}

			return cts;

		} catch (Exception e) {
			// System.out.println(e.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	// **********
	// Elders
	// **********

	@PostMapping("web/private/elder/add")
	public void addElder(@RequestBody Map<String, Object> elderdetails) {
		// System.out.println(elderdetails.toString());
		try {

			String homeId = (String) elderdetails.get("homeId");
			String careTakerNic = (String) elderdetails.get("careTakerNic");

			Elder elder = new Elder();
			elder.setNic((String) elderdetails.get("nic"));
			elder.setName((String) elderdetails.get("name"));
			elder.setAddress((String) elderdetails.get("address"));
			elder.setPhone((String) elderdetails.get("phone"));
			elder.setMac((String) elderdetails.get("mac"));
			elder.setImage((String) elderdetails.get("image"));

			List<Home> homes = homeService.getHomes();
			for (Home home1 : homes) {
				for (CareTaker existingct : home1.getCareTakers()) {
					for (Elder exisitngelder : existingct.getElders()) {
						if (elder.getNic().equals(exisitngelder.getNic())) {
							throw new Exception();
						}
					}

				}
			}

			Home home = homeService.getHomeDetail(homeId);
			CareTaker ct = null;
			for (CareTaker existingct : home.getCareTakers()) {
				if (careTakerNic.equals(existingct.getNic())) {
					ct = existingct;
				}
			}
			System.out.println(ct.toString());
			List<Elder> elders = ct.getElders();
			elders.add(elder);
			System.out.println(ct.toString());
			homeService.addHome(home);

		} catch (Exception e) {
			// System.out.println(e.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("web/private/elder/update")
	public void updateElder(@RequestBody Map<String, Object> elderdetails) {
		// System.out.println(elderdetails.toString());
		try {

			String homeId = (String) elderdetails.get("homeId");
			String careTakerNic = (String) elderdetails.get("careTakerNic");

			Elder elder = new Elder();
			elder.setNic((String) elderdetails.get("nic"));// LOCKED
			elder.setName((String) elderdetails.get("name"));
			elder.setAddress((String) elderdetails.get("address"));
			elder.setPhone((String) elderdetails.get("phone"));
			elder.setMac((String) elderdetails.get("mac"));
			elder.setImage((String) elderdetails.get("image"));

			Home home = homeService.getHomeDetail(homeId);
			CareTaker ct = null;
			for (CareTaker existingct : home.getCareTakers()) {
				if (careTakerNic.equals(existingct.getNic())) {
					ct = existingct;
					break;
				}
			}

			Elder eldertoupdate = null;
			for (Elder elderexisting : ct.getElders()) {
				if (elderexisting.getNic().equals(elder.getNic())) {
					eldertoupdate = elderexisting;
					break;
				}
			}

			eldertoupdate.setName(elder.getName());
			eldertoupdate.setAddress(elder.getAddress());
			eldertoupdate.setPhone(elder.getPhone());
			eldertoupdate.setMac(elder.getMac());
			eldertoupdate.setImage(elder.getImage());

			homeService.addHome(home);

		} catch (Exception e) {
			// System.out.println(e.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("web/private/elder/delete")
	public void deleteElder(@RequestBody Map<String, Object> elderdetails) {
		// System.out.println(elderdetails.toString());
		try {

			String homeId = (String) elderdetails.get("homeId");
			String careTakerNic = (String) elderdetails.get("careTakerNic");
			String nic = (String) elderdetails.get("nic");

			Home home = homeService.getHomeDetail(homeId);
			CareTaker ct = null;
			for (CareTaker existingct : home.getCareTakers()) {
				if (careTakerNic.equals(existingct.getNic())) {
					ct = existingct;
					break;
				}
			}

			Elder eldertodelete = null;
			for (Elder elderexisting : ct.getElders()) {
				if (elderexisting.getNic().equals(nic)) {
					eldertodelete = elderexisting;
					break;
				}
			}

			ct.getElders().remove(eldertodelete);

			homeService.addHome(home);

		} catch (Exception e) {
			// System.out.println(e.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("web/private/elder/list")
	public List<Map<String, Object>> listElders() {
		try {

			List<Home> homes = homeService.getHomes();
			List<Map<String, Object>> elders = new ArrayList<>();

			for (Home home1 : homes) {
				for (CareTaker existingct : home1.getCareTakers()) {
					for (Elder exisitngelder : existingct.getElders()) {
						Map<String, Object> elderdetailstosend = new HashMap<>();
						elderdetailstosend.put("nic", exisitngelder.getNic());
						elderdetailstosend.put("name", exisitngelder.getName());
						elderdetailstosend.put("address", exisitngelder.getAddress());
						elderdetailstosend.put("phone", exisitngelder.getPhone());
						elderdetailstosend.put("mac", exisitngelder.getMac());
						elderdetailstosend.put("houseid", home1.getHomeId());
						elderdetailstosend.put("careTakerNic", existingct.getNic());
						elderdetailstosend.put("image", exisitngelder.getImage());
						elders.add(elderdetailstosend);
					}

				}
			}

			return elders;

		} catch (Exception e) {
			// System.out.println(e.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	// **********
	// Common
	// **********

	@GetMapping("web/private/summary")
	public Map<String, Object> getSummary(HttpServletRequest req) {
		try {
			String origin = req.getHeader(HttpHeaders.ORIGIN);
			System.out.println(origin);
			List<Home> homes = homeService.getHomes();
			
			int noOfHomes=0;
			int noOfCareTakers=0;
			int noOfElders=0;
			int noOfDevices=0;
			
			for (Home home1 : homes) {
				noOfHomes++;
				for (CareTaker existingct : home1.getCareTakers()) {
					noOfCareTakers++;
					for (Elder exisitngelder : existingct.getElders()) {
						noOfElders++;
						if (exisitngelder.getMac().length()>0) {
							noOfDevices++;
						}
						
					}
				}
			}
			Map<String,Object> summary=new HashMap<>();
			summary.put("noOfHomes", noOfHomes);
			summary.put("noOfCareTakers", noOfCareTakers);
			summary.put("noOfElders", noOfElders);
			summary.put("noOfDevices", noOfDevices);
			summary.put("homes", homes);

			return summary;

		} catch (Exception e) {
			// System.out.println(e.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}
}
