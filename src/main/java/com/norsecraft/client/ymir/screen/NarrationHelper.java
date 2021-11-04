package com.norsecraft.client.ymir.screen;

import com.norsecraft.client.ymir.widget.YmirPanel;
import com.norsecraft.client.ymir.widget.YmirWidget;
import com.norsecraft.client.ymir.widget.data.NarrationMessages;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.text.TranslatableText;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A simple class that helps with the narrations
 */
public final class NarrationHelper {

    public static void addNarrations(YmirPanel rootPanel, NarrationMessageBuilder builder) {
        List<YmirWidget> narratableWidgets = getAllWidgets(rootPanel)
                .filter(YmirWidget::isNarratable)
                .collect(Collectors.toList());

        for (int i = 0, childCount = narratableWidgets.size(); i < childCount; i++) {
            YmirWidget child = narratableWidgets.get(i);
            if (!child.isFocused() && !child.isHovered()) continue;

            if (narratableWidgets.size() > 1) {
                builder.put(NarrationPart.POSITION, new TranslatableText(NarrationMessages.SCREEN_POSITION_KEY, i + 1, childCount));

                if (child.isFocused()) {
                    builder.put(NarrationPart.USAGE, NarrationMessages.COMPONENT_LIST_USAGE);
                }
            }

            child.addNarrations(builder.nextMessage());
        }
    }

    private static Stream<YmirWidget> getAllWidgets(YmirPanel panel) {
        return Stream.concat(Stream.of(panel), panel.streamChildren().flatMap(widget -> {
            if (widget instanceof YmirPanel nested) {
                return getAllWidgets(nested);
            }

            return Stream.of(widget);
        }));
    }

}
