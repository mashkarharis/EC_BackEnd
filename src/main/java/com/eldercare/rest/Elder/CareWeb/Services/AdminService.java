package com.eldercare.rest.Elder.CareWeb.Services;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class AdminService {

	public String loginAdmin(String username, String password) throws Exception {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		DocumentReference documentReference = dbFirestore.collection("admin").document("credentials");
		ApiFuture<DocumentSnapshot> future = documentReference.get();

		DocumentSnapshot document = future.get();
		Map<String, Object> adminCreds = document.getData();

		boolean isUserNameMatch = adminCreds.get("username").toString().equals(username);
		boolean isPasswordMatch = adminCreds.get("password").toString().equals(password);

		if (isPasswordMatch && isUserNameMatch) {
			String token = UUID.randomUUID().toString();
			adminCreds.put("token", token);
			documentReference.set(adminCreds);
			return token;
		}
		return null;
	}

	public boolean introspect(String token) throws Exception {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		DocumentReference documentReference = dbFirestore.collection("admin").document("credentials");
		ApiFuture<DocumentSnapshot> future = documentReference.get();

		DocumentSnapshot document = future.get();
		Map<String, Object> adminCreds = document.getData();

		return adminCreds.get("token").toString().equals(token);

	}

}
