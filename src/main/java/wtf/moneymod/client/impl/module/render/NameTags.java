package wtf.moneymod.client.impl.module.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.math.MathUtil;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.utility.impl.render.Renderer2D;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorRenderManager;

import java.awt.*;
import java.util.HashMap;

@Module.Register( label = "NameTags", cat = Module.Category.RENDER)
public class NameTags extends Module {

    @Value(value = "Size") @Bounds(min = 0.1f,max = 1) public float sizeNameTags = 0.5f;
    @Value(value = "Range") @Bounds(min = 80,max = 300) public int range = 300;
    @Value(value = "Thickness") @Bounds(min = 0.1f,max = 3.0f) public float thickness = 1f;

    @Value(value = "Fill Color") public JColor fillColor = new JColor(0, 0, 0, false);
    @Value( value = "Outline Color") public JColor outlineColor = new JColor(0, 0, 0, false);
    @Value(value = "Text Color") public JColor textColor = new JColor(255, 255, 255, false);

    @Value(value = "Fill") public boolean fill = true;
    @Value(value = "Outline") public boolean outline = false;
    @Value(value = "Self") public boolean self = false;
    @Value(value = "Name") public boolean name = true;
    @Value(value = "Friend") public boolean friend = true;
    @Value(value = "Ping") public boolean ping = true;
    @Value(value = "Health") public boolean health = true;
    @Value(value = "HealthColor") public boolean healthcolor = true;
    @Value(value = "Gamemode") public boolean gamemode = false;
    @Value(value = "Totem") public boolean totems = false;
    @Value(value = "Items") public boolean items = true;
    @Value(value = "MainHand") public boolean mainhand = true;
    @Value(value = "Offhand") public boolean offhand = true;
    @Value(value = "Armor") public boolean armor = true;
    @Value(value = "Armor Dura") public boolean armorDura = true;

    AccessorRenderManager renderManager = ( AccessorRenderManager ) mc.getRenderManager( );
    HashMap<String, Integer> totemPops = new HashMap<>( );

    /*
    @SubscribeEvent public void onTotemPop( TotemPopEvent event ) {
        String name = event.getEntityPlayerSP( ).getName( );
        if ( totemPops.get( name ) == null ) {
            totemPops.putIfAbsent( name, 1 );
            return;
        }
        totemPops.replace( name, totemPops.get( name ) + 1 );
    }
   
     */

    @SubscribeEvent public void onTick( TickEvent.ClientTickEvent event ) {
        if ( event.phase == TickEvent.Phase.START ) return;
        if ( nullCheck( ) ) {
            totemPops.clear( );
            return;
        }
        mc.world.loadedEntityList.forEach( e -> {
            if ( e instanceof EntityPlayer ) {
                if ( totemPops.containsKey( e.getName( ) ) && ( ( ( ( EntityPlayer ) e ).getHealth( ) ) <= 0 || e.isDead || mc.player.getDistance( e ) >= range ) ) {
                    totemPops.remove( e.getName( ) );
                }
            }
        } );

    }

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent event ) {
        for ( Entity player : mc.world.loadedEntityList ) {
            if ( !( player instanceof EntityPlayer ) || player.isDead || !( ( ( EntityPlayer ) player ).getHealth( ) > 0.0f ) || mc.player.getDistance( player ) > range )
                continue;
            if ( player == mc.player ) {
                if ( self ) renderNameTage( ( EntityPlayer ) player );
                continue;
            }
            renderNameTage( ( EntityPlayer ) player );
        }
    }

    private void renderNameTage( EntityPlayer player ) {
        if ( mc.getRenderViewEntity( ) == null ) return;
        double x = MathUtil.INSTANCE.INSTANCE.interpolate( player.lastTickPosX, player.posX, mc.getRenderPartialTicks( ) ) - renderManager.getRenderPosX( );
        double y = MathUtil.INSTANCE.interpolate( player.lastTickPosY, player.posY, mc.getRenderPartialTicks( ) ) - renderManager.getRenderPosY( ) + ( player.isSneaking( ) ? 0.5 : 0.7 );
        double z = MathUtil.INSTANCE.interpolate( player.lastTickPosZ, player.posZ, mc.getRenderPartialTicks( ) ) - renderManager.getRenderPosZ( );
        double delta = mc.getRenderPartialTicks( );
        Entity localPlayer = mc.getRenderViewEntity( );
        double originalPositionX = localPlayer.posX;
        double originalPositionY = localPlayer.posY;
        double originalPositionZ = localPlayer.posZ;
        localPlayer.posX = MathUtil.INSTANCE.interpolate( localPlayer.prevPosX, localPlayer.posX, delta );
        localPlayer.posY = MathUtil.INSTANCE.interpolate( localPlayer.prevPosY, localPlayer.posY, delta );
        localPlayer.posZ = MathUtil.INSTANCE.interpolate( localPlayer.prevPosZ, localPlayer.posZ, delta );
        String tag = getTagString( player );
        double distance = localPlayer.getDistance( x + mc.getRenderManager( ).viewerPosX, y + mc.getRenderManager( ).viewerPosY, z + mc.getRenderManager( ).viewerPosZ );
        int width = mc.fontRenderer.getStringWidth( tag ) >> 1;
        double scale = ( float ) ( ( ( distance / 5 <= 2 ? 2.0F : ( distance / 5 ) * ( ( sizeNameTags ) + 1 ) ) * 2.5f ) * ( sizeNameTags / 100 ) );
        if ( distance <= 8.0 ) {
            scale = 0.0245;
        }

        GlStateManager.pushMatrix( );
        RenderHelper.enableStandardItemLighting( );
        GlStateManager.enablePolygonOffset( );
        GlStateManager.doPolygonOffset( 1.0f, -1500000f );
        GlStateManager.disableLighting( );
        GlStateManager.translate( x, y + 1.4f, z );
        GlStateManager.rotate( -mc.getRenderManager( ).playerViewY, 0.0f, 1.0f, 0.0f );
        GlStateManager.rotate( mc.getRenderManager( ).playerViewX, mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f, 0.0f, 0.0f );
        GlStateManager.scale( -scale, -scale, scale );
        GL11.glDepthRange( 0, 0.1 );

        if ( fill ) {
            Renderer2D.drawRect( -width - 2, -( this.mc.fontRenderer.FONT_HEIGHT + 1 ), ( float ) width + 2.0f, 1.5f, fillColor.getColor().getRGB() );
        }
        if ( outline ) {
            Renderer2D.drawOutline( -width - 2, -( this.mc.fontRenderer.FONT_HEIGHT + 1 ), ( float ) width + 2.0f, 1.5f, (float)thickness, outlineColor.getColor( ).getRGB() );
        }

        if ( items ) {
            GlStateManager.pushMatrix( );
            int xOffset = -8;
            for ( int i = 0; i < 4; ++i ) {
                xOffset -= 8;
            }
            ItemStack renderOffhand = player.getHeldItemOffhand( ).copy( );
            xOffset -= 8;
            if ( offhand ) {
                renderItemStack( renderOffhand, xOffset );
                renderDurabilityLabel( renderOffhand, xOffset, -50 );
            }
            xOffset += 16;
            for ( int i = 0; i < 4; ++i ) {
                if ( armor ) renderItemStack( player.inventory.armorInventory.get( i ).copy( ), xOffset );
                if ( armorDura ) {
                    renderDurabilityLabel( player.inventory.armorInventory.get( i ).copy( ), xOffset, armor ? -50 : -21 );
                }
                xOffset += 16;
            }
            if ( mainhand ) {
                renderItemStack( player.getHeldItemMainhand( ).copy( ), xOffset );
                renderDurabilityLabel( player.getHeldItemMainhand( ).copy( ), xOffset, -50 );
            }
            GlStateManager.popMatrix( );
        }

        mc.fontRenderer.drawStringWithShadow( tag, -width, -8.0f, textColor.getColor().getRGB() );
        localPlayer.posX = originalPositionX;
        localPlayer.posY = originalPositionY;
        localPlayer.posZ = originalPositionZ;
        GL11.glDepthRange( 0, 1 );
        GlStateManager.disableBlend( );
        GlStateManager.disablePolygonOffset( );
        GlStateManager.doPolygonOffset( 1.0f, 1500000f );
        GlStateManager.popMatrix( );
    }

    private void renderItemStack( ItemStack stack, int x ) {
        GlStateManager.depthMask( true );
        GlStateManager.clear( 256 );
        RenderHelper.enableStandardItemLighting( );
        mc.getRenderItem( ).zLevel = -150.0f;
        GlStateManager.disableAlpha( );
        GlStateManager.enableDepth( );
        GlStateManager.disableCull( );
        mc.getRenderItem( ).renderItemAndEffectIntoGUI( stack, x, -27 );
        mc.getRenderItem( ).renderItemOverlays( this.mc.fontRenderer, stack, x, -27 );
        mc.getRenderItem( ).zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting( );
        GlStateManager.enableCull( );
        GlStateManager.enableAlpha( );
        GlStateManager.scale( 0.5f, 0.5f, 0.5f );
        GlStateManager.disableDepth( );
        renderEnchantmentLabel( stack, x, -27 );
        GlStateManager.enableDepth( );
        GlStateManager.scale( 2.0f, 2.0f, 2.0f );
    }

    private void renderEnchantmentLabel( ItemStack stack, int x, int y ) {
        int enchantmentY = y - 8;
        NBTTagList enchants = stack.getEnchantmentTagList( );
        for ( int index = 0; index < enchants.tagCount( ); ++index ) {
            short value = enchants.getCompoundTagAt( index ).getShort( "value" );
            short level = enchants.getCompoundTagAt( index ).getShort( "lvl" );
            Enchantment enc = Enchantment.getEnchantmentByID( ( int ) value );
            if ( enc == null || enc.getName( ).contains( "fall" ) || !enc.getName( ).contains( "all" ) && !enc.getName( ).contains( "explosion" ) )
                continue;
            mc.fontRenderer.drawStringWithShadow( enc.isCurse( ) ? TextFormatting.RED + enc.getTranslatedName( ( int ) level ).substring( 11 ).substring( 0, 1 ).toLowerCase( ) : enc.getTranslatedName( ( int ) level ).substring( 0, 1 ).toLowerCase( ) + level, ( float ) ( x * 2 ), ( float ) enchantmentY, -1 );
            enchantmentY -= 8;
        }
    }

    private void renderDurabilityLabel( ItemStack stack, int x, int y ) {
        GlStateManager.scale( 0.5f, 0.5f, 0.5f );
        GlStateManager.disableDepth( );
        if ( stack.getItem( ) instanceof ItemArmor || stack.getItem( ) instanceof ItemSword || stack.getItem( ) instanceof ItemTool) {
            //Definitely not pasted from xulu
            float green = ( ( float ) stack.getMaxDamage( ) - ( float ) stack.getItemDamage( ) ) / ( float ) stack.getMaxDamage( );
            float red = 1 - green;
            int dmg = 100 - ( int ) ( red * 100 );
            // ^^^
            mc.fontRenderer.drawStringWithShadow( dmg + "%", x * 2 + 4, y - 10, new Color( ( int ) ( red * 255 ), ( int ) ( green * 255 ), 0 ).getRGB( ) );
        }
        GlStateManager.enableDepth( );
        GlStateManager.scale( 2.0f, 2.0f, 2.0f );
    }

    private String getTagString( EntityPlayer player ) {
        StringBuilder sb = new StringBuilder( );

        if ( Main.getMain().getFriendManagement().is( player.getName( ) ) && friend ) sb.append( ChatFormatting.AQUA );

        if ( ping ) {
            try {
                NetworkPlayerInfo npi = mc.player.connection.getPlayerInfo( player.getGameProfile( ).getId( ) );
                sb.append( npi.getResponseTime( ) );
            } catch ( Exception e ) {
                sb.append("0");
            }
            sb.append( "ms" );
        }

        if ( name ) {
            sb.append( " " ).append( player.getName( ) );
        }

        if ( health ) {
            if ( healthcolor ) {
                sb.append( getHealthColor( EntityUtil.getHealth( player ) ) );
            }
            sb.append( " " ).append( ( int ) EntityUtil.getHealth( player ) ).append( ChatFormatting.RESET );
        }

        if ( Main.getMain().getFriendManagement().is( player.getName( ) ) && friend ) sb.append( ChatFormatting.AQUA );

        if ( gamemode ) {
            sb.append( " [" );
            try {
                String sus = getShortName( mc.player.connection.getPlayerInfo( player.getGameProfile( ).getId( ) ).getGameType( ).getName( ) );
                sb.append( sus );
            } catch ( Exception ignored ) {
                sb.append( "S" );
            }
            sb.append( "]" );
        }

        if ( totems ) {
            totemPops.putIfAbsent( player.getName( ), 0 );
            if ( totemPops.get( player.getName( ) ) != 0 )
                sb.append( " -" ).append( totemPops.get( player.getName( ) ) );
        }

        return sb.toString( );
    }

    private ChatFormatting getHealthColor( double health ) {
        if ( health >= 20 ) return ChatFormatting.GREEN;
        else if ( health >= 16 ) return ChatFormatting.DARK_GREEN;
        else if ( health >= 10 ) return ChatFormatting.GOLD;
        else if ( health >= 4 ) return ChatFormatting.RED;
        else return ChatFormatting.DARK_RED;
    }

    private String getShortName( String gameType ) {
        if ( gameType.equalsIgnoreCase( "survival" ) ) return "S";
        else if ( gameType.equalsIgnoreCase( "creative" ) ) return "C";
        else if ( gameType.equalsIgnoreCase( "adventure" ) ) return "A";
        else if ( gameType.equalsIgnoreCase( "spectator" ) ) return "SP";
        else return "NONE";
    }
}