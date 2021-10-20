package com.norsecraft.common.block.entity;

import com.norsecraft.client.render.model.block.campfire.CampfireDefaultModel;
import com.norsecraft.client.render.model.block.campfire.CampfireWithStandAndBowlModel;
import com.norsecraft.client.render.model.block.campfire.CampfireWithStandModel;
import com.norsecraft.client.render.model.joint.JointModel;
import com.norsecraft.client.render.model.joint.JointStateHandler;
import com.norsecraft.common.gui.CampfireGuiInterpretation;
import com.norsecraft.common.registry.NCBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CampfireBlockEntity extends AbstractFurnaceBlockEntity implements IAnimatable, JointStateHandler<CampfireBlockEntity> {

    public static final int DEFAULT_STATE = 0;
    public static final int WITH_STAND_STATE = 1;
    public static final int WITH_STAND_AND_BOWL_STATE = 2;

    private final AnimationFactory factory = new AnimationFactory(this);

    private State modelState = State.DEFAULT;
    private State prevModelState = State.DEFAULT;
    private int currentState = DEFAULT_STATE;
    private final JointModel<CampfireBlockEntity> jointModel = new JointModel<>(this);
    private final CampfireDefaultModel defaultModel = new CampfireDefaultModel();
    private final CampfireWithStandModel modelWithStand = new CampfireWithStandModel();
    private final CampfireWithStandAndBowlModel modelWithStandAndBowl = new CampfireWithStandAndBowlModel();

    private AnimatedGeoModel<CampfireBlockEntity> currentModel = defaultModel;

    public CampfireBlockEntity(BlockPos pos, BlockState state) {
        super(NCBlockEntities.campfireBlockEntity, pos, state, RecipeType.BLASTING /*TODO: Change after fixing the joint models issue*/);
    }

    public void setModelState(State modelState) {
        this.prevModelState = this.modelState;
        this.modelState = modelState;
    }

    public State getModelState() {
        return modelState;
    }

    @Override
    public void registerControllers(AnimationData animationData) {

    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    protected Text getContainerName() {
        return Text.of("Campfire");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new CampfireGuiInterpretation(syncId, playerInventory, ScreenHandlerContext.create(this.world, this.pos), this.propertyDelegate);
    }

    //=========================JointStateHandler=========================

    @Override
    public boolean canGoNext() {
        return this.prevModelState.ordinal() < this.modelState.ordinal();
    }

    @Override
    public boolean canGoBack() {
        return this.modelState.ordinal() < this.prevModelState.ordinal();
    }

    @Override
    public void next() {
        if (this.currentState > DEFAULT_STATE) {
            if (this.currentState == WITH_STAND_STATE) {
                this.currentState = WITH_STAND_AND_BOWL_STATE;
                this.modelState = State.WITH_BOWL_STAND;
                this.currentModel = modelWithStandAndBowl;
            }
        } else {
            if (this.currentState == DEFAULT_STATE) {
                this.currentState = WITH_STAND_STATE;
                this.modelState = State.WITH_STAND;
                this.currentModel = modelWithStand;
            }
        }
    }

    @Override
    public void back() {
        if (this.currentState > DEFAULT_STATE) {
            if (this.currentState == WITH_STAND_STATE) {
                currentState = DEFAULT_STATE;
                this.modelState = State.DEFAULT;
                this.currentModel = defaultModel;
            } else if (this.currentState == WITH_STAND_AND_BOWL_STATE) {
                currentState = WITH_STAND_STATE;
                this.modelState = State.WITH_STAND;
                this.currentModel = modelWithStand;
            }
        }
    }

    @Override
    public AnimatedGeoModel<CampfireBlockEntity> getCurrentModel() {
        return this.currentModel;
    }

    @Override
    public JointModel<CampfireBlockEntity> getJointModel() {
        return this.jointModel;
    }

    public enum State {

        NONE,
        DEFAULT,
        WITH_STAND,
        WITH_BOWL_STAND

    }
}
