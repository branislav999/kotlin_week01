import java.io.BufferedWriter
import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter
import java.io.File
import java.security.MessageDigest
import java.io.*

class User(val username: String, val password: String)

fun main() {
    //initialize a list of usernames and passwords
    val list = listOf(
        User("jessicalindsey93@gmail.com", "Password123"),
        User("michaelthomas84@yahoo.com", "SecurePassword1"),
        User("sophialee77@hotmail.com", "Passw0rd!"),
        User("davidwilson65@gmail.com", "Secret123"),
        User("emilyroberts88@yahoo.com", "P@ssw0rd"),
        User("ryanjohnson79@hotmail.com", "Password!"),
        User("oliviasmith99@gmail.com", "Password1234"),
        User("ethanbrown71@yahoo.com", "SecurePassword!"),
        User("avaanderson92@hotmail.com", "Passw0rd123"),
        User("williammartinez83@gmail.com", "Secure123")
    )
    //add the list to the passwords.txt file
    writeFile(list)

    while (true)
    {
        //display the menu
        println("Choose an option: ")
        println("1. Login")
        println("2. Generate Random Password")
        println("3. Add Login Credentials to the Database")
        println("4. Change the Password")
        println("5. Exit the Program")


        var input = readLine()
        var choice = input?.toIntOrNull()

        //process the user's choice
        when (choice) {
            1 -> authenticate()
            2 -> println(generateRandomPassword())
            3 -> appendFile()
            4 -> changePassword()
            5 -> break
        }
    }
}

fun writeFile(list: List<User>) {
    //write the list of usernames and passwords to the .txt file
    BufferedWriter(FileWriter("passwords.txt")).use { writer ->
        list.forEach { user ->
            writer.write("${user.username},${encryptPassword(user.password)}\n")
        }
    }
}

fun getUsername(): String {
    var username = ""

    while (username.length < 8){
        //get the username greater than 8 characters
        println("Please enter the username (at least 8 characters):")
        username = readLine().toString()
    }
    return username
}


fun getPassword(): String{
    var password = ""

    while (password.length < 8){
        //get the password greater than 8 characters
        println("Please enter the password (at least 8 characters):")
        password = readLine().toString()
    }

    return password
}

fun appendFile() {
    //get credentials
    val username = getUsername()
    val password = getPassword()

    //append the new username and password to the .txt file
    BufferedWriter(FileWriter("passwords.txt", true)).use { writer ->
        writer.append("$username,${password?.let { encryptPassword(it) }}\n")
    }
}

fun changePassword() {
    var list = mutableListOf<User>()
    println("Please enter your username: ")
    val userUsername = readLine()
    BufferedReader(FileReader("passwords.txt")).use { reader ->
        var line: String? = reader.readLine()
        while (line != null) {
            val (username, password) = line.split(",")
            if (userUsername == username) {
                println("Please enter the password: ")
                val userPassword = readLine()
                if (userPassword?.let { encryptPassword(it) } == password) {
                    println("Password correct. ")
                    val newPassword = getPassword()
                    list.add(User(username, newPassword))
                    println("Successfully changed the password!")
                } else {
                    println("Incorrect password")
                }
            } else {
                list.add(User(username, password))
            }
            line = reader.readLine()
        }
    }
    // Write the updated list back to the file
    writeFile(list)
}


fun authenticate() {
    // get the username
    println("Please enter your username: ")
    var userUsername = readLine()
    var usernameFound = false

    BufferedReader(FileReader("passwords.txt")).use { reader ->
        var line: String? = reader.readLine() //read the first line of the code
        while (line != null) {
            val (username, password) = line.split(",") //divide a pair into username and password
            if (userUsername == username){
                usernameFound = true
                println("Please enter the password: ")
                val userPassword = readLine()
                if (userPassword?.let { encryptPassword(it) } == password){
                    println("\nAuthenticated\n")
                }
                else{
                    println("\nIncorrect password\n")
                }
            }
            line = reader.readLine() //read the next line of the file
        }
        if (!usernameFound){
            println("\nUsername does not exist\n")
        }
    }
}
fun generateRandomPassword(): String {
    println("Please enter the length of the password: ")
    val inputLength = readLine()

    //validate the input of the user input
    val length = try {
        inputLength?.toInt() ?: 8
    } catch (e: NumberFormatException) {
        println("Invalid input length. Using default length of 8.")
        8
    }

    // generate a random string of letters
    val allowedChars = ('a'..'z') + ('A'..'Z') + ('0'..'9') + listOf('!', '@', '#', '$', '%', '^', '&', '*', '(', ')')
    val randomPassword = (1..length)
        .map { allowedChars.random() }
        .joinToString("")

    return randomPassword
}
fun encryptPassword(password: String): String {
    //convert the password to bytes
    val bytes = password.toString().toByteArray()
    //use the SHA-256 algorithm to digest the bytes
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    //convert the bytes back to the string
    val string = digest.fold("", { str, it -> str + "%02x".format(it) })
    return string
}
