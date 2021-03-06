package lermitage.intellij.nightandday.cfg;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

// see http://www.jetbrains.org/intellij/sdk/docs/basics/persisting_state_of_components.html
@SuppressWarnings({"WeakerAccess", "unused"})
@State(
    name = "NightanddaySettings",
    storages = @Storage("lermitage-nightandday.xml")
)
public class SettingsService implements PersistentStateComponent<SettingsService> {

    private final Logger LOG = Logger.getInstance(getClass().getName());

    @Override
    public SettingsService getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull SettingsService state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
