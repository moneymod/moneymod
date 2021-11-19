package wtf.moneymod.client.impl.module.render;


import com.mojang.authlib.GameProfile;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.TotemPopEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Module.Register( label = "PopChams", cat = Module.Category.RENDER)
public class PopChams extends Module {

    @Value( value = "Color" ) public JColor color = new JColor(0, 255, 0, true);
    @Value( value = "Self" ) public boolean self = false;
    @Value(value = "Copy Animations") public boolean anim = true;
    @Value( value = "MaxOffset" ) @Bounds(max = 15) public double maxOffset = 5d;
    @Value( value = "Speed" ) @Bounds(max = 5) public double speed = 1d;

    public final CopyOnWriteArrayList<Person> popList = new CopyOnWriteArrayList<>( );

    @Handler
    public Listener<TotemPopEvent> onPop = new Listener<>(TotemPopEvent.class, e -> {
        if ( !self ) {
            if ( e.getEntityPlayerSP( ) == mc.player ) return;
        }
        EntityPlayer sp = e.getEntityPlayerSP();
        EntityPlayer entity = new EntityPlayer( mc.world, new GameProfile( sp.getUniqueID( ), sp.getName( ) ) ) {
            @Override public boolean isSpectator ( ) {return false;}

            @Override public boolean isCreative ( ) {return false;}
        };
        entity.copyLocationAndAnglesFrom( sp );

        if(anim) {
            entity.limbSwing = sp.limbSwing;
            entity.limbSwingAmount = sp.limbSwingAmount;
            entity.setSneaking(sp.isSneaking());
        }
        popList.add( new Person( entity ) );
    });

    @Override public void onRender3D(float partialTicks) {
        GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
        GlStateManager.tryBlendFuncSeparate( GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO );
        GlStateManager.glLineWidth( 1.5F );
        GlStateManager.disableTexture2D( );
        GlStateManager.depthMask( false );
        GlStateManager.enableBlend( );
        GlStateManager.disableDepth( );
        GlStateManager.disableLighting( );
        GlStateManager.disableCull( );
        GlStateManager.enableAlpha( );
        popList.forEach( person -> {
            person.update( popList );
            person.modelPlayer.bipedLeftLegwear.showModel = false;
            person.modelPlayer.bipedRightLegwear.showModel = false;
            person.modelPlayer.bipedLeftArmwear.showModel = false;
            person.modelPlayer.bipedRightArmwear.showModel = false;
            person.modelPlayer.bipedBodyWear.showModel = false;
            person.modelPlayer.bipedHead.showModel = true;
            person.modelPlayer.bipedHeadwear.showModel = false;
            GlStateManager.color( color.getColor().getRed() / 255f, color.getColor().getGreen( ) / 255f, color.getColor().getBlue( ) / 255f, ( float ) person.alpha / 255f );
            GL11.glPolygonMode( GL11.GL_FRONT_AND_BACK, GL11.GL_FILL );
            renderEntity( person.player, person.modelPlayer, person.player.limbSwing,
                    person.player.limbSwingAmount, person.player.ticksExisted, person.player.rotationYawHead, person.player.rotationPitch, 1 );


            GL11.glPolygonMode( GL11.GL_FRONT_AND_BACK, GL11.GL_LINE );
            renderEntity( person.player, person.modelPlayer, person.player.limbSwing,
                    person.player.limbSwingAmount, person.player.ticksExisted, person.player.rotationYawHead, person.player.rotationPitch, 1 );

            GL11.glPolygonMode( GL11.GL_FRONT_AND_BACK, GL11.GL_FILL );
        } );
        GlStateManager.enableCull( );
        GlStateManager.depthMask( true );
        GlStateManager.enableTexture2D( );
        GlStateManager.enableBlend( );
        GlStateManager.enableDepth( );
    }

    private class Person {
        private double alpha, ticks;
        private final EntityPlayer player;
        private final ModelPlayer modelPlayer;

        public Person ( EntityPlayer player ) {
            this.player = player;
            this.modelPlayer = new ModelPlayer( 0, false );
            this.alpha = 180;
            this.ticks = 0;
        }

        public void update ( CopyOnWriteArrayList<Person> arrayList ) {
            ticks++;
            if ( alpha <= 0 ) {
                arrayList.remove( this );
                mc.world.removeEntity( player );
                return;
            }
            this.alpha -= 180 / speed * Main.getMain().getFpsManagement().getFrametime( );
            player.posY += maxOffset / speed * Main.getMain().getFpsManagement().getFrametime( );
        }
    }

    public static void renderEntity (EntityLivingBase entity, ModelBase modelBase, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale ) {
        if ( modelBase instanceof ModelPlayer ) {
            ModelPlayer modelPlayer = ( ( ModelPlayer ) modelBase );
            modelPlayer.bipedBodyWear.showModel = false;
            modelPlayer.bipedLeftLegwear.showModel = false;
            modelPlayer.bipedRightLegwear.showModel = false;
            modelPlayer.bipedLeftArmwear.showModel = false;
            modelPlayer.bipedRightArmwear.showModel = false;
            modelPlayer.bipedHeadwear.showModel = true;
            modelPlayer.bipedHead.showModel = false;
        }

        float partialTicks = mc.getRenderPartialTicks( );
        double x = entity.posX - mc.getRenderManager( ).viewerPosX;
        double y = entity.posY - mc.getRenderManager( ).viewerPosY;
        double z = entity.posZ - mc.getRenderManager( ).viewerPosZ;

        GlStateManager.pushMatrix( );

        if ( entity.isSneaking( ) ) {
            y -= 0.125D;
        }
        GlStateManager.translate( ( float ) x, ( float ) y, ( float ) z );
        GlStateManager.rotate( 180 - entity.rotationYaw, 0, 1, 0 );
        float f4 = prepareScale( entity, scale );
        float yaw = entity.rotationYawHead;

        GlStateManager.enableAlpha( );
        modelBase.setLivingAnimations( entity, limbSwing, limbSwingAmount, partialTicks );
        modelBase.setRotationAngles( limbSwing, limbSwingAmount, 0, yaw, entity.rotationPitch, f4, entity );
        modelBase.render( entity, limbSwing, limbSwingAmount, 0, yaw, entity.rotationPitch, f4 );

        GlStateManager.popMatrix( );
    }

    private static float prepareScale ( EntityLivingBase entity, float scale ) {
        GlStateManager.enableRescaleNormal( );
        GlStateManager.scale( -1.0F, -1.0F, 1.0F );
        double widthX = entity.getRenderBoundingBox( ).maxX - entity.getRenderBoundingBox( ).minX;
        double widthZ = entity.getRenderBoundingBox( ).maxZ - entity.getRenderBoundingBox( ).minZ;

        GlStateManager.scale( scale + widthX, scale * entity.height, scale + widthZ );
        float f = 0.0625F;

        GlStateManager.translate( 0.0F, -1.501F, 0.0F );
        return f;
    }


}
