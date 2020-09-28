package no.ntnu.backend.pentbrukt.Security;

public class JwtProperties {

    public static final String SECRETCODE  = "extremeludvig";
    public static final int EXPIRATION_time  = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    /*

    "Bearer: " works, but triggers exception

    "Bearer " returns OK 200 but no json data...

    WTF MOTHERFUCKER WHY AIDS HIV BITCH EBOLACUNT SPAGHETTIFUCKER

     */

}
