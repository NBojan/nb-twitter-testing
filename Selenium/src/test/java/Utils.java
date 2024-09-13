import java.util.Random;

public class Utils {

    public String getSaltString(int length) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }
    public String getSaltStringCyrilic(int length) {
        String SALTCHARS = "АБВГДЃЕЖЗSИЈКЛЉМНЊОПРСТЌУФХЦЧЏШ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }

    public String getRandomNumber(int max) {
        int test = (int) (Math.random() * max) + 1;
        return String.valueOf(test);
    }

    String url = "https://nb-twitter.vercel.app/";
    String registerUrl = "https://nb-twitter.vercel.app/registration";
    String dummyUrl = "https://nb-twitter.vercel.app/profile/dummies";
    String myTestingAccountUrl = "https://nb-twitter.vercel.app/profile/MyTestingAccount";
    String forzaMilanPostUrl = "https://nb-twitter.vercel.app/tweet/WZMIjKLqQV2pIIpFJ22q";
    String helloFromMePostUrl = "https://nb-twitter.vercel.app/tweet/Ne71KVRfNOFpbNnSVpL6";

    String alwaysLikedPostLocator = "//p[text()='PostThatWillAlwaysBeLiked']/ancestor::article";
    String ownPostLocator = "//p[text()='TestingPurposesPostDontTouch']/ancestor::article";
    // String ownPostLocator = "//p[text()='Hello from me dummies.']/ancestor::article"; //for dummy account test
    String anotherUserPostLocator = "//p[text()='Forza Milan Sempre!!']/ancestor::article";

    String ownCommentLocator = "//p[text()='forTestingComment']/ancestor::article";
    // String ownCommentLocator = "//p[text()='hi :D']/ancestor::article";

    String anotherUserCommentLocator = "//p[text()='testingDummiesComment']/ancestor::article";
    // String anotherUserCommentLocator = "//p[text()='helloFromMeTooTesting']/ancestor::article";

}
