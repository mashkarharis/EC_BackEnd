package com.eldercare.rest.Elder.CareWeb.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.eldercare.rest.Elder.CareWeb.Models.CareTaker;
import com.eldercare.rest.Elder.CareWeb.Models.Coordinate;
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
public class IoTService {
	@Autowired
	HomeService homeService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IoTService.class);

	public void storeCoordinates(Coordinate coordinate) throws Exception {

		Firestore dbFirestore = FirestoreClient.getFirestore();
		ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("coordinates")
				.document(coordinate.getMac()).set(coordinate);
	}

	public List<Coordinate> getCoordinates() throws Exception {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		ApiFuture<QuerySnapshot> collectionsApiFuture = dbFirestore.collection("coordinates").get();
		QuerySnapshot docs = collectionsApiFuture.get();
		List<Coordinate> coordinates = new ArrayList<>();
		for (QueryDocumentSnapshot doc : docs) {
			// System.out.println(doc.getId());
			coordinates.add(getCoordinatesDetail(doc.getId()));
		}
		return coordinates;
	}

	public Coordinate getCoordinatesDetail(String id) throws InterruptedException, ExecutionException {
		Firestore dbFirestore = FirestoreClient.getFirestore();
		DocumentReference documentReference = dbFirestore.collection("coordinates").document(id);
		ApiFuture<DocumentSnapshot> future = documentReference.get();

		DocumentSnapshot document = future.get();

		Coordinate coordinate = null;

		if (document.exists()) {
			coordinate = document.toObject(Coordinate.class);
			return coordinate;
		} else {
			return null;
		}
	}
}
