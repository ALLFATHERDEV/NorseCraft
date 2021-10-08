package com.norsecraft.datagen;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Base class for norse craft data generating
 * <p>
 * by OdinAllfather
 */
public interface NorseCraftDataGenerator {

    List<NorseCraftDataGenerator> DATA_GENERATORS = Lists.newArrayList(new BlockStatesDataGenerator(), new ItemModelDataGenerator());

    void generate();

    String getDataGeneratorName();

}
