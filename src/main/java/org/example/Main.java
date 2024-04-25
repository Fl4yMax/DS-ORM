package org.example;

import applicationClasses.*;
import dataMapper.*;

import java.util.List;
import java.util.Random;


public class Main {

    public static final String RESET = "\033[0m";
    public static final String BLACK = "\033[0;30m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";
    public static final String WHITE = "\033[0;37m";

    public static void main(String[] args) {
        //
        PersonMapper personM = PersonMapper.getInstance();
        AccountMapper accountM = AccountMapper.getInstance();
        AddressMapper addressM = AddressMapper.getInstance();
        CompanyMapper companyM = CompanyMapper.getInstance();
        OrderMapper orderM = OrderMapper.getInstance();
        OrderProductMapper orderProductM = OrderProductMapper.getInstance();
        ProductMapper productM = ProductMapper.getInstance();
        WorkMapper workM = WorkMapper.getInstance();

        /*Work w1 = workM.find(1);
        Work w2 = workM.find(2);
        Work w3 = workM.find(3);
        Work w4 = workM.find(4);

        w1.setMade(150);
        w2.setMade(10);
        w3.setMade(17);
        w4.setMade(8);

        workM.update(w1);
        workM.update(w2);
        workM.update(w3);
        workM.update(w4);*/

        testCompanyMapper(companyM);
        testOrderProductMapper(orderProductM, orderM, productM);
        testOrderMapper(orderM);
        testProductMapper(productM);
        testAccountMapper(accountM);
        testPersonMapper(personM);

        resetColor();
    }

    private static void testCompanyMapper(CompanyMapper companyM){
        //C001 - Show company info
        Company company = companyM.find(1);
        if(company != null) {
            System.out.println("COMPANY INFO... - NAME: " + company.getName() + " CIN: " + company.getCin() + " TIN: " + company.getTin());
        }

        //C002 - Create
        /*company = new Company("BigWilly's", "3533", "34242522");
        companyM.create(company);
*/
        //C003 - Link company
        companyM.linkCompanyToPerson(company, PersonMapper.getInstance().find(2));
    }

    private static void testOrderProductMapper(OrderProductMapper orderProductM, OrderMapper orderM, ProductMapper productM){

        System.out.println(GREEN + "Creating OrderProducts...");
        //OrdPro002 - CREATE
        /*Order o = orderM.find(1);
        Product p = productM.find(1);
        Product p2 = productM.find(2);
        orderProductM.create(new OrderProduct(100, o, p));
        orderProductM.create(new OrderProduct(200, o, p2));*/

        //System.out.println("Products in the order are:");
        //OrdPro001 - Show all products in the order
        List<OrderProduct> products = orderProductM.showOrderProducts(orderM.find(1));
        for(OrderProduct orderp : products){
            System.out.println("PRODUCT: " + orderp.getProduct().getName() + " COUNT: " + orderp.getCount());
        }
    }

    private static void testOrderMapper(OrderMapper orderM){
        System.out.println(YELLOW);
        //001 - Show active or inactive orders
        List<Order> orders = orderM.showOrders(true);
        for(Order order : orders){
            System.out.println("ORD002 - SHOWING ORDERS: " + order.getId() + " " + order.getPerson().getFirstName() + " " + order.getPerson().getLastName());
        }

        //002 - Show information about customer whose order it is
        Person pes = orderM.showCustomerInfo(1);
        System.out.println("CUSTOMER INFORMATION - NAME: " + pes.getFirstName() + " " + pes.getLastName());

        //003 - Create
        /*Person p = PersonMapper.getInstance().find(2);
        Order o = new Order(p);
        orderM.create(o);*/

        Person p = PersonMapper.getInstance().find(1);

        String filter = "person";
        //004 - Search for order by filter
        orders = orderM.searchOrderByFilter(filter, p.getId());
        for (Order order : orders) {
            System.out.println("SEARCHING ORDER BY FILTER: " + filter + " - " + "ORDER ID: " + order.getId());
        }

        //005 - Verify quality of the order
        Order o = OrderMapper.getInstance().find(1);
        orderM.verityQuality(o);

        //006 - Set order as exported
        orderM.exportOrder(o);
    }

    private static void testProductMapper(ProductMapper productM){
        System.out.println(PURPLE);
        //Pro001 - Show product
        Product product = productM.find(1);
        System.out.println("PRO001 - NAME: " + product.getName() + " PRICE: " + product.getPrice() + " IN STORAGE: " + product.getCount());

        //Pro002 - Show all the products with its subproducts
        productM.showProductWithItsSubproducts(null, "Chair");

        //Pro003 - Copy product with subproducts
        /*Product p = productM.find(2);
         productM.copyProductWithSubproducts(p, "TurboChair");*/

        //Pro004 - Create
        /*p = new Product("Handle", 0.45f, 85000, null);
        productM.create(p);
        Product p2 = new Product("Door", 2345, 2500, p);
        productM.create(p2);*/


        //Pro005 - Set as subproduct
        /*Product subProduct = productM.find(2);
        subProduct.setPrice(0.35f);
        productM.update(subProduct);
        product = productM.find(1);
        product.setProduct(subProduct);
        productM.update(product);*/
    }

    private static void testAccountMapper(AccountMapper accountM){
        System.out.println(BLUE);
        Random random = new Random();
        int r = random.nextInt(9999);

        //Acc001 - Hours worked person that has account and has worked from day 15 to day 14 next month
        //Person p = new Person("jan" + r, "Vaclav", "Janda", null, null, AddressMapper.getInstance().find(2), false, "worker" );
        Person p = PersonMapper.getInstance().find(2);
        accountM.showHoursWorked(p, "2024-04-20", "2024-04-30");

        //Acc002 - Current salary
        Account account = accountM.find(2);
        System.out.println("Current salary for: " + account.getPerson().getFirstName() + " " + account.getPerson().getLastName() + " is: " + account.getSalary());

        //Acc003 - Show rank
        System.out.println("Current rank for: " + account.getPerson().getFirstName() + " " + account.getPerson().getLastName() + " is: " + account.getRank());

        String from = "2024-04-20";
        String to = "2024-04-30";
        //Acc004 - Show LeaderBoard
        System.out.println("LEADERBOARD FROM: " + from + " TO " + to);
        accountM.showLeaderboard(from, to);

        //Acc005 - Create account
        /*Person person = PersonMapper.getInstance().find(3);
        Account acc = new Account(25000, 0, 2, 1204, "2323adwd2242", true, "2424", "214242", "5252362123", person);
        accountM.create(acc);*/
    }

    private static void testPersonMapper(PersonMapper personM){
        System.out.println(CYAN);
        Random random = new Random();
        int r = random.nextInt(9999);
        Person person = personM.find(2);


        //P001 - Show curret task
        Work work = personM.showCurrentTask(person);
        System.out.println("CURRENT TASKS: " + work.getProduct().getName() + " PERSON: " + work.getPerson().getFirstName() + " " + work.getPerson().getLastName());

        //P002 - Show active personel
        List<Person> personel = personM.showPersonel(true);
        if(personel.isEmpty()){
            System.out.println("When searching for active/inactive, found nothing");
        }
        for(Person pe : personel) {
            System.out.println("ACTIVE PERSONEL: " + pe.getFirstName() + " " + pe.getLastName());
        }

        //P003 - DONE
        /*Person p = new Person("rin" + r, "Michal", "Rink", null, null, AddressMapper.getInstance().find(1), false, "worker" );
        personM.create(person);*/

        //P004 - Show all the tasks
        //Person p = personM.find(2);
        List<Work> w = personM.showTasks(personM.find(2));
        for(Work wo : w) {
            System.out.println("SHOWING TASKS FOR PERSON: " + wo.getPerson().getFirstName() + " " + wo.getPerson().getLastName() + " Work_ID: " + wo.getId() + " PRODUCT: " + wo.getProduct().getName());
        }

        String filter = "firstname";
        //P005 - Search by filter
        List<Person> test = personM.searchPersonel(filter, "Jane");
        if(test.isEmpty()){
            System.out.println("When searching, found nothing");
        }
        for(Person per : test) {
            System.out.println("FOUND PERSON BY FILTER: " + filter + " " + per.getFirstName() + " " + per.getLastName());
        }

        //P006 - Set person active or inactive
        personM.setPersonActiveInactive(false, person);
    }

    private static void resetColor(){
        System.out.println(RESET);
    }

    private static void create() {

        PersonMapper personM = PersonMapper.getInstance();
        AccountMapper accountM = AccountMapper.getInstance();
        AddressMapper addressM = AddressMapper.getInstance();
        CompanyMapper companyM = CompanyMapper.getInstance();
        OrderMapper orderM = OrderMapper.getInstance();
        OrderProductMapper orderProductM = OrderProductMapper.getInstance();
        ProductMapper productM = ProductMapper.getInstance();
        WorkMapper workM = WorkMapper.getInstance();

        Company c1 = new Company("Company A", "CIN123", "TIN456");
        companyM.create(c1);
        Company c2 = new Company("Company B", "CIN789", "TIN012");
        companyM.create(c2);
        Company c3 = new Company("Company C", "CIN345", "TIN678");
        companyM.create(c3);
        Company c4 = new Company("Company D", "CIN901", "TIN234");
        companyM.create(c4);

        Address a1 = new Address("District X", "123456789", "info@companyA.com", "City A", "12345", "Street A", 1, "Czechia");
        addressM.create(a1);
        Address a2 = new Address("District Y", "987654321", "info@companyB.com", "City B", "54321", "Street B", 2, "Country B");
        addressM.create(a2);
        Address a3 = new Address("District Z", "987654321", "info@companyC.com", "City C", "54321", "Street C", 3, "Country C");
        addressM.create(a3);
        Address a4 = new Address("District W", "123456789", "info@companyD.com", "City D", "12345", "Street D", 4, "Country D");
        addressM.create(a4);

        Person p1 = new Person("doe1112", "John", "Doe", null, c1, a1, true, "worker");
        personM.create(p1);
        Person p2 = new Person("smi0765", "Jane", "Smith", null, c2, a2, false, "worker");
        personM.create(p2);
        Person p3 = new Person("joh9223", "Michael", "Johnson", null, c3, a3, false, "worker");
        personM.create(p3);
        Person p4 = new Person("wil0991", "Emily", "Wilson", null, null, a4, false, "admin");
        personM.create(p4);

        Account acc1 = new Account(50000, 1, 2500, "passwordsA", true, "1234", "567890", "1234567890", p1);
        accountM.create(acc1);
        Account acc2 = new Account(60000, 2, 100, "passwordsB", true, "5678", "098765", "9876543210", p2);
        accountM.create(acc2);
        Account acc3 = new Account(70000, 3, 200, "passwordsC", true, "1234", "567890", "1234567890", p3);
        accountM.create(acc3);
        Account acc4 = new Account(80000, 0, 1600, "passwordsD", true, "5678", "098765", "9876543210", p4);
        accountM.create(acc4);

        Order o1 = new Order(p1);
        orderM.create(o1);
        Order o2 = new Order(p1);
        orderM.create(o2);
        Order o3 = new Order(p2);
        orderM.create(o3);
        Order o4 = new Order(p4);
        orderM.create(o4);

        Product pr3 = new Product("Nail", 0.35f, 1000, null);
        productM.create(pr3);
        Product pr2 = new Product("Leg", 69.99f, 50, pr3);
        productM.create(pr2);
        Product pr1 = new Product("Chair", 499.99f, 100, pr2);
        productM.create(pr1);
        Product pr4 = new Product("Table", 2499.99f, 75, pr2);
        productM.create(pr4);
        Product pr5 = new Product("Wrench", 149.99f, 40, null);
        productM.create(pr5);

        /*-- Insert into Payment table
        INSERT INTO Payment (paymentDate, amount, personID) VALUES (SYSDATE, 1000, 1);
        INSERT INTO Payment (paymentDate, amount, personID) VALUES (SYSDATE, 2000, 2);
        INSERT INTO Payment (paymentDate, amount, personID) VALUES (SYSDATE, 3000, 3);
        INSERT INTO Payment (paymentDate, amount, personID) VALUES (SYSDATE, 4000, 4);*/

        Work w1 = new Work(p1, pr1, 300, 15, null);
        workM.create(w1);
        Work w2 = new Work(p2, pr2, 25, 8, null);
        workM.create(w2);
        Work w3 = new Work(p3, pr5, 30, 25, null);
        workM.create(w3);
        Work w4 = new Work(p2, pr4, 20, 3, null);
        workM.create(w4);

        OrderProduct op1 = new OrderProduct(100, o1, pr1);
        orderProductM.create(op1);
        OrderProduct op2 = new OrderProduct(50, o1, pr2);
        orderProductM.create(op2);
        OrderProduct op3 = new OrderProduct(80, o2, pr3);
        orderProductM.create(op3);
        OrderProduct op4 = new OrderProduct(25, o3, pr4);
        orderProductM.create(op4);
    }
}