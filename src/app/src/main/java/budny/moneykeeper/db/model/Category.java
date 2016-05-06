package budny.moneykeeper.db.model;

import io.realm.RealmObject;

public class Category extends RealmObject {
    public static final String FIELD_NAME = "mName";
    public static final String FIELD_TYPE = "mType";
    public enum Type {
        INCOME, OUTCOME
    }

    private String mName;
    // store enums as strings
    private String mType;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Type getType() {
        return Type.valueOf(mType);
    }

    public void setType(Type type) {
        mType = type.toString();
    }
}