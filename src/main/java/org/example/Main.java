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

        /*Address address = new Address("DistrictName", "123456789", "example@example.com", "CityName", "12345", "StreetName", 123, "CountryName");
        addressM.create(address);
        Person p = new Person("vol2223", "Pepa", "Volheim", null, null, address, false, "worker");

        personM.create(p);*/


        Person p = personM.find(5);
        System.out.println(p.getLastName());
        /*Order o = OrderMapper.getInstance().find(2);
        System.out.println(o.getPerson().getId());*/

        /*testCompanyMapper(companyM);
        testOrderMapper(orderM);
        testOrderProductMapper(orderProductM, orderM, productM);
        testPersonMapper(personM);
        testProductMapper(productM);
        testAccountMapper(accountM);*/
    }

    /*Person p = new Person("jan" + r, "Pepa", "Janků", null, null, addressM.find(1), false, "worker");
        personM.create(p);
    Person p2 = new Person("div" + r, "Richard", "Divný", null, null, addressM.find(1), false, "worker");
        personM.create(p2);
    Account account = new Account(36000, 0, 2500, "HEHEHWFNJA", true, "2321", "242453", "2424443221", p);
        accountM.create(account);*/
    private static void testCompanyMapper(CompanyMapper companyM){
        //C001 - Show company info
        Company company = companyM.find(1);
        if(company != null) {
            System.out.println("NAME: " + company.getName() + " CIN: " + company.getCin() + " TIN: " + company.getTin());
        }

        //C002 - Create
        company = new Company("BigWilly's", "3533", "34242522");
        companyM.create(company);

        //C003 - Link company
        companyM.linkCompanyToPerson(company, PersonMapper.getInstance().find(4));
    }

    private static void testOrderProductMapper(OrderProductMapper orderProductM, OrderMapper orderM, ProductMapper productM){

        System.out.println(GREEN + "Creating OrderProducts...");
        //OrdPro002 - CREATE
        Order o = orderM.find(1);
        Product p = productM.find(1);
        Product p2 = productM.find(2);
        orderProductM.create(new OrderProduct(100, o, p));
        orderProductM.create(new OrderProduct(200, o, p2));

        System.out.println("Products in the order are:");
        //OrdPro001 - Show all products in the order
        List<OrderProduct> products = orderProductM.showOrderProducts(orderM.find(1));
        for(OrderProduct orderp : products){
            System.out.println(orderp.getProduct().getName());
        }
    }

    private static void testOrderMapper(OrderMapper orderM){
        System.out.println(YELLOW);
        //001 - Show active or inactive orders
        List<Order> orders = orderM.showOrders(true);
        for(Order order : orders){
            System.out.println(order.getId() + " " + order.getPerson().getFirstName() + order.getPerson().getLastName());
        }

        //002 - Show information about customer whose order it is
        Person pes = orderM.showCustomerInfo(1);
        System.out.println("NAME: " + pes.getFirstName() + " " + pes.getLastName());

        //003 - Create
        Person p = PersonMapper.getInstance().find(2);
        Order o = new Order(p);
        orderM.create(o);

        //004 - Search for order by filter
        orders = orderM.searchOrderByFilter("person", p.getId());
        for (Order order : orders) {
            System.out.println("FILTER: " + "ID: " + order.getId());
        }

        //005 - Verify quality of the order
        orderM.verityQuality(o);

        //006 - Set order as exported
        orderM.exportOrder(o);
    }

    private static void testProductMapper(ProductMapper productM){
        System.out.println(PURPLE);
        //Pro001 - Show product
        Product product = productM.find(1);
        System.out.println("NAME: " + product.getName() + " PRICE: " + product.getPrice() + " IN STORAGE: " + product.getCount());

        //Pro002 - Show all the products with its subproducts
        productM.showProductWithItsSubproducts(null, "Chair");

        //Pro003 - Copy product with subproducts
        Product p = productM.find(2);
        productM.copyProductWithSubproducts(p, "TurboChair");

        //Pro004 - Create
        p = new Product("Handle", 0.45f, 85000, null);
        productM.create(p);
        Product p2 = new Product("Door", 2345, 2500, p);
        productM.create(p2);


        //Pro005 - Set as subproduct
        Product subProduct = productM.find(2);
        subProduct.setPrice(0.35f);
        productM.update(subProduct);
        product = productM.find(1);
        product.setProduct(subProduct);
        productM.update(product);
    }

    private static void testAccountMapper(AccountMapper accountM){
        System.out.println(BLUE);
        Random random = new Random();
        int r = random.nextInt(9999);

        //Acc001 - Hours worked person that has account and has worked from day 15 to day 14 next month
        Person p = new Person("jan" + r, "Vaclav", "Janda", null, null, AddressMapper.getInstance().find(2), false, "worker" );
        accountM.showHoursWorked(p, "2022-04-20", "2022-04-30");

        //Acc002 - Current salary
        Account account = accountM.find(2);
        System.out.println("Current salary is: " + account.getSalary());

        //Acc003 - Show rank
        System.out.println("Current rank is: " + account.getRank());

        //Acc004 - Show LeaderBoard
        accountM.showLeaderboard("2022-04-20", "2022-04-30");

        //Acc005 - Create account
        Person person = PersonMapper.getInstance().find(3);
        Account acc = new Account(25000, 0, 2, 1204, "2323adwd2242", true, "2424", "214242", "5252362123", person);
        accountM.create(acc);
    }

    private static void testPersonMapper(PersonMapper personM){
        System.out.println(CYAN);
        Random random = new Random();
        int r = random.nextInt(9999);
        //Person person = new Person("paz" + r, "Gabriel", "Paznocht", null, company, address, true, "admin");
        Person person = personM.find(2);


        //P001 - Show curret task
        Work work = personM.showCurrentTask(person);
        System.out.println(work.getProduct().getName());

        //P002 - Show active personel
        List<Person> personel = personM.showPersonel(false);
        if(personel.isEmpty()){
            System.out.println("When searching for active/inactive, found nothing");
        }
        for(Person pe : personel) {
            System.out.println(pe.getFirstName() + pe.getLastName());
        }

        //P003 - DONE
        Person p = new Person("rin" + r, "Michal", "Rink", null, null, AddressMapper.getInstance().find(1), false, "worker" );
        personM.create(person);

        //P004 - Show all the tasks
        //Person p = personM.find(2);
        List<Work> w = personM.showTasks(person);
        for(Work wo : w) {
            System.out.println(wo.getId());
        }


        //P005 - Search by filter
        List<Person> test = personM.searchPersonel("firstname", "Gabriel");
        if(test.isEmpty()){
            System.out.println("When searching, found nothing");
        }
        for(Person per : test) {
            System.out.println(per.getFirstName() + per.getLastName());
        }

        //P006 - Set person active or inactive
        personM.setPersonActiveInactive(false, person);
    }

    private static void resetColor(){
        System.out.println(RESET);
    }
}