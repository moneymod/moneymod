package wtf.moneymod.client.api.management.impl;

import com.google.common.reflect.ClassPath;
import net.minecraft.launchwrapper.Launch;
import org.reflections.Reflections;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.management.IManager;
import wtf.moneymod.client.impl.command.Command;
import wtf.moneymod.client.impl.module.Module;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cattyn
 * @since 11/02/21
 */

public class CommandManagement extends ArrayList<Command> implements IManager<CommandManagement> {

    String prefix = "$";

    @Override public CommandManagement register() {
        if( !Main.isLoaderPresent( ) )
        {
            new Reflections("wtf.moneymod.client.impl.command.impl").getSubTypesOf(Command.class).forEach(c -> {
                try {
                    add(c.newInstance());
                    System.out.println(c.getName());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        }
        else
        {
            try
            {
                Field f = Class.forName( "wtf.moneymod.loader.LoaderMod", false, Launch.classLoader ).getDeclaredField( "commands" );
                f.setAccessible( true );
                List< Class< ? > > comclasses = ( List< Class< ? > > )f.get( null );
                for( Class< ? > clazz : comclasses )
                {
                    if( !clazz.getName( ).contains( "$" ) )
                        add( ( Command )clazz.newInstance( ) );
                }
            }
            catch( Exception e )
            {
                e.printStackTrace( );
            }
        }
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

}
