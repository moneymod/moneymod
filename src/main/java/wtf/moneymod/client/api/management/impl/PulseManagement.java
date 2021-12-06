package wtf.moneymod.client.api.management.impl;

import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.global.Global;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PulseManagement
{
    private Global mod = ( Global )Main.getMain( ).getModuleManager( ).get( Global.class );

    public static int min = 110;
    public static int max = 255;
    private int current = 0;
    private int frame = 0;

    public List< Integer > values = new ArrayList< >( );

    public PulseManagement( )
    {
        for( int i = 0; i <= max * 2; i++ )
        {
            int value = i;
            if( value > max )
                value = max - ( i - max );

            values.add( value );
        }

        values = values.stream( ).filter( v -> v >= min && v <= max ).collect( Collectors.toList( ) );
    }

    public int getDifference( int niga )
    {
        frame++;

        int ret = current + frame + niga;
        if( ret >= values.size( ) )
            ret = ( values.size( ) - ret );

        ret = values.get( Math.abs( ret ) );

        return ret;
    }

    public void update( )
    {
        frame = 0;

        double frametime = Main.getMain( ).getFpsManagement( ).getFrametime( );
        int next = current + ( int )Math.ceil( ( values.size( ) / ( double )mod.pulseSpeed ) * frametime );
        if( next >= values.size( ) )
            next = ( values.size( ) - next );

        current = next;
    }
}
