import android.content.ContentValues
import android.content.Context
import android.content.res.Resources
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.diplomna.R
import com.example.diplomna.models.Client
import com.example.diplomna.models.Employee
import com.example.diplomna.models.Vehicle
import com.example.diplomna.models.VehicleTypes
import kotlin.math.PI

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "AutoIns"
        private const val EMPLOYEE_TABLE = "Employee_Table"
        private const val CLIENT_TABLE = "Client_Table"
        private const val VEHICLE_TABLE = "Vehicle_Table"

        private const val KEY_ID = "_id"

        private const val KEY_NICKNAME_EMP = "nickname"
        private const val KEY_FIRSTNAME_EMP = "first_name"
        private const val KEY_LASTNAME_EMP = "last_name"
        private const val KEY_POSITION_EMP = "position"
        private const val KEY_SALT_EMP = "salt"
        private const val KEY_PASSWORD_EMP = "password"

        /*
        едно към много - клиент към МПС
        добави ид-та за primary key вместо егн и рег номер
        ид-то на клиента е foreign key в мпс-то
        */

        private const val KEY_PIN_CLIENT = "PIN"
        private const val KEY_FIRSTNAME_CLIENT = "first_name"
        private const val KEY_MIDDLENAME_CLIENT = "middle_name"
        private const val KEY_LASTNAME_CLIENT = "last_name"

        private const val KEY_CLIENT_ID = "client_id"
        private const val KEY_LICENSE_PLATE_VEHICLE = "license_plate"
        private const val KEY_VIN_VEHICLE = "VIN"
        private const val KEY_REGISTRATION_CERTIFICATE_VEHICLE = "registration_certificate"
        private const val KEY_ENGINE_VEHICLE = "engine"
        private const val KEY_TYPE_VEHICLE = "vehicle_type"
        private const val KEY_BRAND_VEHICLE = "brand"
        private const val KEY_MODEL_VEHICLE = "model"
        private const val KEY_DATE = "date"
        private const val KEY_PRICE = "price"
        private const val KEY_VALID = "valid"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        // може да го направиш котлин стил
        val CREATE_EMPLOYEE_TABLE = ("CREATE TABLE " + EMPLOYEE_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NICKNAME_EMP + " TEXT,"
                + KEY_FIRSTNAME_EMP + " TEXT,"
                + KEY_LASTNAME_EMP + " TEXT,"
                + KEY_POSITION_EMP + " TEXT,"
                + KEY_SALT_EMP + " TEXT,"
                + KEY_PASSWORD_EMP + " TEXT" + ")")
        db?.execSQL(CREATE_EMPLOYEE_TABLE)

        val CREATE_CLIENT_TABLE = ("CREATE TABLE " + CLIENT_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_PIN_CLIENT + " TEXT,"
                + KEY_FIRSTNAME_CLIENT + " TEXT,"
                + KEY_MIDDLENAME_CLIENT + " TEXT,"
                + KEY_LASTNAME_CLIENT + " TEXT" + ")")
        db?.execSQL(CREATE_CLIENT_TABLE)

        val CREATE_VEHICLE_TABLE = ("CREATE TABLE " + VEHICLE_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_CLIENT_ID + " INTEGER,"
                + KEY_LICENSE_PLATE_VEHICLE + " TEXT,"
                + KEY_VIN_VEHICLE + " TEXT,"
                + KEY_REGISTRATION_CERTIFICATE_VEHICLE + " TEXT,"
                + KEY_ENGINE_VEHICLE + " INTEGER,"
                + KEY_TYPE_VEHICLE + " TEXT,"
                + KEY_BRAND_VEHICLE + " TEXT,"
                + KEY_MODEL_VEHICLE + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_PRICE + " REAL,"
                + KEY_VALID + " INTEGER,"
                // дата на сключване и премия
                // FOREIGN KEY(orderId) REFERENCES order(Id),
                + " FOREIGN KEY(" + KEY_CLIENT_ID + ") REFERENCES " + CLIENT_TABLE + "(" + KEY_ID + ")" + ")")
        db?.execSQL(CREATE_VEHICLE_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $EMPLOYEE_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $CLIENT_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $VEHICLE_TABLE")
        onCreate(db)
    }

    /**
     * Function to insert data
     */
    fun addEmployee(emp: Employee): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NICKNAME_EMP, emp.nickname)
        contentValues.put(KEY_FIRSTNAME_EMP, emp.firstName)
        contentValues.put(KEY_LASTNAME_EMP, emp.lastName)
        contentValues.put(KEY_POSITION_EMP,emp.position)
        contentValues.put(KEY_SALT_EMP,emp.salt)
        contentValues.put(KEY_PASSWORD_EMP,emp.password)

        // Inserting employee details using insert query.
        val success = db.insert(EMPLOYEE_TABLE, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success
    }

    /**
     * Function to update record
     */
    fun updateEmployee(emp: Employee): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NICKNAME_EMP, emp.nickname) // EmpModelClass Name
        contentValues.put(KEY_FIRSTNAME_EMP, emp.firstName) // EmpModelClass Email
        contentValues.put(KEY_LASTNAME_EMP, emp.lastName) // EmpModelClass Email
        contentValues.put(KEY_POSITION_EMP,emp.position)
        contentValues.put(KEY_SALT_EMP,emp.salt)
        contentValues.put(KEY_PASSWORD_EMP,emp.password)

        // Updating Row
        val success = db.update(EMPLOYEE_TABLE, contentValues, KEY_ID + "=" + emp.id, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }

    /**
     * Function to delete record
     */
    fun deleteEmployee(emp: Employee): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.id) // EmpModelClass id
        // Deleting Row
        var success = -1
        if(emp.id != 1) {
            success = db.delete(EMPLOYEE_TABLE, KEY_ID + "=" + emp.id, null)
        }
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }

    fun addClient(client: Client): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_PIN_CLIENT, client.PIN)
        contentValues.put(KEY_FIRSTNAME_CLIENT, client.firstName)
        contentValues.put(KEY_MIDDLENAME_CLIENT, client.middleName)
        contentValues.put(KEY_LASTNAME_CLIENT,client.lastName)

        val success = db.insert(CLIENT_TABLE, null, contentValues)
        db.close()
        return success
    }

    fun updateClient(client: Client): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_PIN_CLIENT, client.PIN)
        contentValues.put(KEY_FIRSTNAME_CLIENT, client.firstName)
        contentValues.put(KEY_MIDDLENAME_CLIENT, client.middleName)
        contentValues.put(KEY_LASTNAME_CLIENT,client.lastName)

        val success = db.update(CLIENT_TABLE, contentValues, KEY_ID + "=" + client.id, null)
        db.close()
        return success
    }

    fun deleteClient(client: Client): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, client.id)
        var success = -1
        if(client.id != 1) {
            success = db.delete(CLIENT_TABLE, KEY_ID + "=" + client.id, null)
        }
        db.close()
        return success
    }

    fun addVehicle(context: Context,vehicle: Vehicle): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_CLIENT_ID, vehicle.clientId)
        contentValues.put(KEY_LICENSE_PLATE_VEHICLE, vehicle.licencePlate)
        contentValues.put(KEY_VIN_VEHICLE, vehicle.VIN)
        contentValues.put(KEY_REGISTRATION_CERTIFICATE_VEHICLE, vehicle.registrationCertificate)
        contentValues.put(KEY_ENGINE_VEHICLE, vehicle.engine)
        contentValues.put(KEY_TYPE_VEHICLE, context.getString(vehicle.type.id)/*R.id(vehicle.type.id)*/)
        contentValues.put(KEY_BRAND_VEHICLE, vehicle.brand)
        contentValues.put(KEY_MODEL_VEHICLE, vehicle.model)
        contentValues.put(KEY_DATE, vehicle.date)
        contentValues.put(KEY_PRICE, vehicle.price)
        contentValues.put(KEY_VALID, vehicle.isValid)

        val success = db.insert(VEHICLE_TABLE, null, contentValues)
        db.close()
        return success
    }

    fun updateVehicle(vehicle: Vehicle): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_CLIENT_ID, vehicle.clientId)
        contentValues.put(KEY_LICENSE_PLATE_VEHICLE, vehicle.licencePlate)
        contentValues.put(KEY_VIN_VEHICLE, vehicle.VIN)
        contentValues.put(KEY_REGISTRATION_CERTIFICATE_VEHICLE, vehicle.registrationCertificate)
        contentValues.put(KEY_ENGINE_VEHICLE, vehicle.engine)
        contentValues.put(KEY_TYPE_VEHICLE,Resources.getSystem().getString(vehicle.type.id))
        contentValues.put(KEY_BRAND_VEHICLE, vehicle.brand)
        contentValues.put(KEY_MODEL_VEHICLE, vehicle.model)
        contentValues.put(KEY_DATE, vehicle.date)
        contentValues.put(KEY_PRICE, vehicle.price)
        contentValues.put(KEY_VALID, vehicle.isValid)

        val success = db.update(VEHICLE_TABLE, contentValues, KEY_ID + "=" + vehicle.id, null)
        db.close()
        return success
    }

    fun deleteVehicle(vehicle: Vehicle): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, vehicle.id)
        var success = -1
        if(vehicle.id != 1) {
            success = db.delete(VEHICLE_TABLE, KEY_ID + "=" + vehicle.id, null)
        }
        db.close()
        return success
    }

    fun viewEmployee() : ArrayList<Employee> {
        val empList = ArrayList<Employee>()

        val selectQuery = "SELECT * FROM $EMPLOYEE_TABLE"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var nickname: String
        var firstName: String
        var lastName: String
        var position: String
        var salt: String
        var password: String

        if(cursor.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                nickname = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NICKNAME_EMP))
                firstName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FIRSTNAME_EMP))
                lastName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LASTNAME_EMP))
                position = cursor.getString(cursor.getColumnIndexOrThrow(KEY_POSITION_EMP))
                salt = cursor.getString(cursor.getColumnIndexOrThrow(KEY_SALT_EMP))
                password = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD_EMP))

                val emp = Employee(id = id,nickname = nickname,firstName = firstName, lastName = lastName,position = position,salt = salt, password = password)
                empList.add(emp)

            } while (cursor.moveToNext())
        }
        return empList
    }

    fun getPositionByNickname(nickname: String): String? {
        val args = listOf(nickname).toTypedArray()
        val nicknameQuery = "SELECT $KEY_POSITION_EMP FROM $EMPLOYEE_TABLE WHERE $KEY_NICKNAME_EMP=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(nicknameQuery, args)
        var position: String? = null
        if(cursor.moveToFirst()){
            do {
                position = cursor.getString(cursor.getColumnIndexOrThrow(KEY_POSITION_EMP))
            } while (cursor.moveToNext())
        }
        return position
    }

    fun getSaltByNickname(nickname: String): String? {
        val args = listOf(nickname).toTypedArray()
        val nicknameQuery = "SELECT $KEY_SALT_EMP FROM $EMPLOYEE_TABLE WHERE $KEY_NICKNAME_EMP=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(nicknameQuery, args)
        var salt: String? = null
        if(cursor.moveToFirst()){
            do {
                salt = cursor.getString(cursor.getColumnIndexOrThrow(KEY_SALT_EMP))
            } while (cursor.moveToNext())
        }
        return salt
    }

    fun getNicknames() : MutableList<String> {
        val nicknames = mutableListOf<String>()
        val nicknamesQuery = "SELECT $KEY_NICKNAME_EMP FROM $EMPLOYEE_TABLE"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(nicknamesQuery, null)
        } catch (e: SQLException){
            db.execSQL(nicknamesQuery)
            return ArrayList()
        }

        var nickname: String
        if(cursor.moveToFirst()){
            do {
                nickname = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NICKNAME_EMP))
                nicknames.add(nickname)
            } while (cursor.moveToNext())
        }
        return nicknames
    }

    fun getIdByPIN(PIN: String): Int {
        val args = listOf(PIN).toTypedArray()
        val pinQuery = "SELECT $KEY_ID FROM $CLIENT_TABLE WHERE $KEY_PIN_CLIENT=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(pinQuery, args)
        var id: Int = 0
        if(cursor.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
            } while (cursor.moveToNext())
        }
        return id
    }

    fun getClientByPIN(PIN: String): Client {
        val args = listOf(PIN).toTypedArray()
        val pinQuery = "SELECT * FROM $CLIENT_TABLE WHERE $KEY_PIN_CLIENT=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(pinQuery, args)
        var id = 0
        var firstName = ""
        var middleName = ""
        var lastName = ""
        if(cursor.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                firstName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FIRSTNAME_CLIENT))
                middleName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MIDDLENAME_CLIENT))
                lastName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LASTNAME_CLIENT))

            } while (cursor.moveToNext())
        }
        return Client(id,PIN,firstName,middleName,lastName)
    }

    fun getIsValidByLicensePlate(licensePlate: String): Boolean {
        val args = listOf(licensePlate).toTypedArray()
        val pinQuery = "SELECT $KEY_VALID FROM $VEHICLE_TABLE WHERE $KEY_LICENSE_PLATE_VEHICLE=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(pinQuery, args)
        var isValid = false
        if(cursor.moveToFirst()){
            do {
                if(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_VALID)) == 1) isValid = true
            } while (cursor.moveToNext())
        }
        return isValid
    }

    fun getVehicleByLicensePlate(licensePlate: String,context: Context): Vehicle {
        val args = listOf(licensePlate).toTypedArray()
        val pinQuery = "SELECT * FROM $VEHICLE_TABLE WHERE $KEY_LICENSE_PLATE_VEHICLE=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(pinQuery, args)
        var id = 0
        var clientId = 0
        var licensePlate = ""
        var VIN = ""
        var registrationCertificate = ""
        var engine = 0
        var type = ""
        var brand = ""
        var model = ""
        var date = ""
        var price = 0.0
        var isValid = false
        if(cursor.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                clientId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_CLIENT_ID))
                licensePlate = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LICENSE_PLATE_VEHICLE))
                VIN = cursor.getString(cursor.getColumnIndexOrThrow(KEY_VIN_VEHICLE))
                registrationCertificate = cursor.getString(cursor.getColumnIndexOrThrow(KEY_REGISTRATION_CERTIFICATE_VEHICLE))
                engine = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ENGINE_VEHICLE))
                type = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE_VEHICLE))
                brand = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BRAND_VEHICLE))
                model = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MODEL_VEHICLE))
                date = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE))
                price = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE))
                if(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_VALID)) == 1) isValid = true
            } while (cursor.moveToNext())
        }
        val typeVehicle = VehicleTypes.values().first { context.getString(it.id) == type }
        return Vehicle(id,clientId,licensePlate,VIN,registrationCertificate,engine,typeVehicle,brand,model,date,price,isValid)
    }

    fun getPINs() : MutableList<String> {
        val PINs = mutableListOf<String>()
        val PINsQuery = "SELECT $KEY_PIN_CLIENT FROM $CLIENT_TABLE"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(PINsQuery, null)
        } catch (e: SQLException){
            db.execSQL(PINsQuery)
            return ArrayList()
        }

        var PIN: String
        if(cursor.moveToFirst()){
            do {
                PIN = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PIN_CLIENT))
                PINs.add(PIN)
            } while (cursor.moveToNext())
        }
        return PINs
    }

    fun getLicensePlates() : MutableList<String> {
        val licensePlates = mutableListOf<String>()
        val licensePlatesQuery = "SELECT $KEY_LICENSE_PLATE_VEHICLE FROM $VEHICLE_TABLE"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(licensePlatesQuery, null)
        } catch (e: SQLException){
            db.execSQL(licensePlatesQuery)
            return ArrayList()
        }

        var licensePlate: String
        if(cursor.moveToFirst()){
            do {
                licensePlate = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LICENSE_PLATE_VEHICLE))
                licensePlates.add(licensePlate)
            } while (cursor.moveToNext())
        }
        return licensePlates
    }

    fun checkLogin(nickname : String, password : String) : Boolean{
        val args = listOf(nickname,password).toTypedArray()
        val nicknamesQuery = "SELECT * FROM $EMPLOYEE_TABLE WHERE $KEY_NICKNAME_EMP=? AND $KEY_PASSWORD_EMP=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(nicknamesQuery, args)
        return cursor.count>0
    }
}