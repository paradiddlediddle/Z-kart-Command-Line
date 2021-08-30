package Zkart.user;


public abstract class User {

    private long id;
    private String name;
    private String email;
    private String password;
    private long phoneNumber;

    public User(String name, String email, String password, long phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = encrypt(password);
        this.phoneNumber = phoneNumber;
    }
    //All args constructor
    public User(long id, String name, String email, String password, long phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    // Constructor with just email
    public User(String email) {
        this.email = email;
    }

    // Password Encryptor
    public void changePassword () {
        // Needs to encrypt and update the password
    }

    public String encrypt (String unEncryptedPassword) {

        String encryptedPassword = "";

        for (int i=0; i<unEncryptedPassword.length(); i++) {
          int oldAscii = (int)(unEncryptedPassword.charAt(i));
          int newAscii;
          switch (oldAscii) {
              case 57: newAscii = 48; break;
              case 90: newAscii = 65; break;
              case 122: newAscii = 97; break;
              default: newAscii = oldAscii + 1; break;
          }
          char character = (char)newAscii;
          encryptedPassword+= character;
        }

        return encryptedPassword;
    }

    public String  decrypt (String encryptedPassword) {
        String unEncryptedPassword = "";

        for (int i=0; i<encryptedPassword.length(); i++) {
            int oldAscii =  (int)(encryptedPassword.charAt(i));
            int newAscii;
            switch (oldAscii) {
                case 48: newAscii = 57; break;
                case 65: newAscii = 90; break;
                case 97: newAscii = 122; break;
                default: newAscii = oldAscii - 1;
            }
            unEncryptedPassword+= (char)newAscii;
        }

        return unEncryptedPassword;
    }

    public boolean verifyPassword (String password) {
        boolean output = false;

        if (password.length() >= 6) {
            int uppercaseCount = 0, lowercaseCount = 0, numberCount = 0;

            for (int i=0; i<password.length(); i++) {
                char character = password.charAt(i);

                if (character > 64 && character < 91) { uppercaseCount++; }
                else if (character > 96 && character < 123) { lowercaseCount++; }
                else if ( Character.isDigit(character) ) { numberCount++; }
            }

            if (uppercaseCount >= 2 && lowercaseCount >= 2 && numberCount >= 2) { output = true; }
        }
        return output;
    }


    // Getters and Setters


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEncryptedPassword() {
        return password;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.password = encryptedPassword;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Need to design the Login Screen


}


