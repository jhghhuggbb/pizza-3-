import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// Pizza shop class
class PizzaShop {
    // Shop name
    private String shopName;
    // Address
    private String address;
    // Email
    private String email;
    // Phone number
    private String phone;
    // Menu, key is pizza name, value is price
    private Map<String, Double> menu;
    // Toppings list
    private List<String> toppings;
    // Side dishes list
    private List<String> sideDishes;
    // Beverages list
    private List<String> beverages;
    // Order ID counter
    private int orderIdCounter;

    public PizzaShop(String shopName, String address, String email, String phone) {
        this.shopName = shopName;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.menu = new HashMap<>();
        this.toppings = new ArrayList<>();
        this.sideDishes = new ArrayList<>();
        this.beverages = new ArrayList<>();
        this.orderIdCounter = 1;
    }

    // Add pizza to the menu
    public void addPizzaToMenu(String pizzaName, double price) {
        menu.put(pizzaName, price);
    }

    // Add topping
    public void addTopping(String topping) {
        toppings.add(topping);
    }

    // Add side dish
    public void addSideDish(String sideDish) {
        sideDishes.add(sideDish);
    }

    // Add beverage
    public void addBeverage(String beverage) {
        beverages.add(beverage);
    }

    // Take an order
    public Order takeOrder() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter three ingredients for your pizza (use spaces to separate ingredients):");
        String[] ingredients = scanner.nextLine().split(" ");
        String ing1 = ingredients[0];
        String ing2 = ingredients[1];
        String ing3 = ingredients[2];

        System.out.println("Enter size of pizza (Small, Medium, Large):");
        String pizzaSize = scanner.nextLine();

        System.out.println("Do you want extra cheese (Y/N):");
        String extraCheese = scanner.nextLine();

        System.out.println("Enter one side dish (Calzone, Garlic bread, None):");
        String sideDish = scanner.nextLine();

        System.out.println("Enter drinks(Cold Coffe, Cocoa drink, Coke, None):");
        String drinks = scanner.nextLine();

        System.out.println("Would you like the chance to pay only half for your order? (Y/N):");
        String wantDiscount = scanner.nextLine();

        if (wantDiscount.equalsIgnoreCase("Y")){
            isItYourBirthday();
        }else{
            makeCardPayment();
        }

        double totalPrice = 10.0;
        List<String> pizzas = new ArrayList<>();
        pizzas.add(ing1 + " " + " " + ing3 + " Pizza");
        List<String> sides = new ArrayList<>();
        sides.add(sideDish);
        List<String> bev = new ArrayList<>();
        bev.add(drinks);

        String orderId = "SOH-" + orderIdCounter++;
        return new Order(orderId, pizzas, sides, bev, totalPrice);
    }

    public void isItYourBirthday(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your birthday (in year-month-day format):");
        String birthdateStr = scanner.nextLine();
        Date birthdate = new Date();
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("year-month-day");
            birthdate = sdf.parse(birthdateStr); 
        } catch (ParseException e){
            System.out.println("Invalid date format.");
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(birthdate);
        int birthYear = cal.get(Calendar.YEAR);
        cal.setTime(new Date());
        int currentYear = cal.get(Calendar.YEAR);
        int age = currentYear - birthYear;

        cal.setTime(birthdate);
        int birthMonth = cal.get(Calendar.MONTH);
        int birthDay = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(new Date());
        int currentMonth = cal.get(Calendar.MONTH);
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);

        if(age < 18 && birthMonth == currentMonth && birthDay == currentDay){
            System.out.println("Congratulations! You pay only half the price for your order");      
        } else {
            System.out.println("Too bad! You do not meet the condtions to get out 50% discount");
        }
    }

    public void makeCardPayment(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your card number:");
        long cardNumber = scanner.nextLong();
        scanner.nextLine();

        System.out.println("Enter the card's expiry date (in month/year format):");
        String expiryDate = scanner.nextLine();

        System.out.println("Enter the card's cvv number:");
        int cvv = scanner.nextInt();

        processCardPayment(cardNumber, expiryDate, cvv);
    }

    public void processCardPayment(long cardNumber, String expiryDate, int cvv){
        String cardNumberStr = Long.toString(cardNumber);
        int cardLength = cardNumberStr.length();
        if (cardLength == 14){
            System.out.println("Card accepted");
        } else {
            System.out.println("Invalid card");
        }

        int firstCardDigit = Integer.parseInt(cardNumberStr.substring(0,1));

        long blacklistedNumber = 12345678901234L;
        if (cardNumber == blacklistedNumber){
            System.out.println("Card is blacklisted. Please use another card");
        }

        int lastFourDigits = Integer.parseInt(cardNumberStr.substring(cardLength - 4));

        String cardNumberToDisplay = cardNumberStr.substring(0, 1) + cardNumberStr.substring(1, cardLength - 4).replaceAll(".", "*") + cardNumberStr.substring(cardLength - 4);
    }

    public void specialOfTheDay(String pizzaOfTheDay, String sideOfTheDay, String specialPrice){
        System.out.println("Today's special:");
        System.out.println("Pizza: " + pizzaOfTheDay);
        System.out.println("Side dish:" + sideOfTheDay);
        System.out.println("Special price: " + specialPrice);
    }

    public void showReceipt(Order order){
        order.printReceipt(shopName, menu);
    }
}    
// Order class
class Order {
    private String orderID;
    private List<String> pizzas;
    private List<String> sideDishes;
    private List<String> beverages;
    private double orderTotal;
    private String pizzaIngredients;
    private static final String DEF_ORDER_ID = "DEF-SOH-099";
    private static final String DEF_PIZZA_INGREDIENTS = "Mozzarella Cheese";
    private static final double DEF_ORDER_TOTAL = 15.00;

    public Order() {
        this.orderID = DEF_ORDER_ID;
        this.pizzaIngredients = DEF_PIZZA_INGREDIENTS;
        this.orderTotal = DEF_ORDER_TOTAL;
        this.sideDishes = new ArrayList<>();
        this.beverages = new ArrayList<>();
        this.pizzas = new ArrayList<>();
    }
    public Order(String orderID, List<String> pizzas, List<String> sideDishes, List<String> beverages, double orderTotal){
        this.orderID = orderID;
        this.pizzas = pizzas;
        this.sideDishes = sideDishes;
        this.beverages = beverages;
        this.orderTotal = orderTotal;
        this.pizzaIngredients = DEF_PIZZA_INGREDIENTS;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public List<String> getPizzas() {
        return pizzas;
    }

    public void setPizzas(List<String> pizzas) {
        this.pizzas = pizzas;
    }

    public List<String> getSideDishes() {
        return sideDishes;
    }

    public void setSideDishes(List<String> sideDishes){
        this.sideDishes = sideDishes;
    }

    public List<String> getBeverages(){
        return beverages;
    }

    public void setBeverages(List<String> beverages){
        this.beverages = beverages;
    }

    public double getOrderTotal(){
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal){
        this.orderTotal = orderTotal;
    }

    public String getPizzaIngredients(){
        return pizzaIngredients;
    }

    public void setPizzaIngredients(String pizzaIngredients){
        this.pizzaIngredients = pizzaIngredients;
    }

    private void printReceipt(String shopName, Map<String, Double> menu){
        System.out.println("--- " + shopName + "Receipt ---");
        System.out.println("Order Id: " + orderID);
        System.out.println("Pizzas: ");
        for (String pizza : pizzas){
            System.out.println("- " + pizza + ": $" + menu.get(pizza));            
        }
        System.out.println("Side Dishes");
        for (String side : sideDishes){
            System.out.println("- " + side + ": $5");
        }
        System.out.println("Beverages: ");
        for (String drink : beverages){
            System.out.println("- " + drink + ": $3");
        }
        System.out.println("Total Price: $" + orderTotal); 
    }    
}

// Main class for testing
public class Main {
    public static void main(String[] args) {
        // Create a pizza shop object
        PizzaShop pizzaShop = new PizzaShop("Slice-o-Heaven", "123 Pizza St", "info@sliceoheaven.com", "555 - 1234");

        // Add pizzas to the menu
        pizzaShop.addPizzaToMenu("Cheese Pizza", 10.0);
        pizzaShop.addPizzaToMenu("Ham Pizza", 12.0);

        // Add toppings
        pizzaShop.addTopping("Mushrooms");
        pizzaShop.addTopping("Green Peppers");

        // Add side dishes
        pizzaShop.addSideDish("French Fries");
        pizzaShop.addSideDish("Chicken Wings");

        // Add beverages
        pizzaShop.addBeverage("Coke");
        pizzaShop.addBeverage("Sprite");

        Order order = pizzaShop.takeOrder();
        pizzaShop.makePizza(order.getPizzas());
        pizzaShop.showReceipt(order);
        pizzaShop.specialOfTheDay("Pepperoni Pizza", "Onion Rings", "$18");
    }
}