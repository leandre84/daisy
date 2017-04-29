/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.service;

import at.technikum.mic16.prj.daisypoints.DaisyPointsCrypter;
import at.technikum.mic16.prj.exception.DaisyPointsEncryptionException;
import at.technikum.mic16.prj.dao.CategoryDAO;
import at.technikum.mic16.prj.dao.OrderItemDAO;
import at.technikum.mic16.prj.dao.PlacedOrderDAO;
import at.technikum.mic16.prj.dao.ProductDAO;
import at.technikum.mic16.prj.dao.RecensionDAO;
import at.technikum.mic16.prj.dao.UserDAO;
import at.technikum.mic16.prj.dao.UserRoleDAO;
import at.technikum.mic16.prj.entity.Category;
import at.technikum.mic16.prj.entity.Product;
import at.technikum.mic16.prj.entity.Recension;
import at.technikum.mic16.prj.entity.User;
import at.technikum.mic16.prj.entity.UserRole;
import at.technikum.mic16.prj.util.JBossPasswordUtil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 *
 * @author leandros
 */
@Singleton
@LocalBean
@Startup
public class InitBean {

    @Inject
    private WebshopService webshopService;

    @Inject
    private CategoryDAO categoryDAO;
    @Inject
    private ProductDAO productDAO;
    @Inject
    private OrderItemDAO orderItemDAO;
    @Inject
    private PlacedOrderDAO placedOrderDAO;
    @Inject
    private RecensionDAO recensionDAO;
    @Inject
    private UserDAO userDAO;
    @Inject
    private UserRoleDAO userRoleDAO;

    @PostConstruct
    public void init() {

        try {
            insertSampleData();
            String token = webshopService.retrieveInstallToken();
            /* if there is no token, retrieving it would fail with FileNotFoundException
            so just go on inserting vulnerability data...
             */
            insertVulnerabilityData(token);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | DaisyPointsEncryptionException ex) {
            Logger.getLogger(InitBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ignore) {
            // retrieving installation token failed
        } catch (IOException ex) {
            Logger.getLogger(InitBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void insertSampleData() throws NoSuchAlgorithmException, UnsupportedEncodingException {

        UserRole user1Role = new UserRole("user1@foo.at", UserRole.Role.CUSTOMER);
        UserRole user2Role = new UserRole("user2@foo.at", UserRole.Role.CUSTOMER);
        userRoleDAO.persist(user1Role, user2Role);

        User user1 = new User("user1@foo.at", JBossPasswordUtil.getPasswordHash("user1"), "User", "1");
        User user2 = new User("user2@foo.at", JBossPasswordUtil.getPasswordHash("user2"), "User", "2");
        userDAO.persist(user1, user2);

        Category clothes = new Category("Clothes");
        categoryDAO.persist(clothes);

        Category men = new Category("Men");
        men.setParent(clothes);
        categoryDAO.persist(men);

        Category trousersMen = new Category("Trousers");
        trousersMen.setParent(men);
        categoryDAO.persist(trousersMen);

        Category electro = new Category("Electro");
        categoryDAO.persist(electro);

        Category telly = new Category("Television");
        telly.setParent(electro);
        categoryDAO.persist(telly);

        Category hoover = new Category("Hoover");
        hoover.setParent(electro);
        categoryDAO.persist(hoover);

        Category smartphone = new Category("Smartphone");
        smartphone.setParent(electro);
        categoryDAO.persist(smartphone);

        Product phillips1 = new Product("Philips 55PUK4900", 679.99f, "This new Phillips is superb...", "images/products/Phillips_55PUK4900.jpg", telly);
        Product phillips2 = new Product("Phillips 55PUS6031", 998.99f, "The brand new Phillips...", "images/products/Phillips_55PUS6031.jpg", telly);
        Product phillips3 = new Product("Phillips 50PFK4109", 328.99f, "This new Phillips is not as good...", "images/products/Phillips_50PFK4109.jpg", telly);
        Product samsung1 = new Product("Samsung UE55JU6470", 850.00f, "This new Samsung is superb...", "images/products/Samsung_UE55JU6470.jpg", telly);
        Product samsung2 = new Product("Samsung UE55K5660", 1100.00f, "This new Samsung is not as good...", "images/products/Samsung_UE55K5650.jpg", telly);
        Product samsung3 = new Product("Samsung UE65JU6070", 1200.99f, "This new Samsung is not as good...", "images/products/Samsung_UE65JU6070.jpg", telly);
        Product panasonic1 = new Product("Panasonic TX-49DXW654", 679.99f, "This new Phillips is superb...", "images/products/Panasonic_TX49DXW654.jpg", telly);
        Product panasonic2 = new Product("Panasonic TX65AXW904", 998.99f, "This new Phillips is not as good...", "images/products/Panasonic_TX65AXW904.jpg", telly);
        Product panasonic3 = new Product("Panasonic TX55CXW684", 328.99f, "This new Phillips is not as good...", "images/products/Panasonic_TX55CXW684.jpg", telly);
        Product hoover1 = new Product("iRobot Roomba 980", 750.90f, "Brand new and strong...", "images/products/Irobot_Roomba980.jpg", hoover);
        Product hoover2 = new Product("iRobot Roomba 886", 930.90f, "Brand new and strong...", "images/products/Irobot_Roomba886.jpg", hoover);
        Product hoover3 = new Product("iRobot Roomba 875", 487.90f, "Brand new and strong...", "images/products/IrobotRoomba875.jpg", hoover);
        Product hoover4 = new Product("Dyson Big Ball Parquet", 640.90f, "Brand new and strong...", "images/products/Dyson_Bigball1.jpg", hoover);
        Product hoover5 = new Product("Dyson DC37c Parquet", 321.90f, "Brand new and strong...", "images/products/Dyson_Dc37.jpg", hoover);
        Product hoover6 = new Product("Dyson DC37 Musclehead", 219.90f, "Brand new and strong...", "images/products/Dyson_Dc37misclehead.jpg", hoover);
        Product smartphone1 = new Product("Apple Iphone 7", 860.90f, "Brand new and strong...", "images/products/Iphone7.jpg", smartphone);
        Product smartphone2 = new Product("Apple Iphone SE", 450.90f, "Brand new and strong...", "images/products/Iphone_SE.jpg", smartphone);
        Product smartphone3 = new Product("Samsung Galaxy S8", 750.90f, "Brand new and strong...", "images/products/Samsung_S8.jpg", smartphone);
        Product smartphone4 = new Product("Samsung Galaxy S6", 650.90f, "Brand new and strong...", "images/products/Samsung_S6.jpg", smartphone);
        Product smartphone5 = new Product("Google Pixel", 800.90f, "Brand new and strong...", "images/products/Google_Pixel.jpg", smartphone);
        Product smartphone6 = new Product("Huawei P10", 700.90f, "Brand new and strong...", "images/products/Huawei_P10.jpg", smartphone);
        Product jeans1 = new Product("Lewis", 110.90f, "Brand new and strong...", "images/products/jeans1.jpg", trousersMen);
        Product jeans2 = new Product("G-Star P10", 120.90f, "Brand new and strong...", "images/products/jeans2.jpg", trousersMen);
        Product jeans3 = new Product("Review P10", 60.90f, "Brand new and strong...", "images/products/jeans3.jpg", trousersMen);
        Product jeans4 = new Product("Replay", 75.90f, "Brand new and strong...", "images/products/jeans4.jpg", trousersMen);
        Product jeans5 = new Product("Diesel", 160.90f, "Brand new and strong...", "images/products/jeans5.jpg", trousersMen);
        Product jeans6 = new Product("Mustang", 55.90f, "Brand new and strong...", "images/products/jeans6.jpg", trousersMen);

        productDAO.persist(phillips1, phillips2, phillips3, samsung1, samsung2, samsung3, panasonic1, panasonic2, panasonic3, hoover1, hoover2, hoover3, hoover4, hoover5, hoover6,
                smartphone1, smartphone2, smartphone3, smartphone4, smartphone5, smartphone6, jeans1, jeans2, jeans3, jeans4, jeans5, jeans6);

        Recension recension1 = new Recension();
        recension1.setCreationDate(LocalDate.now().minusDays(14));
        recension1.setProduct(phillips1);
        recension1.setRating(4);
        recension1.setUser(user1);
        recension1.setText("I like it");

        Recension recension2 = new Recension();
        recension2.setCreationDate(LocalDate.now().minusDays(3));
        recension2.setProduct(phillips1);
        recension2.setRating(3);
        recension2.setUser(user2);
        recension2.setText("It's ok, don't expect too much.");

        recensionDAO.persist(recension1, recension2);

    }

    /**
     * Create reward tokens for exploited vulnerabilities
     * @param installationToken
     * @throws DaisyPointsEncryptionException
     * @throws IOException 
     */
    public void insertVulnerabilityData(String installationToken) throws DaisyPointsEncryptionException, IOException {
        
        Category hoover = categoryDAO.findByName("Hoover");
        Product prod1 = new Product("SQL Injection exploited!", 666, "Congratulations, here is your token for the points system:\n".concat(DaisyPointsCrypter.encryptMessage(installationToken, "Vulnerability|1")), "images/thumbs_up.png", hoover);
        prod1.setActive(false);
        productDAO.persist(prod1);
        
        File f = new File("/tmp/TOKEN_REWARD.TXT");
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        bw.write("Command execution exploited, here is your token for the points system:");
        bw.newLine();
        bw.write(DaisyPointsCrypter.encryptMessage(installationToken, "Vulnerability|2"));
        bw.newLine();
        bw.flush();
        try {
            bw.close();
        } catch (IOException ignore) {
            
        }
        
        
    }
    
    
    /**
     * Delete reward tokens
     */
    public void deleteVulnerabilityData() {
        for (Product p : productDAO.findInactive()) {
            productDAO.delete(p);
        }
    }

}
