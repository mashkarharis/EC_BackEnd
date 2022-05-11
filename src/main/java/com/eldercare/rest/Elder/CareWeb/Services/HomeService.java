package com.eldercare.rest.Elder.CareWeb.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.eldercare.rest.Elder.CareWeb.Models.Home;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class HomeService {
	
	public String updateHome(Home home) throws Exception {
		Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("homes").document(home.getHomeId()).update(home.toSetMap());
        return collectionsApiFuture.get().getUpdateTime().toString();  
	}

	public String deleteHome(String homeId) throws Exception {
		Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("homes").document(homeId).delete();
        return collectionsApiFuture.get().getUpdateTime().toString();  
	}

	public List<Home> getHomes() throws Exception {
		Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> collectionsApiFuture = dbFirestore.collection("homes").get();
        QuerySnapshot docs= collectionsApiFuture.get();
        List<Home> homes=new ArrayList<>();
        for (QueryDocumentSnapshot doc : docs) {
			//System.out.println(doc.getId());
			homes.add(getHomeDetail(doc.getId()));
		}
        return homes;
	}

	public String addHome(Home home) throws Exception {
		//System.out.println(home.toString());
		Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("homes").document(home.getHomeId()).set(home);
        return collectionsApiFuture.get().getUpdateTime().toString();  
	}
	
	
	public Home getHomeDetail(String homeId) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("homes").document(homeId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot document = future.get();

        Home home = null;

        if(document.exists()) {
            home = document.toObject(Home.class);
            return home;
        }else {
            return null;
        }
    }

}
