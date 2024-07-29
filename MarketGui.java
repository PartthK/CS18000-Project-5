import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Project 05 -- MarketGui.java
 * <p>
 * <p>
 * Manages all the GUI components in this program
 * Each panel is separated into its own method that manages it appropriately
 * The main frame that stays the same is in the run() method
 * <p>
 * Here are some sources I used:
 * - To edit button text size and font:
 * https://stackoverflow.com/questions/20462167/increasing-font-size-in-a-jbutton
 * <p>
 * - To create the layout I wanted:
 * https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html
 * <p>
 * - To make sure the text inputs are the correct format:
 * https://docs.oracle.com/javase/tutorial/uiswing/components/formattedtextfield.html
 *
 * @author Group 2 - L32
 * @version December 9, 2023
 */
public class MarketGui extends JComponent implements Runnable {
    //this creates a client
    //right now the host and port are hard coded!
    Client client = new Client("localHost", 4242);

    //the person currently logged in
    Account currentAccount;

    //this creates a null account object
    Account account = null;

    String currentStoreName;
    String currentItemName;
    Item selectedItem;
    String[] itemDetails;
    //frame
    //there is only one frame in this program, where panels are added and removed from
    static JFrame frame;

    //panels
    JPanel welcomePanel;
    JPanel loginPanel;
    JPanel createAccountPanel;
    JPanel mainMarketListing;
    JPanel shoppingCartPanel;
    JPanel customerStatsPanel;
    JPanel purchaseHistoryPanel;
    JPanel manageCustomerAccountPanel;
    JPanel allItemsListPanel;
    JPanel itemDetailPanel;
    JPanel viewStoresPanel;
    JPanel sellerStatsPanel;
    JPanel manageSellerAccountPanel;
    JPanel editStoresPanel;

    //current panel
    // this is updated every time a panel is removed/added
    // it is needed so you can use JMenu
    JPanel currentPanel;
    JPanel currentSubPanel;

    //JMenu initializations for the customer drop down menu
    JMenuBar customerMenuBar;
    JMenu customerMenu;
    JMenuItem allListings;
    JMenuItem viewCart;
    JMenuItem customerStats;
    JMenuItem viewPurchaseHistory;
    JMenuItem manageCustomerAccount;
    JMenuItem customerLogout;

    //JMenu initializations for the seller drop down menu
    JMenuBar sellerMenuBar;
    JMenu sellerMenu;
    JMenuItem viewStores;
    JMenuItem sellerStats;
    JMenuItem toCSV;
    JMenuItem manageSellerAccount;
    JMenuItem sellerLogout;

    //buttons
    JButton loginButton;
    JButton createAccountButton;
    JButton loginWithInfo;
    JButton backToWelcomeButton;
    JButton makeAccountWithInfoButton;
    JButton searchButton;
    JButton updatePassButton;
    JButton deleteStoreButton;
    JButton openNewStoreButton;
    JButton importNewStoreButton;
    JButton backToAllItemButton;
    JButton backToAllStoresButton;
    JButton deleteItemInStore;
    JButton createItemInStore;
    JButton totalRevenue;
    JButton salesByStore;
    JButton shoppingCartData;
    JButton dataByProduct;
    JButton dataByCustomer;
    JButton sellerHistory;
    JButton dataByProductsSold;
    JButton dataByPurchaseHistory;
    JButton deleteCustomerButton;
    JButton deleteSellerButton;
    JButton buyAllItems;
    JButton clearCart;
    JButton exportPurchaseHistoryToFile;

    //comboBoxes -> dropdown boxes so users are forced to only pick certain options
    JComboBox<String> userRole;
    JComboBox<String> sortingOptions;
    JComboBox<String> addCartQuantityOptions;
    JComboBox<String> buyNowQuantOptions;

    //Tables
    JTable allStoresList;
    JTable allItemsTable;
    JTable itemsInStoreTable;

    //list of all the storeNames for the certain seller
    String[][] storeNameList;

    //text fields
    JTextField email;
    JTextField password;
    JTextField searchFor;
    JTextField oldPassword;
    JTextField newPassword;


    //creates the actionListener which tells the program what to do when a certain button is pressed
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == backToWelcomeButton) {
                frame.remove(currentPanel);
                frame.add(welcomeScreen(), BorderLayout.CENTER);
                frame.validate();
            }
            //if the login button is pressed ...
            if (e.getSource() == loginButton) {
                frame.remove(welcomePanel);
                frame.add(loginScreen(), BorderLayout.CENTER);
                frame.validate();

                currentPanel = loginPanel;
            }

            //if the login with info button is pressed...
            if (e.getSource() == loginWithInfo) {
                String response = null;
                try {
                    response = client.login(email.getText(), password.getText());
                    currentAccount = new Account(email.getText(), password.getText(), response.split(",")[1]);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                //if the account log
                // get into is a customer account
                if (response.split(",")[0].equalsIgnoreCase("Success")) {
                    if (response.split(",")[1].equalsIgnoreCase("Customer")) {
                        frame.remove(loginPanel);
                        frame.add(viewMarket(), BorderLayout.NORTH);
                        frame.validate();
                        frame.repaint();

                        currentPanel = mainMarketListing;

                        //if the account logged into is a seller account
                    }
                    if (response.split(",")[1].equalsIgnoreCase("Seller")) {
                        sellerMenuBar = createSellerMenuBar();
                        frame.remove(loginPanel);
                        try {
                            frame.add(viewStoresPanel(), BorderLayout.NORTH);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        frame.validate();
                        frame.repaint();

                        currentPanel = viewStoresPanel;
                    }
                } else if (response.split(",")[0].equalsIgnoreCase("Failure")) {
                    JOptionPane.showMessageDialog(null, response.split(",")[1], "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            //if the create account button is pressed...
            if (e.getSource() == createAccountButton) {
                frame.remove(welcomePanel);
                frame.add(createAccountScreen(), BorderLayout.CENTER);
                frame.validate();

                currentPanel = createAccountPanel;
            }

            //if the makeAccountWithInfoButton is pressed...
            if (e.getSource() == makeAccountWithInfoButton) {
                String accountCreationSuccessful = null;
                try {
                    accountCreationSuccessful = client.createAccount(email.getText(), password.getText(),
                            (String) userRole.getSelectedItem());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                if (accountCreationSuccessful.equalsIgnoreCase("Success")) {
                    frame.remove(createAccountPanel);
                    frame.add(welcomeScreen(), BorderLayout.CENTER);
                    frame.validate();

                    currentPanel = welcomePanel;
                } else {
                    JOptionPane.showMessageDialog(null, accountCreationSuccessful.split(",")[1],
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            //if the user (customer or seller) wants to update their password
            if (e.getSource() == updatePassButton) {
                try {
                    currentAccount.editAccount(currentAccount.getEmail(), newPassword.getText(),
                            currentAccount.getRole());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                currentAccount.setPassword(newPassword.getText());

                JOptionPane.showMessageDialog(null, "Password Updated!", "Update Status",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            //action listeners for customer menu

            //if the all listings menu option is selected...
            if (e.getSource() == allListings) {

                frame.remove(currentPanel);
                frame.add(viewMarket(), BorderLayout.NORTH);
                frame.validate();
                frame.repaint();

                currentPanel = mainMarketListing;
            }

            if (e.getSource() == sortingOptions) {
                mainMarketListing.remove(allItemsListPanel);
                GridBagConstraints layout = new GridBagConstraints();
                layout.gridwidth = 2;
                layout.gridx = 0;
                layout.gridy = 8;
                try {
                    mainMarketListing.add(allItemsListPanel(), layout);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                mainMarketListing.validate();

                currentSubPanel = allItemsListPanel;
                currentPanel = mainMarketListing;
            }
            if (e.getSource() == searchButton) {
                mainMarketListing.remove(currentSubPanel);
                GridBagConstraints layout = new GridBagConstraints();
                layout.gridwidth = 2;
                layout.gridx = 0;
                layout.gridy = 8;
                try {
                    mainMarketListing.add(allItemsListPanel(), layout);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                mainMarketListing.validate();

                currentSubPanel = allItemsListPanel;
                currentPanel = mainMarketListing;
            }

            if (e.getSource() == addCartQuantityOptions) {
                try {
                    if (client.addProductToCart(email.getText(), currentStoreName, currentItemName, itemDetails[0],
                                    itemDetails[1], itemDetails[2], (String) addCartQuantityOptions.getSelectedItem())
                            .equalsIgnoreCase("Success")) {
                        JOptionPane.showMessageDialog(null, "Product added to cart!",
                                "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error. " +
                                        "The amount in your cart will" +
                                        " exceed quantity available.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error, try again",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }


            if (e.getSource() == buyNowQuantOptions) {
                try {
                    String emailForStore = client.getEmailFromStoreName(currentStoreName);
                    itemDetails = client.getItemDetails(emailForStore, currentItemName, currentStoreName);
                    client.purchaseNow(email.getText(), currentStoreName, currentItemName, itemDetails[0],
                            itemDetails[1], itemDetails[2],
                            String.valueOf(buyNowQuantOptions.getSelectedItem()));
                    JOptionPane.showMessageDialog(null, "Product purchased!",
                            "Confirmation", JOptionPane.INFORMATION_MESSAGE);

                    frame.remove(currentPanel);
                    frame.add(itemDetailPanel(currentStoreName, currentItemName), BorderLayout.NORTH);
                    frame.validate();
                    frame.repaint();

                    currentPanel = itemDetailPanel;
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error, try again",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }

            //if the back button is pressed from the item details...
            if (e.getSource() == backToAllItemButton) {
                frame.remove(currentPanel);
                frame.add(viewMarket(), BorderLayout.NORTH);
                frame.validate();

                currentPanel = mainMarketListing;
            }
            //if the view cart menu option is selected...
            if (e.getSource() == viewCart) {
                frame.remove(currentPanel);
                try {
                    frame.add(shoppingCartPanel(), BorderLayout.NORTH);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                frame.validate();
                frame.repaint();

                currentPanel = shoppingCartPanel;
            }
            //if the view purchase history menu option is selected...
            if (e.getSource() == viewPurchaseHistory) {
                frame.remove(currentPanel);
                try {
                    frame.add(purchaseHistoryPanel(), BorderLayout.NORTH);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                frame.validate();
                frame.repaint();

                currentPanel = purchaseHistoryPanel;
            }
            //export purchase history button
            if (e.getSource() == exportPurchaseHistoryToFile) {
                try {
                    client.exportPurchaseHistory(email.getText());
                    JOptionPane.showMessageDialog(null, "Purchase history exported to file!",
                            "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error, try again", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }
            //buy All Items button
            if (e.getSource() == buyAllItems) {
                try {
                    int option = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want to purchase the Cart?",
                            "Confirmation", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        String purchaseCartCheck = client.purchaseCart(email.getText());
                        if (purchaseCartCheck.equals("Success")) {
                            JOptionPane.showMessageDialog(null, "Cart Purchased! Congrats!",
                                    "Purchase Complete", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Nothing to purchase yet!",
                                    "Purchase Cart", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error, try again", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
                frame.remove(currentPanel);
                try {
                    frame.add(shoppingCartPanel(), BorderLayout.NORTH);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                frame.validate();
                frame.repaint();

                currentPanel = shoppingCartPanel;

            }
            //Clear cart button
            if (e.getSource() == clearCart) {
                //implement function below here
                try {
                    if (client.deleteCart(email.getText()).equals("Success")) {
                        int option = JOptionPane.showConfirmDialog(null,
                                "Are you sure you want to clear your Cart?",
                                "Confirmation", JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION) {
                            client.deleteCart(email.getText());
                            JOptionPane.showMessageDialog(null, "Your cart has been cleared.",
                                    "Clear Cart", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "You have nothing in your cart!",
                                "Clear Cart", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error, try again", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
                frame.remove(currentPanel);
                try {
                    frame.add(shoppingCartPanel(), BorderLayout.NORTH);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                frame.validate();
                frame.repaint();

                currentPanel = shoppingCartPanel;

            }
            //if the manage customer account menu option is selected...
            if (e.getSource() == manageCustomerAccount) {
                frame.remove(currentPanel);
                frame.add(manageCustomerAccountPanel(), BorderLayout.NORTH);
                frame.validate();
                frame.repaint();

                currentPanel = manageCustomerAccountPanel;
            }

            //actionListeners for seller menu

            //if the view stores menu option is selected... or you want to go back to this panel
            if (e.getSource() == viewStores) {
                frame.remove(currentPanel);
                try {
                    frame.add(viewStoresPanel(), BorderLayout.NORTH);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                frame.validate();
                frame.repaint();

                currentPanel = viewStoresPanel;
            }
            if (e.getSource() == backToAllStoresButton) {
                frame.remove(currentPanel);
                try {
                    frame.add(viewStoresPanel(), BorderLayout.NORTH);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                frame.validate();
                frame.repaint();

                currentPanel = viewStoresPanel;
            }
            //if the seller stats menu option is selected...
            if (e.getSource() == sellerStats) {
                frame.remove(currentPanel);
                frame.add(sellerStatsPanel(), BorderLayout.NORTH);
                frame.validate();
                frame.repaint();

                currentPanel = sellerStatsPanel;
            }

            //if the customer stats menu option is selected...
            if (e.getSource() == customerStats) {
                frame.remove(currentPanel);
                frame.add(customerStatsPanel(), BorderLayout.NORTH);
                frame.validate();
                frame.repaint();

                currentPanel = customerStatsPanel;
            }

            //buttons clicked on customer stats page, pop up accord to JOptionPane
            if (e.getSource() == dataByProductsSold) {
                try {
                    if (!client.dataByProductsSold(email.getText()).isEmpty()) {
                        JOptionPane.showMessageDialog(null, client.dataByProductsSold(email.getText()),
                                "Data by Products Sold", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Nothing purchased from store yet",
                                "Data by Products Sold", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error, try again", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }
            if (e.getSource() == dataByPurchaseHistory) {
                try {
                    if (!client.dataByPurchaseHistory(email.getText()).isEmpty()) {
                        JOptionPane.showMessageDialog(null,
                                client.dataByPurchaseHistory(email.getText()),
                                "Data by Purchase History", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Nothing purchased from store yet!",
                                "Data by Purchase History", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error, try again", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }

            //buttons clicked on seller stats page, pop up according JOptionPane

            if (e.getSource() == dataByProduct) {
                try {
                    String temp = client.salesOfSpecificProduct(email.getText());
                    if (!temp.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Sales of a Specific Product: "
                                + temp, "Data by Product", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Nothing purchased from store yet!",
                                "databyProduct", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error, try again", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }

            if (e.getSource() == dataByCustomer) {
                try {
                    String temp = client.salesOfSpecificCustomer(email.getText());
                    if (!temp.isEmpty()) {
                        JOptionPane.showMessageDialog(null,
                                "Sales of a Specific Customer: " + temp,
                                "Data by Customer", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Nothing purchased from store yet",
                                "Data by Customer", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error, try again", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }
            if (e.getSource() == shoppingCartData) {
                try {
                    String temp = client.getCustomerShoppingcartData(email.getText());
                    if (!temp.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Shopping Cart Data: " + temp,
                                "Shopping Cart Data", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Nothing purchased from store yet",
                                "Shopping Cart Data", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error, try again",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }
            if (e.getSource() == salesByStore) {
                try {
                    String temp = client.salesByStore(email.getText());
                    if (!temp.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Sales By Store: " + temp,
                                "Sales By Store", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Nothing purchased from store yet",
                                "Sales By Store", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error, try again",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }
            if (e.getSource() == totalRevenue) {
                try {
                    String temp = client.totalRevenue(email.getText());
                    if (!temp.isEmpty()) {
                        JOptionPane.showMessageDialog(null, temp,
                                "Total Revenue", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Nothing purchased from store yet",
                                "Total Revenue", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error, try again",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }
            if (e.getSource() == sellerHistory) {
                try {
                    String temp = client.sellerHistory(email.getText());
                    if (!temp.isEmpty()) {
                        JOptionPane.showMessageDialog(null, temp,
                                "Seller History", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Nothing purchased from store yet",
                                "Seller History", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error, try again",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }

            //if the toCSV menu option is selected...
            if (e.getSource() == toCSV) {
                try {
                    String worked = client.exportProductsToCsv(email.getText());
                    if (worked.equals("Success")) {
                        JOptionPane.showMessageDialog(null, worked +
                                        ", Export to CSV Complete!" +
                                        "\nThe name of the file should be "
                                        + email.getText() + "-products.csv",
                                "Export Status", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error, try again",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }

            }
            //if the manage seller account menu option is selected...
            if (e.getSource() == manageSellerAccount) {
                frame.remove(currentPanel);
                frame.add(manageSellerAccountPanel(), BorderLayout.NORTH);
                frame.validate();
                frame.repaint();

                currentPanel = manageSellerAccountPanel;
            }

            if (e.getSource() == deleteStoreButton) {
                //JTextField storeName = new JTextField();
                String[] storeNames = new String[storeNameList.length];
                for (int i = 0; i < storeNameList.length; i++) {
                    storeNames[i] = storeNameList[i][0];
                }
                Object selection = JOptionPane.showInputDialog(null,
                        "Please Select the name of the store you would like to delete:",
                        "Delete Store", JOptionPane.QUESTION_MESSAGE,
                        null, storeNames, null);
                if (selection != null) {
                    for (int i = 0; i < storeNames.length; i++) {
                        if (selection.equals(storeNames[i])) {
                            try {
                                String worked = client.deleteStore(email.getText(), (String) selection);
                                if (worked.equals("Success")) {
                                    JOptionPane.showMessageDialog(null,
                                            "Store Deleted Successfully!",
                                            "Delete Store", JOptionPane.INFORMATION_MESSAGE);
                                }
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(null, "Error, try again!",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                }
                frame.remove(currentPanel);
                try {
                    frame.add(viewStoresPanel(), BorderLayout.NORTH);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                frame.validate();
                frame.repaint();

                currentPanel = viewStoresPanel;
            }

            //if they open a new store menu option is selected...
            if (e.getSource() == openNewStoreButton) {
                JTextField storeName = new JTextField();
                JTextField itemsToMakeCount = new JTextField();

                itemsToMakeCount.setText("0");
                Object[] textFields = {"Input the Store Name", storeName
                        , "Enter the amount of items you would like to create", itemsToMakeCount};
                try {
                    Integer.parseInt(itemsToMakeCount.getText());
                    int values = JOptionPane.showConfirmDialog(null, textFields
                            , "Open new store", JOptionPane.OK_CANCEL_OPTION);
                    if (Integer.parseInt(itemsToMakeCount.getText()) == 0) {
                        JOptionPane.showMessageDialog(null,
                                "You cannot create a store without any items"
                                , "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (values == JOptionPane.OK_OPTION) {
                        int newItemsMade = 0;

                        ArrayList<Item> itemsForNewStore = new ArrayList<>();
                        int val;

                        do {
                            JTextField itemName = new JTextField();
                            JTextField itemDescription = new JTextField();
                            JFormattedTextField itemQuantAvailable = new JFormattedTextField("0");
                            JTextField itemPrice = new JTextField("0.00");

                            Object[] itemInputs = {"Item Name: ", itemName
                                    , "Item Description: ", itemDescription
                                    , "Quantity Available (enter an integer): ", itemQuantAvailable
                                    , "Item Price (enter as 12.34): $", itemPrice};
                            val = JOptionPane.showConfirmDialog(null, itemInputs,
                                    "Open new store", JOptionPane.OK_CANCEL_OPTION);
                            try {
                                Item item = new Item(storeName.getText(), itemName.getText(),
                                        itemDescription.getText(), Integer.parseInt(itemQuantAvailable.getText()),
                                        Integer.parseInt(itemQuantAvailable.getText()));

                                itemsForNewStore.add(item);

                                currentPanel = viewStoresPanel;
                            } catch (NumberFormatException numWrong) {
                                JOptionPane.showMessageDialog(null,
                                        "Error, the quantity must be an integer " +
                                                "and the price must be a double",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }

                            newItemsMade += 1;
                        } while (newItemsMade < Integer.parseInt(itemsToMakeCount.getText())
                                && val == JOptionPane.OK_OPTION);
                        if (val == JOptionPane.OK_OPTION) {
                            try {
                                String newStore = client.createStore(email.getText(),
                                        storeName.getText(), itemsForNewStore);
                                if (!newStore.equals("Success")) {
                                    JOptionPane.showMessageDialog(null,
                                            "Error, store already exists",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(null,
                                        "Error, try again",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                                throw new RuntimeException(ex);
                            }
                            frame.remove(currentPanel);
                            try {
                                frame.add(viewStoresPanel(), BorderLayout.NORTH);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            frame.validate();
                            frame.repaint();
                        }
                    }
                } catch (NumberFormatException numWrong) {
                    JOptionPane.showMessageDialog(null,
                            "Error, you must make the items to create an integer",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }

            }

            if (e.getSource() == importNewStoreButton) {
                JTextField fileName = new JTextField();
                Object[] textFields = {"Input the Store Name", fileName};
                int values = JOptionPane.showConfirmDialog(null, textFields,
                        "Import New Store", JOptionPane.OK_CANCEL_OPTION);
                File f = new File(fileName.getText());
                if (f.isFile()) {
                    ArrayList<Item> itemsForNewStore = null;
                    try {
                        itemsForNewStore = Seller.importItem(fileName.getText());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error, try again",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        throw new RuntimeException(ex);
                    }
                    try {
                        if (itemsForNewStore != null) {
                            client.createStore(email.getText(), itemsForNewStore.get(0).getStoreName(),
                                    itemsForNewStore);
                        } else {
                            JOptionPane.showMessageDialog(null, "Error. Your CSV file is " +
                                    "likely not formatted correctly.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error, try again",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "There is no file associated with the given file name.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                frame.remove(currentPanel);
                try {
                    frame.add(viewStoresPanel(), BorderLayout.NORTH);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                frame.validate();
                frame.repaint();

                currentPanel = viewStoresPanel;
            }

            if (e.getSource() == createItemInStore) {
                JTextField itemName = new JTextField();
                JTextField itemDescription = new JTextField();
                JTextField itemQuantAvailable = new JTextField();
                JTextField itemPrice = new JTextField();

                Object[] itemInputs = {"Item Name: ", itemName, "Item Description: ", itemDescription
                        , "Quantity Available (enter an integer): ", itemQuantAvailable
                        , "Item Price (enter as 12.34): $", itemPrice};
                int val = JOptionPane.showConfirmDialog(null, itemInputs,
                        "Add New Item to Store", JOptionPane.OK_CANCEL_OPTION);

                if (val == JOptionPane.OK_OPTION) {
                    try {
                        try {
                            Integer.parseInt(itemQuantAvailable.getText());
                            Double.parseDouble(itemPrice.getText());
                            if (client.newItemInStore(email.getText(), currentStoreName,
                                    itemName.getText(), itemDescription.getText(),
                                    itemQuantAvailable.getText(),
                                    itemPrice.getText()).equals("false")) {
                                JOptionPane.showMessageDialog(null,
                                        "Error, item already exists in store",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }

                            frame.remove(currentPanel);
                            frame.add(editStoresPanel(), BorderLayout.NORTH);
                            frame.validate();
                            frame.repaint();

                            currentPanel = editStoresPanel;
                        } catch (NumberFormatException numError) {
                            JOptionPane.showMessageDialog(null, "Error, you must enter an " +
                                            "integer for the quantity available and a double for the price",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error, try again",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        throw new RuntimeException(ex);
                    }
                }
            }

            if (e.getSource() == deleteItemInStore) {
                String[] itemNames;
                try {
                    ArrayList<Item> itemListForStore = client.getItemList("", currentStoreName);
                    itemNames = new String[itemListForStore.size()];
                    for (int i = 0; i < itemNames.length; i++) {
                        itemNames[i] = itemListForStore.get(i).getItemName();
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                String itemName = (String) JOptionPane.showInputDialog(null,
                        "Select the item to delete",
                        "Delete Item In Store", JOptionPane.QUESTION_MESSAGE, null,
                        itemNames, null);
                try {
                    client.deleteItemInStore(email.getText(), currentStoreName, itemName);
                    JOptionPane.showMessageDialog(null, "Item Deleted",
                            "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error, try again",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
                frame.remove(currentPanel);
                frame.add(editStoresPanel(), BorderLayout.NORTH);
                frame.validate();
                frame.repaint();

                currentPanel = editStoresPanel;
            }

            //if the logout option is pressed from the menu (on customer or seller menu)...
            if (e.getSource() == sellerLogout || e.getSource() == customerLogout) {
                frame.remove(currentPanel);
                JOptionPane.showMessageDialog(null, "Logout successful!",
                        "Logout Status",
                        JOptionPane.INFORMATION_MESSAGE);
                frame.add(welcomeScreen(), BorderLayout.CENTER);
                frame.validate();

                currentAccount = null;
                currentPanel = welcomePanel;
            }

            if (e.getSource() == deleteCustomerButton || e.getSource() == deleteSellerButton) {
                try {
                    client.deleteAccount(email.getText(), password.getText(), currentAccount.getRole());
                    JOptionPane.showMessageDialog(null, "Account deleted successfully!",
                            "Delete Account", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error, account couldn't be deleted please try again",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }

                frame.remove(currentPanel);
                frame.add(welcomeScreen(), BorderLayout.CENTER);
                frame.validate();
                frame.repaint();

                currentPanel = welcomePanel;
            }
        }
    };
    ListSelectionListener listSelectionListener = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (allStoresList.getSelectedRow() > -1 && allStoresList != null) {
                currentStoreName = allStoresList.getValueAt(allStoresList.getSelectedRow(), 0).toString();
                frame.remove(currentPanel);
                frame.add(editStoresPanel(), BorderLayout.NORTH);
                frame.validate();
                frame.repaint();

                currentPanel = editStoresPanel;
            }
        }
    };

    ListSelectionListener listSelectionListener2 = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (allItemsTable.getSelectedRow() > -1 && allItemsTable != null) {
                currentStoreName = allItemsTable.getValueAt(allItemsTable.getSelectedRow(), 0).toString();
                currentItemName = allItemsTable.getValueAt(allItemsTable.getSelectedRow(), 1).toString();
                frame.remove(currentPanel);
                try {
                    frame.add(itemDetailPanel(currentStoreName, currentItemName), BorderLayout.NORTH);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error, try again",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
                frame.validate();
                frame.repaint();

                currentPanel = itemDetailPanel;
            }
        }
    };

    ListSelectionListener listSelectionListener3 = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (itemsInStoreTable.getSelectedColumn() > -1 && itemsInStoreTable != null) {
                selectedItem = new Item(currentStoreName,
                        (String) itemsInStoreTable.getValueAt(itemsInStoreTable.getSelectedRow(), 0),
                        (String) itemsInStoreTable.getValueAt(itemsInStoreTable.getSelectedRow(), 1),
                        Integer.parseInt(String.valueOf(
                                itemsInStoreTable.getValueAt(itemsInStoreTable.getSelectedRow(), 2))),
                        Double.parseDouble(
                                String.valueOf(itemsInStoreTable.getValueAt(
                                        itemsInStoreTable.getSelectedRow(), 3)).split("\\$")[1]));

                String updated = "";
                switch (itemsInStoreTable.getSelectedColumn()) {
                    case 0:
                        updated = JOptionPane.showInputDialog(null,
                                "Please enter the new item name",
                                "Update Item Name",
                                JOptionPane.INFORMATION_MESSAGE);
                        if (updated != null) {
                            try {
                                client.editItemInStore(email.getText(), selectedItem.getStoreName(),
                                        selectedItem.getItemName(), selectedItem.getItemDescription(),
                                        String.valueOf(selectedItem.getAvailable()),
                                        String.valueOf(selectedItem.getPrice()),
                                        "itemName:" + updated);
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(null,
                                        "Error, please try again",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                                throw new RuntimeException(ex);
                            }
                        }
                        break;
                    case 1:
                        updated = JOptionPane.showInputDialog(null,
                                "Please enter the new item description",
                                "Update Item Description",
                                JOptionPane.INFORMATION_MESSAGE);
                        if (updated != null) {
                            try {
                                client.editItemInStore(email.getText(), selectedItem.getStoreName(),
                                        selectedItem.getItemName(), selectedItem.getItemDescription(),
                                        String.valueOf(selectedItem.getAvailable()),
                                        String.valueOf(selectedItem.getPrice()),
                                        "itemDescription:" + updated);
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(null,
                                        "Error, please try again",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                                throw new RuntimeException(ex);
                            }
                        }
                        break;
                    case 2:

                        updated = JOptionPane.showInputDialog(null,
                                "Please enter the new quantity available",
                                "Update Item Quantity",
                                JOptionPane.INFORMATION_MESSAGE);
                        if (updated != null) {
                            try {
                                Integer.parseInt(updated);
                                try {
                                    client.editItemInStore(email.getText(), selectedItem.getStoreName(),
                                            selectedItem.getItemName(), selectedItem.getItemDescription(),
                                            String.valueOf(selectedItem.getAvailable()),
                                            String.valueOf(selectedItem.getPrice()),
                                            "itemQuantAvail:" + updated);
                                } catch (IOException ex) {
                                    JOptionPane.showMessageDialog(null,
                                            "Error, please try again",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    throw new RuntimeException(ex);
                                }
                            } catch (NumberFormatException numWrong) {
                                JOptionPane.showMessageDialog(null,
                                        "Error, the quantity must be an integer",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        break;
                    case 3:
                        updated = JOptionPane.showInputDialog(null,
                                "Please enter the new item price",
                                "Update Item Price", JOptionPane.INFORMATION_MESSAGE);
                        if (updated != null) {
                            try {
                                Double.parseDouble(updated);
                                try {
                                    client.editItemInStore(email.getText(), selectedItem.getStoreName(),
                                            selectedItem.getItemName(), selectedItem.getItemDescription(),
                                            String.valueOf(selectedItem.getAvailable()),
                                            String.valueOf(selectedItem.getPrice()),
                                            "itemPrice:" + updated);
                                } catch (IOException ex) {
                                    JOptionPane.showMessageDialog(null,
                                            "Error, please try again",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    throw new RuntimeException(ex);
                                }
                            } catch (NumberFormatException numWrong) {
                                JOptionPane.showMessageDialog(null,
                                        "Error, the quantity must be a double",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        break;
                }


                frame.remove(currentPanel);
                frame.add(editStoresPanel(), BorderLayout.NORTH);
                frame.validate();
                frame.repaint();

                currentPanel = editStoresPanel;

            }
        }
    };

    public MarketGui() throws IOException {
    }

    //creates the welcome panel
    //this welcomes the user to the market
    //it then has three buttons which allows the user to login, create account, or exit the program
    //it then takes the user to the corresponding screen
    public JPanel welcomeScreen() {
        welcomePanel = new JPanel();
        welcomePanel.setLayout(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();

        //creates a label with a welcome message
        JLabel welcomeMessage = new JLabel("Welcome to the Furniture Market!");
        layout.fill = GridBagConstraints.BOTH;
        layout.ipady = 40;
        layout.gridx = 0;
        layout.gridy = 0;
        welcomeMessage.setFont(new Font("Arial", Font.BOLD, 25));
        welcomePanel.add(welcomeMessage, layout);

        //creates the login button
        loginButton = new JButton("Login");
        layout.fill = GridBagConstraints.BOTH;
        layout.ipady = 10;
        layout.gridx = 0;
        layout.gridy = 1;
        loginButton.setFont(new Font("Arial", Font.BOLD, 25));
        loginButton.addActionListener(actionListener);
        welcomePanel.add(loginButton, layout);


        //creates the createAccountButton
        createAccountButton = new JButton("Create New Account");
        layout.fill = GridBagConstraints.BOTH;
        layout.ipady = 10;
        layout.gridx = 0;
        layout.gridy = 2;
        layout.insets = new Insets(20, 0, 0, 0);
        createAccountButton.setFont(new Font("Arial", Font.BOLD, 25));
        createAccountButton.addActionListener(actionListener);
        welcomePanel.add(createAccountButton, layout);

        return welcomePanel;

    }

    //makes the create account screen
    //this allows the user to create an account with a role, email/username, and password
    //then they click the create account button
    public JPanel createAccountScreen() {
        //creates the panel and sets the layout
        createAccountPanel = new JPanel();
        createAccountPanel.setLayout(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();

        //gives the user the choice to select a customer or seller role
        JLabel role = new JLabel("Select Role from menu:");
        String[] userOptions = new String[2];
        userOptions[0] = "Customer";
        userOptions[1] = "Seller";
        userRole = new JComboBox<String>(userOptions);
        layout.fill = GridBagConstraints.BOTH;
        layout.gridx = 0;
        layout.gridy = 0;
        role.setFont(new Font("Arial", Font.BOLD, 15));
        createAccountPanel.add(role);
        layout.fill = GridBagConstraints.BOTH;
        layout.ipadx = 30;
        layout.gridx = 1;
        layout.gridy = 0;
        userRole.setFont(new Font("Arial", Font.BOLD, 15));
        createAccountPanel.add(userRole);

        //prompts the user to enter an email
        JLabel emailPrompt = new JLabel("Email:");
        email = new JTextField("smith123@email.net", 20);
        layout.fill = GridBagConstraints.BOTH;
        layout.gridx = 0;
        layout.gridy = 1;
        layout.insets = new Insets(20, 0, 0, 0);
        emailPrompt.setFont(new Font("Arial", Font.BOLD, 15));
        createAccountPanel.add(emailPrompt, layout);
        layout.fill = GridBagConstraints.BOTH;
        layout.gridx = 1;
        layout.gridy = 1;
        email.setFont(new Font("Arial", Font.BOLD, 15));
        createAccountPanel.add(email, layout);

        //prompts the user to enter a password
        JLabel passPrompt = new JLabel("Password:");
        password = new JTextField("Password123!", 20);
        layout.fill = GridBagConstraints.BOTH;
        layout.gridx = 0;
        layout.gridy = 2;
        passPrompt.setFont(new Font("Arial", Font.BOLD, 15));
        createAccountPanel.add(passPrompt, layout);
        layout.fill = GridBagConstraints.BOTH;
        layout.gridx = 1;
        layout.gridy = 2;
        password.setFont(new Font("Arial", Font.BOLD, 15));
        createAccountPanel.add(password, layout);

        //creates the makeAccountWithInfoButton and adds it to panel
        makeAccountWithInfoButton = new JButton("Create Account");
        makeAccountWithInfoButton.addActionListener(actionListener);
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.weightx = 0.0;
        layout.gridwidth = 3;
        layout.gridx = 0;
        layout.gridy = 3;
        layout.insets = new Insets(20, 0, 0, 0);
        makeAccountWithInfoButton.setFont(new Font("Arial", Font.BOLD, 20));
        createAccountPanel.add(makeAccountWithInfoButton, layout);

        backToWelcomeButton = new JButton("Back");
        layout.gridx = 0;
        layout.gridy = 5;
        layout.insets = new Insets(20, 0, 0, 0);
        backToWelcomeButton.addActionListener(actionListener);
        createAccountPanel.add(backToWelcomeButton, layout);
        //returns the panel
        return createAccountPanel;
    }

    //this panel is seen after the user clicks login from the welcome screen
    //it allows them to put in their username and password and then click login
    public JPanel loginScreen() {
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();

        layout.insets = new Insets(10, 0, 0, 0);

        JLabel instructions = new JLabel("Please enter the email and password associated with your account");
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.weightx = 0.0;
        layout.gridwidth = 3;
        layout.gridx = 0;
        layout.gridy = 0;
        instructions.setFont(new Font("Arial", Font.BOLD, 15));
        loginPanel.add(instructions, layout);

        //creates the prompt and text field for the user to enter their email
        layout.gridwidth = 1;
        JLabel emailPrompt = new JLabel("Email:");
        email = new JTextField("smith123@email.net", 20);
        layout.fill = GridBagConstraints.BOTH;
        layout.gridx = 0;
        layout.gridy = 1;
        emailPrompt.setFont(new Font("Arial", Font.BOLD, 15));
        loginPanel.add(emailPrompt, layout);
        layout.fill = GridBagConstraints.BOTH;
        layout.gridx = 1;
        layout.gridy = 1;
        email.setFont(new Font("Arial", Font.BOLD, 15));
        loginPanel.add(email, layout);

        //creates the prompt and text field for the user to enter their password
        JLabel passPrompt = new JLabel("Password: ");
        password = new JTextField("Password123!", 20);
        layout.fill = GridBagConstraints.BOTH;
        layout.gridwidth = 1;
        layout.gridx = 0;
        layout.gridy = 2;
        passPrompt.setFont(new Font("Arial", Font.BOLD, 15));
        loginPanel.add(passPrompt, layout);
        layout.fill = GridBagConstraints.BOTH;
        layout.gridx = 1;
        layout.gridy = 2;
        password.setFont(new Font("Arial", Font.BOLD, 15));
        loginPanel.add(password, layout);

        //creates the login button for once the user has entered their information
        loginWithInfo = new JButton("Login");
        loginWithInfo.addActionListener(actionListener);
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.weightx = 0.0;
        layout.gridwidth = 3;
        layout.gridx = 0;
        layout.gridy = 3;
        loginWithInfo.setFont(new Font("Arial", Font.BOLD, 20));
        loginPanel.add(loginWithInfo, layout);

        backToWelcomeButton = new JButton("Back");
        layout.gridx = 0;
        layout.gridy = 5;
        layout.insets = new Insets(20, 0, 0, 0);
        backToWelcomeButton.addActionListener(actionListener);
        loginPanel.add(backToWelcomeButton, layout);

        return loginPanel;
    }

    //view all the market listings
    //the first thing the customer goes to after they login
    public JPanel viewMarket() {
        // creates the panel and sets the layout
        mainMarketListing = new JPanel();
        mainMarketListing.setLayout(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();

        // Set preferred size for mainMarketListing (adjust values as needed)
        mainMarketListing.setPreferredSize(new Dimension(10, 750));

        // menu
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.gridwidth = 3;
        layout.gridx = 0;
        layout.gridy = 0;
        mainMarketListing.add(createCustomerMenuBar(), layout);

        // title label for all market listings
        JLabel title = new JLabel("All Listings");
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.gridwidth = 1;
        layout.gridx = 0;
        layout.gridy = 4;
        layout.insets = new Insets(10, 10, 0, 10);
        layout.weightx = 1.0;
        title.setFont(new Font("Arial", Font.BOLD, 20));
        mainMarketListing.add(title, layout);

        // search prompt so the user has clear instructions
        JLabel viewMorePrompt = new JLabel("To search for a certain item " +
                "please enter the product name/description, or store name" +
                " and then hit the search button");
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.gridwidth = 1;
        layout.gridx = 0;
        layout.gridy = 5;
        layout.insets = new Insets(5, 10, 5, 10);
        layout.weightx = 1.0;
        mainMarketListing.add(viewMorePrompt, layout);

        // search box where you type things in
        searchFor = new JTextField();
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.gridwidth = 1;
        layout.gridx = 0;
        layout.gridy = 6;
        layout.insets = new Insets(0, 10, 0, 0);
        layout.weightx = 1.0;
        mainMarketListing.add(searchFor, layout);

        // search button to click when you are ready to see results
        searchButton = new JButton("Search");
        searchButton.addActionListener(actionListener);
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.gridx = 1;
        layout.gridy = 6;
        layout.insets = new Insets(0, 0, 0, 10);
        layout.weightx = 1.0;
        mainMarketListing.add(searchButton, layout);

        // Sorting options dropdown
        String[] userOptions = new String[3];
        userOptions[0] = "Original Sort";
        userOptions[1] = "Sort by Price (High to Low)";
        userOptions[2] = "Sort by Quantity (High to Low)";
        sortingOptions = new JComboBox<String>(userOptions);
        sortingOptions.addActionListener(actionListener);
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.gridx = 0;
        layout.gridy = 7;
        layout.insets = new Insets(0, 10, 0, 0);
        layout.weightx = 1.0;
        sortingOptions.setFont(new Font("Arial", Font.BOLD, 15));
        mainMarketListing.add(sortingOptions, layout);

        // All items list panel (assuming allItemsListPanel() returns a JPanel)
        layout.gridwidth = 2;
        layout.gridx = 0;
        layout.gridy = 8;
        layout.insets = new Insets(0, 0, 0, 0);
        try {
            mainMarketListing.add(allItemsListPanel(), layout);
            mainMarketListing.validate();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        currentSubPanel = allItemsListPanel;

        return mainMarketListing;
    }


    public JPanel allItemsListPanel() throws IOException {
        // makes panel and sets layout
        allItemsListPanel = new JPanel();
        allItemsListPanel.setLayout(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();

        // sets allItems to empty, then tries to fill it.
        // if it gives an exception because there's no products, it catches it by setting it to empty again
        ArrayList<Item> allItems;
        try {
            allItems = client.getItemList((String) sortingOptions.getSelectedItem(),
                    String.valueOf(searchFor.getText()));
        } catch (ArrayIndexOutOfBoundsException e) {
            allItems = new ArrayList<>();
        }
        String[][] infoList = new String[allItems.size()][3];

        if (allItems.size() == 0) {
            JLabel noItems = new JLabel("No items are available for purchase at the moment");
            layout.gridx = 0;
            layout.gridy = 0;
            layout.insets = new Insets(0, 10, 0, 0);
            layout.weightx = 1.0;
            noItems.setFont(new Font("Arial", Font.BOLD, 20));
            allItemsListPanel.add(noItems, layout);
        } else {
            for (int i = 0; i < allItems.size(); i++) {
                infoList[i][0] = allItems.get(i).getStoreName();
                infoList[i][1] = allItems.get(i).getItemName();
                infoList[i][2] = String.format("$%.2f", allItems.get(i).getPrice());
            }

            String[] columnNames = {"Store Name:", "Item Name", "Item Price"};
            allItemsTable = new JTable(infoList, columnNames);
            allItemsTable.getSelectionModel().addListSelectionListener(listSelectionListener2);

            allItemsTable.setDefaultEditor(Object.class, null);
            allItemsTable.getTableHeader().setReorderingAllowed(false);
            allItemsTable.getTableHeader().setResizingAllowed(false);

            layout.gridx = 0;
            layout.gridy = 0; // Update to align the table to the top
            layout.ipadx = frame.getWidth();
            layout.ipady = frame.getHeight();
            layout.insets = new Insets(0, 10, 0, 10);
            layout.weightx = 1.0;

            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setViewportView(allItemsTable);
            allItemsListPanel.add(scrollPane, layout);
        }

        return allItemsListPanel;
    }


    public JPanel itemDetailPanel(String storeName, String itemName) throws IOException {
        //either could be given the store object or the name and look through file for info?
        itemDetailPanel = new JPanel();
        itemDetailPanel.setLayout(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();

        //adds the menu bar to the panel
        layout.fill = GridBagConstraints.BOTH;
        layout.gridwidth = frame.getWidth();
        layout.ipadx = frame.getWidth();
        layout.gridheight = 3;
        layout.gridx = 0;
        layout.gridy = 0;
        itemDetailPanel.add(customerMenuBar, layout);

        //sets margins from now on
        layout.insets = new Insets(0, 10, 0, 10);
        layout.weightx = 1.0;
        String emailForThis = client.getEmailFromStoreName(storeName);
        String[] details = client.getItemDetails(emailForThis, itemName, storeName);
        itemDetails = details;
        itemName = itemName.toUpperCase().charAt(0) + itemName.substring(1);
        JLabel title = new JLabel(itemName + " Details");
        layout.gridheight = 1;
        layout.gridx = 0;
        layout.gridy = 4;
        title.setFont(new Font("Arial", Font.BOLD, 25));
        itemDetailPanel.add(title, layout);

        JLabel description = new JLabel("Item Description: ");
        layout.gridheight = 1;
        layout.gridx = 0;
        layout.gridy = 5;
        title.setFont(new Font("Arial", Font.BOLD, 20));
        itemDetailPanel.add(description, layout);

        JLabel itemDescription = new JLabel(itemDetails[0]);
        layout.gridx = 0;
        layout.gridy = 6;
        itemDetailPanel.add(itemDescription, layout);

        JLabel quantity = new JLabel("Item Quantity Available: ");
        layout.gridheight = 1;
        layout.gridx = 0;
        layout.gridy = 7;
        title.setFont(new Font("Arial", Font.BOLD, 20));

        itemDetailPanel.add(quantity, layout);

        JLabel itemQuantity = new JLabel(itemDetails[1]);
        layout.gridx = 0;
        layout.gridy = 8;
        itemDetailPanel.add(itemQuantity, layout);

        JLabel price = new JLabel("Item Price: ");
        layout.gridheight = 1;
        layout.gridx = 0;
        layout.gridy = 9;
        title.setFont(new Font("Arial", Font.BOLD, 20));
        itemDetailPanel.add(price, layout);

        JLabel itemPrice = new JLabel("$" + itemDetails[2]);
        layout.gridx = 0;
        layout.gridy = 10;
        itemDetailPanel.add(itemPrice, layout);

        JLabel emailText = new JLabel("Seller Email to Contact: ");
        layout.gridheight = 1;
        layout.gridx = 0;
        layout.gridy = 11;
        title.setFont(new Font("Arial", Font.BOLD, 20));
        itemDetailPanel.add(emailText, layout);

        JLabel sellerEmail = new JLabel(client.getEmailFromStoreName(currentStoreName));
        layout.gridx = 0;
        layout.gridy = 12;
        itemDetailPanel.add(sellerEmail, layout);

        JLabel instructions = new JLabel("Select the number you would like to add to your cart: ");
        layout.gridheight = 1;
        layout.gridx = 0;
        layout.gridy = 13;
        title.setFont(new Font("Arial", Font.BOLD, 20));
        itemDetailPanel.add(instructions, layout);


        String[] quantOptions = new String[Integer.parseInt(itemDetails[1])];
        for (int i = 0; i < quantOptions.length; i++) {
            quantOptions[i] = String.valueOf(i + 1);
        }
        addCartQuantityOptions = new JComboBox<String>(quantOptions);
        addCartQuantityOptions.addActionListener(actionListener);
        layout.gridx = 0;
        layout.gridy = 14;
        itemDetailPanel.add(addCartQuantityOptions, layout);

        JLabel buyNowInstructions = new JLabel("Select the number you would like to buy now: ");
        layout.gridheight = 1;
        layout.gridx = 0;
        layout.gridy = 15;
        title.setFont(new Font("Arial", Font.BOLD, 20));
        itemDetailPanel.add(buyNowInstructions, layout);

        String[] quantOptionsBuyNow = new String[Integer.parseInt(itemDetails[1])];
        for (int i = 0; i < quantOptionsBuyNow.length; i++) {
            quantOptionsBuyNow[i] = String.valueOf(i + 1);
        }
        buyNowQuantOptions = new JComboBox<String>(quantOptionsBuyNow);
        buyNowQuantOptions.addActionListener(actionListener);
        layout.gridx = 0;
        layout.gridy = 16;
        itemDetailPanel.add(buyNowQuantOptions, layout);

        //adds a back button!
        backToAllItemButton = new JButton("Back");
        layout.gridx = 0;
        layout.gridy = 17;
        //changes inset to add space above back button
        layout.insets = new Insets(20, 10, 0, 10);
        backToAllItemButton.addActionListener(actionListener);
        itemDetailPanel.add(backToAllItemButton, layout);

        return itemDetailPanel;
    }

    public JPanel shoppingCartPanel() throws IOException {
        //shopping cart panel and set the layout to GridBag
        shoppingCartPanel = new JPanel();
        shoppingCartPanel.setLayout(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();

        //adds the menu bar to the panel
        layout.fill = GridBagConstraints.BOTH;
        layout.gridwidth = frame.getWidth();
        layout.ipadx = frame.getWidth();
        layout.gridheight = 3;
        layout.gridx = 0;
        layout.gridy = 0;
        shoppingCartPanel.add(customerMenuBar, layout);

        //sets the margins for the rest of the panel
        layout.insets = new Insets(0, 10, 0, 10);
        layout.weightx = 1.0;

        JLabel cartPanelLabel = new JLabel("Shopping Cart");
        layout.fill = GridBagConstraints.BOTH;
        layout.gridheight = 1;
        layout.gridx = 0;
        layout.gridy = 4;
        cartPanelLabel.setFont(new Font("Arial", Font.BOLD, 20));
        shoppingCartPanel.add(cartPanelLabel, layout);

        //loop through an array list of all items in shopping cart, maybe create a sub-panel?
        String cart = client.getShoppingCart(email.getText());

        buyAllItems = new JButton("Buy all the items in your cart!");
        buyAllItems.addActionListener(actionListener);
        layout.gridheight = 1;
        layout.gridx = 0;
        layout.gridy = 5;
        shoppingCartPanel.add(buyAllItems, layout);

        clearCart = new JButton("Clear your shopping cart");
        clearCart.addActionListener(actionListener);
        layout.gridheight = 1;
        layout.gridx = 0;
        layout.gridy = 6;
        shoppingCartPanel.add(clearCart, layout);

        //if there are no items in the cart
        if (cart.isEmpty()) {
            JLabel noItemsInCart = new JLabel("There are no items currently in the cart");
            layout.gridx = 0;
            layout.gridy = 7;
            shoppingCartPanel.add(noItemsInCart, layout);
        } else { //otherwise (if there are items in the cart, show them)
            JLabel printCart = new JLabel("<html>" + cart + "</html>");
            layout.gridx = 0;
            layout.gridy = 7;
            layout.insets = new Insets(0, 10, 0, 10);
            layout.weightx = 1.0;
            layout.weighty = 1.0;
            JScrollPane cartScroll = new JScrollPane(printCart);
            cartScroll.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));
            shoppingCartPanel.add(cartScroll, layout);
        }
        //created button for buying cart


        //return the panel created
        return shoppingCartPanel;
    }


    public JPanel purchaseHistoryPanel() throws IOException {
        // creates panel and sets layout
        purchaseHistoryPanel = new JPanel();
        purchaseHistoryPanel.setLayout(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();

        layout.fill = GridBagConstraints.BOTH;
        layout.gridwidth = GridBagConstraints.REMAINDER; // Set to REMAINDER for full width
        purchaseHistoryPanel.add(customerMenuBar, layout);

        // sets margins from now on
        layout.insets = new Insets(0, 10, 0, 10);
        layout.weightx = 1.0;

        JLabel title = new JLabel("Purchase History");
        layout.gridx = 0;
        layout.gridy = 2; // Adjusted gridy
        title.setFont(new Font("Arial", Font.BOLD, 20));
        purchaseHistoryPanel.add(title, layout);

        exportPurchaseHistoryToFile = new JButton("Click here to Export Purchase History to File!");
        exportPurchaseHistoryToFile.addActionListener(actionListener);
        layout.gridheight = 1;
        layout.gridx = 0;
        layout.gridy = 1;
        purchaseHistoryPanel.add(exportPurchaseHistoryToFile, layout);


        // sets allPurchasedItems to empty, tries to fill it, and if there is no purchase history it sets back to empty
        // this prevents a crash
        ArrayList<Item> allPurchasedItems = new ArrayList<>();
        try {
            allPurchasedItems = client.getPurchaseHistory(email.getText());

        } catch (ArrayIndexOutOfBoundsException e) {
            allPurchasedItems = new ArrayList<>();
        }

        String[][] infoList = new String[allPurchasedItems.size()][4];

        if (allPurchasedItems.size() == 0) {
            JLabel noItemsPurchased = new JLabel("No items have been purchased yet");
            layout.gridx = 0;
            layout.gridy = 3; // Adjusted gridy
            layout.insets = new Insets(0, 10, 0, 0);
            layout.weightx = 1.0;
            noItemsPurchased.setFont(new Font("Arial", Font.BOLD, 15));
            purchaseHistoryPanel.add(noItemsPurchased, layout);
        } else {
            for (int i = 0; i < allPurchasedItems.size(); i++) {
                infoList[i][0] = allPurchasedItems.get(i).getStoreName();
                infoList[i][1] = allPurchasedItems.get(i).getItemName();
                infoList[i][2] = allPurchasedItems.get(i).getItemDescription();
                infoList[i][3] = String.format("$%.2f", allPurchasedItems.get(i).getPrice());
            }

            String[] columnNames = {"Store Name", "Item Name:", "Description", "Price"};
            JTable allPurchasedTable = new JTable(infoList, columnNames);
            layout.gridx = 0;
            layout.gridy = 2; // Adjusted gridy
            layout.gridwidth = GridBagConstraints.REMAINDER; // Set to REMAINDER for full width
            layout.fill = GridBagConstraints.BOTH; // Make the component expand both horizontally and vertically
            layout.insets = new Insets(0, 10, 0, 10);
            layout.weightx = 1.0;
            layout.weighty = 1.0; // Set weighty to allow vertical expansion

            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setViewportView(allPurchasedTable);
            purchaseHistoryPanel.add(scrollPane, layout);
        }
        // returns the panel
        return purchaseHistoryPanel;
    }

    public JPanel manageCustomerAccountPanel() {
        manageCustomerAccountPanel = new JPanel();
        manageCustomerAccountPanel.setLayout(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();

        layout.fill = GridBagConstraints.BOTH;
        layout.gridwidth = frame.getWidth();
        layout.gridheight = 3;
        layout.gridx = 0;
        layout.gridy = 0;
        manageCustomerAccountPanel.add(customerMenuBar, layout);

        // sets margins from now on
        layout.insets = new Insets(0, 10, 0, 10);
        layout.weightx = 1.0;

        JLabel title = new JLabel("Manage Account");
        layout.gridheight = 1;
        layout.gridwidth = 1;
        layout.gridx = 0;
        layout.gridy = 4;
        title.setFont(new Font("Arial", Font.BOLD, 20));
        manageCustomerAccountPanel.add(title, layout);

        // general instructions
        JLabel changePassPrompt = new JLabel("To change your account password," +
                " enter the following information:");
        layout.gridx = 0;
        layout.gridy = 5;
        title.setFont(new Font("Arial", Font.BOLD, 15));
        manageCustomerAccountPanel.add(changePassPrompt, layout);

        // prompts for old password
        JLabel oldPassPrompt = new JLabel("Please enter your old password: ");
        layout.fill = GridBagConstraints.BOTH;
        layout.gridx = 0;
        layout.gridy = 7;
        manageCustomerAccountPanel.add(oldPassPrompt, layout);

        oldPassword = new JTextField();
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.ipadx = 200;
        layout.gridwidth = 2;
        layout.gridx = 1;
        layout.gridy = 7;
        manageCustomerAccountPanel.add(oldPassword, layout);

        // prompts for new password
        JLabel newPassPrompt = new JLabel("Please enter your new password: ");
        layout.fill = GridBagConstraints.BOTH;
        layout.gridwidth = 1;
        layout.gridx = 0;
        layout.gridy = 8;
        manageCustomerAccountPanel.add(newPassPrompt, layout);

        newPassword = new JTextField();
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.ipadx = 200;
        layout.gridwidth = 2;
        layout.gridx = 1;
        layout.gridy = 8;
        manageCustomerAccountPanel.add(newPassword, layout);

        // creates the button to click when all information is entered
        updatePassButton = new JButton("Update Password");
        layout.fill = GridBagConstraints.BOTH;
        layout.gridwidth = 4;
        layout.gridx = 0;
        layout.gridy = 9;
        layout.insets = new Insets(5, 10, 0, 10);
        updatePassButton.addActionListener(actionListener);
        manageCustomerAccountPanel.add(updatePassButton, layout);

        // create the delete customer button
        deleteCustomerButton = new JButton("Delete Customer");
        layout.fill = GridBagConstraints.BOTH;
        layout.gridwidth = 4;
        layout.gridx = 0;
        layout.gridy = 10;
        layout.insets = new Insets(5, 10, 0, 10);
        deleteCustomerButton.addActionListener(actionListener);
        manageCustomerAccountPanel.add(deleteCustomerButton, layout);

        return manageCustomerAccountPanel;
    }


    public JPanel viewStoresPanel() throws IOException {
        viewStoresPanel = new JPanel();
        viewStoresPanel.setLayout(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();

        layout.fill = GridBagConstraints.BOTH;
        layout.gridwidth = frame.getWidth();
        layout.gridheight = 3;
        layout.gridx = 0;
        layout.gridy = 0;
        viewStoresPanel.add(sellerMenuBar, layout);

        // sets margins from now on
        layout.insets = new Insets(0, 10, 0, 10);
        layout.weightx = 1.0;

        JLabel title = new JLabel("All of your Stores");
        layout.gridheight = 1;
        layout.gridx = 0;
        layout.gridy = 4;
        title.setFont(new Font("Arial", Font.BOLD, 20));
        viewStoresPanel.add(title, layout);

        deleteStoreButton = new JButton("Delete A Store");
        deleteStoreButton.addActionListener(actionListener);
        layout.gridx = 0;
        layout.gridy = 5;
        // changes margins to include a space above and below the button
        layout.insets = new Insets(10, 10, 10, 10);
        viewStoresPanel.add(deleteStoreButton, layout);

        openNewStoreButton = new JButton("Open A New Store");
        openNewStoreButton.addActionListener(actionListener);
        layout.gridx = 0;
        layout.gridy = 6;
        viewStoresPanel.add(openNewStoreButton, layout);

        importNewStoreButton = new JButton("Open A New Store by Importing CSV File");
        importNewStoreButton.addActionListener(actionListener);
        layout.gridx = 0;
        layout.gridy = 7;
        viewStoresPanel.add(importNewStoreButton, layout);

        // resets margins
        layout.insets = new Insets(0, 10, 0, 10);

        String[] columnNames = {"Store Name"};
        ArrayList<String> storeNames = new ArrayList<>();

        ArrayList<Store> allStores = client.getStores(email.getText());
        if (allStores.size() == 0) {
            JLabel noStores = new JLabel("No stores have been created yet");
            layout.gridx = 0;
            layout.gridy = 8;
            viewStoresPanel.add(noStores, layout);
        } else {

            storeNameList = new String[allStores.size()][1];
            for (int i = 0; i < storeNameList.length; i++) {
                storeNameList[i][0] = allStores.get(i).getStoreName();
            }

            // makes the table with the needed information
            allStoresList = new JTable(storeNameList, columnNames);

            // I used this source to make the JTable uneditable but still be able to see which option they selected
            // https://stackoverflow.com/questions/9919230/disable-user-edit-in-jtable
            allStoresList.setDefaultEditor(Object.class, null);
            layout.gridx = 0;
            layout.gridy = 8;

            allStoresList.getSelectionModel().addListSelectionListener(listSelectionListener);
            JScrollPane scrollPane = new JScrollPane(allStoresList);
            layout.gridx = 0;
            layout.gridy = 8;
            layout.ipadx = frame.getWidth(); // Makes the table the whole width of the screen
            layout.ipady = frame.getHeight();
            layout.anchor = GridBagConstraints.NORTH; // Aligns the table to the top
            layout.weighty = 1.0; // Allow vertical expansion
            viewStoresPanel.add(scrollPane, layout);

        }

        return viewStoresPanel;
    }


    //starting from here - pp
    //creating a panel that allows you to see the Store Name, etc
    public JPanel storeInformationPanel() {
        JPanel storeInformationPanel = new JPanel();
        storeInformationPanel.setLayout(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();

        layout.insets = new Insets(10, 10, 10, 10);
        layout.weightx = 1.0;

        deleteItemInStore = new JButton("Delete An Item in the Store");
        deleteItemInStore.addActionListener(actionListener);
        layout.gridwidth = GridBagConstraints.REMAINDER;
        layout.gridx = 0;
        layout.gridy = 0;
        storeInformationPanel.add(deleteItemInStore, layout);

        createItemInStore = new JButton("Create An Item in the Store");
        createItemInStore.addActionListener(actionListener);
        layout.gridx = 0;
        layout.gridy = 1;
        storeInformationPanel.add(createItemInStore, layout);

        layout.weighty = 1.0;
        layout.gridy = 2;
        layout.fill = GridBagConstraints.BOTH;

        ArrayList<Item> storesItems = new ArrayList<>();
        ArrayList<Item> allItems = null;
        try {
            allItems = client.getItemList("", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Item item : allItems) {
            if (item.getStoreName().equals(currentStoreName)) {
                storesItems.add(item);
            }
        }

        String[][] itemsInStoreInfo = new String[storesItems.size()][4];
        String[] columnNames = {"Name", "Description", "Quantity Available", "Price"};

        for (int i = 0; i < storesItems.size(); i++) {
            Item currentItem = storesItems.get(i);
            itemsInStoreInfo[i][0] = currentItem.getItemName();
            itemsInStoreInfo[i][1] = currentItem.getItemDescription();
            itemsInStoreInfo[i][2] = String.valueOf(currentItem.getAvailable());
            itemsInStoreInfo[i][3] = String.format("$%.2f", currentItem.getPrice());
        }

        itemsInStoreTable = new JTable(itemsInStoreInfo, columnNames);
        itemsInStoreTable.setDefaultEditor(Object.class, null);
        itemsInStoreTable.getTableHeader().setReorderingAllowed(false);
        itemsInStoreTable.getTableHeader().setResizingAllowed(false);
        itemsInStoreTable.getSelectionModel().addListSelectionListener(listSelectionListener3);

        JScrollPane scrollPane = new JScrollPane(itemsInStoreTable);
        storeInformationPanel.add(scrollPane, layout);

        return storeInformationPanel;
    }


    public JPanel sellerStatsPanel() {
        sellerStatsPanel = new JPanel();
        sellerStatsPanel.setLayout(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();

        layout.fill = GridBagConstraints.BOTH;
        layout.gridwidth = frame.getWidth();
        layout.gridheight = 3;
        layout.gridx = 0;
        layout.gridy = 0;
        sellerStatsPanel.add(sellerMenuBar, layout);

        //sets margins from now on
        layout.insets = new Insets(0, 10, 0, 10);
        layout.weightx = 1.0;

        JLabel title = new JLabel("Statistics");
        layout.gridheight = 1;
        layout.gridx = 0;
        layout.gridy = 4;
        title.setFont(new Font("Arial", Font.BOLD, 20));
        sellerStatsPanel.add(title, layout);


        layout.insets = new Insets(10, 10, 0, 10);
        layout.weightx = 1.0;

        dataByCustomer = new JButton("View Your Sales Data Sorted by Customer");
        dataByCustomer.addActionListener(actionListener);
        layout.gridx = 0;
        layout.gridy = 5;
        sellerStatsPanel.add(dataByCustomer, layout);

        dataByProduct = new JButton("View Your Sales Data Sorted by Product");
        dataByProduct.addActionListener(actionListener);
        layout.gridx = 0;
        layout.gridy = 6;
        sellerStatsPanel.add(dataByProduct, layout);

        shoppingCartData = new JButton("View Your Shopping Cart Data");
        shoppingCartData.addActionListener(actionListener);
        layout.gridx = 0;
        layout.gridy = 7;
        sellerStatsPanel.add(shoppingCartData, layout);

        salesByStore = new JButton("View Your List of Sales by Store");
        salesByStore.addActionListener(actionListener);
        layout.gridx = 0;
        layout.gridy = 8;
        sellerStatsPanel.add(salesByStore, layout);

        totalRevenue = new JButton("View Your Total Revenue");
        totalRevenue.addActionListener(actionListener);
        layout.gridx = 0;
        layout.gridy = 9;
        sellerStatsPanel.add(totalRevenue, layout);

        sellerHistory = new JButton("View Your Seller History");
        sellerHistory.addActionListener(actionListener);
        layout.gridx = 0;
        layout.gridy = 10;
        sellerStatsPanel.add(sellerHistory, layout);


        return sellerStatsPanel;
    }

    public JPanel customerStatsPanel() {
        customerStatsPanel = new JPanel();
        customerStatsPanel.setLayout(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();

        layout.fill = GridBagConstraints.BOTH;
        layout.gridwidth = frame.getWidth();
        layout.gridheight = 3;
        layout.gridx = 0;
        layout.gridy = 0;
        customerStatsPanel.add(customerMenuBar, layout);

        //sets margins from now on
        layout.insets = new Insets(0, 10, 0, 10);
        layout.weightx = 1.0;

        JLabel title = new JLabel("View Customer Statistics");
        layout.gridx = 0;
        layout.gridy = 4;
        title.setFont(new Font("Arial", Font.BOLD, 20));
        customerStatsPanel.add(title, layout);

        //add customer stats to this panel
        //\n1. View Store Data Sorted by Products Sold" +
        //  "\n2. View Store Data Sorted by Purchase History" +

        dataByProductsSold = new JButton("View Store Data Sorted by Products Sold");
        dataByProductsSold.addActionListener(actionListener);
        layout.gridheight = 1;
        layout.gridx = 0;
        layout.gridy = 8;
        customerStatsPanel.add(dataByProductsSold, layout);
        // customer.seeAllStoresAndSales();

        dataByPurchaseHistory = new JButton("View Store Data Sorted by Purchase History");
        dataByPurchaseHistory.addActionListener(actionListener);
        layout.gridx = 0;
        layout.gridy = 10;
        customerStatsPanel.add(dataByPurchaseHistory, layout);
        // customer.howManyProductsFromEachStore();


        return customerStatsPanel;
    }

    public JPanel manageSellerAccountPanel() {
        manageSellerAccountPanel = new JPanel();
        manageSellerAccountPanel.setLayout(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();

        layout.fill = GridBagConstraints.BOTH;
        layout.gridwidth = frame.getWidth();
        layout.gridheight = 3;
        layout.gridx = 0;
        layout.gridy = 0;
        manageSellerAccountPanel.add(sellerMenuBar, layout);

        // sets margins from now on
        layout.insets = new Insets(0, 10, 0, 10);
        layout.weightx = 1.0;

        JLabel title = new JLabel("Manage Account");
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.gridx = 0;
        layout.gridy = 4;
        title.setFont(new Font("Arial", Font.BOLD, 20));
        manageSellerAccountPanel.add(title, layout);

        // you should only be able to change the password since file information is related to email and role

        // general instructions
        JLabel changePassPrompt = new JLabel("To change your account password, " +
                "enter the following information:");
        layout.gridx = 0;
        layout.gridy = 5;
        title.setFont(new Font("Arial", Font.BOLD, 15));
        manageSellerAccountPanel.add(changePassPrompt, layout);

        // prompts for old password
        JLabel oldPassPrompt = new JLabel("Please enter your old password: ");
        layout.fill = GridBagConstraints.BOTH;
        layout.gridx = 0;
        layout.gridy = 7;
        manageSellerAccountPanel.add(oldPassPrompt, layout);

        oldPassword = new JTextField();
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.ipadx = 200;
        layout.gridwidth = 2;
        layout.gridx = 1;
        layout.gridy = 7;
        manageSellerAccountPanel.add(oldPassword, layout);

        // prompts for new password
        JLabel newPassPrompt = new JLabel("Please enter your new password: ");
        layout.fill = GridBagConstraints.BOTH;
        layout.gridwidth = 1;
        layout.gridx = 0;
        layout.gridy = 8;
        manageSellerAccountPanel.add(newPassPrompt, layout);

        newPassword = new JTextField();
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.ipadx = 200;
        layout.gridwidth = 2;
        layout.gridx = 1;
        layout.gridy = 8;
        manageSellerAccountPanel.add(newPassword, layout);

        // creates the button to click when all information is entered
        updatePassButton = new JButton("Update Password");
        layout.fill = GridBagConstraints.BOTH;
        layout.gridwidth = 4;
        layout.gridx = 0;
        layout.gridy = 9;
        layout.insets = new Insets(5, 10, 10, 0);
        updatePassButton.addActionListener(actionListener);
        manageSellerAccountPanel.add(updatePassButton, layout);

        // create the delete seller button
        deleteSellerButton = new JButton("Delete Seller");
        layout.fill = GridBagConstraints.BOTH;
        layout.gridwidth = 4;
        layout.gridx = 0;
        layout.gridy = 10;
        layout.insets = new Insets(5, 10, 10, 0);
        deleteSellerButton.addActionListener(actionListener);
        manageSellerAccountPanel.add(deleteSellerButton, layout);

        return manageSellerAccountPanel;
    }


    public JPanel editStoresPanel() {
        editStoresPanel = new JPanel();
        editStoresPanel.setLayout(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();

        layout.fill = GridBagConstraints.BOTH;
        layout.gridwidth = frame.getWidth();
        layout.gridheight = 3;
        layout.gridx = 0;
        layout.gridy = 0;
        editStoresPanel.add(sellerMenuBar, layout);

        //sets margins from now on
        layout.insets = new Insets(0, 10, 0, 10);
        layout.weightx = 1.0;

        JLabel title = new JLabel("Edit Stores");
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.gridx = 0;
        layout.gridy = 4;
        title.setFont(new Font("Arial", Font.BOLD, 20));
        editStoresPanel.add(title, layout);

        backToAllStoresButton = new JButton("Back");
        backToAllStoresButton.addActionListener(actionListener);
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.gridx = 0;
        layout.gridy = 5;
        editStoresPanel.add(backToAllStoresButton, layout);

        layout.gridwidth = 1;
        layout.gridx = 0;
        layout.gridy = 6;
        editStoresPanel.add(storeInformationPanel(), layout);

        return editStoresPanel;
    }


    //menu bar for seller
    public JMenuBar createSellerMenuBar() {
        //make menu bar to add menu to
        sellerMenuBar = new JMenuBar();

        //make menu to add options to
        sellerMenu = new JMenu("Menu");
        sellerMenu.setFont(new Font("Arial", Font.BOLD, 15));

        //make menu options/items to add to menu
        viewStores = new JMenuItem("View Stores");
        viewStores.addActionListener(actionListener);
        sellerStats = new JMenuItem("View Statistics");
        sellerStats.addActionListener(actionListener);
        toCSV = new JMenuItem("Export to CSV file");
        toCSV.addActionListener(actionListener);
        manageSellerAccount = new JMenuItem("Manage Account");
        manageSellerAccount.addActionListener(actionListener);
        sellerLogout = new JMenuItem("Logout");
        sellerLogout.addActionListener(actionListener);

        //adds all items/ submenus to the menu
        sellerMenu.add(viewStores);
        sellerMenu.add(sellerStats);
        sellerMenu.add(toCSV);
        sellerMenu.add(manageSellerAccount);
        sellerMenu.add(sellerLogout);

        //adds the menu to the menu bar
        sellerMenuBar.add(sellerMenu);

        //returns the menu bar
        return sellerMenuBar;
    }

    //menu bar for customer
    public JMenuBar createCustomerMenuBar() {
        //make menu bar for customer
        customerMenuBar = new JMenuBar();

        //make menu for customer
        customerMenu = new JMenu("Menu");
        customerMenu.setFont(new Font("Arial", Font.BOLD, 15));

        allListings = new JMenuItem("View Market");
        allListings.addActionListener(actionListener);
        viewCart = new JMenuItem("View Cart");
        viewCart.addActionListener(actionListener);
        customerStats = new JMenuItem("Customer Stats");
        customerStats.addActionListener(actionListener);
        viewPurchaseHistory = new JMenuItem("View Purchase History");
        viewPurchaseHistory.addActionListener(actionListener);
        manageCustomerAccount = new JMenuItem("Manage Account");
        manageCustomerAccount.addActionListener(actionListener);
        customerLogout = new JMenuItem("Logout");
        customerLogout.addActionListener(actionListener);

        customerMenu.add(allListings);
        customerMenu.add(viewCart);
        customerMenu.add(customerStats);
        customerMenu.add(viewPurchaseHistory);
        customerMenu.add(manageCustomerAccount);
        customerMenu.add(customerLogout);

        customerMenuBar.add(customerMenu);
        return customerMenuBar;
    }

    //run
    //this creates the frame that has all panels added and removed
    //it also makes it close nicely and
    public void run() {
        //creates the frame and sets the layout
        frame = new JFrame("Market");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());

        //sets the size of the frame
        frame.setSize(800, 600);

        //sets the location of the frame
        //so when the program starts it opens in the middle of the screen
        frame.setLocationRelativeTo(null);

        //makes it so, you can press the 'x' on the frame and it closes nicely

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Perform actions before closing
                client.exit();

                // Dispose the frame
                ((Window) e.getSource()).dispose();
            }
        });

        //makes the frame visible
        frame.setVisible(true);

        content.add(this, BorderLayout.CENTER);
        frame.setLayout(new BorderLayout(4, 4));
        frame.add(welcomeScreen(), BorderLayout.CENTER);
    }

    //main
    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new MarketGui());
    }
}
