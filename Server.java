import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * Project 05 -- Server
 * <p>
 * Reads input from client
 * process the information and do the correct method
 * writes back to the client *
 *
 * @author Group 2- L32
 * @version December 9, 2023
 */

class Server implements Runnable {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public Server(Socket socket) {
        this.clientSocket = socket;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        request();
    }

    public synchronized void request() {
        try {
            String inputLine;

            // Read input from the client
            while ((inputLine = in.readLine()) != null) {

                String[] methodCaller = inputLine.split(",", -1);


                if (methodCaller[0].equals("login")) {
                    String found = "false";
                    found = Account.login(methodCaller[1], methodCaller[2]);
                    out.write(found);
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("createAccount")) {
                    boolean found = false;
                    Account account = new Account(methodCaller[1], methodCaller[2], methodCaller[3]);
                    found = account.createAccount();
                    if (found) {
                        out.write("Success");
                    } else out.write("Failure,Email is already associated with an account");
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("deleteAccount")) {
                    boolean found = false;
                    Account account = new Account(methodCaller[1], methodCaller[2], methodCaller[3]);
                    found = account.removeAccount();
                    if (found) {
                        out.write("Success");
                    } else out.write("deleteAccountException");
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("editAccount")) {
                    boolean found = false;
                    Account account = new Account(methodCaller[1], methodCaller[2], methodCaller[3]);
                    found = account.editAccount(methodCaller[1], methodCaller[2], methodCaller[3]);
                    if (found) {
                        out.write("Success");
                    } else out.write("changeAccountException");
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("getItemList")) {
                    ArrayList<Item> allProducts = Seller.getAllProducts(methodCaller[1], methodCaller[2]);
                    String s = "";
                    for (int i = 0; i < allProducts.size(); i++) {
                        if (i != allProducts.size() - 1) s += allProducts.get(i).semiColonfileToString() + ",";
                        else s += allProducts.get(i).semiColonfileToString();
                    }
                    out.write(s);
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("getItemDetails")) {
                    String itemDetails = "Failure, no items match the name";
                    Seller seller = new Seller(methodCaller[1]);
                    ArrayList<Store> stores = seller.readStores("allStores.txt",
                            methodCaller[1]);

                    for (int i = 0; i < stores.size(); i++) {
                        ArrayList<Item> itemsInStore = stores.get(i).getItemList();
                        for (int j = 0; j < itemsInStore.size(); j++) {
                            if (itemsInStore.get(j).getItemName().equals(methodCaller[2])) {
                                itemDetails = itemsInStore.get(j).getItemDescription() + ","
                                        + itemsInStore.get(j).getAvailable()
                                        + "," + itemsInStore.get(j).getPrice();
                                break; // Exit the loop once item is found
                            }
                        }
                    }
                    out.write(itemDetails);
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("addProductToCart")) {
                    int quantityToAdd = Integer.parseInt(methodCaller[7]);
                    int quantityInCart = 0;
                    int quantityInStore = Integer.parseInt(methodCaller[5]);
                    String storeNameCheck = methodCaller[2];
                    String itemNameCheck = methodCaller[3];
                    File shoppingCartData = new File("shoppingcartdata.txt");
                    ShoppingCart shoppingCart = ShoppingCart.readShoppingCart(shoppingCartData, methodCaller[1]);
                    Item item = new Item(storeNameCheck, itemNameCheck, methodCaller[4],
                            Integer.parseInt(methodCaller[5]), Double.parseDouble(methodCaller[6]));
                    for (int i = 0; i < shoppingCart.getItemsInCart().size(); i++) {
                        if ((shoppingCart.getItemsInCart().get(i).getStoreName().equals(storeNameCheck)) &&
                                (shoppingCart.getItemsInCart().get(i).getItemName().equals(itemNameCheck))) {
                            quantityInCart++;
                        }
                    }
                    if ((quantityToAdd + quantityInCart) <= quantityInStore) {
                        for (int i = 0; i < quantityToAdd; i++) {
                            shoppingCart.addToCart(item);
                        }
                        ShoppingCart.saveShoppingCart(shoppingCart, methodCaller[1]);
                        out.write("Success");
                        out.println();
                        out.flush();
                    } else {
                        out.write("Failure");
                        out.println();
                        out.flush();
                    }
                } else if (methodCaller[0].equals("getShoppingCart")) {
                    File shoppingCartData = new File("shoppingcartdata.txt");
                    ShoppingCart shoppingCart = ShoppingCart.readShoppingCart(shoppingCartData, methodCaller[1]);
                    String s = shoppingCart.displayToString();
                    out.write(s);
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("purchaseCart")) { //Changed customer methods - Update customer
                    String s = "";
                    File shoppingCartData = new File("shoppingcartdata.txt");
                    Customer customer = new Customer(methodCaller[1]);
                    ShoppingCart shoppingCart = ShoppingCart.readShoppingCart(shoppingCartData, methodCaller[1]);

                    if (shoppingCart.getItemsInCart().isEmpty()) {
                        out.write("No Items in Cart");
                    } else {
                        Map<String, Integer> quantityBought = new HashMap<String, Integer>();
                        for (int i = 0; i < shoppingCart.getItemsInCart().size(); i++) {
                            Item item = shoppingCart.getItemsInCart().get(i);
                            String itemString = item.getStoreName() + "," + item.getItemName() + ","
                                    + item.getItemDescription() + "," + item.getAvailable() + "," + item.getPrice();
                            if (quantityBought.containsKey(itemString)) {
                                quantityBought.put(itemString, quantityBought.get(itemString) + 1);
                            } else quantityBought.put(itemString, 1);
                        }

                        for (Map.Entry<String, Integer> entry : quantityBought.entrySet()) {
                            String itemString = entry.getKey();

                            String[] itemDeets = itemString.split(",", -1);
                            Item item = new Item(itemDeets[0], itemDeets[1], itemDeets[2],
                                    Integer.parseInt(itemDeets[3]), Double.parseDouble(itemDeets[4]));

                            // Assuming the customer object is available
                            customer.purchaseItemForPartth(item.getStoreName(), item,
                                    methodCaller[1], entry.getValue());
                        }
                        

                        //deletes the shopping cart data once the cart is purchased
                        ShoppingCart.deleteShoppingCartData(methodCaller[1]);

                        out.write("Success");
                    }
                    //out.write("Success");
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("purchaseNow")) {
                    for (int i = 0; i <= Integer.parseInt(methodCaller[7]); i++) {
                        Item item = new Item(methodCaller[2], methodCaller[3], methodCaller[4],
                                Integer.parseInt(methodCaller[5]), Double.parseDouble(methodCaller[6]));
                        Customer customer = new Customer(methodCaller[1]);
                        customer.purchaseItemForPartth(methodCaller[2], item, customer.getEmail(),
                                Integer.parseInt(methodCaller[7]));
                    }

                    out.write("Success");
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("getEmailFromStoreName")) {
                    out.write(Seller.getEmailFromStoreName(methodCaller[1]));
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("deleteCart")) {
                    String s = "";
                    ShoppingCart.deleteShoppingCartData(methodCaller[1]);

                    out.write("Success");
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("getPurchaseHistory")) {
                    String s = Customer.getPurchaseHistoryStream(methodCaller[1]);
                    if (s.isEmpty()) s = "Nothing Purchased";
                    out.write(s);
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("exportPurchaseHistory")) {
                    Customer.getPurchaseHistory(methodCaller[1]);
                    out.write("Success");
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("getStores")) {
                    String s = Seller.readStoresForPartth(methodCaller[1]);
                    out.write(s);
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("editItemInStore")) { //Edited seller push seller
                    Seller seller = new Seller(methodCaller[1]);
                    Item item;
                    String[] whatToChange = methodCaller[7].split(":");
                    //switch statement that edits product based off of what is being changed
                    switch (whatToChange[0]) {
                        case "itemName":
                            item = new Item(methodCaller[2], whatToChange[1], methodCaller[4],
                                    Integer.parseInt(methodCaller[5]), Double.parseDouble(methodCaller[6]));
                            seller.editProductInTheStore(methodCaller[2], methodCaller[3], item);
                            break;
                        case "itemDescription":
                            item = new Item(methodCaller[2], methodCaller[3], whatToChange[1],
                                    Integer.parseInt(methodCaller[5]), Double.parseDouble(methodCaller[6]));
                            seller.editProductInTheStore(methodCaller[2], methodCaller[3], item);
                            break;
                        case "itemQuantAvail":
                            item = new Item(methodCaller[2], methodCaller[3], methodCaller[4],
                                    Integer.parseInt(whatToChange[1]), Double.parseDouble(methodCaller[6]));
                            seller.editProductInTheStore(methodCaller[2], methodCaller[3], item);
                            break;
                        case "itemPrice":
                            item = new Item(methodCaller[2], methodCaller[3], methodCaller[4],
                                    Integer.parseInt(methodCaller[5]), Double.parseDouble(whatToChange[1]));
                            seller.editProductInTheStore(methodCaller[2], methodCaller[3], item);
                            break;
                    }

                    out.write("Success");
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("deleteItemInStore")) {
                    Seller seller = new Seller(methodCaller[1]);
                    seller.deleteProductInTheStore(methodCaller[2], methodCaller[3]);
                    out.write("Success");
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("newItemInStore")) {
                    Seller seller = new Seller(methodCaller[1]);
                    Item item = new Item(methodCaller[2], methodCaller[3], methodCaller[4],
                            Integer.parseInt(methodCaller[5]), Double.parseDouble(methodCaller[6]));
                    String found = String.valueOf(seller.addProductInTheStore(methodCaller[2], item));
                    out.write(found);
                    out.println();
                    out.flush();

                } else if (methodCaller[0].equals("createStore")) {
                    Seller seller = new Seller(methodCaller[1]);
                    ArrayList<Item> storeItems = new ArrayList<>();
                    for (String items : methodCaller[3].split("-")) {
                        String[] itemStuff = items.split(";");
                        Item i = new Item(itemStuff[0], itemStuff[1], itemStuff[2],
                                Integer.parseInt(itemStuff[3]), Double.parseDouble(itemStuff[4]));
                        storeItems.add(i);
                    }
                    Store store = new Store(storeItems);
                    out.write(seller.createStore(store));
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("deleteStore")) {
                    Seller seller = new Seller(methodCaller[1]);
                    seller.deleteStore(methodCaller[2]);
                    out.write("Success");
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("exportProductsToCsv")) {
                    Seller seller = new Seller(methodCaller[1]);
                    seller.exportItem();
                    out.write("Success");
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("storeSalesData")) { //Edited salesOfSpecificCustomer in Seller
                    Seller seller = new Seller(methodCaller[1]);
                    String s = "";
                    if (methodCaller[2].equals("customer")) {
                        s = seller.salesOfSpecificCustomer();
                    } else s = seller.salesOfSpecificProduct();
                    out.write(s);
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("salesOfSpecificProduct")) {
                    //seller stats menu ppp
                    //dataByProduct (in Market GUI)
                    Seller seller = new Seller(methodCaller[1]);
                    String s = seller.salesOfSpecificProduct();

                    out.write(s);
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("salesOfSpecificCustomer")) { //dataByCustomer (in Market GUI)
                    Seller seller = new Seller(methodCaller[1]);
                    String s = seller.salesOfSpecificCustomer();
                    out.write(s);
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("getCustomerShoppingcartData")) { //shoppingCartData(in MarketGUI)
                    Seller seller = new Seller(methodCaller[1]);
                    String s = seller.getCustomerShoppingcartData();
                    out.write(s);
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("Exit")) {
                    break;
                } else if (methodCaller[0].equals("viewListOfSalesByStore")) {
                    //salesByStore (in MarketGUI)
                    //Edited viewListOfSalesByStore method in Seller.java
                    Seller seller = new Seller(methodCaller[1]);
                    String s = seller.viewListOfSalesByStore();
                    out.write(s);
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("totalRevenue")) {
                    //totalRevenue (in MarketGUI)
                    //Edited viewTotalRevenue method in Seller.java
                    Seller seller = new Seller(methodCaller[1]);
                    String s = seller.viewTotalRevenue();
                    out.write(s);
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("sellerHistory")) {
                    //sellerHistory (in MarketGUI)
                    //Edited sellerCustomerHistory method in Seller.java
                    Seller seller = new Seller(methodCaller[1]);
                    String s = seller.sellerCustomerHistory();
                    out.write(s);
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("salesOfSpecificProduct")) {
                    Seller seller = new Seller(methodCaller[1]);
                    String s = seller.salesOfSpecificProduct();

                    out.write(s);
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("dataByProductsSold")) { //customer stats menu
                    Customer customer = new Customer(methodCaller[1]);
                    String s = customer.seeAllStoresAndSales();
                    out.write(s);
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("dataByPurchaseHistory")) { //edited --> changed method to String??
                    Customer customer = new Customer(methodCaller[1]);
                    String s = customer.howManyProductsFromEachStore();
                    out.write(s);
                    out.println();
                    out.flush();
                } else if (methodCaller[0].equals("Exit")) {
                    break;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
