# Testing Documentation

## Table of Contents
- [Introduction](#introduction)
- [Testing Strategy](#testing-strategy)
- [Test Cases](#test-cases)
- [Conclusion](#conclusion)

## Introduction
This will be an outline of the testing we did and that can be done to make sure all components of our project were implemented correctly

## Testing Strategy
In order to test our code, there were a few different strategies we used. Our main technique, that this explanation will focus on, is trying all possible actions a user may take, and confirming they give the correct response. Another strategy we used, especially when we were facing bugs, was to include print statements and use the IDE debugger to test what was actually causing an error in our code.


## Test Cases

### Test 1: Concurrency
1. Run MultiClient Server
2. Run Market GUI
3. Run Market GUI more times to use multiple accounts concurrently

Test Status: Passed

### Test 2: User creates an account
1. Click the create account button on the welcome page
2. Select a role from the drop-down menu
3. Input an email
4. Input a password
5. Click create account button
6. Confirm that the account was created by checking in the accounts.txt file

Test Status: Passed

### Test 3: User login
1. Once the account is created, from the welcome screen click the login button
2. Input incorrect information and click the login button
3. Then the JOptionPane will pop up saying the information is incorrect
4. Click again and try to login, this time with the correct information
5. Confirm that you are logged in by making sure the correct display is showing (look for either the customer or seller menus)

Test Status: Passed

### Test 4: Seller creates store
1. Once logged in, the first screen that pops up as a seller should be a table of the store the seller has and two buttons to create or delete a store.
2. To create a store, select the create store button.
3. Then insert a store name and the number of items to be created in that store and click OK.
4. Then insert the information to create an item and click OK. Repeat this process for each item you would like to create.
5. To confirm, check the JTable again and the store should appear.
6. Also, check the allStores.txt file to make sure the information was appended to this file.

Test Status: Passed

### Test 5: Seller views stores
1. Once logged in, the first screen that pops up as a seller should be a table of the store names that the seller has.
2. To confirm this is correct, compare the JTable to the allStores.txt file and make sure each store is displayed

Test Status: Passed

### Test 6: Seller adds item to store
1. Go to the view stores page as a seller and click on a store in the table to add an item to.
2. This will take you to the page that displays all the items in a table, a back button, and two buttons to create or delete an item in the store.
3. Click the create an item in store button.
4. Input the information it asks for and then click OK.
5. Confirm the new item shows up in the table with the correct information.
6. Also, confirm in the allStores.txt file that the new item was added to the store with the correct information.

Test Status: Passed

### Test 7: Seller edits item in store
1. Make sure you are on the store information page that says Edit Items which has a table of all items in the store.
2. Click on a certain row and column that is the information for the product you would like to edit.
3. Enter the new information you would like to update and click OK.
4. Confirm the information is updated in the table.
5. Also, confirm in the allStores.txt file that the parameter was changed.

Test Status: Passed

### Test 8: Seller deletes item in store
1.  Make sure you are on the store information page that says Edit Items which has a table of all items in the store.
2.  Click the delete an item in store button and click the item you would like to delete from the drop-down menu and click OK.
3.  Confirm the item is removed from the table.
4.  Also, confirm in the allStores.txt files that the item was deleted from the line.

Test Status: Passed

### Test 9: Seller deletes store
1. Make sure you are on the view stores page as a seller.
2. Select the delete a store button.
3. Pick the store you would like to delete from the drop-down and click ok.
4. Confirm the store is deleted from the table.
5. Also, confirm the store is removed from the allStores.txt file.

Test Status: Passed

### Test 10: Seller exports items to CSV
1. Go on to the JMenu drop-down and select the export to CSV option.
2. Confirm that the JOptionPane is shown saying the export was a success and that it includes the file name.
3. Also, confirm the new file exists and that it contains the correct information

Test Status: Passed

### Test 11: Seller change password
1. Click on the Manage Account seller drop-down menu option.
2. Enter the old password for the account you are in and the password you would like to update it to.
3. Click the update password button.
4. Confirm the password was changed in the accounts.txt file.
   
Test Status: Passed

### Test 12: Seller logout
1. Select the logout seller drop-down menu option.
2. Confirm a JOptionPane pops up confirming the logout was a success and click OK.
3. Confirm the program returns to the welcome screen.
   
Test Status: Passed

### Test 13: Customer sorts items
1. Once you are logged in as a customer, confirm you are at the All Listings page.
2. Select a way to sort from the drop-down box (either original, by price, or by quantity).
3. Confirm the table was changed correctly based on the sorting option.

Test Status: Passed

### Test 14: Customer searches for item
1. Once you are logged in as a customer, confirm you are at the All Listings page.
2. Into the search text field, write the term you would like to search by.
3. Click the search button.
4. Confirm the table was changed correctly based on the search term.
   
Test Status: Passed

### Test 15: Customer views item details
1. From the All Listings page, click the row of the item you would like to see the details for.
2. Confirm this takes you to an Item Details page with the correct information
3. Compare the information shown on this page and the information for the item in allStores.txt matches.

Test Status: Passed

### Test 16: Customer buys a product directly from item page
1. From the item details page of the item you would like to buy, select the quantity you would like to buy now from the second drop-down that says "Select the number you would like to buy now:"
2. Confirm the JOptionPane is displayed confirming the item was purchased.
3. View the purchase history and confirm the item is displayed there.

Test Status: Passed

### Test 17: Customer adds product to shopping cart
1. From the item details page of the item you would like to buy, select the quantity you would like to add to the cart from the first drop-down that says "Select the number you would like to add to your cart:"
2. Confirm the JOptionPane is confirming the item was added to the cart (also confirm the specific quantity).
3. View the shopping cart and confirm the item is displayed there correctly

Test Status: Passed

### Test 18: Customer views and purchases shopping cart
1. Select the View Cart option from the customer drop-down menu.
2. Confirm the information displayed here is correct by comparing it to the shoppingcartdata.txt file.
3. Once that is confirmed, click the purchase cart button.
4. Confirm the JOptionPane is shown which confirms the cart is purchased.
5. View the purchase history and confirm the items are displayed there.

Test Status: Passed

### Test 19: Customer views purchase history
1. Select the View Purchase History option from the customer drop-down menu.
2. Confirm that the information displayed on the screen matches the information in the allCustomerPurchases.txt file.

Test Status: Passed

### Test 20: Customer views store data sorted by products sold
1. Select the Customer Stats option from the customer drop-down menu.
2. Click on "View Store Data Sorted by Products Sold"
3. Confirm a JOptionPane is displayed with the correct information based on all products sold.

Test Status: Passed

### Test 21: Customer views store data by purchase history
1. Select the Customer Stats option from the customer drop-down menu.
2. Click on "View Store Data Sorted by Purchase History"
3. Confirm a JOptionPane is displayed with the correct information based on the allCustomerPurchaseHistory.txt file

Test Status: Passed

### Test 22: Customer manages account password
1. Select the manage account option from the customer drop-down menu
2. Enter the old password for the account you are in and the password you would like to update it to.
3. Click the update password button.
4. Confirm the password was changed in the accounts.txt file.
   
Test Status: Passed

### Test 23: Customer logout
1. Select the logout option from the customer drop-down menu
2. Confirm a JOptionPane pops up confirming the logout was a success and click OK.
3. Confirm the program returns to the welcome screen.
   
Test Status: Passed

### Test 24: Seller views sale data sorted by customer 
1. Once you are logged back in as a seller, select the view stats option from the seller drop-down menu.
2. Click on the "View Your Sales Data Sorted by Customer"
3. Confirm a JOptionPane is shown with the correct information

Test Status: Passed 

### Test 25: Seller views sales data sorted by Product
1. Select the view stats option from the seller drop-down menu.
2. Click on the "View Your Sales Data Sorted by Product"
3. Confirm a JOptionPane is shown with the correct information

Test Status: Passed

### Test 26: Seller views shopping cart data
1. Select the view stats option from the seller drop-down menu.
2. Click on the "View Your SShopping Cart Data"
3. Confirm a JOptionPane is shown with the correct information

Test Status: Passed

### Test 27: Seller views list of sales by store
1. Select the view stats option from the seller drop-down menu.
2. Click on the "View Your List of Sales by Store"
3. Confirm a JOptionPane is shown with the correct information

Test Status: Passed

### Test 28: Seller views total revenue
1. Select the view stats option from the seller drop-down menu.
2. Click on "View Your Total Revenue"
3. Confirm a JOptionPane is shown with the correct information

Test Status: Passed

### Test 29: Seller views seller history
1. Select the view stats option from the seller drop-down menu.
2. Click on "View Your Seller History"
3. Confirm a JOptionPane is shown with the correct information

Test Status: Passed

### Test 30: Delete an account
1. Go to the manage account option from the customer or seller drop-down menu
2. Click the delete account button
3. Confirm a JOptionPane is shown saying the account is deleted.
4. Also confirm the account has been deleted from the accounts.txt file

Test Status: Passed

## Conclusion
Overall, we used these test cases to make sure our project worked correctly and efficiently.

