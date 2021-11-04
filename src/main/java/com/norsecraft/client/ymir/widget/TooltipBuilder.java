package com.norsecraft.client.ymir.widget;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.Collections;
import java.util.List;

/**
 * Helper class for building tooltips
 */
@Environment(EnvType.CLIENT)
public final class TooltipBuilder {

    final List<OrderedText> lines = Lists.newArrayList();

    int size() {
        return lines.size();
    }

    public TooltipBuilder add(Text... lines) {
        for(Text line : lines)
            this.lines.add(line.asOrderedText());
        return this;
    }

    public TooltipBuilder add(OrderedText... lines) {
        Collections.addAll(this.lines, lines);
        return this;
    }

}
