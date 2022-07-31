package md.dev.state;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.prefs.Preferences;

public class StateHandler {
    private static final Preferences PREFERENCES = Preferences.userRoot();
    private static final Logger LOG = LoggerFactory.getLogger(StateHandler.class);


    public static String getPropertyFromState(String stateId, String property) {
        LOG.info("required property {} of state {}", property, stateId);
        return PREFERENCES.get(stateId + property, null);
    }


    public static void savePropertyInState(String stateId, String property, String value) {
        LOG.info("saving property {} with stateId {}", property, stateId);
        PREFERENCES.put(stateId + property, value);
    }


    public static void removePropertyFromState(String stateId, String property) {
        LOG.info("removing property {} from state {}", property, stateId);
        PREFERENCES.remove(stateId + property);
    }


    public static boolean hasProperty(String stateId, String property) {
        return PREFERENCES.get(stateId + property, null) != null;
    }


    public static void saveSensitivePropertyInState(String secretKey,
                                                    String salt,
                                                    String stateId,
                                                    String property,
                                                    String value) {
        savePropertyInState(stateId, property, Encryptor.encrypt(secretKey, salt, value));
    }


    public static String getSensitivePropertyFromState(String secretKey,
                                                       String salt,
                                                       String stateId,
                                                       String property) {
        return Encryptor.decrypt(secretKey, salt, getPropertyFromState(stateId, property));
    }


    @SneakyThrows
    public static void removeAllStates() {
        PREFERENCES.clear();
    }
}
