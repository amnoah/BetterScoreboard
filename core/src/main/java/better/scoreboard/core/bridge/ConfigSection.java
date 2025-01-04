package better.scoreboard.core.bridge;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface ConfigSection {

    Collection<String> getChildren();

    // Can be null
    ConfigSection getConfigSection(String node);

    <E> List<E> getList(Class<E> classType, String node);

    String getName();

    <E> E getObject(Class<E> classType, String node, E defaultValue);

    ConfigSection getParent();

    boolean hasNode(String node);
}
