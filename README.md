# CS180-Project-5
Code repository for CS180 Project 5.

# Furniture Marketplace

This is a furniture marketplace that allows users to sell or buy different pieces of furniture. Whether it be your first sofa, moving into a new house, setting up a swing set, or signing your divorce papers on a sturdy wooden table, furniture holds a variety of memories and value. Choosing the right pieces not only improves the quality and the overall appearance, but it can also make life easier. Come visit our marketplace to create your next new memory!  

## Project Compilation and Execution

Instructions on how to compile and run this project:

- It is vital that all of the text files are downloaded. To run the project, it is vital that Connector is ran first and then MarketGUI. This will allow you to see the actual workspace/GUI. After this, using the buttons you will go through everything and perform the actions that need to be performed. To log in from multiple accounts, the Connector should be ran. Then, split the terminal and run the MarketGUI again.


## Submission Information

**Submitted Report on Brightspace:**
- Lauren Ellis

**Submitted Vocareum Workspace:**
- Lauren Ellis

**Submitted Presentation Video:**
- Lauren Ellis

## Report of all Classes

### Account Class

**Functionality/Relationship to other classes:**
- Account.java focuses on the creation of the actual account when the customer/seller login and works closely with the AccountsFile.java class. It not only contains methods that allow the user to login, but allows for methods that create, delete, and edit accounts. These methods usually iterate through the list of accounts. 
- In the createAccount method, it makes sure that the email is not already in use and adds the appropriate email and password to the list to store the account. In the deleteAccount method, it looks for the email, and deletes the whole account, making sure that if a seller is deleting their account it also deletes their corresponding stores and items in the store. The editAccount is similar in the sense where it checks if the new values are the same and then replaces the old account with the new one. 

**Testing:**
- Our classes were very long and called a variety of methods and other classes, we made sure to test the other classes/methods separately instead and do a final check by performing a variety of possible scenarios. Manual Testing was the primary form of testing. 
- This includes debugging our programs with print statements and the IntelliJ debugger.
- Most methods were checked manually and through the code in MarketGui.java. 
- The file check methods were checked with separate main methods depending on the situation and were done as if the file already existed and contained the information necessary for that specific method. 


### AccountsFile Class

**Functionality/Relationship to other classes:**
- AccountsFile.java works with the Account.java class and contains the readAccounts, writeAccounts, and add Account methods. The first reads all of the logins from the login CSVfile (not user inputed, the file is defined here), in the String Array, while the writeAccount writes all logins to the file. The addAccount then appends a login to the end of the login file. 

**Testing:**
- Our classes were very long and called a variety of methods and other classes, we made sure to test the other classes/methods separately instead and do a final check by performing a variety of possible scenarios. Manual Testing was the primary form of testing. 
- This includes debugging our programs with print statements and the IntelliJ debugger.
- Most methods were checked manually and through the code in MarketGui.java. 
- The file check methods were checked with separate main methods depending on the situation and were done as if the file already existed and contained the information necessary for that specific method. 


### Client Class

**Functionality/Relationship to other classes:**
- Client.java holds a variety of methods that are called by MarketGui.java
- These write a string of the method name and the methods parameters separated by commas, so that Server.java can run the needed methods from the other classes
- The methods included are from Seller, Customer, and ShoppingCart
- It also reads the response from Server.java, which can include if it was successful at the operation or a string to be used for various reasons (like displaying on a pane)


**Testing:**
- Our classes were very long and called a variety of methods and other classes, we made sure to test the other classes/methods separately instead and do a final check by performing a variety of possible scenarios. Manual Testing was the primary form of testing. 
- This includes debugging our programs with print statements and the IntelliJ debugger.
- Most methods were checked manually and through the code in MarketGui.java. 
- The file check methods were checked with separate main methods depending on the situation and were done as if the file already existed and contained the information necessary for that specific method. 

### Server Class
**Functionality/Relationship to other classes:**
- Server.java includes a large else if block that reads the string passed from Client.java methods and decodes the proper method operations
- For example, if createStore,email,itemList was passed into the class, it would call the method create server using the parameters of email and the list of items to be added
-It then sends the information back to the client for use in the Gui


**Testing:**
- Our classes were very long and called a variety of methods and other classes, we made sure to test the other classes/methods separately instead and do a final check by performing a variety of possible scenarios. Manual Testing was the primary form of testing. 
- This includes debugging our programs with print statements and the IntelliJ debugger.
- Most methods were checked manually and through the code in MarketGui.java. 
- The file check methods were checked with separate main methods depending on the situation and were done as if the file already existed and contained the information necessary for that specific method. 


### Customer Class

**Functionality/Relationship to other classes:**
- The customer class has a variety of methods that allow the customer to perform different actions. The class includes private fields such as email, which refers to the customer’s email and the corresponding getter/setter methods, their shopping cart, and purchase history stores a list of all the customer’s purchases, something that will be used in the main as well as other classes. 
- The getPurchaseHistory method reads the purchase history from a corresponding text file and then writes it based on the customer’s email. In addition, the getPurchaseHistoryStream method allows this information to be outputted to the terminal. 
- The purchased item method simply acts as if the customer is purchasing the item and does a variety of things that would occur when the customer completes this act. This includes decreasing the availability of the item (quantity) and recording the purchase in an all customer purchases file. The reviewPurchaseHistory method allows this to be inputted to the terminal when the customer asks for it.  The customer class also contains two of the methods related to the customer side of the statistics dashboard.
- Overall, this class works with the store and item classes and the main classes to work effectively. 

**Testing:**
- Our classes were very long and called a variety of methods and other classes, we made sure to test the other classes/methods separately instead and do a final check by performing a variety of possible scenarios. Manual Testing was the primary form of testing. 
- This includes debugging our programs with print statements and the IntelliJ debugger.
- Most methods were checked manually and through the code in MarketGui.java. 
- The file check methods were checked with separate main methods depending on the situation and were done as if the file already existed and contained the information necessary for that specific method. 

### Item Class

**Functionality/Relationship to other classes:**
- The Item class mainly creates an item object that includes all the specific parameters it requires such as the store name, the item name, the item description, the item availability (quantity), and the item price, as well as the caressing getter and setter methods. The item class can also be thought of as the product. 
- It also includes methods that create a formatted version of the strong to file, with specific separators that allow us to split the line in later classes for added ease. 
- This class is used in a variety of other classes such as store, seller, customer, etc. 


**Testing:**
- Our classes were very long and called a variety of methods and other classes, we made sure to test the other classes/methods separately instead and do a final check by performing a variety of possible scenarios. Manual Testing was the primary form of testing. 
- This includes debugging our programs with print statements and the IntelliJ debugger.
- Most methods were checked manually and through the code in MarketGui.java. 
- The file check methods were checked with separate main methods depending on the situation and were done as if the file already existed and contained the information necessary for that specific method. 



### MarketGUI Class

**Functionality/Relationship to other classes:**
- MarketGui.java is the the main client side integration of this project
- It has many panels for each of the major features, including the marketplace, the customer and seller statistics, the shopping cart and the export methods
- Each panel has various options and displays depending on the feature it is displaying
- The user is first taken to a page that allows them to create a new account or login. When creating an account, they are able to select their role from the menu and create the email and password and effectively create the account or go back. The user can also click login and enter. their information to be taken to the respective page that is related to the role they chose. 
- Customers were able to see all the store names and the corresponding items that the marketplace offers, and are then able to click on the row for more information. This then takes the customers to a panel where they are able to take a look at important information related to the item including a drop down menu where they can add the product to their cart, the description, the price of the item, and the item name. In addition, the main marketplace contains a search bar at the top of the table for convenience. This allows the customer to search for an item by type the store, item name, or description. Furthermore, they can sort the marketplace by quantity and price. For the customer, the menu bar at the top left also contains a dropdown menu that contains all of the things a customer can do including: viewing the market, the shopping cart, the customer statistics, view purchase history, manage account and logout.
- When logging in as a seller, the user sees a completely different panel with the respective seller options. They are able to see all their stores, and store Names, and are also able to delete a store, open a new store, and open a new store by importing a CSV file. The seller can click on their store to be taken to a page that contains the name of all of the items in their store. They can easily edit the elements they want, such as the name, description, quantity available, and price by simply clicking on the table and entering the new information. In addition, they can also delete an item in the store by typing the name, creating an item in the store, or going back. The drop down menu bar also contains options to view the seller statistics, export to a CSV file, manage the account or log out.
- All of the backend is connected through Client.java, which interacts with the server.
- As each option is pressed, typed, or selected in the GUI, it sends its request to Client.java, which passes it to the Connector.java and finally to Server.java. The success/failure/request is then fed back for MarketGui to display. 


**Testing:**
- Our classes were very long and called a variety of methods and other classes, we made sure to test the other classes/methods separately instead and do a final check by performing a variety of possible scenarios. Manual Testing was the primary form of testing. 
- This includes debugging our programs with print statements and the IntelliJ debugger.
- Most methods were checked manually and through the code in MarketGui.java. 
- The file check methods were checked with separate main methods depending on the situation and were done as if the file already existed and contained the information necessary for that specific method. 


### Connector Class

**Functionality/Relationship to other classes:**
- Connecter.java runs constantly, looking for client connections. When one is found it creates a new server thread for the client to use for method operations
- Once the Client is disconnected, the connector removes the server thread


**Testing:**
- Our classes were very long and called a variety of methods and other classes, we made sure to test the other classes/methods separately instead and do a final check by performing a variety of possible scenarios. Manual Testing was the primary form of testing. 
- This includes debugging our programs with print statements and the IntelliJ debugger.
- Most methods were checked manually and through the code in MarketGui.java. 
- The file check methods were checked with separate main methods depending on the situation and were done as if the file already existed and contained the information necessary for that specific method. 


### Seller Class

**Functionality/Relationship to other classes:**
- The seller class contains a variety of methods that perform different tasks. This class works with almost all of the other classes, and most of the methods involved are called in main. 
- The seller class also contains two of the methods related to the seller side of the statistics dashboard.

**Testing:**
- Our classes were very long and called a variety of methods and other classes, we made sure to test the other classes/methods separately instead and do a final check by performing a variety of possible scenarios. Manual Testing was the primary form of testing. 
- This includes debugging our programs with print statements and the IntelliJ debugger.
- Most methods were checked manually and through the code in MarketGui.java. 
- The file check methods were checked with separate main methods depending on the situation and were done as if the file already existed and contained the information necessary for that specific method. 


### ShoppingCart Class

**Functionality/Relationship to other classes:**
- This class contains an ArrayList of items and allows the user to add and remove items from the cart. This works with the customer class and contains methods that save this cart data to a file and read the data from a file for further use. In addition, a productsString method is created and used in methods when printing out to a file, etc. The class also creates methods to create and add a store as well as add, edit, and delete a product in the store.
- Other than the primary methods that the seller requires, it contains a variety of methods that are all called in main and other methods to perform a specific function. 

**Testing:**
- Our classes were very long and called a variety of methods and other classes, we made sure to test the other classes/methods separately instead and do a final check by performing a variety of possible scenarios. Manual Testing was the primary form of testing. 
- This includes debugging our programs with print statements and the IntelliJ debugger.
- Most methods were checked manually and through the code in MarketGui.java. 
- The file check methods were checked with separate main methods depending on the situation and were done as if the file already existed and contained the information necessary for that specific method. 

### Store Class

**Functionality/Relationship to other classes:**
- This class mainly creates a store object that includes a store name and an array list of items.
- One of the methods, storeRevenue calculates the revenue of the store every time an item is bought from the store and updates this information in a text file called storeRevenue.txt.
- It also contains a variety of smaller methods such as deleteItem, addItem, etc that are called on in the Seller class. 


**Testing:**
- Our classes were very long and called a variety of methods and other classes, we made sure to test the other classes/methods separately instead and do a final check by performing a variety of possible scenarios. Manual Testing was the primary form of testing. 
- This includes debugging our programs with print statements and the IntelliJ debugger.
- Most methods were checked manually and through the code in MarketGui.java. 
- The file check methods were checked with separate main methods depending on the situation and were done as if the file already existed and contained the information necessary for that specific method. 
