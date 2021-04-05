package co.schemati.trevor.api.instance;

import co.schemati.trevor.api.data.User;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InstanceUserMap {

    private final Map<UUID, User> users;

    public InstanceUserMap() {
        this.users = new ConcurrentHashMap<>();
    }

    /**
     * Returns the {@link User} stored with the provided {@link UUID} wrapped in an {@link Optional}.
     *
     * @param uuid the user's uuid
     *
     * @return the optional wrapper user
     */
    public Optional<User> get(UUID uuid) {
        return Optional.ofNullable(users.get(uuid));
    }

    /**
     * Adds a {@link User} to the map with the provided {@link UUID} as the key.
     *
     * @param uuid the user's uuid
     * @param user the user
     */
    public void add(UUID uuid, User user) {
        users.put(uuid, user);
    }

    /**
     * Removes a {@link User} from the internal cache using the provided {@link UUID} and returns
     * whether a key-value pair existed with the provided key.
     *
     * @param uuid the user's uuid
     *
     * @return whether the key-value pair existed
     */
    public boolean remove(UUID uuid) {
        return users.remove(uuid) != null;
    }
}
