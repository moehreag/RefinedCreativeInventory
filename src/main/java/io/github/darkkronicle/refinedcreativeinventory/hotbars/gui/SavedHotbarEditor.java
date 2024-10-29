package io.github.darkkronicle.refinedcreativeinventory.hotbars.gui;

import java.util.List;

import io.github.darkkronicle.darkkore.gui.ConfigScreen;
import io.github.darkkronicle.darkkore.gui.Tab;
import io.github.darkkronicle.darkkore.gui.components.impl.ButtonComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.PositionedComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.StringUtil;
import io.github.darkkronicle.refinedcreativeinventory.hotbars.SavedHotbar;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;

public class SavedHotbarEditor extends ConfigScreen {

    private final SavedHotbar hotbar;
    private final HotbarProfileComponent profile;

    public static SavedHotbarEditor getEditor(Screen parent, HotbarProfileComponent profile, SavedHotbar hotbar) {
        return new SavedHotbarEditor(parent, Tab.ofOptions(Identifier.of("rci", "hotkey"), "rci.section.hotkey", hotbar.getOptions()), profile, hotbar);
    }

    protected SavedHotbarEditor(Screen parent, Tab tab, HotbarProfileComponent profile, SavedHotbar hotbar) {
        super(List.of(tab));
        this.hotbar = hotbar;
        this.profile = profile;
        setParent(parent);
    }

    @Override
    public void initImpl() {
        super.initImpl();
        ButtonComponent delete = new ButtonComponent(
                this,
                StringUtil.translateToText("rci.itemedit.delete"),
                new Color(100, 100, 100, 100),
                new Color(150, 150, 150, 150),
                (button) -> {
                    profile.getProfile().remove(hotbar);
                    close();
                    profile.updateHotbars();
                }
        );
        addComponent(new PositionedComponent(
                this,
                delete,
                10,
                10,
                -1,
                -1
        ));
    }
}
