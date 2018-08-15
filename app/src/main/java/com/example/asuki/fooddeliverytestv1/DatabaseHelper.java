package com.example.asuki.fooddeliverytestv1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Asuki on 2017. 11. 20..
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // region DataTags
    private static final String TAG = "DatabaseHelper";

    // region Tables
    public static final String FOOD_TABLE = "food";
    public static final String DELIVERS_TABLE = "delivers";
    public static final String ORDERS_TABLE = "orders";
    public static final String CLIENTS_TABLE = "clients";
    public static final String CLIENT_ORDER_TABLE = "clients_order_by";
    public static final String REGISTRATIONS_TABLE = "registrations";
    public static final String DELIVERY_TABLE = "delivery_items";
    public static final String SUPPLIERS_FOOD_TABLE = "sfood";
    public static final String SUPPLIERS_CLIENTS_TABLE =  "sclients";
    // endregion

    private static final String DATABASE_NAME = "db_food_delivery";
    private static final int DATABASE_VERSION = 1;

    public final int REGISTRATION_SUPPLIER_FIELD = 2;
    public final int REGISTRATION_RESTAURANT_FIELD = 1;
    public final int ID_POSITION = 0;

    // region Tables columns
    private static final String ID = " id ";
    private static final String NAME = " name ";
    private static final String DESCRIPTION = " description ";
    private static final String ADDRESS = " address ";
    private static final String MOBILE = " mobile ";
    private static final String EMAIL = " email ";

    // ** Food special columns
    private static final String FOOD_CODE = " code ";

    // ** Clients special columns
    private static final String SUPPLIER_ID = " supplier_id ";

    // ** Delivery special columns
    private static final String DELIVERY_CLIENT_ID = "client_id";
    private static final String DELIVERY_CLIENT_NAME = "client_name";
    private static final String DELIVERY_CLIENT_ADDRESS = "client_address";
    private static final String DELIVERY_CLIENT_MOBILE = "client_mobile";
    private static final String DELIVERY_CLIENT_ORDER_NUMBER = "client_order_number";
    private static final String DELIVERY_FOOD_CODE = "food_code";
    private static final String DELIVERY_FOOD_NAME = "food_name";
    // endregion

    // region tables base creations
    // ** General columns
    private static final String CREATE_TABLE = " create table if not exists ";
    private static final String CREATE_ID = ID + " integer primary key autoincrement ";
    private static final String CREATE_NAME = NAME + " varchar(200) ";
    private static final String CREATE_DESCRIPTION = DESCRIPTION + " varchar(200) ";
    private static final String CREATE_ADDRESS = ADDRESS + " varchar(250) ";
    private static final String CREATE_MOBILE = MOBILE + " varchar(20) ";
    private static final String CREATE_EMAIL = EMAIL + " varchar(100) ";
    // ** Special columns
    private static final String CREATE_FOOD_CODE = FOOD_CODE + " character(20) unique ";
    private static final String CREATE_SUPPLIER_ID = SUPPLIER_ID + " integer ";
    // endregion

    // region Create tables
    // ** Food table
    private static final String CREATE_FOOD_TABLE = CREATE_TABLE + FOOD_TABLE + " ( " +
            CREATE_ID + " , " +
            CREATE_FOOD_CODE + " , " +
            CREATE_NAME + " , " +
            CREATE_DESCRIPTION + " ); ";

    // ** Suppliers food table
    private static final String CREATE_SUPPLIERS_FOOD_TABLE = CREATE_TABLE + SUPPLIERS_FOOD_TABLE + " ( " +
            CREATE_ID + " , " +
            CREATE_FOOD_CODE + " , " +
            CREATE_NAME + " , " +
            CREATE_DESCRIPTION + " ); ";

    // ** Clients table
    private static final String CREATE_CLIENTS_TABLE = CREATE_TABLE + CLIENTS_TABLE + " ( " +
            CREATE_ID + " , " +
            CREATE_NAME + " , " +
            CREATE_ADDRESS + " , " +
            CREATE_MOBILE + " , " +
            CREATE_EMAIL + " , " +
            CREATE_SUPPLIER_ID + " ); ";

    // ** Suppliers clients table
    private static final String CREATE_SUPPLIERS_CLIENTS_TABLE = CREATE_TABLE + SUPPLIERS_CLIENTS_TABLE + " ( " +
            CREATE_ID + " , " +
            CREATE_NAME + " , " +
            CREATE_ADDRESS + " , " +
            CREATE_MOBILE + " , " +
            CREATE_EMAIL + " , " +
            CREATE_SUPPLIER_ID + " ); ";

    // ** Delivery table
    private static final String CREATE_DELIVERY_TABLE = "create table if not exists " + DELIVERY_TABLE + "( " +
            CREATE_ID + " , " +
            DELIVERY_CLIENT_ID + " integer, " +
            DELIVERY_CLIENT_NAME + " varchar(50), " +
            DELIVERY_CLIENT_ADDRESS + " varchar(250), " +
            DELIVERY_CLIENT_MOBILE + " varchar(20), " +
            DELIVERY_CLIENT_ORDER_NUMBER + " integer, " +
            DELIVERY_FOOD_CODE + " character(20), " +
            DELIVERY_FOOD_NAME + " varchar(200));";
    //endregion


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //endregion

    @Override
    public void onCreate(SQLiteDatabase db) {
        // delivers_table
        String createTable;
        createTable = "create table if not exists " + DELIVERS_TABLE + "( " +
                "  id integer primary key autoincrement, " +
                "  deliver_name varchar(50) unique " +
                ")";
        db.execSQL(createTable);
        Log.d(TAG, "onCreate: create " + DELIVERS_TABLE + " table + " + createTable);

        // food_table
        db.execSQL(CREATE_FOOD_TABLE);
        Log.d(TAG, "onCreate: create " + FOOD_TABLE + " table + " + CREATE_FOOD_TABLE);

        // suppliers_food_table
        db.execSQL(CREATE_SUPPLIERS_FOOD_TABLE);
        Log.d(TAG, "onCreate: create " + FOOD_TABLE + " table + " + CREATE_SUPPLIERS_FOOD_TABLE);

        // clients_table
        db.execSQL(CREATE_CLIENTS_TABLE);
        Log.d(TAG, "onCreate: create " + CLIENTS_TABLE + " table" + CREATE_CLIENTS_TABLE);

        // suppliers_clients_table
        db.execSQL(CREATE_SUPPLIERS_CLIENTS_TABLE);
        Log.d(TAG, "onCreate: create " + SUPPLIERS_CLIENTS_TABLE + " table" + CREATE_SUPPLIERS_CLIENTS_TABLE);

        // orders_table
        createTable = "CREATE TABLE " + ORDERS_TABLE + " ( " +
                "  id integer primary key autoincrement, " +
                "  deliver_id int not null, " +
                "  client_id int not null, " +
                "  food_id int not null, " +
                "  order_date date not null " +
                ")";
        db.execSQL(createTable);
        Log.d(TAG, "onCreate: create " + ORDERS_TABLE + " table " + createTable);

        // registrations_table
        createTable = "create table if not exists " + REGISTRATIONS_TABLE + "( " +
                "  id integer primary key autoincrement, " +
                "  restaurant_name varchar(200), " +
                "  deliver_name varchar(50), " +
                "  loged_in integer default 0)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DELIVERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FOOD_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CLIENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ORDERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CLIENT_ORDER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + REGISTRATIONS_TABLE);
        onCreate(db);
    }


    // region Supplier functions
    /**
     * Add a new supplier into the suppliers table
     * @param name The name of the supplier what you want to insert into the table
     * @return false if insert failed, true otherwise
     */
    public boolean addSupplier(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("deliver_name", name);

        Log.d(TAG, "addOrder: Adding " + name + DELIVERS_TABLE);
        long result = db.insert(DELIVERS_TABLE, null, contentValues);
        if (-1 == result) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param name
     * @return
     */
    public Cursor getSupplierID(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DELIVERS_TABLE + " WHERE deliver_name = '" + name + "'";
        Log.d(TAG, "getSupplierID: query " + query);
        Cursor data = db.rawQuery(query, null);
        Log.d(TAG, "getSupplierID: query run successfully");
        return data;
    }

    public boolean modifySupplierName(String supplier, String newSupplierName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("deliver_name", newSupplierName);
        long result = db.update(DELIVERS_TABLE, contentValues, " deliver_name = ?", new String[]{supplier});
        Log.d(TAG, result + "");
        if (-1 == result)
            return false;
        else
            return true;
    }

    /**
     *
     * @param name
     */
    public void deleteSupplier(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DELIVERS_TABLE, "deliver_name = ?", new String[]{name});
    }

    public Cursor getSupplier(int id){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + DELIVERS_TABLE + " WHERE id = " + id + " order by deliver_name";
        Cursor data = db.rawQuery(query, null);

        return data;
    }
    // endregion

    // region Order functions
    /**
     * @param deliverID
     * @param clientID
     * @param foodID
     * @param orderDate
     * @return
     */
    public boolean addOrder(int deliverID, int clientID, int foodID, String orderDate) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("deliver_id", deliverID);
        contentValues.put("client_id", clientID);
        contentValues.put("food_id", foodID);
        contentValues.put("order_date", orderDate);

        Log.d(TAG, "addOrder: Adding " + deliverID + " " + clientID + " " + foodID + " " +
                orderDate + " to " + ORDERS_TABLE);
        long result = db.insert(ORDERS_TABLE, null, contentValues);
        if (-1 == result) {
            return false;
        }
        return true;
    }
    // endregion

    // region Registration functions
    public boolean addRegistration(String deliveryName, String restaurant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("deliver_name", deliveryName);
        contentValues.put("restaurant_name", restaurant);

        Log.d(TAG, "addRegistration: Adding '" + deliveryName + "' '" + restaurant + "' to "
                + REGISTRATIONS_TABLE);
        long result = db.insert(REGISTRATIONS_TABLE, null, contentValues);
        if (-1 == result)
            return false;
        return true;
    }

    public void login(String restaurant, String supplierName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "update " + REGISTRATIONS_TABLE + " set loged_in = 1 where restaurant_name = '"
                + restaurant + "' and deliver_name = '" + supplierName + "'";
        db.execSQL(query);
    }

    public void logout() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "update " + REGISTRATIONS_TABLE + " set loged_in = 0 where 1 = 1";
        db.execSQL(query);
    }

    public Cursor getLogins() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from " + REGISTRATIONS_TABLE + " WHERE loged_in = 1";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    // endregion

    // region Food functions
    public boolean addFood(String code, String foodName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("code", code);
        contentValues.put("name", foodName);
        Log.d(TAG, "Adding to " + FOOD_TABLE);
        Log.d(TAG, "addFood: inserting elements: code = " + code + " name = " + foodName);
        long result = db.insert(FOOD_TABLE, null, contentValues);
        if (-1 == result)
            return false;
        return true;
    }

    public Cursor getFoodID(String foodCode){
        SQLiteDatabase db = getWritableDatabase();
        String query = "select * from " + FOOD_TABLE + " where code = '" + foodCode + "'";
        Cursor data = db.rawQuery(query, null);
        return  data;
    }
    // endregion

    // region Client functions

    public Cursor getClientList(String partOfMobile){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + CLIENTS_TABLE + " WHERE mobile like '" + partOfMobile + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public boolean addClient(String name, String address, String mobile, String email, int supplierID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("address", address);
        contentValues.put("email", email);
        contentValues.put("mobile", mobile);
        contentValues.put("supplier_id", supplierID);
        long result = db.insert(CLIENTS_TABLE, null, contentValues);
        if (-1 == result)
            return false;
        return true;
    }
    // endregion

    /**
     *
     * @param tableName The name of the table what from we want get the data
     * @return the chosen table's data
     */
    public Cursor getTable(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + tableName;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void createShippingTable(int supplierId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + DELIVERY_TABLE);
        db.execSQL(CREATE_DELIVERY_TABLE);
        String selectQuery = "select client_id, cl.name client_name, address, mobile, 0, code food_code, name " +
                " from " + ORDERS_TABLE  + " as ord" +
                " left join " + CLIENTS_TABLE + " as cl on ord.client_id = cl.id" +
                " left join " + FOOD_TABLE + " as fd on ord.food_id = fd.id" +
                " where ord.deliver_id = " + supplierId;
        String query = "insert into " + DELIVERY_TABLE + " ( " +
                DELIVERY_CLIENT_ID + ", " +
                DELIVERY_CLIENT_NAME + ", " +
                DELIVERY_CLIENT_ADDRESS + ", " +
                DELIVERY_CLIENT_MOBILE + ", " +
                DELIVERY_CLIENT_ORDER_NUMBER + ", " +
                DELIVERY_FOOD_CODE + ", " +
                DELIVERY_FOOD_NAME + ")" +
                selectQuery;
        db.execSQL(query);
        Log.d(TAG, "running query: " + query);
        // TODO: Make a filter for datetime in query
    }

    // region Database test functions
    public void createDelivery() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + ORDERS_TABLE);
        String createTable = "CREATE TABLE " + ORDERS_TABLE + " ( " +
                "  id integer primary key autoincrement, " +
                "  deliver_id int not null, " +
                "  client_id int not null, " +
                "  food_id int not null, " +
                "  order_date date not null " +
                ")";
        db.execSQL(createTable);
    }

    public void deleteDeliveryItems(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + DELIVERY_TABLE + " where 1 = 1");
    }

    public boolean addDeliveryItem(String clientID, String clientName, String clientAddress, String clientMobile, String clientOrderNumber, String foodCode, String foodName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DELIVERY_CLIENT_ID, Integer.parseInt(clientID));
        contentValues.put(DELIVERY_CLIENT_NAME, clientName);
        contentValues.put(DELIVERY_CLIENT_ADDRESS, clientAddress);
        contentValues.put(DELIVERY_CLIENT_MOBILE, clientMobile);
        contentValues.put(DELIVERY_CLIENT_ORDER_NUMBER, Integer.parseInt(clientOrderNumber));
        contentValues.put(DELIVERY_FOOD_CODE, foodCode);
        contentValues.put(DELIVERY_FOOD_NAME, foodName);
        Log.d(TAG, "Adding to " + DELIVERY_TABLE);
        Log.d(TAG, "addDeliveryItem: inserting elements: client_id = " + clientID + " client_name = " + clientName + " client_address = " + clientAddress +
            " client_mobile = " + clientMobile + " client_order_number = " + clientOrderNumber + " food_code = " + foodCode +
            " food_name = " + foodName);
        long result = db.insert(DELIVERY_TABLE, null, contentValues);
        if (-1 == result)
            return false;
        return true;
    }

    public void update(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "update " + CLIENTS_TABLE + " set supplier_id = 6 where 1 = 1";
        db.rawQuery(query, null);
    }
    // endregion

}






