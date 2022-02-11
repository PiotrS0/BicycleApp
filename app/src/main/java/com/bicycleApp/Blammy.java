package com.bicycleApp;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Serializable;
import java.util.LinkedList;

public class Blammy implements Serializable {

    private LinkedList<LatLng> list;

    public Blammy(final PolylineOptions polylineOptions, LinkedList<LatLng> list)
    {
        this.list = list;
        //retrieve all values and store in Blammy class members.
    }

    public PolylineOptions generatePolylineOptions()
    {
        PolylineOptions returnValue = new PolylineOptions();

        // set all polyline options values.


        return returnValue;
    }




    /*
    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException
    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException;
    private void readObjectNoData()
            throws ObjectStreamException;
     */

}
