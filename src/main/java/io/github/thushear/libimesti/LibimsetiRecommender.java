package io.github.thushear.libimesti;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.common.io.Resources;
import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.IDRescorer;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;

/**
 *  Created by kongming on 2016/7/11.
 *  http://libimseti.cz/  Recommender
 *
 */
public class LibimsetiRecommender implements Recommender {


    private final Recommender delegate;

    private final DataModel model;

    private final FastIDSet men;

    private final FastIDSet women;

    private final FastIDSet usersRateMoreMen;

    private final FastIDSet usersRateLessMen;


    public LibimsetiRecommender() throws IOException, TasteException {
        this(new FileDataModel(readResourceToTempFile("ratings.dat")));
    }


    public LibimsetiRecommender(DataModel model) throws TasteException, IOException {
        UserSimilarity similarity = new EuclideanDistanceSimilarity(model);
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(2,similarity,model);
        delegate = new GenericUserBasedRecommender(model,neighborhood,similarity);
        this.model = model;
        FastIDSet[] menWomen = GenderRescorer.parseMenWomen(readResourceToTempFile("gender.dat"));
        men = menWomen[0];
        women = menWomen[1];
        usersRateMoreMen = new FastIDSet(50000);
        usersRateLessMen = new FastIDSet(50000);
    }





    public List<RecommendedItem> recommend(long userID, int howMany) throws TasteException {
        IDRescorer rescorer = new GenderRescorer(men,women,usersRateMoreMen,usersRateLessMen,userID,model);
        return delegate.recommend(userID,howMany,rescorer);
    }

    public List<RecommendedItem> recommend(long userID, int howMany, IDRescorer idRescorer) throws TasteException {
        return delegate.recommend(userID,howMany,idRescorer);
    }

    public float estimatePreference(long userID, long itemID) throws TasteException {
        IDRescorer rescorer = new GenderRescorer(men,women,usersRateMoreMen,usersRateLessMen,userID,model);;
        return (float) rescorer.rescore(itemID,delegate.estimatePreference(userID,itemID));
    }

    public void setPreference(long userID, long itemID, float value) throws TasteException {
        delegate.setPreference(userID,itemID,value);
    }

    public void removePreference(long userID, long itemID) throws TasteException {
        delegate.removePreference(userID,itemID);
    }

    public DataModel getDataModel() {
        return delegate.getDataModel();
    }

    public void refresh(Collection<Refreshable> collection) {
        delegate.refresh(collection);
    }



    static File readResourceToTempFile(String resourceName) throws IOException {
        String absoluteResource = resourceName.startsWith("/") ? resourceName : '/' + resourceName;
        InputSupplier<? extends InputStream> inputSupplier;

        try {
            URL resourceURL = Resources.getResource(LibimsetiRecommender.class,absoluteResource);
            inputSupplier = Resources.newInputStreamSupplier(resourceURL);
        } catch (Exception e) {
            File resourceFile = new File(resourceName);
            inputSupplier = Files.newInputStreamSupplier(resourceFile);
        }

        File tempFile = File.createTempFile("taste",null);
        tempFile.deleteOnExit();
        Files.copy(inputSupplier,tempFile);
        return tempFile;

    }

}
