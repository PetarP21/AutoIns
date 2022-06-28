import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.diplomna.models.*

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        if (!db?.isReadOnly!!) {
            db.execSQL("PRAGMA foreign_keys=ON;")
        }
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "AutoIns"
        private const val EMPLOYEE_TABLE = "Employee_Table"
        private const val CLIENT_TABLE = "Client_Table"
        private const val VEHICLE_TABLE = "Vehicle_Table"
        private const val POSITION_TABLE = "Position_Table"
        private const val VEHICLE_TYPE_TABLE = "Vehicle_type_Table"
        private const val VALIDITY_TABLE = "Validity_Table"
        private const val SECURITY_QUESTION_TABLE = "Security_question_Table"

        private const val KEY_ID = "_id"

        private const val KEY_NICKNAME_EMP = "nickname"
        private const val KEY_FIRSTNAME_EMP = "first_name"
        private const val KEY_MIDDLENAME_EMP = "middle_name"
        private const val KEY_LASTNAME_EMP = "last_name"
        private const val KEY_EMAIL_EMP = "email"
        private const val KEY_POSITION_ID = "position_id"
        private const val KEY_SECURITY_QUESTION_ID = "security_question_id"
        private const val KEY_SECURITY_ANSWER_EMP = "security_answer"
        private const val KEY_SALT_EMP = "salt"
        private const val KEY_PASSWORD_EMP = "password"

        private const val KEY_POSITION = "position"

        private const val KEY_VEHICLE_TYPE = "vehicle_type"

        private const val KEY_VALIDITY = "validity"

        private const val KEY_SECURITY_QUESTION = "security_question"

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
        private const val KEY_TYPE_VEHICLE_ID = "vehicle_type_id"
        private const val KEY_BRAND_VEHICLE = "brand"
        private const val KEY_MODEL_VEHICLE = "model"
        private const val KEY_DATE = "date"
        private const val KEY_PRICE = "price"
        private const val KEY_VALID_ID = "valid_id"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        // може да го направиш котлин стил
        val CREATE_EMPLOYEE_TABLE = ("CREATE TABLE " + EMPLOYEE_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NICKNAME_EMP + " TEXT,"
                + KEY_FIRSTNAME_EMP + " TEXT,"
                + KEY_MIDDLENAME_EMP + " TEXT,"
                + KEY_LASTNAME_EMP + " TEXT,"
                + KEY_EMAIL_EMP + " TEXT,"
                + KEY_POSITION_ID + " INTEGER,"
                + KEY_SECURITY_QUESTION_ID + " INTEGER,"
                + KEY_SECURITY_ANSWER_EMP + " TEXT,"
                + KEY_SALT_EMP + " TEXT,"
                + KEY_PASSWORD_EMP + " TEXT,"
                + " CONSTRAINT fk_positions"
                + " FOREIGN KEY(" + KEY_POSITION_ID + ") REFERENCES " + POSITION_TABLE + "(" + KEY_ID + ")"
                + " ON DELETE CASCADE,"
                + " CONSTRAINT fk_sec_questions"
                + " FOREIGN KEY(" + KEY_SECURITY_QUESTION_ID + ") REFERENCES " + SECURITY_QUESTION_TABLE + "(" + KEY_ID + ")"
                + " ON DELETE CASCADE"
                + ")")
        db?.execSQL(CREATE_EMPLOYEE_TABLE)

        val CREATE_POSITION_TABLE = ("CREATE TABLE " + POSITION_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_POSITION + " TEXT" + ")")
        db?.execSQL(CREATE_POSITION_TABLE)

        val CREATE_VEHICLE_TYPE_TABLE = ("CREATE TABLE " + VEHICLE_TYPE_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_VEHICLE_TYPE + " TEXT" + ")")
        db?.execSQL(CREATE_VEHICLE_TYPE_TABLE)

        val CREATE_VALIDITY_TABLE = ("CREATE TABLE " + VALIDITY_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_VALIDITY + " TEXT" + ")")
        db?.execSQL(CREATE_VALIDITY_TABLE)

        val CREATE_SECURITY_QUESTION_TABLE = ("CREATE TABLE " + SECURITY_QUESTION_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_SECURITY_QUESTION + " TEXT" + ")")
        db?.execSQL(CREATE_SECURITY_QUESTION_TABLE)

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
                + KEY_TYPE_VEHICLE_ID + " INTEGER,"
                + KEY_BRAND_VEHICLE + " TEXT,"
                + KEY_MODEL_VEHICLE + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_PRICE + " REAL,"
                + KEY_VALID_ID + " INTEGER,"
                + " CONSTRAINT fk_clients"
                + " FOREIGN KEY(" + KEY_CLIENT_ID + ") REFERENCES " + CLIENT_TABLE + "(" + KEY_ID + ")"
                + " ON DELETE CASCADE,"
                + " CONSTRAINT fk_vehicle_types"
                + " FOREIGN KEY(" + KEY_TYPE_VEHICLE_ID + ") REFERENCES " + VEHICLE_TYPE_TABLE + "(" + KEY_ID + ")"
                + " ON DELETE CASCADE,"
                + " CONSTRAINT fk_validity_options"
                + " FOREIGN KEY(" + KEY_VALID_ID + ") REFERENCES " + VALIDITY_TABLE + "(" + KEY_ID + ")"
                + " ON DELETE CASCADE"
                + ")")
        db?.execSQL(CREATE_VEHICLE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $EMPLOYEE_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $CLIENT_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $VEHICLE_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $POSITION_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $VEHICLE_TYPE_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $VALIDITY_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $SECURITY_QUESTION_TABLE")
        onCreate(db)
    }

    /**
     * Function to insert data
     */

    fun addSecurityQuestion(securityQuestion: SecurityQuestion): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_SECURITY_QUESTION, securityQuestion.securityQuestion)
        val success = db.insert(SECURITY_QUESTION_TABLE, null, contentValues)
        db.close()
        return success
    }

    fun addValidity(validity: Validity): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_VALIDITY, validity.validity)
        val success = db.insert(VALIDITY_TABLE, null, contentValues)
        db.close()
        return success
    }

    fun addPosition(position: Position): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_POSITION, position.position)
        val success = db.insert(POSITION_TABLE, null, contentValues)
        db.close()
        return success
    }

    fun updatePosition(position: Position): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_POSITION, position.position)
        val success = db.update(POSITION_TABLE, contentValues, KEY_ID + " = " + position.id, null)
        db.close()
        return success
    }

    fun deletePosition(position: Position): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, position.id)
        var success = -1
        if (position.id != 1) {
            success = db.delete(POSITION_TABLE, KEY_ID + "=" + position.id, null)
        }
        db.close()
        return success
    }



    fun addVehicleType(vehicleType: VehicleType): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_VEHICLE_TYPE, vehicleType.vehicleType)
        val success = db.insert(VEHICLE_TYPE_TABLE, null, contentValues)
        db.close()
        return success
    }

    fun updateVehicleType(vehicleType: VehicleType): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_VEHICLE_TYPE, vehicleType.vehicleType)
        val success =
            db.update(VEHICLE_TYPE_TABLE, contentValues, KEY_ID + " = " + vehicleType.id, null)
        db.close()
        return success
    }

    fun deleteVehicleType(vehicleType: VehicleType): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_VEHICLE_TYPE, vehicleType.vehicleType)
        var success = -1
        if (vehicleType.id != 1) {
            success = db.delete(VEHICLE_TYPE_TABLE, KEY_ID + "=" + vehicleType.id, null)
        }
        db.close()
        return success
    }

    fun getSecurityQuestions(): MutableList<String> {
        val securityQuestions = mutableListOf<String>()
        val securityQuestionsQuery = "SELECT $KEY_SECURITY_QUESTION FROM $SECURITY_QUESTION_TABLE"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(securityQuestionsQuery, null)
        } catch (e: SQLException) {
            db.execSQL(securityQuestionsQuery)
            return ArrayList()
        }

        var securityQuestion: String
        if (cursor.moveToFirst()) {
            do {
                securityQuestion = cursor.getString(cursor.getColumnIndexOrThrow(
                    KEY_SECURITY_QUESTION))
                securityQuestions.add(securityQuestion)
            } while (cursor.moveToNext())
        }
        return securityQuestions
    }

    fun getVehicleTypes(): MutableList<String> {
        val vehicleTypes = mutableListOf<String>()
        val vehicleTypesQuery = "SELECT $KEY_VEHICLE_TYPE FROM $VEHICLE_TYPE_TABLE"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(vehicleTypesQuery, null)
        } catch (e: SQLException) {
            db.execSQL(vehicleTypesQuery)
            return ArrayList()
        }

        var vehicleType: String
        if (cursor.moveToFirst()) {
            do {
                vehicleType = cursor.getString(cursor.getColumnIndexOrThrow(KEY_VEHICLE_TYPE))
                vehicleTypes.add(vehicleType)
            } while (cursor.moveToNext())
        }
        return vehicleTypes
    }

    fun getValidityOptions(): MutableList<String> {
        val validityOptions = mutableListOf<String>()
        val validityQuery = "SELECT $KEY_VALIDITY FROM $VALIDITY_TABLE"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(validityQuery, null)
        } catch (e: SQLException) {
            db.execSQL(validityQuery)
            return ArrayList()
        }

        var validity: String
        if (cursor.moveToFirst()) {
            do {
                validity = cursor.getString(cursor.getColumnIndexOrThrow(KEY_VALIDITY))
                validityOptions.add(validity)
            } while (cursor.moveToNext())
        }
        return validityOptions
    }

    fun getPositions(): MutableList<String> {
        val positions = mutableListOf<String>()
        val positionsQuery = "SELECT $KEY_POSITION FROM $POSITION_TABLE"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(positionsQuery, null)
        } catch (e: SQLException) {
            db.execSQL(positionsQuery)
            return ArrayList()
        }

        var position: String
        if (cursor.moveToFirst()) {
            do {
                position = cursor.getString(cursor.getColumnIndexOrThrow(KEY_POSITION))
                positions.add(position)
            } while (cursor.moveToNext())
        }
        return positions
    }

    fun getIdByPosition(position: String): Int {
        val args = listOf(position).toTypedArray()
        val pinQuery = "SELECT $KEY_ID FROM $POSITION_TABLE WHERE $KEY_POSITION=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(pinQuery, args)
        var id = 0
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
            } while (cursor.moveToNext())
        }
        return id
    }

    fun getIdBySecurityQuestion(securityQuestion: String): Int {
        val args = listOf(securityQuestion).toTypedArray()
        val pinQuery = "SELECT $KEY_ID FROM $SECURITY_QUESTION_TABLE WHERE $KEY_SECURITY_QUESTION=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(pinQuery, args)
        var id = 0
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
            } while (cursor.moveToNext())
        }
        return id
    }

    fun getIdByVehicleType(vehicleType: String): Int {
        val args = listOf(vehicleType).toTypedArray()
        val pinQuery = "SELECT $KEY_ID FROM $VEHICLE_TYPE_TABLE WHERE $KEY_VEHICLE_TYPE=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(pinQuery, args)
        var id = 0
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
            } while (cursor.moveToNext())
        }
        return id
    }

    fun getClientByVehicle(vehicle: Vehicle): Client {
        val vehicleQuery = "SELECT * FROM $VEHICLE_TABLE" +
                " INNER JOIN $CLIENT_TABLE ON $VEHICLE_TABLE.$KEY_CLIENT_ID = $CLIENT_TABLE.$KEY_ID WHERE $CLIENT_TABLE.$KEY_ID=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(vehicleQuery, arrayOf(vehicle.clientId.toString()))
        var id = 0
        var pin = ""
        var firstName = ""
        var middleName = ""
        var lastName = ""
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_CLIENT_ID))
                pin = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PIN_CLIENT))
                firstName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FIRSTNAME_CLIENT))
                middleName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MIDDLENAME_CLIENT))
                lastName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LASTNAME_CLIENT))

            } while (cursor.moveToNext())
        }
        return Client(id, pin, firstName, middleName, lastName)
    }

    fun getVehicleTypeByVehicle(vehicle: Vehicle): VehicleType {
        val vehicleQuery = "SELECT * FROM $VEHICLE_TABLE" +
                " INNER JOIN $VEHICLE_TYPE_TABLE ON $VEHICLE_TABLE.$KEY_TYPE_VEHICLE_ID = $VEHICLE_TYPE_TABLE.$KEY_ID WHERE $VEHICLE_TYPE_TABLE.$KEY_ID=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(vehicleQuery, arrayOf(vehicle.vehicleTypeId.toString()))
        var id = 0
        var vehicleType = ""
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_TYPE_VEHICLE_ID))
                vehicleType = cursor.getString(cursor.getColumnIndexOrThrow(KEY_VEHICLE_TYPE))
            } while (cursor.moveToNext())
        }
        return VehicleType(id, vehicleType)
    }

    fun getValidityByVehicle(vehicle: Vehicle): Validity {
        val vehicleQuery = "SELECT * FROM $VEHICLE_TABLE" +
                " INNER JOIN $VALIDITY_TABLE ON $VEHICLE_TABLE.$KEY_VALID_ID = $VALIDITY_TABLE.$KEY_ID WHERE $VALIDITY_TABLE.$KEY_ID=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(vehicleQuery, arrayOf(vehicle.validityId.toString()))
        var id = 0
        var validity = 0
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_VALID_ID))
                validity = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_VALIDITY))
            } while (cursor.moveToNext())
        }
        return Validity(id, validity)
    }



    fun getPositionByEmployee(employee: Employee): Position {
        val positionQuery = "SELECT * FROM $EMPLOYEE_TABLE" +
                " INNER JOIN $POSITION_TABLE ON $EMPLOYEE_TABLE.$KEY_POSITION_ID = $POSITION_TABLE.$KEY_ID WHERE $POSITION_TABLE.$KEY_ID=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(positionQuery, arrayOf(employee.positionId.toString()))
        var id = 0
        var position = ""
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_POSITION_ID))
                position = cursor.getString(cursor.getColumnIndexOrThrow(KEY_POSITION))
            } while (cursor.moveToNext())
        }
        return Position(id, position)
    }

    fun getSecurityQuestionByEmployee(employee: Employee): SecurityQuestion {
        val positionQuery = "SELECT * FROM $EMPLOYEE_TABLE" +
                " INNER JOIN $SECURITY_QUESTION_TABLE ON $EMPLOYEE_TABLE.$KEY_SECURITY_QUESTION_ID = $SECURITY_QUESTION_TABLE.$KEY_ID WHERE $SECURITY_QUESTION_TABLE.$KEY_ID=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(positionQuery, arrayOf(employee.securityQuestionId.toString()))
        var id = 0
        var securityQuestion = ""
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_SECURITY_QUESTION_ID))
                securityQuestion = cursor.getString(cursor.getColumnIndexOrThrow(
                    KEY_SECURITY_QUESTION))
            } while (cursor.moveToNext())
        }
        return SecurityQuestion(id,securityQuestion)
    }

    fun addEmployee(emp: Employee): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NICKNAME_EMP, emp.nickname)
        contentValues.put(KEY_FIRSTNAME_EMP, emp.firstName)
        contentValues.put(KEY_MIDDLENAME_EMP, emp.middleName)
        contentValues.put(KEY_LASTNAME_EMP, emp.lastName)
        contentValues.put(KEY_EMAIL_EMP, emp.email)
        contentValues.put(KEY_POSITION_ID, emp.positionId)
        contentValues.put(KEY_SECURITY_QUESTION_ID, emp.securityQuestionId)
        contentValues.put(KEY_SECURITY_ANSWER_EMP, emp.securityAnswer)
        contentValues.put(KEY_SALT_EMP, emp.salt)
        contentValues.put(KEY_PASSWORD_EMP, emp.password)

        val success = db.insert(EMPLOYEE_TABLE, null, contentValues)

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
        contentValues.put(KEY_MIDDLENAME_EMP, emp.middleName)
        contentValues.put(KEY_LASTNAME_EMP, emp.lastName) // EmpModelClass Email
        contentValues.put(KEY_EMAIL_EMP, emp.email)
        contentValues.put(KEY_POSITION_ID, emp.positionId)
        contentValues.put(KEY_SECURITY_QUESTION_ID, emp.securityQuestionId)
        contentValues.put(KEY_SECURITY_ANSWER_EMP, emp.securityAnswer)
        contentValues.put(KEY_SALT_EMP, emp.salt)
        contentValues.put(KEY_PASSWORD_EMP, emp.password)

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
        if (emp.id != 1) {
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
        contentValues.put(KEY_LASTNAME_CLIENT, client.lastName)

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
        contentValues.put(KEY_LASTNAME_CLIENT, client.lastName)

        val success = db.update(CLIENT_TABLE, contentValues, KEY_ID + "=" + client.id, null)
        db.close()
        return success
    }

    fun deleteClient(client: Client): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, client.id)
        var success = -1
        success = db.delete(CLIENT_TABLE, KEY_ID + "=" + client.id, null)
        db.close()
        return success
    }

    fun addVehicle(vehicle: Vehicle): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_CLIENT_ID, vehicle.clientId)
        contentValues.put(KEY_LICENSE_PLATE_VEHICLE, vehicle.licencePlate)
        contentValues.put(KEY_VIN_VEHICLE, vehicle.VIN)
        contentValues.put(KEY_REGISTRATION_CERTIFICATE_VEHICLE, vehicle.registrationCertificate)
        contentValues.put(KEY_ENGINE_VEHICLE, vehicle.engine)
        contentValues.put(KEY_TYPE_VEHICLE_ID, vehicle.vehicleTypeId)
        contentValues.put(KEY_BRAND_VEHICLE, vehicle.brand)
        contentValues.put(KEY_MODEL_VEHICLE, vehicle.model)
        contentValues.put(KEY_DATE, vehicle.date)
        contentValues.put(KEY_PRICE, vehicle.price)
        contentValues.put(KEY_VALID_ID, vehicle.validityId)

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
        contentValues.put(KEY_TYPE_VEHICLE_ID, vehicle.vehicleTypeId)
        contentValues.put(KEY_BRAND_VEHICLE, vehicle.brand)
        contentValues.put(KEY_MODEL_VEHICLE, vehicle.model)
        contentValues.put(KEY_DATE, vehicle.date)
        contentValues.put(KEY_PRICE, vehicle.price)
        contentValues.put(KEY_VALID_ID, vehicle.validityId)

        val success = db.update(VEHICLE_TABLE, contentValues, KEY_ID + "=" + vehicle.id, null)
        db.close()
        return success
    }

    fun deleteVehicle(vehicle: Vehicle): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, vehicle.id)
        var success = -1
        success = db.delete(VEHICLE_TABLE, KEY_ID + "=" + vehicle.id, null)
        db.close()
        return success
    }

    fun viewEmployee(): ArrayList<Employee> {
        val empList = ArrayList<Employee>()

        val selectQuery = "SELECT * FROM $EMPLOYEE_TABLE"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var nickname: String
        var firstName: String
        var middleName: String
        var lastName: String
        var email: String
        var positionId: Int
        var securityQuestionId: Int
        var securityAnswer: String
        var salt: String
        var password: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                nickname = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NICKNAME_EMP))
                firstName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FIRSTNAME_EMP))
                middleName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MIDDLENAME_EMP))
                lastName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LASTNAME_EMP))
                email = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL_EMP))
                positionId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_POSITION_ID))
                securityQuestionId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                        KEY_SECURITY_QUESTION_ID
                    )
                )
                securityAnswer = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        KEY_SECURITY_ANSWER_EMP
                    )
                )
                salt = cursor.getString(cursor.getColumnIndexOrThrow(KEY_SALT_EMP))
                password = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD_EMP))

                val emp = Employee(
                    id = id,
                    nickname = nickname,
                    firstName = firstName,
                    middleName = middleName,
                    lastName = lastName,
                    email = email,
                    positionId = positionId,
                    securityQuestionId = securityQuestionId,
                    securityAnswer = securityAnswer,
                    salt = salt,
                    password = password
                )
                empList.add(emp)

            } while (cursor.moveToNext())
        }
        return empList
    }

    fun getPositionIdByNickname(nickname: String): Int {
        val args = listOf(nickname).toTypedArray()
        val nicknameQuery =
            "SELECT $KEY_POSITION_ID FROM $EMPLOYEE_TABLE WHERE $KEY_NICKNAME_EMP=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(nicknameQuery, args)
        var positionId = 0
        if (cursor.moveToFirst()) {
            do {
                positionId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_POSITION_ID))
            } while (cursor.moveToNext())
        }
        return positionId
    }

    fun getPositionById(id: Int): Position {
        val args = listOf(id.toString()).toTypedArray()
        val pinQuery = "SELECT * FROM $POSITION_TABLE WHERE $KEY_ID=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(pinQuery, args)
        var position = ""
        if (cursor.moveToFirst()) {
            do {
                position = cursor.getString(cursor.getColumnIndexOrThrow(KEY_POSITION))
            } while (cursor.moveToNext())
        }
        return Position(id, position)
    }

    fun getSaltByNickname(nickname: String): String? {
        val args = listOf(nickname).toTypedArray()
        val nicknameQuery = "SELECT $KEY_SALT_EMP FROM $EMPLOYEE_TABLE WHERE $KEY_NICKNAME_EMP=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(nicknameQuery, args)
        var salt: String? = null
        if (cursor.moveToFirst()) {
            do {
                salt = cursor.getString(cursor.getColumnIndexOrThrow(KEY_SALT_EMP))
            } while (cursor.moveToNext())
        }
        return salt
    }

    fun getNicknames(): MutableList<String> {
        val nicknames = mutableListOf<String>()
        val nicknamesQuery = "SELECT $KEY_NICKNAME_EMP FROM $EMPLOYEE_TABLE"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(nicknamesQuery, null)
        } catch (e: SQLException) {
            db.execSQL(nicknamesQuery)
            return ArrayList()
        }

        var nickname: String
        if (cursor.moveToFirst()) {
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
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
            } while (cursor.moveToNext())
        }
        return id
    }

    fun getClientById(id: Int): Client {
        val args = listOf(id.toString()).toTypedArray()
        val pinQuery = "SELECT * FROM $CLIENT_TABLE WHERE $KEY_ID=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(pinQuery, args)
        var pin = ""
        var firstName = ""
        var middleName = ""
        var lastName = ""
        if (cursor.moveToFirst()) {
            do {
                pin = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PIN_CLIENT))
                firstName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FIRSTNAME_CLIENT))
                middleName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MIDDLENAME_CLIENT))
                lastName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LASTNAME_CLIENT))

            } while (cursor.moveToNext())
        }
        return Client(id, pin, firstName, middleName, lastName)
    }

    fun getValidityByLicensePlate(licensePlate: String): Validity {
        val args = listOf(licensePlate).toTypedArray()
        /*
        val vehicleQuery = "SELECT * FROM $VEHICLE_TABLE" +
                " INNER JOIN $CLIENT_TABLE ON $VEHICLE_TABLE.$KEY_CLIENT_ID = $CLIENT_TABLE.$KEY_ID WHERE $CLIENT_TABLE.$KEY_ID=?"
         */
        val pinQuery = "SELECT * FROM $VEHICLE_TABLE WHERE $KEY_LICENSE_PLATE_VEHICLE=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(pinQuery, args)
        var id = 0
        var validity = 0
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_VALID_ID))
                validity = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_VALIDITY))
            } while (cursor.moveToNext())
        }
        return Validity(id, validity)
    }

    fun getClientByPIN(PIN: String): Client {
        val args = listOf(PIN).toTypedArray()
        val pinQuery = "SELECT * FROM $CLIENT_TABLE WHERE $KEY_PIN_CLIENT=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(pinQuery, args)
        var id = 0
        var pin = ""
        var firstName = ""
        var middleName = ""
        var lastName = ""
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                pin = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PIN_CLIENT))
                firstName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FIRSTNAME_CLIENT))
                middleName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MIDDLENAME_CLIENT))
                lastName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LASTNAME_CLIENT))

            } while (cursor.moveToNext())
        }
        return Client(id, pin, firstName, middleName, lastName)
    }

    fun checkClientByPIN(PIN: String): Client? {
        val args = listOf(PIN).toTypedArray()
        val pinQuery = "SELECT * FROM $CLIENT_TABLE WHERE $KEY_PIN_CLIENT=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(pinQuery, args)
        var id = 0
        var pin = ""
        var firstName = ""
        var middleName = ""
        var lastName = ""
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                pin = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PIN_CLIENT))
                firstName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FIRSTNAME_CLIENT))
                middleName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MIDDLENAME_CLIENT))
                lastName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LASTNAME_CLIENT))

            } while (cursor.moveToNext())
        }
        return if (pin == "") {
            null
        } else Client(id, pin, firstName, middleName, lastName)
    }

    fun getEmployeeByNickname(nickname: String): Employee? {
        val args = listOf(nickname).toTypedArray()
        val pinQuery = "SELECT * FROM $EMPLOYEE_TABLE WHERE $KEY_NICKNAME_EMP=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(pinQuery, args)
        var id = 0
        var nickname = ""
        var firstName = ""
        var middleName = ""
        var lastName = ""
        var email = ""
        var positionId = 0
        var securityQuestionId = 0
        var securityAnswer = ""
        var salt = ""
        var password = ""
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                nickname = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NICKNAME_EMP))
                firstName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FIRSTNAME_EMP))
                middleName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MIDDLENAME_EMP))
                lastName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LASTNAME_EMP))
                email = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL_EMP))
                positionId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_POSITION_ID))
                securityQuestionId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                        KEY_SECURITY_QUESTION_ID
                    )
                )
                securityAnswer = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        KEY_SECURITY_ANSWER_EMP
                    )
                )
                salt = cursor.getString(cursor.getColumnIndexOrThrow(KEY_SALT_EMP))
                password = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD_EMP))

            } while (cursor.moveToNext())
        }
        return if (nickname == "") {
            null
        } else Employee(
            id,
            nickname,
            firstName,
            middleName,
            lastName,
            email,
            positionId,
            securityQuestionId,
            securityAnswer,
            salt,
            password
        )
    }

    fun getEmployeeByEmail(email: String): Employee? {
        val args = listOf(email).toTypedArray()
        val pinQuery = "SELECT * FROM $EMPLOYEE_TABLE WHERE $KEY_EMAIL_EMP=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(pinQuery, args)
        var id = 0
        var nickname = ""
        var firstName = ""
        var middleName = ""
        var lastName = ""
        var email = ""
        var positionId = 0
        var securityQuestionId = 0
        var securityAnswer = ""
        var salt = ""
        var password = ""
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                nickname = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NICKNAME_EMP))
                firstName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FIRSTNAME_EMP))
                middleName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MIDDLENAME_EMP))
                lastName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LASTNAME_EMP))
                email = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL_EMP))
                positionId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_POSITION_ID))
                securityQuestionId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                        KEY_SECURITY_QUESTION_ID
                    )
                )
                securityAnswer = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        KEY_SECURITY_ANSWER_EMP
                    )
                )
                salt = cursor.getString(cursor.getColumnIndexOrThrow(KEY_SALT_EMP))
                password = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD_EMP))

            } while (cursor.moveToNext())
        }
        return if (email == "") {
            null
        } else Employee(
            id,
            nickname,
            firstName,
            middleName,
            lastName,
            email,
            positionId,
            securityQuestionId,
            securityAnswer,
            salt,
            password
        )
    }

    fun getVehicleByLicensePlate(licensePlate: String): Vehicle {
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
        var vehicleTypeId = 0
        var brand = ""
        var model = ""
        var date = ""
        var price = 0.0
        var validityId = 0
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                clientId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_CLIENT_ID))
                licensePlate =
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_LICENSE_PLATE_VEHICLE))
                VIN = cursor.getString(cursor.getColumnIndexOrThrow(KEY_VIN_VEHICLE))
                registrationCertificate = cursor.getString(
                    cursor.getColumnIndexOrThrow(KEY_REGISTRATION_CERTIFICATE_VEHICLE)
                )
                engine = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ENGINE_VEHICLE))
                vehicleTypeId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_TYPE_VEHICLE_ID))
                brand = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BRAND_VEHICLE))
                model = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MODEL_VEHICLE))
                date = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE))
                price = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE))
                validityId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_VALID_ID))
            } while (cursor.moveToNext())
        }
       /* if (type == "") {
            type = context.getString(VehicleTypes.CAR.id)
        }
        var vehicleType = VehicleTypes.CAR
        if (type != "") {
            vehicleType =
                VehicleTypes.values().first { context.getString(it.id) == type }
        } */
        return Vehicle(
            id,
            clientId,
            licensePlate,
            VIN,
            registrationCertificate,
            engine,
            vehicleTypeId,
            brand,
            model,
            date,
            price,
            validityId
        )
    }

    fun getVehicleByVIN(VIN: String, context: Context): Vehicle? {
        val args = listOf(VIN).toTypedArray()
        val pinQuery = "SELECT * FROM $VEHICLE_TABLE WHERE $KEY_VIN_VEHICLE=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(pinQuery, args)
        var id = 0
        var clientId = 0
        var licensePlate = ""
        var VIN = ""
        var registrationCertificate = ""
        var engine = 0
        var vehicleTypeId = 0
        var brand = ""
        var model = ""
        var date = ""
        var price = 0.0
        var validityId = 0
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                clientId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_CLIENT_ID))
                licensePlate =
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_LICENSE_PLATE_VEHICLE))
                VIN = cursor.getString(cursor.getColumnIndexOrThrow(KEY_VIN_VEHICLE))
                registrationCertificate = cursor.getString(
                    cursor.getColumnIndexOrThrow(KEY_REGISTRATION_CERTIFICATE_VEHICLE)
                )
                engine = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ENGINE_VEHICLE))
                vehicleTypeId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_TYPE_VEHICLE_ID))
                brand = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BRAND_VEHICLE))
                model = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MODEL_VEHICLE))
                date = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE))
                price = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE))
                validityId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_VALID_ID))
            } while (cursor.moveToNext())
        }
        if (VIN == "") {
            return null
        }
        /*
        var vehicleType = VehicleTypes.CAR
        if (type != "") {
            vehicleType =
                VehicleTypes.values().first { context.getString(it.id) == type }
        }
         */
        return Vehicle(
            id,
            clientId,
            licensePlate,
            VIN,
            registrationCertificate,
            engine,
            vehicleTypeId,
            brand,
            model,
            date,
            price,
            validityId
        )
    }

    fun getVehicleByRegistrationCertificate(
        registrationCertificate: String
    ): Vehicle? {
        val args = listOf(registrationCertificate).toTypedArray()
        val pinQuery = "SELECT * FROM $VEHICLE_TABLE WHERE $KEY_REGISTRATION_CERTIFICATE_VEHICLE=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(pinQuery, args)
        var id = 0
        var clientId = 0
        var licensePlate = ""
        var VIN = ""
        var registrationCertificate = ""
        var engine = 0
        var vehicleTypeId = 0
        var brand = ""
        var model = ""
        var date = ""
        var price = 0.0
        var validityId = 0
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                clientId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_CLIENT_ID))
                licensePlate =
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_LICENSE_PLATE_VEHICLE))
                VIN = cursor.getString(cursor.getColumnIndexOrThrow(KEY_VIN_VEHICLE))
                registrationCertificate = cursor.getString(
                    cursor.getColumnIndexOrThrow(KEY_REGISTRATION_CERTIFICATE_VEHICLE)
                )
                engine = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ENGINE_VEHICLE))
                vehicleTypeId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_TYPE_VEHICLE_ID))
                brand = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BRAND_VEHICLE))
                model = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MODEL_VEHICLE))
                date = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE))
                price = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE))
                validityId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_VALID_ID))
            } while (cursor.moveToNext())
        }
        if (registrationCertificate == "") {
            return null
        }
        /*
        var vehicleType = VehicleTypes.CAR
        if (type != "") {
            vehicleType =
                VehicleTypes.values().first { context.getString(it.id) == type }
        }
         */
        return Vehicle(
            id,
            clientId,
            licensePlate,
            VIN,
            registrationCertificate,
            engine,
            vehicleTypeId,
            brand,
            model,
            date,
            price,
            validityId
        )
    }

    fun getPINs(): MutableList<String> {
        val PINs = mutableListOf<String>()
        val PINsQuery = "SELECT $KEY_PIN_CLIENT FROM $CLIENT_TABLE"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(PINsQuery, null)
        } catch (e: SQLException) {
            db.execSQL(PINsQuery)
            return ArrayList()
        }

        var PIN: String
        if (cursor.moveToFirst()) {
            do {
                PIN = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PIN_CLIENT))
                PINs.add(PIN)
            } while (cursor.moveToNext())
        }
        return PINs
    }

    fun getLicensePlates(): MutableList<String> {
        val licensePlates = mutableListOf<String>()
        val licensePlatesQuery = "SELECT $KEY_LICENSE_PLATE_VEHICLE FROM $VEHICLE_TABLE"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(licensePlatesQuery, null)
        } catch (e: SQLException) {
            db.execSQL(licensePlatesQuery)
            return ArrayList()
        }

        var licensePlate: String
        if (cursor.moveToFirst()) {
            do {
                licensePlate =
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_LICENSE_PLATE_VEHICLE))
                licensePlates.add(licensePlate)
            } while (cursor.moveToNext())
        }
        return licensePlates
    }

    fun getAllVehicles(): ArrayList<Vehicle> {
        val vehicleList = ArrayList<Vehicle>()

        val selectQuery = "SELECT * FROM $VEHICLE_TABLE"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var clientId: Int
        var licensePlate: String
        var VIN: String
        var registrationCertificate: String
        var engine: Int
        var vehicleTypeId: Int
        var brand: String
        var model: String
        var date: String
        var price: Double
        var validityId: Int

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                clientId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_CLIENT_ID))
                licensePlate =
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_LICENSE_PLATE_VEHICLE))
                VIN = cursor.getString(cursor.getColumnIndexOrThrow(KEY_VIN_VEHICLE))
                registrationCertificate = cursor.getString(
                    cursor.getColumnIndexOrThrow(KEY_REGISTRATION_CERTIFICATE_VEHICLE)
                )
                engine = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ENGINE_VEHICLE))
                vehicleTypeId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_TYPE_VEHICLE_ID))
                brand = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BRAND_VEHICLE))
                model = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MODEL_VEHICLE))
                date = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE))
                price = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE))
                validityId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_VALID_ID))


                // val typeVehicle = VehicleTypes.values().first { context.getString(it.id) == type }
                val vehicle = Vehicle(
                    id,
                    clientId,
                    licensePlate,
                    VIN,
                    registrationCertificate,
                    engine,
                    vehicleTypeId,
                    brand,
                    model,
                    date,
                    price,
                    validityId
                )
                vehicleList.add(vehicle)

            } while (cursor.moveToNext())
        }
        return vehicleList
    }

    fun getVehiclesByClientId(clientId: Int, context: Context): ArrayList<Vehicle> {
        val args = listOf(clientId.toString()).toTypedArray()
        val vehicleList = ArrayList<Vehicle>()

        val selectQuery = "SELECT * FROM $VEHICLE_TABLE WHERE $KEY_CLIENT_ID=?"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, args)

        var id: Int
        var clientId: Int
        var licensePlate: String
        var VIN: String
        var registrationCertificate: String
        var engine: Int
        var vehicleTypeId: Int
        var brand: String
        var model: String
        var date: String
        var price: Double
        var validityId: Int

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                clientId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_CLIENT_ID))
                licensePlate =
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_LICENSE_PLATE_VEHICLE))
                VIN = cursor.getString(cursor.getColumnIndexOrThrow(KEY_VIN_VEHICLE))
                registrationCertificate = cursor.getString(
                    cursor.getColumnIndexOrThrow(KEY_REGISTRATION_CERTIFICATE_VEHICLE)
                )
                engine = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ENGINE_VEHICLE))
                vehicleTypeId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_TYPE_VEHICLE_ID))
                brand = cursor.getString(cursor.getColumnIndexOrThrow(KEY_BRAND_VEHICLE))
                model = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MODEL_VEHICLE))
                date = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE))
                price = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRICE))
                validityId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_VALID_ID))


                // val typeVehicle = VehicleTypes.values().first { context.getString(it.id) == type }
                val vehicle = Vehicle(
                    id,
                    clientId,
                    licensePlate,
                    VIN,
                    registrationCertificate,
                    engine,
                    vehicleTypeId,
                    brand,
                    model,
                    date,
                    price,
                    validityId
                )
                vehicleList.add(vehicle)

            } while (cursor.moveToNext())
        }
        return vehicleList
    }

    fun getAllClients(): ArrayList<Client> {
        val clientList = ArrayList<Client>()

        val selectQuery = "SELECT * FROM $CLIENT_TABLE"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var pin: String
        var firstName: String
        var middleName: String
        var lastName: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                pin = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PIN_CLIENT))
                firstName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FIRSTNAME_CLIENT))
                middleName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MIDDLENAME_CLIENT))
                lastName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LASTNAME_CLIENT))

                val client = Client(id, pin, firstName, middleName, lastName)
                clientList.add(client)

            } while (cursor.moveToNext())
        }
        return clientList
    }

    fun checkLogin(nickname: String, password: String): Boolean {
        val args = listOf(nickname, password).toTypedArray()
        val nicknamesQuery =
            "SELECT * FROM $EMPLOYEE_TABLE WHERE $KEY_NICKNAME_EMP=? AND $KEY_PASSWORD_EMP=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(nicknamesQuery, args)
        return cursor.count > 0
    }

    fun checkPasswordReset(
        nickname: String,
        securityQuestionId: Int,
        securityAnswer: String
    ): Boolean {
        val args = listOf(nickname, securityQuestionId.toString(), securityAnswer).toTypedArray()
        val nicknamesQuery =
            "SELECT * FROM $EMPLOYEE_TABLE WHERE $KEY_NICKNAME_EMP=? AND $KEY_SECURITY_QUESTION_ID=? AND $KEY_SECURITY_ANSWER_EMP=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(nicknamesQuery, args)
        return cursor.count > 0
    }
}