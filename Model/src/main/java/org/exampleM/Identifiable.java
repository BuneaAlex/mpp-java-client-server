package org.exampleM;

import java.io.Serializable;

public interface Identifiable<Tid> extends Serializable {
    Tid getID();
    void setID(Tid id);
}
