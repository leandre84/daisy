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
import at.technikum.mic16.prj.data.Vulnerability;
import at.technikum.mic16.prj.entity.Category;
import at.technikum.mic16.prj.entity.Product;
import at.technikum.mic16.prj.entity.Recension;
import at.technikum.mic16.prj.entity.User;
import at.technikum.mic16.prj.entity.UserRole;
import at.technikum.mic16.prj.util.FileUtil;
import at.technikum.mic16.prj.util.JBossPasswordUtil;
import org.apache.commons.codec.binary.Base64;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.inject.Inject;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * This bean is intended for initialisation operations during application startup
 * such as inserting sample data, creation of reward tokens and so on.
 * @author leandros
 */
@Singleton
@LocalBean
@Startup
public class InitBean {
    
    private static final String XSS_FILE_PATH = "/home/daisy/.config";
    public static final String HIDDEN_FILE_PATH = "/tmp/TOKEN_REWARD.TXT";
    private static final String USER_WITH_TOKEN = "user2@foo.at";

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
    
    private String installationToken;
    private Map<Vulnerability, String> rewardTokens;

    
    public void setInstallationToken(String installationToken) {
        this.installationToken = installationToken;
    }

    public Map<Vulnerability, String> getRewardTokens() {
        return rewardTokens;
    }

    public void setRewardTokens(Map<Vulnerability, String> rewardTokens) {
        this.rewardTokens = rewardTokens;
    }
    
    

    @PostConstruct
    public void init() {

        try {
            insertSampleData();
            installationToken = webshopService.retrieveInstallationToken();
            /* if there is no token, retrieving it would fail with FileNotFoundException
            so just go on inserting vulnerability data...
             */
            generateRewardTokens();
            insertVulnerabilityData();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(InitBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ignore) {
            // retrieving installation token failed
        } catch (IOException ex) {
            Logger.getLogger(InitBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void insertSampleData() throws NoSuchAlgorithmException, UnsupportedEncodingException {

        UserRole user1Role = new UserRole("han", UserRole.Role.CUSTOMER);
        UserRole user2Role = new UserRole(USER_WITH_TOKEN, UserRole.Role.CUSTOMER);
        userRoleDAO.persist(user1Role, user2Role);

        User user1 = new User("user1@foo.at", JBossPasswordUtil.getPasswordHash("user1"), "User", "1");
        User user2 = new User(USER_WITH_TOKEN, JBossPasswordUtil.getPasswordHash(RandomStringUtils.randomAlphanumeric(12)), "User", "2");
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
        recension1.setCreationDate(LocalDateTime.now().minusDays(14));
        recension1.setProduct(phillips1);
        recension1.setRating(4);
        recension1.setUser(user1);
        recension1.setText("I like it");

        Recension recension2 = new Recension();
        recension2.setCreationDate(LocalDateTime.now().minusDays(3).minusSeconds((int) (Math.random()*1337)));
        recension2.setProduct(phillips1);
        recension2.setRating(3);
        recension2.setUser(user2);
        recension2.setText("It's ok, don't expect too much.");

        recensionDAO.persist(recension1, recension2);

    }
    
    private void generateRewardTokens() {
        rewardTokens = new HashMap<>();
        for (Vulnerability v : Vulnerability.values()) {
            try {
                rewardTokens.put(v, DaisyPointsCrypter.encryptMessage(installationToken, "Vulnerability|" + v.name()));
                //Logger.getLogger(InitBean.class.getName()).log(Level.INFO, "Generated token: ".concat(rewardTokens.get(v)));
            } catch (DaisyPointsEncryptionException ex) {
                rewardTokens = null;
                Logger.getLogger(InitBean.class.getName()).log(Level.SEVERE, "Error generating reward tokens", ex);
            }

        }
    }

    /**
     * Create reward tokens for exploited vulnerabilities
     * @throws IOException 
     */
    public void insertVulnerabilityData() throws IOException {
        
        /*
        this should only happen upon invocation via TokenController and not
        on subsequent restarts, when token is already known
        */
        if (rewardTokens == null) {
            generateRewardTokens();
        }
        
        // hidden product - find via SQL injection
        Category hoover = categoryDAO.findByName("Hoover");
        Product prod1 = new Product("SQL Injection exploited!", 666, "Congratulations, here is your token for the points system:\n".concat(rewardTokens.get(Vulnerability.SQLI_PRODUCTS)), "images/thumbs_up.png", hoover);
        prod1.setActive(false);
        productDAO.persist(prod1);
        
        // hidden user - find via indirect object reference
        User user2 = userDAO.findById(USER_WITH_TOKEN);
        user2.setDescription(rewardTokens.get(Vulnerability.INSECURE_DIRECT_OBJECT_REFERENCE));
        
        // hidden file - find via hidden directory and CommandService
        File f = new File(HIDDEN_FILE_PATH);
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(f));
            bw.write("Command execution exploited, here is your token for the points system:");
            bw.newLine();
            bw.write(rewardTokens.get(Vulnerability.HIDDEN_FILE));
            bw.newLine();
            bw.flush();
        } finally {
            FileUtil.safeClose(bw);
        }

        // prepare phantom JS script
        f = new File(XSS_FILE_PATH);
        bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(f));
            bw.write(preparePhantomJSScript());
            bw.flush();
        } finally {
            FileUtil.safeClose(bw);
        }

    }
    
    /**
     * Delete reward tokens
     */
    public void deleteVulnerabilityData() {
        for (Product p : productDAO.findInactive()) {
            productDAO.delete(p);
        }
        
        User user2 = userDAO.findById(USER_WITH_TOKEN);
        user2.setDescription("");
        
        File f = new File(HIDDEN_FILE_PATH);
        f.delete();
        
        f = new File(XSS_FILE_PATH);
        f.delete();
        
    }
    
    /**
     Writes token in script invoked by phantom JS
     * @param token
     * @return
     * @throws UnsupportedEncodingException
     * @throws DaisyPointsEncryptionException 
     */
    private String preparePhantomJSScript() throws UnsupportedEncodingException {
        String base64 = "dmFyIHBhZ2UgPSByZXF1aXJlKCd3ZWJwYWdlJykuY3JlYXRlKCk7CgpwYWdlLnNldHRpbmdzLnVzZXJBZ2VudCA9ICdUT0tFTic7CnBhZ2Uudmlld3BvcnRTaXplID0geyB3aWR0aDogMTkyMCwgaGVpZ2h0OiAxMDgwIH07CgpwYWdlLm9wZW4oJ2h0dHA6Ly8xMjcuMC4wLjE6ODA4MC9kYWlzeS13ZWIvJywgZnVuY3Rpb24oKSB7CgogICAgICAgIHBhZ2UuZXZhbHVhdGUoZnVuY3Rpb24oKSB7CiAgICAgICAgICAgICAgICBQcmltZUZhY2VzLmFiKHtzOmRvY3VtZW50LnF1ZXJ5U2VsZWN0b3IoJ1thbHQ9InByb2R1Y3QtMSJdJykuZ2V0QXR0cmlidXRlKCJpZCIpfSk7CiAgICAgICAgfSk7CgogICAgICAgIHNldFRpbWVvdXQoZnVuY3Rpb24oKSB7CiAgICAgICAgICAgICAgICBwYWdlLmV2YWx1YXRlKGZ1bmN0aW9uKCkge30pOwogICAgICAgIH0sIDIwMDApOwoKICAgICAgICBjb25zb2xlLmxvZygiZmluaXNoIik7CgogICAgICAgIHNldFRpbWVvdXQoZnVuY3Rpb24oKSB7CiAgICAgICAgICAgICAgICAvL3BhZ2UucmVuZGVyKCd0ZXN0LnBuZycpOwogICAgICAgICAgICAgICAgcGhhbnRvbS5leGl0KCk7CiAgICAgICAgfSwgMjAwMCk7Cn0pOwo=";
        String script = new String(Base64.decodeBase64(base64), "UTF-8");
        return script.replace("TOKEN", rewardTokens.get(Vulnerability.XSS_REMOTE_SCRIPT));
    }

}
