package au.edu.sydney.cpa.erp.feaa;

import au.edu.sydney.cpa.erp.auth.AuthToken;
import au.edu.sydney.cpa.erp.ordering.Order;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DriveSaveOrderForUOW {
    public static void saveOrders(AuthToken token, List<Order> orders){

        SaveOrderRunnableForUOW saveOrderRunnableForUOW = new SaveOrderRunnableForUOW(token,orders);

        ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.execute(saveOrderRunnableForUOW);
    }
}
