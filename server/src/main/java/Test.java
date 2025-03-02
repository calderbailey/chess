
import requestresult.LoginRequest;
import service.UserService;

public class Test {
    public static void main(String[] args) {
        UserService user = new UserService();
        LoginRequest logReq = new LoginRequest(args[0], args[1]);
        user.register(logReq);
        UserService user2 = new UserService();
        LoginRequest logReq2 = new LoginRequest(args[0], args[1]);
        System.out.printf(user.login(logReq2).toString());
        System.out.printf(user.login(new LoginRequest("he", "h")).toString());

    }
}
