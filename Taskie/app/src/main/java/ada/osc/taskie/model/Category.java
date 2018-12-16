package ada.osc.taskie.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.UUID;

import ada.osc.taskie.database.category.CategoryDaoImpl;
import ada.osc.taskie.database.category.SafeCategoryDaoImpl;

@DatabaseTable(tableName = Category.TABLE_NAME_CATEGORIES, daoClass = SafeCategoryDaoImpl.class)
public class Category implements Serializable {

    public static final String TABLE_NAME_CATEGORIES = "categories";
    public static final String CATEGORYNAME_FIELD_NAME = "name";

    @DatabaseField(id = true)
    private String mId = UUID.randomUUID().toString();
    @DatabaseField(columnName = CATEGORYNAME_FIELD_NAME)
    private String mName;

    public Category(){

    }

    public Category(String name){
        mName = name;
    }

    public String getId(){
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = mName;
    }

    @Override
    public String toString() {
        return mName;
    }
}
