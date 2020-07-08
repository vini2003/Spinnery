package spinnery.widget;

import net.minecraft.util.Identifier;
import org.apache.commons.lang3.mutable.Mutable;

/**
 * A WAbstractBar provides the basics necessary for a
 * general progress-bar-like widget, like an {@link WVerticalBar}
 * and {@link WHorizontalBar}.
 */
public abstract class WAbstractBar extends WAbstractWidget {
    protected Mutable<Number> limit;
    protected Mutable<Number> progress;

    /**
     * Retrieves the limit of this bar as a Mutable of a Number.
     * such that one does not need to constantly update it,
     * as that is done by the Mutable of a Number itself.
     *
     * @return The limit of this bar as a Mutable of a Number.
     */
    public Mutable<Number> getLimit() {
        return limit;
    }

    /**
     * Sets the limit of this bar as a Mutable of a Number,
     * such that one does not need to constantly update it,
     * as that is done by the Mutable of a Number itself.
     *
     * @param limit Mutable of a Number to be used as limit.
     */
    public <W extends WAbstractBar> W setLimit(Mutable<Number> limit) {
        this.limit = limit;
        return (W) this;
    }

    /**
     * Retrieves the progress of this bar as a Mutable of Number
     * such that one does not need to constantly update it,
     * as that is done by the Mutable of a Number itself.
     *
     * @return The progress of this bar as a Mutable of Number.
     */
    public Mutable<Number> getProgress() {
        return progress;
    }

    /**
     * Sets the progress of this bar as a Mutable of a Number
     * such that one does not need to constantly update it,
     * as that is done by the Mutable of a Number itself.
     *
     * @param progress Mutable of a Number to be used as progress.
     */
    public <W extends WAbstractBar> W setProgress(Mutable<Number> progress) {
        this.progress = progress;
        return (W) this;
    }

    /**
     * Retrieves the Identifier of the background texture of this bar.
     *
     * @return The Identifier of the background texture of this bar.
     */
    public Identifier getBackgroundTexture() {
        return getStyle().asIdentifier("background");
    }

    /**
     * Sets the Identifier of the background texture of this bar.
     *
     * @param backgroundTexture Identifier to be used as background texture.
     */
    public <W extends WAbstractBar> W setBackgroundTexture(Identifier backgroundTexture) {
        overrideStyle("background", backgroundTexture);
        return (W) this;
    }

    /**
     * Retrieves the Identifier of the foreground texture of this bar.
     *
     * @return The Identifier of the foreground texture of this bar.
     */
    public Identifier getForegroundTexture() {
        return getStyle().asIdentifier("foreground");
    }

    /**
     * Sets the Identifier of the foreground texture of this bar.
     *
     * @param foregroundTexture Identifier to be used as foreground texture.
     */
    public <W extends WAbstractBar> W setForegroundTexture(Identifier foregroundTexture) {
        overrideStyle("foreground", foregroundTexture);
        return (W) this;
    }
}
