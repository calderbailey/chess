
import requestresult.LoginRequest;
import requestresult.RegisterRequest;
import requestresult.RegisterResult;
import service.UserService;

public class Test {
    public static void main(String[] args) {
        UserService user = new UserService();
        RegisterRequest regReq = new RegisterRequest(args[0], args[1], args[2]);
        System.out.printf(user.register(regReq).toString());
        UserService user2 = new UserService();
        LoginRequest logReq2 = new LoginRequest(args[0], args[1]);
        System.out.printf(user.login(logReq2).toString());
        System.out.printf(user.login(new LoginRequest("he", "h")).toString());
    }
}
