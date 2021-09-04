package com.norsecraft.common.entity.dwarf;

import com.mojang.serialization.Dynamic;
import com.norsecraft.common.entity.dwarf.reputation.IReputation;
import com.norsecraft.common.entity.dwarf.reputation.ReputationDecayType;
import com.norsecraft.common.entity.dwarf.reputation.ReputationManager;
import com.norsecraft.common.entity.dwarf.reputation.ReputationType;
import com.norsecraft.common.registry.NCEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;
import java.util.Random;

public abstract class AbstractDwarfEntity extends PathAwareEntity implements IAnimatable, IReputation {

    private static final String WALK_ANIMATION_STR = "animation.norsecraft.dwarf.walk";
    private static final String IDLE_ANIMATION_STR = "animation.norsecraft.dwarf.idle";

    private final ReputationManager reputationManager = new ReputationManager();
    private final AnimationFactory factory = new AnimationFactory(this);
    private ModelVar modelVar;
    private TextureVar textureVar;
    private long lastReputationDecay;

    public AbstractDwarfEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
        this.setCustomName(new LiteralText(DwarfEntity.DwarfNames.getRandomName()));
        this.setCustomNameVisible(true);
    }

    protected void setRandomModel() {
        this.modelVar = DwarfEntity.ModelVar.getRandomVar();
        this.textureVar = DwarfEntity.TextureVar.getRandomVar();
    }

    public static DefaultAttributeContainer.Builder createDwarfAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D).add(EntityAttributes.GENERIC_MAX_HEALTH, 40);
    }


    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(2, new LookAroundGoal(this));
        this.goalSelector.add(3, new EscapeDangerGoal(this, 1.25D));
    }


    public DwarfEntity.ModelVar getModelVar() {
        return modelVar;
    }

    public DwarfEntity.TextureVar getTextureVar() {
        return textureVar;
    }

    @Override
    public void tick() {
        super.tick();
        this.tickReputation();
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        return ActionResult.SUCCESS;
    }

    @Override
    public void onDeath(DamageSource source) {
        if (source.getAttacker() instanceof PlayerEntity) {
            Box axisalignedbb = this.getBoundingBox().expand(10.0D, 8.0D, 10.0D);

            List<AbstractDwarfEntity> nearbyDwarfs = this.world.getEntitiesByClass(AbstractDwarfEntity.class, axisalignedbb, EntityPredicates.maxDistance(96, 96, 96, 96));
            for (AbstractDwarfEntity le : nearbyDwarfs) {
                AbstractDwarfEntity dw = le;
                dw.updateReputation(ReputationType.DEAD, 25, (PlayerEntity) source.getAttacker());
            }
        }
        super.onDeath(source);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getAttacker() instanceof PlayerEntity) {
            Box axisalignedbb = this.getBoundingBox().expand(10.0D, 8.0D, 10.0D);
            List<AbstractDwarfEntity> nearbyDwarfs = this.world.getEntitiesByClass(AbstractDwarfEntity.class, axisalignedbb, EntityPredicates.maxDistance(96, 96, 96, 96));
            for (AbstractDwarfEntity le : nearbyDwarfs) {
                AbstractDwarfEntity dw = le;
                dw.updateReputation(ReputationType.DEAD, 25, (PlayerEntity) source.getAttacker());
            }
            this.updateReputation(ReputationType.HURT, 25, (PlayerEntity) source.getAttacker());
        }
        return super.damage(source, amount);
    }


    private void tickReputation() {
        long i = this.world.getTime();
        if (this.lastReputationDecay == 0L)
            this.lastReputationDecay = i;
        else if (i >= this.lastReputationDecay + 24000L) {
            this.reputationManager.tick();
            this.lastReputationDecay = i;
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(WALK_ANIMATION_STR));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(IDLE_ANIMATION_STR, true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }


    @Override
    public void updateReputation(ReputationType type, int value, PlayerEntity target) {
        if (type == ReputationType.HURT) {
            this.reputationManager.add(target.getUuid(), ReputationDecayType.MINOR_NEGATIVE, value);
        } else if (type == ReputationType.DEAD)
            this.reputationManager.add(target.getUuid(), ReputationDecayType.MAJOR_NEGATIVE, value);
    }

    @Override
    public int getPlayerReputation(PlayerEntity player) {
        return this.reputationManager.getReputation(player.getUuid(), (type) -> true);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("Reputation", this.reputationManager.write(NbtOps.INSTANCE).getValue());
        nbt.putLong("LastReputationDecay", this.lastReputationDecay);
        if(this.textureVar != null)
            nbt.putString("TextureVar", this.textureVar.toString());
        if(this.modelVar != null)
            nbt.putString("ModelVar", this.modelVar.toString());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        NbtList listnbt = nbt.getList("Reputation", 10);
        this.reputationManager.read(new Dynamic<>(NbtOps.INSTANCE, listnbt));
        this.lastReputationDecay = nbt.getLong("LastReputationDecay");
        if(nbt.contains("TextureVar"))
            this.textureVar = TextureVar.valueOf(nbt.getString("TextureVar"));
        if(nbt.contains("ModelVar"))
            this.modelVar = ModelVar.valueOf(nbt.getString("ModelVar"));
    }

    public enum ModelVar {

        VAR0("dwarf.geo.json"),
        VAR1("dwarf_var3.geo.json"),
        VAR2("dwarf_var4.geo.json");

        public static final ModelVar[] VALUES = values();
        private static final Random RAND = new Random();

        private final String geoPath;

        ModelVar(String geoPath) {
            this.geoPath = geoPath;
        }

        public String getGeoPath() {
            return geoPath;
        }

        public static ModelVar getRandomVar() {
            return VALUES[RAND.nextInt(VALUES.length - 1)];
        }
    }

    public enum TextureVar {

        TEX0("entity/dwarf.png"),
        TEX1("entity/dwarf_var3_citizen1.png"),
        TEX2("entity/dwarf_var4_citizen1.png"),
        TEX3("entity/dwarf_var4_citizen2.png");

        public static final TextureVar[] VALUES = values();
        private static final Random RAND = new Random();

        private final String texPath;

        TextureVar(String texPath) {
            this.texPath = texPath;
        }

        public String getTexPath() {
            return texPath;
        }

        public static TextureVar getRandomVar() {
            return VALUES[RAND.nextInt(VALUES.length - 1)];
        }
    }

    public static class DwarfNames {

        private static final String[] NAMES = new String[]{
                "Ai",
                "Alfr",
                "Alfrigg",
                "Alfrikr",
                "Alius",
                "Altjofr",
                "Alviss",
                "An",
                "Anarr",
                "Andavari",
                "Atvarör",
                "Aurgrminir",
                "Aurvangr",
                "Austri",
                "Bafurr",
                "Bari",
                "Berling",
                "Bifurr",
                "Bildr",
                "Billingr",
                "Blainn",
                "Blindviör",
                "Blövurr",
                "Bömburr",
                "Brisingr",
                "Brokkr",
                "Bruni",
                "Buinn",
                "Buri",
                "Daginnr",
                "Dainn",
                "Dani",
                "Darri",
                "Dellingr",
                "Dolgr",
                "Dolgtrasir",
                "Döri",
                "Draupnir",
                "Dufr",
                "Duneyrr",
                "Durator",
                "Durinn",
                "Dumir",
                "Dvalinn",
                "Eggmoinn",
                "Eikinskjaldi",
                "Eilifr",
                "Eitri",
                "Fafnir",
                "Fainn",
                "Far",
                "Farli",
                "Fili",
                "Finnr",
                "Fjalar",
                "Fjolsvior",
                "Forvi",
                "Frægr",
                "Frar",
                "Friör",
                "Frosti",
                "Fullangr",
                "Fundinn",
                "Gandalfr",
                "Galar",
                "Ginnarr",
                "Gloi",
                "Gloni",
                "Gollmævill",
                "Grer",
                "Grimr",
                "Gud",
                "Gustr",
                "Hanarr",
                "Har",
                "Haugspori",
                "Hepti",
                "Heptifili",
                "Heri",
                "Herrauör",
                "Herriör",
                "Hildingr",
                "Hleöjofr",
                "Hlevangr",
                "Hljoöolfr",
                "Horr",
                "Hreiömarr",
                "Hustari",
                "Iri",
                "Ivaldi",
                "Jaki",
                "Jari",
                "Kili",
                "Liöskjalfr",
                "Litr",
                "Ljomi",
                "Lofarr",
                "Loinn",
                "Loni",
                "Miöviö",
                "Mjöövitnir",
                "Mjoklituö",
                "Moinn",
                "Mondull",
                "Motsognir",
                "Munin",
                "Nabbi",
                "Næfr",
                "Nainn",
                "Nali",
                "Nar",
                "Nefi",
                "Niöhoggr",
                "Niöi",
                "Nipingr",
                "Noröri",
                "Nori",
                "Nyl",
                "Nyr",
                "Nyraor",
                "oinn",
                "Olius",
                "Olni",
                "Onn",
                "Ori",
                "Otr",
                "Patti",
                "Raöspakr",
                "Raösviör",
                "Regin",
                "Rekkr",
                "Siarr",
                "Sindri",
                "Skavær",
                "Skafiör",
                "Skirfir",
                "Suöri",
                "Ivaldis son",
                "Sviurr",
                "tekkr",
                "Tigvi",
                "tjoðrœrir",
                "tjorr",
                "Töki",
                "Tolinn",
                "Torin",
                "Trainn",
                "Trasir",
                "Tror",
                "Tuta",
                "Uni",
                "uri",
                "Varr",
                "Vegdrasill",
                "Veggr",
                "Veigr",
                "Vestri",
                "Viör",
                "Vifir",
                "Viggr",
                "Vigr",
                "Vili",
                "Vindalfr",
                "Virwir",
                "Vitr",
                "Yngvi"
        };

        private static final Random RaNDOM = new Random();

        public static String getRandomName() {
            return NAMES[RaNDOM.nextInt(NAMES.length - 1)];
        }
    }
}
