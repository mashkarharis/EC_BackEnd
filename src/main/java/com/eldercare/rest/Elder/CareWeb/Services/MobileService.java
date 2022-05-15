package com.eldercare.rest.Elder.CareWeb.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eldercare.rest.Elder.CareWeb.Models.CareTaker;
import com.eldercare.rest.Elder.CareWeb.Models.Home;
import com.eldercare.rest.Elder.CareWeb.Models.Token;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class MobileService {
	@Autowired
	HomeService homeService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);

	public String storeToken(String email) throws Exception {
		String token = UUID.randomUUID().toString();
		Token tk = new Token();
		tk.setEmail(email);
		tk.setToken(token);

		Firestore dbFirestore = FirestoreClient.getFirestore();
		ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("tokens")
				.document(email).set(tk);
		return token;
	}

	public List<Token> getTokens() throws Exception {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		ApiFuture<QuerySnapshot> collectionsApiFuture = dbFirestore.collection("tokens").get();
		QuerySnapshot docs = collectionsApiFuture.get();
		List<Token> tokens = new ArrayList<>();
		for (QueryDocumentSnapshot doc : docs) {
			// System.out.println(doc.getId());
			tokens.add(getTokenDetail(doc.getId()));
		}
		return tokens;
	}

	public Token getTokenDetail(String tokenId) throws InterruptedException, ExecutionException {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		DocumentReference documentReference = dbFirestore.collection("tokens").document(tokenId);
		ApiFuture<DocumentSnapshot> future = documentReference.get();

		DocumentSnapshot document = future.get();

		Token token = null;

		if (document.exists()) {
			token = document.toObject(Token.class);
			return token;
		} else {
			return null;
		}
	}

	public boolean introspect(String token) throws Exception {
		List<Token> tokelist = getTokens();
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
					return true;
				}
			}
		}

		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	}
}
