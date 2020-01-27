package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Deprecated
public class WList extends WVerticalList {
    public WList(WPosition position, WSize size, WInterface linkedInterface) {
        super(position, size, linkedInterface);
    }
}
