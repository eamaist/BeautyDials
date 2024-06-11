package com.example.BeautyDials;

import android.provider.BaseColumns;

public final class SQLiteTable {
    private SQLiteTable(){}


    public static class User implements BaseColumns {
        public static final String TABLE_NAME = "User";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_SELLER = "Seller";
        public static final String COLUMN_PHONE = "Phone";
        public static final String COLUMN_PASSWORD = "Password";
        public static final String COLUMN_IMAGE = "Image";
        public static final String COLUMN_USER_LOGIN = "User_Login"; // новый столбец

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS \"" + TABLE_NAME + "\" (" +
                "\"" + COLUMN_ID + "\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "\"" + COLUMN_NAME + "\" TEXT, " +
                "\"" + COLUMN_IMAGE + "\" BLOB, " +
                "\"" + COLUMN_SELLER + "\" INTEGER DEFAULT (0) NOT NULL, " +
                "\"" + COLUMN_PHONE + "\" DEFAULT (89999999999) NOT NULL, " +
                "\"" + COLUMN_PASSWORD + "\" TEXT DEFAULT (123) NOT NULL, " +
                "\"" + COLUMN_USER_LOGIN + "\" TEXT NOT NULL);"; // добавлено описание нового столбца

        public static final String[] FILL_DATA = {
                "INSERT INTO User VALUES(1,'Олеся',NULL,0,'89999999999','123','user1@mail.ru');",
                "INSERT INTO User VALUES(2,'Наташа',NULL,1,'89999999991','123','user2@mail.ru');",
                "INSERT INTO User VALUES(3,'Марина',NULL,1,'89999999992','123','user3mail.ru');",
                "INSERT INTO User VALUES(4,'Света',NULL,0,'89999999993','123','user4mail.ru');",
                "INSERT INTO User VALUES(5,'Юля',NULL,0,'89999999994','123','user5mail.ru');"
        };
    }


    public static class Ads implements BaseColumns{
        public static final String TABLE_NAME = "Ads";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_USER = "User_ID";
        public static final String COLUMN_DESCRIPTIONS = "Descriptions";
        public static final String COLUMN_PRICE = "Price";
        public static final String COLUMN_IMAGE = "Image";
        public static final String COLUMN_RELEVANT = "Relevant";

        public static final String CREATE_TABLE ="CREATE TABLE IF NOT EXISTS \""+TABLE_NAME+"\" (\n" +
                "\""+COLUMN_ID+"\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\""+COLUMN_USER+"\" INTEGER, "+COLUMN_RELEVANT+" INTEGER DEFAULT (1) NOT NULL, "+COLUMN_NAME+" TEXT, "+COLUMN_PRICE+" INTEGER, "+COLUMN_IMAGE+" BLOB, "+COLUMN_DESCRIPTIONS+" TEXT,\n" +
                "\tFOREIGN KEY (\""+COLUMN_USER+"\") REFERENCES \"User\"("+COLUMN_ID+") ON DELETE CASCADE\n" +
                ");";
        public static final String[] FILL_DATA ={
                "INSERT INTO Ads (ID, User_ID, Relevant, Name, Price, Image, Descriptions) VALUES (1, 2, 1, 'Френч', 2000, NULL, 'Френч маникюр - это классический и элегантный вид маникюра, который подходит для любого случая и образа.');\n",
                "INSERT INTO Ads (ID, User_ID, Relevant, Name, Price, Image, Descriptions) VALUES(2,2,1,'Крутая прическа',1000000,NULL,'на выпускной');\n",
                "INSERT INTO Ads (ID, User_ID, Relevant, Name, Price, Image, Descriptions) VALUES(3,3,1,'Однотон',1000,NULL,'Для бедных');\n",
                "INSERT INTO Ads (ID, User_ID, Relevant, Name, Price, Image, Descriptions) VALUES(4,4,1,'Наращивание',3000,NULL,'Грабли лишними не будут');\n",
                "INSERT INTO Ads (ID, User_ID, Relevant, Name, Price, Image, Descriptions) VALUES(5,5,1,'Уход за волосами',1000000000,NULL,'Хотите дешево - побрейтесь налысо');\n",
                "INSERT INTO Ads (ID, User_ID, Relevant, Name, Price, Image, Descriptions) VALUES(7,2,1,'Педикюр',5000,NULL,'Раз в год можно');\n"};

    }
    public static class Favourites implements BaseColumns{
        public static final String TABLE_NAME = "Favourites";
        public static final String COLUMN_AD = "Ad_ID";
        public static final String COLUMN_USER = "User_ID";
        public static final String CREATE_TABLE ="CREATE TABLE IF NOT EXISTS \""+TABLE_NAME+"\" (\n" +
                "  \""+COLUMN_USER+"\" INTEGER,\n" +
                "  \""+COLUMN_AD+"\" INTEGER,\n" +
                "   FOREIGN KEY (\""+COLUMN_USER+"\") REFERENCES \"User\"(id) ON DELETE CASCADE,\n" +
                "   FOREIGN KEY (\""+COLUMN_AD+"\") REFERENCES \"Ads\"(id) ON DELETE CASCADE\n" +
                ");";
        public static final String[] FILL_DATA ={"INSERT INTO Favourites VALUES(2,3);\n",
                "INSERT INTO Favourites VALUES(2,4);\n",
                "INSERT INTO Favourites VALUES(2,5);"};
    }
}
