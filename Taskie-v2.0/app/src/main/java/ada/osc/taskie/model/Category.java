package ada.osc.taskie.model;

import java.io.Serializable;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Category extends RealmObject implements Serializable {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();

    private String name;

    public Category(){
        name = "";
    }

    public Category(String name){
        this.name = name;
    }

    public String getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = this.name;
    }

    @Override
    public String toString() {
        return name;
    }
}
