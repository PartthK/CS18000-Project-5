import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Project 05 -- Client.java
 * <p>
 * Manages GUI and server interactions
 * Takes information from the GUI and gives it to the server
 * Then takes the server response and gives it back to the GUI in the required form
 *
 * @author Group 2 - L32
 * @version December 9, 2023
 */

public class Client {
    //fields
    private String hostname;
    private int port;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    //Constructor
    public Client(String hostname, int port) throws IOException {
        this.hostname = hostname;
        this.port = port;
        socket = new Socket(hostname, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream());
    }

    // client code to send request and cast object and/or handle exceptions
    public String login(String email, String password) throws IOException {
        String[] apirequest = {"login", email, password};
        writer.write(apirequest[0] + "," + apirequest[1] + "," + apirequest[2]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        return response;

    }

    public String createAccount(String email, String password, String role) throws IOException {
        String[] apirequest = {"createAccount", email, password, role};
        writer.write(apirequest[0] + "," + apirequest[1] + "," + apirequest[2] + "," + apirequest[3]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        return response;

    }

    public String deleteAccount(String email, String password, String role) throws IOException {
        String[] apirequest = {"deleteAccount", email, password, role};
        writer.write(apirequest[0] + "," + apirequest[1] + "," + apirequest[2] + "," + apirequest[3]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        return response;

    }

    public String changeAccountPassword(String email, String newPassword) throws IOException {
        String[] apirequest = {"changeAccountPassword", email};
        writer.write(apirequest[0] + "," + apirequest[1]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        return response;

    }

    public ArrayList<Item> getItemList(String sortOrder, String searchTerm) throws IOException {
        String[] apirequest = {"getItemList", sortOrder, searchTerm};
        writer.write(apirequest[0] + "," + apirequest[1] + "," + apirequest[2]);
        writer.println();
        writer.flush();

        String response = reader.readLine();

        String[] responseSplit = response.split(",");
        ArrayList<Item> itemsInStore = new ArrayList<>();

        for (int i = 0; i < responseSplit.length; i++) {
            String[] itemDetailSplit = responseSplit[i].split(";");
            Item item = new Item(itemDetailSplit[0], itemDetailSplit[1], itemDetailSplit[2],
                    Integer.parseInt(itemDetailSplit[3]), Double.parseDouble(itemDetailSplit[4]));
            itemsInStore.add(item);
        }

        return itemsInStore;

    }


    public String[] getItemDetails(String sellerEmail, String itemName, String storeName) throws IOException {
        String[] apirequest = {"getItemDetails", itemName, sellerEmail, storeName};
        writer.write(apirequest[0] + "," + apirequest[2] + "," + apirequest[1] + "," + apirequest[3]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        String[] itemDetails = response.split(",");
        return itemDetails;
    }

    public String getShoppingCart(String emailID) throws IOException {
        String[] apirequest = {"getShoppingCart", emailID};
        writer.write(apirequest[0] + "," + apirequest[1]);
        writer.println();
        writer.flush();


        String response = reader.readLine();

        return response;

    }

    public String addProductToCart(String emailID, String storeName, String itemName, String itemDescription,
                                   String itemQuantAvail, String itemPrice, String quantityBought) throws IOException {
        String[] apirequest = {"addProductToCart", emailID, storeName, itemName, itemDescription
                , itemQuantAvail, itemPrice, quantityBought};
        writer.write(apirequest[0] + "," + apirequest[1] + "," + apirequest[2] + "," + apirequest[3]
                + "," + apirequest[4] + "," + apirequest[5] + "," + apirequest[6] + "," + apirequest[7]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        return response;
    }

    public String purchaseCart(String emailID) throws IOException {
        String[] apirequest = {"purchaseCart", emailID};
        writer.write(apirequest[0] + "," + apirequest[1]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        return response;

    }

    public String purchaseNow(String emailID, String storeName, String itemName, String itemDescription,
                              String itemQuant, String itemPrice, String quantToBuy) throws IOException {
        String[] apirequest = {"purchaseNow", emailID, storeName, itemName
                , itemDescription, itemQuant, itemPrice, quantToBuy};

        writer.write(apirequest[0] + "," + apirequest[1] + "," + apirequest[2]
                + "," + apirequest[3] + "," + apirequest[4] + "," + apirequest[5] + "," + apirequest[6]
                + "," + apirequest[7]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        return response;
    }

    public String getEmailFromStoreName(String storeName) throws IOException {
        String[] apirequest = {"getEmailFromStoreName", storeName};
        writer.write(apirequest[0] + "," + apirequest[1]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        return response;
    }

    public String deleteCart(String emailID) throws IOException {
        String[] apirequest = {"deleteCart", emailID};
        writer.write(apirequest[0] + "," + apirequest[1]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        return response;

    }


    public String exportProductsToCsv(String emailID) throws IOException {
        String[] apirequest = {"exportProductsToCsv", emailID};
        writer.write(apirequest[0] + "," + apirequest[1]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        return response;
    }

    public ArrayList<Item> getPurchaseHistory(String emailID) throws IOException {
        String[] apirequest = {"getPurchaseHistory", emailID};
        writer.write(apirequest[0] + "," + apirequest[1]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        String[] splitResponse = response.split(";");
        ArrayList<Item> itemArrayList = new ArrayList<>();
        for (int i = 0; i < splitResponse.length; i += 5) {
            Item newItem = new Item(splitResponse[i],
                    splitResponse[i + 1],
                    splitResponse[i + 2],
                    Integer.parseInt(splitResponse[i + 3]),
                    Double.parseDouble(splitResponse[i + 4]));
            itemArrayList.add(newItem);
        }

        return itemArrayList;

    }

    public String exportPurchaseHistory(String emailID) throws IOException {
        String[] apirequest = {"exportPurchaseHistory", emailID};
        writer.write(apirequest[0] + "," + apirequest[1]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        return response;
    }

    //added get stores rn
    public ArrayList<Store> getStores(String emailID) throws IOException {
        String[] apirequest = {"getStores", emailID};
        writer.write(apirequest[0] + "," + apirequest[1]);
        writer.println();
        writer.flush();

        String response = reader.readLine();

        String[] responseSplit = response.split("-");
        ArrayList<Store> storeNames = new ArrayList<>();

        for (int i = 1; i < responseSplit.length; i++) {
            ArrayList<Item> itemsInStore = new ArrayList<>(); // Create a new itemsInStore for each store
            String[] responseSplitAgain = responseSplit[i].split(";");

            for (int j = 0; j < responseSplitAgain.length; j++) {
                String[] itemDetailSplit = responseSplitAgain[j].split(",");
                Item item = new Item(itemDetailSplit[0], itemDetailSplit[1], itemDetailSplit[2],
                        Integer.parseInt(itemDetailSplit[3]), Double.parseDouble(itemDetailSplit[4]));
                itemsInStore.add(item);
            }

            Store store = new Store(itemsInStore);
            storeNames.add(store);
        }
        return storeNames;
    }

    public String createStore(String emailID, String storeName, ArrayList<Item> itemsForNewStore) throws IOException {
        String[] apirequest = {"createStore", emailID, storeName};
        String s = "";
        for (Item item : itemsForNewStore) {
            s += item.getStoreName() + ";" + item.getItemName() + ";" + item.getItemDescription() +
                    ";" + item.getAvailable() + ";" + item.getPrice() + "-";
        }
        writer.write(apirequest[0] + "," + apirequest[1] + "," + apirequest[2] + "," + s);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        return response;
    }

    public String deleteStore(String emailID, String storeName) throws IOException {
        String[] apirequest = {"deleteStore", emailID, storeName};
        writer.write(apirequest[0] + "," + apirequest[1] + "," + apirequest[2]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        return response;
    }

    //seller stats menu
    //dataByProduct (in Market GUI)

    public String salesOfSpecificProduct(String emailID) throws IOException {
        String[] apirequest = {"salesOfSpecificProduct", emailID};
        writer.write(apirequest[0] + "," + apirequest[1]);
        writer.println();
        writer.flush();
        String response = reader.readLine();
        return response;
    }

    //dataByCustomer (in Market GUI)
    public String salesOfSpecificCustomer(String emailID) throws IOException {
        String[] apirequest = {"salesOfSpecificCustomer", emailID};
        writer.write(apirequest[0] + "," + apirequest[1]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        return response;
    }

    //shoppingCartData (in Market GUI)
    public String getCustomerShoppingcartData(String emailID) throws IOException {
        String[] apirequest = {"getCustomerShoppingcartData", emailID};
        writer.write(apirequest[0] + "," + apirequest[1]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        return response;
    }

    //salesByStore (in MarketGUI)
    public String salesByStore(String emailID) throws IOException {
        String[] apirequest = {"viewListOfSalesByStore", emailID};
        writer.write(apirequest[0] + "," + apirequest[1]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        return response;
    }


    //totalRevenue (In MarketGUI)
    public String totalRevenue(String emailID) throws IOException {
        String[] apirequest = {"totalRevenue", emailID};
        writer.write(apirequest[0] + "," + apirequest[1]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        return response;
    }

    //sellerHistory (In MarketGUI)
    public String sellerHistory(String emailID) throws IOException {
        String[] apirequest = {"sellerHistory", emailID};
        writer.write(apirequest[0] + "," + apirequest[1]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        return response;
    }

    //customer stats added ppp
    public String dataByProductsSold(String emailID) throws IOException {
        String[] apirequest = {"dataByProductsSold", emailID};
        writer.write(apirequest[0] + "," + apirequest[1]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        return response;
    }

    public String dataByPurchaseHistory(String emailID) throws IOException {
        String[] apirequest = {"dataByPurchaseHistory", emailID};
        writer.write(apirequest[0] + "," + apirequest[1]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        return response;
    }

    public String editItemInStore(String emailID, String storeName, String itemName, String itemDescription,
                                  String itemQuantAvail, String itemPrice, String updated) throws IOException {
        String[] apirequest = {"editItemInStore", emailID, storeName, itemName, itemDescription
                , itemQuantAvail, itemPrice, updated};
        writer.write(apirequest[0] + "," + apirequest[1] + "," + apirequest[2] + "," + apirequest[3]
                + "," + apirequest[4] + "," + apirequest[5] + "," + apirequest[6] + "," + apirequest[7]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        return response;
    }

    public String deleteItemInStore(String emailID, String storeName, String itemName) throws IOException {
        String[] apirequest = {"deleteItemInStore", emailID, storeName, itemName};
        writer.write(apirequest[0] + "," + apirequest[1] + "," + apirequest[2] + "," + apirequest[3]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        return response;
    }

    public String newItemInStore(String emailID, String storeName, String itemName,
                                 String itemDescription, String itemQuant, String itemPrice) throws IOException {
        String[] apirequest = {"newItemInStore", emailID, storeName, itemName
                , itemDescription, itemQuant, itemPrice};
        writer.write(apirequest[0] + "," + apirequest[1] + "," + apirequest[2] + "," + apirequest[3]
                + "," + apirequest[4] + "," + apirequest[5] + "," + apirequest[6]);
        writer.println();
        writer.flush();

        String response = reader.readLine();
        return response;
    }

    public void exit() {
        writer.write("Exit,");
        writer.println();
        writer.flush();
    }

}
