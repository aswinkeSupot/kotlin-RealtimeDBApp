# 1. Firebase Adding
> **Reference URL -** https://console.firebase.google.com/  
login to firebase and open firebase console page.
```
Create a Project -> "kotlin-FirebaseApp" -> Continue ->
    Choose or create a Google Analytics account : Default Account for Firebase 
    -> Create porject -> Continue
Open the project "kotlin-FirebaseApp" in firebase console
Get started by adding Firebase to your app - select 'android' for Add an app to get started 
```
- 1 Register app
```
->  Android package Name : com.aswin.firebaseapp
    App nickname : kotlin FirebaseApp
    Debug signing certificate SHA-1 (Optional) :
    -> Register App
```
- 2 Download and then add config file
```
Download google.services.json file and Move your downloaded google-services.json file
into your module (app-level) root directory.
-> Click Next
```
- 3 Add Firebase SDK

1. Add the plugin as a dependency to your project-level build.gradle.kts file:
- Root-level (project-level) Gradle file (<project>/build.gradle.kts):
```
plugins {
  // Add the dependency for the Google services Gradle plugin
  id("com.google.gms.google-services") version "4.4.2" apply false
}
```
2 .Then, in your module (app-level) build.gradle.kts file, add both the google-services plugin and any Firebase SDKs that you want to use in your app:
- Module (app-level) Gradle file (<project>/<app-module>/build.gradle.kts):
```
plugins {
  id("com.android.application")

  // Add the Google services Gradle plugin
  id("com.google.gms.google-services")

  ...
}

dependencies {
  // Import the Firebase BoM
  implementation(platform("com.google.firebase:firebase-bom:33.3.0"))


  // TODO: Add the dependencies for Firebase products you want to use
  // When using the BoM, don't specify versions in Firebase dependencies
  implementation("com.google.firebase:firebase-analytics")


  // Add the dependencies for any other desired Firebase products
  // https://firebase.google.com/docs/android/setup#available-libraries
}
```
Click Next -> Click Continue to the console.


- **Note**  How to Generate the SHA-1 is listed below
 ```
Method 1 :
    Go to Android Studio for -> File -> settings -> Experimental -> 
    UnTick - Do not build Gradle task list during Gradle sync -> Apply -> OK

    ->Build -> Clean and rebuild the project
    -> Sync Project with Gradle Files -> Open 'Gradle' in the right side -> FirebaseApp -> Tasks -> android -> signinReport (RtClick ) 
    -> Run 'FirebaseApp[Sigining..]' -> This will generate SHA1
     
Method 2 : 
    if the above case is note working use programatic Approch:
    fun getSHA1() {
        try {
            val info: PackageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA-1")
                md.update(signature.toByteArray())
                val sha1 = md.digest()

                // Convert byte array to hex string with colons between each byte
                val hexString = sha1.joinToString(separator = ":") { byte ->
                    "%02X".format(byte) // %02X formats to uppercase hex
                }

                Log.d("SHA1", hexString) // Print the SHA-1 hash in the desired format
            }
        } catch (e: Exception) {
            Log.e("SHA1", "Error: ${e.message}")
        }
    }
```

3. Add Internet and Access Network State permission in AndroidManifest.xml
```
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```
#
#
# 2. Realtime Database
> **Reference URL -** https://console.firebase.google.com/project/kotlin-firebaseapp/overview
```
Open the appliction in Firebase console -> Build (Left side) -> Realtime Database
Create Database ->  
                   Realtime Database location : United States(us-central1) -> Next
                   Security rules : Start in test mode - Enable  (test mode will enable for 30days. We can change this later. )
 
 This will open a Realtime Database page
 Eg :- https://kotlin-firebaseapp-default-rtdb.firebaseio.com  (This link is the reference to our Realtime Database)
```

   - 1. Add the Realtime Database SDK to your app
First go to Documentation for more details
> **Reference URL -**  https://firebase.google.com/docs?authuser=0&hl=en
Build -> Realtime Database(From left side) -> Android -> Get Started

**build.gradle.kts -**  Module (app-level) Gradle file (<project>/<app-module>/build.gradle.kts):
```
dependencies {
    // Add the dependency for the Realtime Database library
    implementation("com.google.firebase:firebase-database")
}
```
#
#
# 3. Kotlin codding
### 1. Get a DatabaseReference to Activity.
> **Reference URL -**  https://firebase.google.com/docs/database/android/read-and-write?hl=en&authuser=0#kotlin+ktx
- Goto 'Get a DatabaseReference'
```
class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** RealTime Database Reference **/
        // https://kotlin-firebaseapp-default-rtdb.firebaseio.com/  // This is the reference
        database = Firebase.database.reference

    }
}
```
### 2. Write simple Data to Firebase Realtime Database
> **Reference URL -** https://firebase.google.com/docs/database/android/read-and-write?hl=en&authuser=0#kotlin+ktx
```
    /**Write Simple Data to Firebase**/
    fun writeSimpleData(){
        database.child("price").setValue("1940 $")
    }
```
### 3. Read simple Data From Firebase Realtime Database
```
    /**Read Simple Data from Firebase**/
    fun ReadSimpleData(){
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val gold_price = snapshot.value
                binding.tvResult.text = gold_price.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        database.child("price").addValueEventListener(postListener)
    }
```
### 4. Write Custom Object to Firebase Realtime Database
Create a Kotlin Data Class "User" -> 
```
data class User(val userName: String = "",
                val password: String = "")
```
```
    /**Write Custom Object to Firebase RealTime Database**/
    fun CreateCustomObject(){
        val user1 = User("Aswin", "123")
        database.child("Users").setValue(user1)
    }
```
### 5. Read Custom Object From Firebase Realtime Database
```
    /**Read Custom Object from Firebase RealTime Database**/
    fun ReadCustomObject() {
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val u1 = snapshot.getValue<User>()
                //binding.tvResult.text = u1.toString()
                binding.tvResult.text = "Name : ${u1?.userName.toString()}  \nPassword : ${u1?.password.toString()}"
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        database.child("Users").addValueEventListener(postListener)
    }
```
< **Error:**  When we get and error like below
```
FATAL EXCEPTION: main 
      Process: com.aswin.firebaseapp, PID: 14581
      com.google.firebase.database.DatabaseException: Class com.aswin.firebaseapp.User does not define a no-argument constructor. If you are using ProGuard, make sure these constructors are not stripped.
```
```
This may cause because we miss initialize a blank value for the field in the Object Data class
data class User(val userName: String = "",
                val password: String = "")
```



































