package Collecto;

import static Collecto.Misc.parseInt;

public class Temp {
    public static boolean isPushValid(int push) {
        return 0 <= push && push <= 27;
    }

    public static Integer parsePush(String string_push) {
        Integer push = parseInt(string_push);
        if (push != null) {
            if (isPushValid(push)) {
                return push;
            }
        }
        return null;
    }

//    public class Commands {
//        public static final String HELP = "HELP";
//    }
//
//    public enum Comms {
//        HELP, LIST
//    }
}
