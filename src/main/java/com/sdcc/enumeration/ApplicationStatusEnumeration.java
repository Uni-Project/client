package com.sdcc.enumeration;

public enum ApplicationStatusEnumeration {

    STARTED, //il middleware può offrirla ai nodi fog
    STOPPED,  //il middleware non può offrirla ai nodi fog...l'app viene rimossa da tutti i fog, ma rimane salvata nel db del middleware il link.
    INVALID
}
